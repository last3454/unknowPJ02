package com.aplab.apsite.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomIdGenerator implements IdentifierGenerator, Configurable{
	
	private String seqName;
	
	private String prefix;
	
	private String digit;
	
	private String pad;
	
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		setSeqName(params.getProperty("seqName"));
		setPrefix(params.getProperty("prefix"));
		setDigit(params.getProperty("digit"));
		setPad(params.getProperty("pad"));
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String ym = this.getYm();
		
		String sql = "SELECT " + this.seqName + ".NEXTVAL FROM DUAL";
        Connection connection = session.connection(); 
        try {
 
            PreparedStatement ps = connection.prepareStatement(sql);
 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("nextval");
                String prefixCode = this.prefix + ym;
                int digitNumber = Integer.parseInt(this.digit);
                String code = "";
                if("L".equals(this.pad)){
                	code = prefixCode + StringUtils.leftPad("" + id, (digitNumber - prefixCode.length()), '0');
                }else{
                	code = prefixCode + StringUtils.rightPad("" + id, (digitNumber - prefixCode.length()), '0');	
                }
                log.debug("####Generated Code####: " + code);
                return code;
            }
 
        } catch (SQLException e) {
            throw new HibernateException("Unable to generate Stock Code Sequence");
        }
		
		return null;
	}

	private void setSeqName(String seqName) {
		if(StringUtils.isEmpty(seqName)) {
			seqName = "";
		}		
		this.seqName = seqName;
	}
	private void setPrefix(String prefix) {
		if(StringUtils.isEmpty(prefix)) {
			prefix = "";
		}		
		this.prefix = prefix;
	}
	private void setDigit(String digit) {
		if(StringUtils.isEmpty(digit)) {
			digit = "20";
		}		
		this.digit = digit;
	}
	private void setPad(String pad) {
		if(StringUtils.isEmpty(pad)) {
			pad = "R";
		}		
		this.pad = pad;
	}
	
	private String getYm() {
		DateFormat df = null;
		if(this.prefix.equals("MA")) {
			df = new SimpleDateFormat("yyyy");
		}else {
			df = new SimpleDateFormat("yyMM",Locale.KOREAN);
		}
		
		Date today = Calendar.getInstance().getTime();
		return df.format(today);
	}
}
