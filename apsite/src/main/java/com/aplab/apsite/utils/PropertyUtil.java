package com.aplab.apsite.utils;

import org.springframework.core.env.Environment;

import com.aplab.apsite.config.ApplicationConfig;

public class PropertyUtil {
	//해당 spring Ioc를 이용하지 않고 new 로 객체를 주입한 경우에 사용하기 위해 만든 부분
    private static Environment environment(){
        return ApplicationConfig.getApplicationContext().getEnvironment();
    }

    public static String getProperty(String key){
        return environment().getProperty(key);
    }
}
