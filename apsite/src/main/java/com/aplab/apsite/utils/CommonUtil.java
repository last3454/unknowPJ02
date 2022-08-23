package com.aplab.apsite.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;

import com.aplab.apsite.model.ParentPagingDTO;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

	/**
     * page 계산
     * @param dto
     * @param totalCnt
     */
    public static void setPaging(ParentPagingDTO dto, int totalCnt) {

    	int pageBlock = 10;
    	int skipCnt = 0;

        dto.setTotalCnt(totalCnt);

        if (dto.getPageSize() == 0) {
            dto.setPageSize(10);
        }

        if (totalCnt == 0) {
            dto.setTotalPageCnt(0);
            dto.setNowPageNo(1);
            dto.setLimit(dto.getPageSize());
            dto.setOffset(0);
            return;
        }

        int totalPageCnt = dto.getTotalCnt() / dto.getPageSize();

        if (dto.getTotalCnt() % dto.getPageSize() > 0) {
            totalPageCnt++;
        }

        dto.setTotalPageCnt(totalPageCnt);

        if (dto.getNowPageNo() > totalPageCnt) {
            dto.setNowPageNo(totalPageCnt);
        }

        if (dto.getNowPageNo() > 1) {
        	skipCnt = (dto.getNowPageNo() - 1) * dto.getPageSize();
        }

        dto.setLimit(dto.getPageSize());
        dto.setOffset((dto.getNowPageNo() - 1) * dto.getPageSize());

        dto.setCurBlock(((dto.getNowPageNo()-1) / pageBlock) + 1);
        dto.setStartPage((dto.getCurBlock() - 1) * pageBlock + 1);
        dto.setEndPage(dto.getStartPage() + pageBlock - 1);
        dto.setStartRownum(skipCnt + 1);
        dto.setEndRownum(skipCnt + dto.getPageSize());

        if(dto.getEndPage() > dto.getTotalPageCnt()){
        	dto.setEndPage(dto.getTotalPageCnt());
        }
    }

    /**
     * Client IP
     * @param req
     * @return
     */
    public static String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null) ip = req.getHeader("Proxy-Client-IP");
        if (ip == null) ip = req.getHeader("WL-Proxy-Client-IP");
        if (ip == null) ip = req.getHeader("HTTP_CLIENT_IP");
        if (ip == null) ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null) ip = req.getRemoteAddr();
        return ip;
    }

    //XSS 방지 처리
    public static String unscript(String data) {
        if (data == null || data.trim().equals("")) {
            return "";
        }

        String ret = data;

        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

        return ret;
    }

	// 문자열에서 <br>만 남기고 모든 HTML 태그 삭제
	public static String removeHTML(String strHtml) {
		String	result	= "";

		if (StringUtils.isEmpty(strHtml)) {
			return "";
		}
		result = strHtml;
		// 개행 문자 삭제
		// 개행 문자를 삭제하지 않으면, 정규표현식이 복잡해지면서 자바 메모리 오류가 발생하는 현상이 있었음
		result	= result.replace(System.lineSeparator(), "");
		result	= result.replaceAll("\\r\\n|\\r|\\n", "");

		// head 부분 제거
		result	= result.replaceAll("<head.+?</head>", "");

		//style 부분을 먼저 삭제 한다.
		result	= result.replaceAll("<style.+?</style>", "");

		result	= replace(result, "<br>", "@#$%^&*");
		result	= replace(result, "<BR>", "@#$%^&*");
		result	= replace(result, "<Br>", "@#$%^&*");
		result	= replace(result, "<bR>", "@#$%^&*");
		result	= replace(result, "</p>", "@#$%^&*");
		result	= replace(result, "</P>", "@#$%^&*");

		// HTML 태그제거
		result	= result.replaceAll("<.+?>", "");

		result	= replace(result, "P {margin-top:2px;margin-bottom:2px;}", "");
		result	= replace(result, "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", "");
		result	= replace(result, "<!--DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"\"-->", "");
		result	= replace(result, "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"\">", "");
		result	= replace(result, "@#$%^&*", "<br/>"+System.lineSeparator());

		result	= HtmlUtils.htmlUnescape(result);

		result	= replace(result, "<o:p>"	, "");
		result	= replace(result, "</o:p>"	, "");

		return result.trim();
	}

	// 문자열 교체 함수
	public static String replace(String origin, String pattern, String replace) {
		int	sIndex	= 0;
		int	eIndex	= 0;

		if (StringUtils.isEmpty(origin)) {
			return "";
		}

		StringBuffer result	= new StringBuffer();

		while ((eIndex = origin.indexOf(pattern, sIndex)) >= 0) {
			result.append(origin.substring(sIndex, eIndex));
			result.append(replace);
			sIndex	= eIndex + pattern.length();
		}
		result.append(origin.substring(sIndex));
		return result.toString();
	}

    // 랜덤 영문 숫자 조합 ( 영문보다 숫자가 나올 확률이 2배 높다. )
 	public static String getRandomString(int strLength) {

 		String 		returnStr	= "";
 		String[] 	strArr		= new String[]{"0123456789", "0123456789", "abcdefghijklmnopqrstuvwxyz"};
 		int			len			= strArr.length;
 		Random		random		= new Random();

 		for (int i = 0; i < strLength; i++)
 		{
 			int iRan1 	= random.nextInt(len);
 			int iRan2	= random.nextInt(strArr[iRan1].length());

 			returnStr	+= strArr[iRan1].substring(iRan2, iRan2+1);
 		}

 		return returnStr;
 	}

	// LPAD, RPAD
	public static String getPad(String type, String str, int len, char spaceChr) {
		String	sResult = "";
		int		strLen  = 0;

		if (str == null) str = "";

		strLen = str.length();

		if (strLen < len){
			for (int i = 0; i < len - strLen ; i++){
				sResult	+= spaceChr;
			}
			if ("LPAD".equals(type.toUpperCase())){
				sResult	+= str;
			}else if ("RPAD".equals(type.toUpperCase())){
				sResult	=  str + sResult;
			}
		}else{
			sResult = str;
		}

		return sResult;
	}

 	public static String getNowDate(String format) {
 		return LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern(format));
 	}

 	// Request 호출
 	public static HttpServletRequest getRequest() {
 		ServletRequestAttributes attr = null;
 		try {
 			attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
			return null;
		}
		return attr.getRequest();
 	}

 	public static HashMap<String,Object> mapFromJson(String json){
    	Gson gson = new Gson().newBuilder().disableHtmlEscaping().create();
		HashMap<String,Object> map = gson.fromJson(json, HashMap.class);
		return map;
 	}

 	public static HashMap<String,Object> getHttpConnection(String method, String addrUrl , HashMap<String,String> headerMap, HashMap<String,Object> bodyMap) throws Exception {
        if (log.isDebugEnabled()) {
        	log.debug("###########API 통신 호출#############");
            log.debug(addrUrl);
        }
 		StringBuilder sb = new StringBuilder();
 		HashMap<String,Object> resultMap = new HashMap<>();
 		URL url = null;
 		HttpsURLConnection con = null;
 		OutputStreamWriter wr = null;
 		BufferedReader br = null;
 		try {
        	url = new URL(addrUrl);
        	con = (HttpsURLConnection) url.openConnection();

        	con.setRequestMethod(method);
        	if("POST".equals(method)) {
        		con.setDoOutput(true);
        	}
        	con.setDoInput(true);
        	con.setConnectTimeout(5000);

        	String hKey;
        	Iterator<String> hItr = headerMap.keySet().iterator();
        	while(hItr.hasNext()) {
        		hKey = hItr.next().toString();
        		con.setRequestProperty(hKey, headerMap.get(hKey));
        	}

        	if(bodyMap != null) {
        		String bodyParams = "";
        		if("json".equals(bodyMap.get("bType"))) {
        			bodyMap.remove("bType");
        			Gson gson = new Gson();
        			bodyParams = gson.toJson(bodyMap);
        		}else {
                	bodyParams = bodyMap.keySet().stream()
        			.map(key -> key + "=" + bodyMap.get(key))
        			.collect(Collectors.joining("&"));
        		}

            	wr = new OutputStreamWriter(con.getOutputStream(),"UTF-8");
            	wr.write(bodyParams);
            	wr.flush();
        	}

        	int HttpResult = con.getResponseCode();
            if (log.isDebugEnabled()) {
            	log.debug("API HTTP_CODE : " + HttpResult);
            }
        	if(HttpResult == HttpsURLConnection.HTTP_OK) {
	        	br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
	        	String line =null;
	        	while((line = br.readLine()) != null) {
	        		sb.append("\n").append(line);
	        	}
	        	br.close();
        	}
        	resultMap.put("code", HttpResult);
        	resultMap.put("data", sb.toString());
        	sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
            log.error("API Exception : " + e);
		} finally {
			if (wr != null) {
				wr.close();
			}
            if (br != null) {
            	br.close();
            }
            if (con != null) {
                con.disconnect();    //Connection 연결 끊기
            }
            if (log.isDebugEnabled()) {
            	log.debug("###########API 통신 호출#############");
                log.debug(addrUrl);
            }
        }
 		return resultMap;
 	}

 	public static HashMap<String,Object> getHttpConnectionBineryFile(String method, String addrUrl , HashMap<String,String> headerMap) throws Exception {
 		if (log.isDebugEnabled()) {
 			log.debug("###########API 통신 호출#############");
 			log.debug(addrUrl);
 		}

 		HashMap<String,Object> resultMap = new HashMap<>();
 		URL url = null;
 		HttpsURLConnection con = null;
 		InputStream is = null;
 		ByteArrayOutputStream out = null;
 		ByteArrayInputStream in = null;
 		try {
 			url = new URL(addrUrl);
 			con = (HttpsURLConnection) url.openConnection();

 			con.setRequestMethod(method);
 			if("POST".equals(method)) {
 				con.setDoOutput(true);
 			}
 			con.setDoInput(true);
 			con.setConnectTimeout(5000);

 			String hKey;
 			Iterator<String> hItr = headerMap.keySet().iterator();
 			while(hItr.hasNext()) {
 				hKey = hItr.next().toString();
 				con.setRequestProperty(hKey, headerMap.get(hKey));
 			}

 			int HttpResult = con.getResponseCode();
 			if (log.isDebugEnabled()) {
 				log.debug("API HTTP_CODE : " + HttpResult);
 			}
 			if(HttpResult == HttpsURLConnection.HTTP_OK) {
 				out = new ByteArrayOutputStream();
 				is = con.getInputStream();
 				final int BUFFER_SIZE = 4096;
 				int bytesRead;
 				byte[] buffer = new byte[BUFFER_SIZE];
 				while ((bytesRead = is.read(buffer)) != -1) {
 					out.write(buffer, 0, bytesRead);
 					out.flush();
 				}
 				in = new ByteArrayInputStream(out.toByteArray());
 				resultMap.put("data", in);
 			}
 			resultMap.put("code", HttpResult);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
			if (in != null) {
				in.close();
			}
 			if (out != null) {
 				out.close();
 			}
 			if (con != null) {
 				con.disconnect();    //Connection 연결 끊기
 			}
 			if (log.isDebugEnabled()) {
 				log.debug("###########API 통신 호출#############");
 				log.debug(addrUrl);
 			}
 		}
		return resultMap;
 	}

    /**
     * 날짜 + days
     * @param date
     * @param days
     * @return
     */
    public static Date getDateAddDays(Date date, long days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rtnDt = null;
        try {
            Date dt = sdf.parse(sdf.format(date));
            rtnDt = new Date(dt.getTime() + (days * 24l * 60l * 60l * 1000l));
        } catch (Exception e) {
            rtnDt = date;
        }
        return rtnDt;
    }

    /**
     * 달 계산
     * @param date
     * @param days
     * @return
     * @throws ParseException
     */
    public static String getDateAddMonth(String date, int Month){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String strDate = "";
		try {
			Date nDate = sdf.parse(date);
	    	GregorianCalendar calendar = new GregorianCalendar();
	    	calendar.setTime(nDate);
	    	calendar.add(Calendar.MONTH, Month);
	        strDate = sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return strDate;
    }

    /**
     * 이름 가운데 마스킹
     * @param name
     * @return
     */
    public static String nameMasking (String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }

        try {
            String regex = "(^[가-힣]+)$";

            Matcher matcher = Pattern.compile(regex).matcher(name);
            if(matcher.find()) {
                int length = name.length();

                String middleMask = "";
                if(length > 2) {
                    middleMask = name.substring(1, length - 1);
                } else {    // 이름이 외자
                    middleMask = name.substring(1, length);
                }

                String dot = "";
                for(int i = 0; i<middleMask.length(); i++) {
                    dot += "*";
                }

                if(length > 2) {
                    return name.substring(0, 1)
                            + middleMask.replace(middleMask, dot)
                            + name.substring(length-1, length);
                } else { // 이름이 외자 마스킹 리턴
                    return name.substring(0, 1)
                            + middleMask.replace(middleMask, dot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 핸드폰 번호 가운데 마스킹
     * @param phoneNo
     * @return
     */
    public static String phoneMasking(String phoneNo) {
        if (StringUtils.isEmpty(phoneNo)) {
            return "";
        }
        try {
            String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

            Matcher matcher = Pattern.compile(regex).matcher(phoneNo);
            if(matcher.find()) {
                String target = matcher.group(2);
                int length = target.length();
                char[] c = new char[length];
                Arrays.fill(c, '*');

                return phoneNo.replace(target, String.valueOf(c));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return phoneNo;
    }

    /**
     * email 마스킹 처리. 5자리부터 마스킹, 4자리까지는 마지막만 마스킹
     * @param email
     * @return
     */
    public static String emailMasking(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        }

        String emailId = email.split("@")[0];
        try {
            if (emailId.length() > 4) {
                return email.replaceAll("(?<=.{4}).(?=.*@)", "*");
            } else {
                return email.replaceAll("(?<=.{" + (emailId.length() - 1) +"}).(?=.*@)", "*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
}
