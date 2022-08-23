package com.aplab.apsite.form;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.aplab.apsite.model.dto.comm.MailDTO;
import com.aplab.apsite.utils.CommonUtil;
import com.aplab.apsite.utils.PropertyUtil;

public class MailForm {

	private String WEB_URL			= PropertyUtil.getProperty("grp.web.url");

	private String TIUM_WEB_URL     = PropertyUtil.getProperty("tium.web.url");

	private String STYLE_TOP 		= "line-height: 1.6;margin: auto;padding: 0;width:100%;max-width:600px;color:#333;background: #fff;letter-spacing:-0.08em;-webkit-font-smoothing: antialiased;-moz-osx-font-smoothing: auto;-moz-osx-font-smoothing: grayscale;font-size:16px;font-family:'맑은 고딕',Malgun Gothic,Dotum,sans-serif;";

	private String STYPE_TABLE		= "line-height: 1.6;margin: 0 auto;padding: 0;width:100%;max-width:100%;color:#666;letter-spacing:-0.08em;-webkit-font-smoothing: antialiased;-moz-osx-font-smoothing: auto;-moz-osx-font-smoothing: grayscale;font-size:17px;font-family:'맑은 고딕',Malgun Gothic,Dotum,sans-serif;border-top: 1px solid #ccc;border-bottom: 1px solid #ccc;";

	private String STYLE_BOTTOM 	= "line-height: 1.4;margin: 0;padding: 11px 40px;font-size: 12px;color: #ccc;text-align: center;letter-spacing: -0.03em;border-top: 1px solid #ececec;";

	public MailForm() {
		HttpServletRequest request = CommonUtil.getRequest();
		if(WEB_URL.indexOf(request.getServerName()) == -1 ) {
			WEB_URL = PropertyUtil.getProperty("grp.web.i-url");
		}
	}

    public String getMailContent(String type, String id, String email) {
        if (StringUtils.isEmpty(type)) {
            return "";
        }

        if (type.equals(MailDTO.MAIL_CON1)) {
            return this.getSignupWaitingForm(id, email);
        } else if(type.equals(MailDTO.MAIL_CON2)) {
            return this.getSignupJoinForm(id, email);
        } else if(type.equals(MailDTO.MAIL_CON4)) {
            return this.getApprovalUserForm(id);
        } else if(type.equals(MailDTO.MAIL_CON5)) {
            return this.getRejectUserForm(id);
        } else if(type.equals(MailDTO.MAIL_CON6)) {
            return this.getSearchIdForm(id, email);
        }

        return "";
    }

    public String getMailContent(String type, String id, String email, HashMap<String, String> params) {
        String mailContent = "";

        if (!StringUtils.isEmpty(type)) {
            switch (type) {
            case MailDTO.MAIL_SMN_CON1:
                mailContent = getSeminarHoldYnForm(id, email, params);
                break;
            case MailDTO.MAIL_SMN_CON2:
                mailContent = getNotifySeminarCancelForRegUserForm(id, email, params);
                break;
            case MailDTO.MAIL_SMN_CON3:
                mailContent = getNotifySeminarCancelForAttendeeForm(params);
                break;
            case MailDTO.MAIL_SMN_CON4:
                mailContent = getSeminarAttendForm(id, email, params);
                break;
            case MailDTO.MAIL_SMN_CON5:
                mailContent = getSeminarOccurForm(params);
                break;
            case MailDTO.MAIL_APPR_CON1:
                mailContent = getApprovalForm(id, params);
                break;
            case MailDTO.MAIL_APPR_CON2:
                mailContent = getRejectForm(id, params);
                break;
            case MailDTO.MAIL_APPR_CON3:
                mailContent = getRejectForm(id, params);
                break;
            case MailDTO.MAIL_CON3:
                mailContent = getRawSourcingInqForm(id, email, params);
                break;
            case MailDTO.MAIL_CON7:
            	mailContent = getDormantUserAlarmForm(id, params);
            	break;
            case MailDTO.MAIL_CON8:
            	mailContent = getDormantUserForm(id, params);
            	break;
            case MailDTO.MAIL_CON9:
            	mailContent = getRetireUserForm(id, params);
            	break;
            case MailDTO.MAIL_SIGNOK_CON1:
            	mailContent = getSignOkApprovalForm(id, params);
            	break;
            case MailDTO.MAIL_RAW_SUBMISSION:
                mailContent = getSubmissionForm(params);
                break;
            case MailDTO.MAIL_RAW_RMQC:
                mailContent = getRmqcCompleteForm(params);
                break;
            case MailDTO.MAIL_NOT_CON1:
                mailContent = getNoticeForm(id, params);
                break;
            case MailDTO.MAIL_ERR_CON1:
                mailContent = getErrorInqForm(id, params);
                break;
            case MailDTO.MAIL_SOR_CON1:
                mailContent = getSourcingForm(id, params);
                break;
            case MailDTO.MAIL_COM_CON1:
                mailContent = getCommunicationForm(id, params);
                break;
            default:
                break;
            }
        }

        return mailContent;
    }

    // mail Header
    public String getTop() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append("\n").append("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ko\" xml:lang=\"ko\">");
        sb.append("\n").append("	<head>");
        sb.append("\n").append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        sb.append("\n").append("		<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        sb.append("\n").append("		<title>원료 정보 시스템</title>");
        sb.append("\n").append("	</head>");
        sb.append("\n").append("	<body style=\"margin: 0;padding: 0;background: #F5F5F5;\">");
        sb.append("\n").append("		<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\""+STYLE_TOP+"\">");
        sb.append("\n").append("			<tbody>");
        sb.append("\n").append("				<tr style=\"line-height: 1.6;margin: 0;padding: 0;background-color: #1461a5;background-image: linear-gradient(280deg, #4174CC 0%, #2861C2 0%, #09B29C 100%);\">");
        sb.append("\n").append("					<th style=\"line-height: 1;margin: 0;padding: 26px 40px 26px;text-align: left;\"><img border=\"0\" src=\""+WEB_URL+"/img/logo.svg\" alt=\"원료 정보 시스템\" style=\"width:215px;\"></th>");
        sb.append("\n").append("				</tr>");
        return sb.toString();
    }

    // mail Bottom
    public String getBottom() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("				<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("					<td style=\""+STYLE_BOTTOM+"\">ⓒ AMOREPACIFIC R&D. All rights reserved.</td>");
        sb.append("\n").append("				</tr>");
        sb.append("\n").append("			<tbody>");
        sb.append("\n").append("		</table>");
        sb.append("\n").append("	</body>");
        sb.append("\n").append("</html>");
        return sb.toString();
    }

    //회원 가입 대기 요청 메일
    public String getSignupWaitingForm(String id, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 회원 가입 승인 대기 중</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">귀하는 "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 <br />원료정보시스템 회원가입을 요청하셨습니다.");
        sb.append("\n").append("	<br />담당자 확인 후 가입 승인을 하면, 로그인이 가능합니다.");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">아이디</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+id+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">이메일</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+email+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }
    //업체 승인 메일
    public String getApprovalForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 업체 승인</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append(			params.get("compNm")+"은(는) "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 업체 승인이 완료되었습니다.");
    	sb.append("\n").append("		<br/> 로그인 후 시스템 이용이 가능합니다.");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //업체 반려 메일
    public String getRejectForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 업체 반려</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append(			params.get("compNm")+"은(는) "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 업체 승인이 반려되었습니다.");
    	sb.append("\n").append("		<br/> 자세한 내용은 관리자에게 문의 부탁드립니다.");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }
    //업체 승인 요청 메일
    public String getApprovalWaiteForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 업체 승인 대기 중</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append(			params.get("compNm")+"은(는) "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 원료정보시스템 업체 승인을 요청하셨습니다.");
    	sb.append("\n").append("		<br/> 확인 후 업체 승인을 부탁드립니다.");
    	sb.append("\n").append("		<br/> ※ 업체 승인 시 마스터 계정도 자동 승인됩니다.");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }
    //회원 가입 승인 메일
    public String getApprovalUserForm(String id) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 가입 승인</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">귀하는 "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 <br />원료정보시스템 회원 가입이 승인되었습니다.");
        sb.append("\n").append("		<br/> 로그인 후 시스템 이용이 가능합니다.");
        sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }
    //회원 가입 반려 메일
    public String getRejectUserForm(String id) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 가입 반려</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">귀하는 "+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 <br />원료정보시스템 회원 가입이 반려되었습니다.");
        sb.append("\n").append("		<br/> 자세한 내용은 관리자에게 문의 부탁드립니다.");
        sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //아이디 찾기
    public String getSearchIdForm(String id, String email) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">아이디 확인</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+email+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">귀하께서 찾으시는 원료정보시스템 로그인 아이디는 아래와 같습니다.");
        sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">아이디</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+id+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //회원 가입 승인 요청 메일
    public String getSignupJoinForm(String id, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 회원 가입 승인 요청</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님이 회원 가입 승인을 요청하셨습니다.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">"+CommonUtil.getNowDate("yyyy년 MM월 dd일 HH:mm:ss")+" 에 <br />원료정보시스템 회원가입을 요청하셨습니다.");
        sb.append("\n").append("		<br/> 확인 후 가입 승인을 부탁드립니다.");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">아이디</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+id+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">이메일</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+email+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 원료소싱 의견 메일 (업체 -> 원료소싱 글작성자)
    public String getRawSourcingInqForm(String id, String email, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료소싱 글에 대한 "+params.get("fromCompNm")+" 업체 의견</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+params.get("toUserNm")+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.5;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("	  상기 업체의 ["+params.get("title")+"] 글에 대한 의견입니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.5;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("	  원료소싱 의견");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("	<tbody>");
        sb.append("\n").append("		<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("		    <th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">"+params.get("mailCont")+"</th>");
        sb.append("\n").append("		</tr>");
        sb.append("\n").append("	</tbody>");
        sb.append("\n").append("</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 세미나 개최여부(to. 개최자) 메일(MAIL_SMN_CON1)
    public String getSeminarHoldYnForm(String id, String email, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 세미나 개최여부 확인 요청</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("	  하기 세미나의 신청 기간이 종료되었습니다.");
        sb.append("\n").append("	  </br>개최 여부를 지정해 주시기 바랍니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">제목</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("title")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나일시</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnDtm")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나장소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnPlc")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 세미나 개최취소 공지(to. 개최자) 메일(MAIL_SMN_CON2)
    public String getNotifySeminarCancelForRegUserForm(String id, String email, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 세미나 개최취소 공지</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    등록하신 세미나가 개최 일시 기준, 3일 이전에 확정 처리되지 않아, 취소 처리됨을 알려드립니다.");
        sb.append("\n").append("    </br>하기 세미나 정보를 확인해 주시기 바랍니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">제목</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("title")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나일시</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnDtm")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나장소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnPlc")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">취소사유</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnCanclReason")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 세미나 개최취소 안내(to. 참석자) 메일(MAIL_SMN_CON3)
    public String getNotifySeminarCancelForAttendeeForm(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 세미나 개최취소 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    참석 신청하신 세미나가 개최자 또는 시스템에 의해 취소되었음을 알려드립니다.");
        sb.append("\n").append("    </br>하기 세미나 정보를 확인해 주시기 바랍니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">제목</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("title")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나일시</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnDtm")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나장소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnPlc")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">취소사유</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnCanclReason")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 세미나 참석 신청/취소 메일(MAIL_SMN_CON4)
    public String getSeminarAttendForm(String id, String email, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 세미나 참석 " + ("REQ".equals(params.get("attendYn")) ? "신청" : "취소") + "</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    하기 세미나의 참석 " + ("REQ".equals(params.get("attendYn")) ? "신청이" : "취소가") + " 완료되었습니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">제목</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("title")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나일시</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnDtm")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나장소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnPlc")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 세미나 발생 메일(MAIL_SMN_CON5)
    public String getSeminarOccurForm(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 세미나 발생 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    하기 세미나가 발생하여 안내드립니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나 제목</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("title")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">세미나 일시</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnDtm")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">신청 기간</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("smnRegDt")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">원료 정보</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\">"+params.get("rawInfo")+"</td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">신청 바로가기</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\"><a href=\""+String.format("%s%s%s", WEB_URL, "/seminar/view?seq=", params.get("seq"))+"\" target=\"_blank\">"+String.format("%s%s%s", WEB_URL, "/seminar/view?seq=", params.get("seq"))+"</a></td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    //휴면 알람 메일
    public String getDormantUserAlarmForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 휴면 예정 알림</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+params.get("userNm")+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append("     	1년 이상 원료관리시스템에 접속하지 않으신 회원님들의 개인정보를 보호하고자 휴면회원 전환 및 개인정보를 안전하게 분리보관할 예정입니다.");
    	sb.append("\n").append("		<br/> 휴면회원 전환된 아이디는 로그인 후 해제 가능합니다.");
    	sb.append("\n").append("		<br/> □ 대상아이디 : " + id);
    	sb.append("\n").append("		<br/> □ 처리예정일 : " + CommonUtil.getDateAddMonth(params.get("lastLoginDt").toString(), 12));
    	sb.append("\n").append("		<br/> □ 관련 근거 법령(개인정보보호법 제 39조의 6)");
    	sb.append("\n").append("		<br/> □ 분리/저장되어 관리되는 개인정보 항목 (이름 / ID / 패스워드 / 이메일 / 전화번호 / 사용자 / 고유식별번호(DI 값)");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //휴면 메일
    public String getDormantUserForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 휴면 알림</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+params.get("userNm")+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append("     	1년 이상 원료관리시스템에 접속하지 않으신 회원님들의 개인정보를 보호하고자 휴면회원 전환 및 개인정보를 안전하게 분리보관할 예정입니다.");
    	sb.append("\n").append("		<br/> 휴면회원 전환된 아이디는 로그인 후 해제 가능합니다.");
    	sb.append("\n").append("		<br/> □ 대상아이디 : " + id);
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //탈퇴 메일
    public String getRetireUserForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 탈퇴 알림</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+params.get("userNm")+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append("     	원료 관리 시스템 탈퇴가 완료되었습니다.");
    	sb.append("\n").append("		<br/> 회원 탈퇴 처리 후에는 회원님의 개인정보를 복원 할수 없습니다.");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    //전자 서명 승인 요청 메일
    public String getSignOkApprovalForm(String id, HashMap<String, String> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n").append(this.getTop());
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 전자서명 승인 요청 알림</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+params.get("userNm")+"</span> 님 안녕하세요.</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
    	sb.append("\n").append("		["+params.get("rawNmKo")+"]에 대한 전자 서명 승인 요청드립니다.");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
    	sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
    	sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
    	sb.append("\n").append("	</td>");
    	sb.append("\n").append("</tr>");
    	sb.append("\n").append(this.getBottom());
    	return sb.toString();
    }

    // 제출 완료 메일
    public String getSubmissionForm(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        String mailUrl = TIUM_WEB_URL + params.get("url");
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 원료 제출 알림</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\">").append(params.get("laborNm")).append("님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("        <span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">[").append(params.get("rawNmKo")).append("]</span> 등록 및 제출되었습니다.<br/>티움넷에서 접수 및 검토를 진행할 수 있습니다.");
        sb.append("\n").append("    </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("        <a href=\"").append(mailUrl).append("\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">바로가기</a>");
        sb.append("\n").append("    </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // RMQC 완료 메일
    public String getRmqcCompleteForm(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 RMQC 완료 알림</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\">").append(params.get("userNm")).append("님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("        <span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">[").append(params.get("rawNmKo")).append("]</span> RMQC 검토 완료되었습니다.");
        sb.append("\n").append("    </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("    <td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("        <a href=\"").append(WEB_URL).append("\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("    </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 공지사항 등록 메일 (MAIL_NOT_CON1)
    public String getNoticeForm(String id, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 공지사항 등록 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    하기 새로운 공지사항 안내드립니다. 아래 링크를 통해 내용 확인해주세요.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">링크 주소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\"><a href=\""+String.format("%s%s%s", WEB_URL, "/notice/view?seq=", params.get("seq"))+"\" target=\"_blank\">"+String.format("%s%s%s", WEB_URL, "/notice/view?seq=", params.get("seq"))+"</a></td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 오류문의 답변 메일 (MAIL_ERR_CON1)
    public String getErrorInqForm(String id, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 오류문의 답변등록 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    오류문의에 대한 답변이 등록되었습니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">링크 주소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\"><a href=\""+String.format("%s%s%s", WEB_URL, "/errorinq/view?seq=", params.get("seq"))+"\" target=\"_blank\">"+String.format("%s%s%s", WEB_URL, "/errorinq/view?seq=", params.get("seq"))+"</a></td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 원료소싱 등록 메일 (MAIL_SOR_CON1)
    public String getSourcingForm(String id, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 원료소싱 등록 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    새로운 원료 소싱이 발생하였습니다. 아래 링크를 통해 내용 확인해주세요.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">링크 주소</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\"><a href=\""+String.format("%s%s%s", WEB_URL, "/sourcing/view?seq=", params.get("seq"))+"\" target=\"_blank\">"+String.format("%s%s%s", WEB_URL, "/sourcing/view?seq=", params.get("seq"))+"</a></td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }

    // 커뮤니케이션 등록 메일 (MAIL_COM_CON1)
    public String getCommunicationForm(String id, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(this.getTop());
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 50px 40px 0;font-size: 23px;font-weight: bold; color: #333;\">원료정보시스템 커뮤니케이션 등록 안내</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append(" <td style=\"line-height: 1.4;margin: 0;padding: 30px 40px 0;font-size: 17px;color: #666;\"><span style=\"font-size: 19px;font-weight: bold;color: #1461A5;letter-spacing: -0.01em;\">"+id+"</span> 님 안녕하세요.</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.45;margin: 0;padding: 13px 40px 0;font-size: 17px;color: #666;\">");
        sb.append("\n").append("    새로운 커뮤니케이션이 등록되었습니다.");
        sb.append("\n").append("  </td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 21px 40px 0;color: #333;\">");
        sb.append("\n").append("	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\""+STYPE_TABLE+"\">");
        sb.append("\n").append("		<colgroup>");
        sb.append("\n").append("			<col width=\"20%\">");
        sb.append("\n").append("			<col width=\"*\">");
        sb.append("\n").append("		</colgroup>");
        sb.append("\n").append("		<tbody>");
        sb.append("\n").append("			<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("				<th style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-weight: normal;text-align: left;letter-spacing: -0.1em;word-break:keep-all;\">원료명</th>");
        sb.append("\n").append("				<td style=\"line-height: 1.4;margin: 0;padding: 10px 20px;border-bottom: 1px solid #E5E5E5;font-size: 19px;text-align: left;letter-spacing: -0.01em;word-break:keep-all;\"><a href=\""+String.format("%s%s%s", WEB_URL, "/ingredient/view?recordCd=", params.get("seq"))+"\" target=\"_blank\">"+String.format("%s%s%s", WEB_URL, "/ingredient/view?recordCd=", params.get("seq"))+"</a></td>");
        sb.append("\n").append("			</tr>");
        sb.append("\n").append("		</tbody>");
        sb.append("\n").append("	</table>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append("<tr style=\"line-height: 1.6;margin: 0;padding: 0;\">");
        sb.append("\n").append("	<td style=\"line-height: 1.4;margin: 0;padding: 60px 0 100px;text-align: center;\">");
        sb.append("\n").append("		<a href=\""+WEB_URL+"\" style=\"line-height: 1.6;display: inline-block;margin: 0;padding: 10px 40px;border-radius: 2px;background: #1461a5;text-decoration: none;color: #fff;letter-spacing: -0.08em;font-size: 16px;\" rel=\"noreferrer noopener\" target=\"_blank\">원료정보시스템 바로가기</a>");
        sb.append("\n").append("	</td>");
        sb.append("\n").append("</tr>");
        sb.append("\n").append(this.getBottom());
        return sb.toString();
    }
}
