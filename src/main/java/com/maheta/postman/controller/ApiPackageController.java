package com.maheta.postman.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.maheta.postman.FrameWork;
import com.maheta.postman.entity.ApiClassesInfo;
import com.maheta.postman.entity.ApiInterfacesInfo;
import com.maheta.postman.entity.ApiPackage;
import com.maheta.postman.entity.ApiPackageClasses;
import com.maheta.postman.service.impl.ApiPackageService;

@RestController
@RequestMapping("/app/packages")
public class ApiPackageController {

    @Autowired
    public ApiPackageService apiPackageService;

    /**
     * Search Api's
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/apiPackage", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiPackage> getApi(@Valid @ModelAttribute final PageRequest pageRequest) {
        return this.apiPackageService.searchAPIPackages(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apiPackageVersion", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiPackage> getByApiVersionAdded(@Valid @ModelAttribute final PageRequest pageRequest) throws Exception {
        return this.apiPackageService.getAPIPackagesByVersionTimeLine(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apiPackageClasses", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiClassesInfo> getApiPackageClasses(@Valid @ModelAttribute final PageRequest pageRequest) {
        return this.apiPackageService.searchAPIPackagesClasses(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apiPackageClassesVersion", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiClassesInfo> getClassesByApiVersionAdded(@Valid @ModelAttribute final PageRequest pageRequest) throws Exception {
        return this.apiPackageService.getAPIPackagesClassesByVersionTimeLine(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apiPackageInterfaces", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiInterfacesInfo> getApiPackageInterfaces(@Valid @ModelAttribute final PageRequest pageRequest) {
        return this.apiPackageService.searchAPIPackagesInterfaces(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apiPackageInterfacesVersion", produces = MediaType.APPLICATION_JSON_VALUE)//, consumes = MediaType.APPLICATION_JSON_VALUE, )
    private Page<ApiInterfacesInfo> getClassesByApiVersionInterfaces(@Valid @ModelAttribute final PageRequest pageRequest) throws Exception {
        return this.apiPackageService.getAPIPackagesInterfacesByVersionTimeLine(pageRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/frameworks", produces = MediaType.APPLICATION_JSON_VALUE)//
    private List<FrameWork> getAvailableFrameworks() {
        return Arrays.asList(FrameWork.values());
    }
}
