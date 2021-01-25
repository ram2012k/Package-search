package com.maheta.postman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maheta.postman.FrameWork;
import com.maheta.postman.service.impl.ApiPackageService;


@RestController
@RequestMapping("/app/admin")
public class AdminController {
    @Autowired
    public ApiPackageService apiPackageService;

    @RequestMapping(method = RequestMethod.PUT, value = "/reindex/{framework}")//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private int reindex(@PathVariable("framework") final FrameWork frameWork) throws Exception {
        return apiPackageService.reIndex(frameWork);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/reindex/{framework}/description")//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private int reindexDescription(@PathVariable("framework") final FrameWork frameWork,
                                   @RequestParam("pinCode") final String pinCode) throws Exception {
        if (!pinCode.trim().equals("121042")) {
            throw new Exception("Resource Intensive API!! , please provide correct pinCode");
        }
        return apiPackageService.reIndexDescription(frameWork);
    }
    
}
