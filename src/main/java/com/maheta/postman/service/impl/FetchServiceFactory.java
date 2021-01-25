package com.maheta.postman.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.maheta.postman.FrameWork;
import com.maheta.postman.service.FetchService;

@Component
public class FetchServiceFactory {
    
    @Autowired
    @Qualifier("androidFetchServiceImpl")
    private FetchService androidFetchServiceImpl;

    @Autowired
    @Qualifier("tomcatFetchServiceImpl")
    private FetchService tomcatFetchServiceImpl;

    /**
     * Autowired other {@link FetchService} implements here
     */
    public static final Map<FrameWork, FetchService> fetchServiceMap = new HashMap<>();
    
    
    @PostConstruct
    public void doPost() {
        /**
         * Add {@link FrameWork} - {@link FetchService}  implementation in fetchServiceMap
         */
        fetchServiceMap.put(FrameWork.ANDROID, androidFetchServiceImpl);
        fetchServiceMap.put(FrameWork.TOMCAT, tomcatFetchServiceImpl);
    }
    
    public static FetchService getFetchService(final  FrameWork frameWork) {
        return fetchServiceMap.get(frameWork);
    }
    
}
