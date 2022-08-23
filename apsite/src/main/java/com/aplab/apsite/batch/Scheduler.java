package com.aplab.apsite.batch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.Md5Utils;
import com.aplab.apsite.dbmst.entity.comm.GrpSearchLogEntity;
import com.aplab.apsite.model.dto.batch.batchLogDTO;
import com.aplab.apsite.service.BatchService;
import com.aplab.apsite.service.CommonService;
import com.aplab.apsite.service.CouncilService;
import com.aplab.apsite.service.IngrdService;
import com.aplab.apsite.service.SeminarService;
import com.aplab.apsite.service.UserService;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
@Profile("!local")
public class Scheduler {

    @Value("${grp.web.url}")
    private String WEB_URL;

    @Value("${aws.log-bucket}")
    private String AWS_LOG_BUCKET;

    @Autowired
    protected BatchService batchService;

    @Autowired
    protected CouncilService councilService;

    @Autowired
    protected SeminarService seminarService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected IngrdService ingrdService;

    @Autowired
    private CommonService commonService;

    /**
     * 신원료협의체 글 등록
     * 호출 주기: 매달 1일 오전 3시
     */
    @Scheduled(cron = "00 00 03 1 * ?")
    @SchedulerLock(name = "createCouncil", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void createCouncil() {
        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_COUNCIL_REG", "M");

            if (execCount == 0) {
                // throw new Exception("서버 오류");
                councilService.insertCouncilDefault();

                afterBatchLog(params, "S", "BATCH_COUNCIL_REG => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            params.setBuffer1(WEB_URL + "/api/council/defualt");
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }

    /**
     * 세미나 여부(취소/확정) 메일 보내기 to 개최자
     * 호출 주기: 매일 오전 3시 5분
     */
    @Scheduled(cron = "00 05 03 * * ?")
    @SchedulerLock(name = "sendSeminarYnEmail", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void sendSeminarYnEmail() {
        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_SEMINAR_SEND_EMAIL", "D");

            if (execCount == 0) {
                // throw new Exception("서버 오류");
                seminarService.sendSeminarYnEmail();

                afterBatchLog(params, "S", "BATCH_SEMINAR_SEND_EMAIL => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }

    /**
     * 세미나 발생 메일 보내기 to 연구원 그룹(SGG000003)
     * 호출 주기: 매일 오전 8시
     */
    @Scheduled(cron = "00 10 08 * * ?")
    @SchedulerLock(name = "sendSeminarOccurEmail", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void sendSeminarOccurEmail() {
        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_SEMINAR_OCCUR_SEND_EMAIL", "D");

            if (execCount == 0) {
                // throw new Exception("서버 오류");
                seminarService.sendSeminarOccurEmail();

                afterBatchLog(params, "S", "BATCH_SEMINAR_OCCUR_SEND_EMAIL => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }

    /**
     * 기간 만료 Token 삭제
     * 호출 주기: 매일 오전 3시 10분
     */
    @Scheduled(cron = "00 10 03 * * ?")
    @SchedulerLock(name = "executeExpiredTokenDelete", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeExpiredTokenDelete() {
        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_TOKEN_DELETE", "D");

            if (execCount == 0) {
                // throw new Exception("서버 오류");
                userService.executeExpiredTokenDelete();

                afterBatchLog(params, "S", "BATCH_TOKEN_DELETE => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }


    /**
     * 세미나 자동 취소
     * 호출 주기: 매일 오전 3시 20분
     */
    @Scheduled(cron = "00 20 03 * * ?")
    @SchedulerLock(name = "executeSeminarAutoCancel", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeSeminarAutoCancel() {
        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_SEMINAR_AUTO_CANCEL", "D");

            if (execCount == 0) {
                // throw new Exception("서버 오류");
                seminarService.executeSeminarAutoCancel();

                afterBatchLog(params, "S", "BATCH_SEMINAR_AUTO_CANCEL => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }

    /**
     * 휴면처리 알람
     * 호출 주기: 매일 오전 06시 00분
     */
    @Scheduled(cron = "00 00 06 * * ?")
    @SchedulerLock(name = "executeDormantUserAlram", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeDormantUserAlarm() {
    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();

    	try {
    		execCount = beforeBatchLog(params, "BATCH_DORMANT_USER_ALRAM", "D");
    		if (execCount == 0) {
    			userService.dormantUserAlarm();
    			afterBatchLog(params, "S", "BATCH_DORMANT_USER_ALRAM => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    	}
    }

    /**
     * 휴면처리
     * 호출 주기: 매일 오전 06시 10분
     */
    @Scheduled(cron = "00 10 06 * * ?")
    @SchedulerLock(name = "executeDormantUser", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeDormantUser() {
    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();

    	try {
    		execCount = beforeBatchLog(params, "BATCH_DORMANT_USER", "D");

    		if (execCount == 0) {
    			// throw new Exception("서버 오류");
    			userService.dormantUser();
    			afterBatchLog(params, "S", "BATCH_DORMANT_USER => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    	}
    }

    /**
     * 3년 후 분리보관 정보 삭제
     * 호출 주기: 매일 오전 06시 20분
     */
    @Scheduled(cron = "00 20 06 * * ?")
    @SchedulerLock(name = "executeThreeYearDormantUser", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeThreeYearDormantUser() {
    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();

    	try {
    		execCount = beforeBatchLog(params, "BATCH_THREE_DORMANT_USER", "D");

    		if (execCount == 0) {
    			// throw new Exception("서버 오류");
    			userService.threeYeardormantUser();
    			afterBatchLog(params, "S", "BATCH_THREE_DORMANT_USER => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    	}
    }

    /**
     * 전자계약 상태 업데이트 및 메일 예약 데이터 저장
     * 호출 주기: 매일 오전 06시 00분
     */
    @Scheduled(cron = "00 00 06 * * ?")
    @SchedulerLock(name = "executeSignOkStatus", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeSignOkStatus() {

        int execCount = 0;
        batchLogDTO params = new batchLogDTO();

        try {
            execCount = beforeBatchLog(params, "BATCH_SIGNOK_STATUS", "D");

            if (execCount == 0) {
            	ingrdService.updateSignOkStatus();
                afterBatchLog(params, "S", "BATCH_SIGNOK_STATUS => SUCC");
            } else {
                afterBatchLog(params, "E", "지정된 횟수 초과 실행");
            }
        } catch (Exception e) {
            afterBatchLog(params, "E", e.getMessage().toString());
            e.printStackTrace();
        } finally {
            params = null;
        }
    }

    /**
     * 전자계약 상태 메일 발송
     * 호출 주기: 매일 오전 08시 00분
     */
    @Scheduled(cron = "00 00 08 * * ?")
    @SchedulerLock(name = "executeSignOkMailStatus", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeSignOkMailStatus() {

    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();

    	try {
    		execCount = beforeBatchLog(params, "BATCH_SIGNOK_STATUS_MAIL", "D");

    		if (execCount == 0) {
    			commonService.findMailReserveSend("RESERVE");
    			afterBatchLog(params, "S", "BATCH_SIGNOK_STATUS_MAIL => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    	}
    }

    /**
     * 원료 입고처리
     * 호출 주기: 매일 오전 07시 00분
     */
    @Scheduled(cron = "00 00 07 * * ?")
    @SchedulerLock(name = "executeUpdateIngrdStep4", lockAtLeastFor = "10m", lockAtMostFor = "10m")
    @Transactional
    public void executeUpdateIngrdStep4() {
    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();

    	try {
    		execCount = beforeBatchLog(params, "BATCH_UPDATE_INGRD_STEP4", "D");

    		if (execCount == 0) {
    			ingrdService.updateRawStep4();
    			afterBatchLog(params, "S", "BATCH_UPDATE_INGRD_STEP4 => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    	}
    }

    /**
     * 조회내역 S3 업로드
     * 호출 주기: 매일 11시 50분에 작동
     */
    @Scheduled(cron = "00 */5 * * * *")
    @SchedulerLock(name = "executeSearchLogS3Upload", lockAtLeastFor = "4m", lockAtMostFor = "4m")
    @Transactional
    public void executeSearchLogS3Upload() {
    	int execCount = 0;
    	batchLogDTO params = new batchLogDTO();
    	ByteArrayOutputStream out = null;
    	ByteArrayInputStream in = null;
    	CSVPrinter csvPrinter = null;
    	try {
    		execCount = beforeBatchLog(params, "BATCH_SEARCH_LOG_S3", "D");

    		if (execCount == 0) {
    			List<GrpSearchLogEntity> list = commonService.findGrpSearchLogList();
    			if(list != null) {
    				List<List<String>> csvBody = new ArrayList<>();
    				list.forEach(obj-> {
    					try {
    						List<String> strList = new ArrayList<String>();
    						strList.add(obj.getRegDtm().toString());
        					strList.add(obj.getLoginId());
        					strList.add(obj.getClientIp());
        					strList.add(obj.getServerIp());
        					strList.add(obj.getActionType());
        					strList.add(obj.getServiceNm());
        					strList.add(obj.getServiceUrl());
        					strList.add(obj.getReturnUrl() != null ? obj.getReturnUrl() : "null");
        					strList.add(String.valueOf(obj.getSearchCount()));
        					strList.add(obj.getPageMark());
        					strList.add(obj.getResponeInfo());
        					strList.add("null");
        					csvBody.add(strList);
						} catch (Exception e) {
							e.printStackTrace();
						}
    				});

    				out = new ByteArrayOutputStream();
    				csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
    				csvPrinter.printRecord("\ufeff");
    				for (List<String> record : csvBody){
    					csvPrinter.printRecord(record);
    				}
    				csvPrinter.flush();
    				in = new ByteArrayInputStream(out.toString("UTF-8").getBytes());
    				LocalDateTime current = LocalDateTime.now();

    				String fileNm 	= "grp-custinfo-log-"+current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))+".csv";
    				String key 		= "grp/custinfo-log/"+current.format(DateTimeFormatter.ofPattern("yyyyMM")) + "/" + fileNm;
    				byte[] bytes 	= IOUtils.toByteArray(in);

    				ObjectMetadata objMeta = new ObjectMetadata();
    				objMeta.setContentType(Mimetypes.getInstance().getMimetype(fileNm));
    				objMeta.setContentLength(bytes.length);
    				objMeta.setContentMD5(Md5Utils.md5AsBase64(bytes));

    				in.reset();
    				PutObjectRequest putObjReq = new PutObjectRequest(AWS_LOG_BUCKET, key, in, objMeta);

    	            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
    	                    .withRegion(Regions.AP_NORTHEAST_2)
    	                    .build();
    	            s3Client.putObject(putObjReq);
    			}
    			afterBatchLog(params, "S", "BATCH_SEARCH_LOG_S3 => SUCC");
    		} else {
    			afterBatchLog(params, "E", "지정된 횟수 초과 실행");
    		}
    	} catch (Exception e) {
    		afterBatchLog(params, "E", e.getMessage().toString());
    		e.printStackTrace();
    	} finally {
    		params = null;
    		try {
    			if(out != null) {
    				out.close();
    			}
    			if(in != null) {
    				in.close();
    			}
    			if(csvPrinter != null) {
    				csvPrinter.close();
    			}
			} catch (Exception e2) {
				e2.printStackTrace();
				afterBatchLog(params, "E", e2.getMessage().toString());
			}
    	}
    }

    /**
     * 배치 로그 기록 및 실행여부 체크
     * @param dto
     * @param typeCd
     * @param period
     * @return
     */
    private int beforeBatchLog(batchLogDTO dto, String typeCd, String period) {
        dto.setTypeCd(typeCd);
        dto.setPeriod(period);
        dto.setFlag("Y");

        int execCount = batchService.getExecBatchCount(dto);

        batchService.insertBatchLog(dto);
        return execCount;
    }

    /**
     * 배치 로그 종료 기록
     * @param dto
     * @param flag
     * @param content
     */
    private void afterBatchLog(batchLogDTO dto, String flag, String content) {
        dto.setFlag(flag);
        if (content != null) {
            dto.setContent(content);
        }

        batchService.updateBatchLog(dto);
    }
}
