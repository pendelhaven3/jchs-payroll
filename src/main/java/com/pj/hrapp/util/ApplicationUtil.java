package com.pj.hrapp.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil {

    private static ApplicationUtil instance;
    
    @Value("${sundayAsWorkingDay:false}")
    private boolean sundayAsWorkingDay;
    
    @PostConstruct
    public void afterPropertiesSet() {
        instance = this;
    }
    
    public static boolean isSundayAWorkingDay() {
        return instance.sundayAsWorkingDay;
    }
    
}
