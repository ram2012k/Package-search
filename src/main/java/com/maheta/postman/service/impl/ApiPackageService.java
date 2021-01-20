package com.maheta.postman.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.maheta.postman.FrameWork;
import com.maheta.postman.controller.PageRequest;
import com.maheta.postman.entity.ApiClassesInfo;
import com.maheta.postman.entity.ApiInterfacesInfo;
import com.maheta.postman.entity.ApiPackage;
import com.maheta.postman.entity.ApiPackageClasses;
import com.maheta.postman.entity.ApiPackageInterfaces;
import com.maheta.postman.repository.ApiPackageClassesRepository;
import com.maheta.postman.repository.ApiPackageInterfacesRepository;
import com.maheta.postman.repository.ApiPackageRepository;
import com.maheta.postman.service.FetchService;

@Service
public class ApiPackageService {

    public static Logger LOG = Logger.getLogger(ApiPackageService.class);
    
    @Autowired
    private ApiPackageRepository apiPackageRepository;
    
    @Autowired
    private ApiPackageClassesRepository apiPackageClassesRepository;
    
    @Autowired
    private ApiPackageInterfacesRepository apiPackageInterfacesRepository;
    
    @Autowired
    @Qualifier("androidFetchServiceImpl")
    private FetchService androidFetchServiceImpl;
    
    public int reIndex(final FrameWork framework) throws Exception {
        
        this.apiPackageRepository.deleteAllByFrameWork(framework.name());
        
        LOG.info("updating indexes");
        final List<ApiPackage> apiPackageList =  androidFetchServiceImpl.getApiPackages();
        
        return this.apiPackageRepository.saveAll(apiPackageList).size();
        
        
//        for(int i = 0; i < apiPackageList.size(); i++) {
//            final ApiPackage savedApiPackage = this.upserApiPackage(apiPackageList.get(i));
//            LOG.info(savedApiPackage);
//            LOG.info("2 " + apiPackageList.get(i));
//           if (apiPackageList.get(i).getInterfacesList() != null)this.upsertApiPackageInterfaces(apiPackageList.get(i), savedApiPackage);
//           if (apiPackageList.get(i).getClassesList() != null)this.upsertApiPackageClasses(apiPackageList.get(i), savedApiPackage);
//        }
//        
    }

    public int reIndexDescription(final FrameWork frameWork) throws Exception {
        
        final List<ApiPackage> apiPackageList = this.apiPackageRepository.findAllByFrameWork(frameWork.name());
        if (apiPackageList == null || apiPackageList.size() == 0) {
            throw new Exception("Index not exist!!. Please reindex before by reindex API");
        }
        for(ApiPackage apiPackage : apiPackageList ) {
            List<ApiPackageInterfaces> apiPackageInterfacesList = apiPackage.getInterfacesList();
            for(ApiPackageInterfaces apiPackageInterfaces : apiPackageInterfacesList) {
                apiPackageInterfaces.setDescription(androidFetchServiceImpl.getDescriptions(apiPackageInterfaces.getReference()));
            }
            List<ApiPackageClasses> apiPackageClassesList = apiPackage.getClassesList();
            for(ApiPackageClasses apiPackageClasses : apiPackageClassesList) {
                apiPackageClasses.setDescription(androidFetchServiceImpl.getDescriptions(apiPackageClasses.getReference()));
            }
        }
        
        return this.apiPackageRepository.saveAll(apiPackageList).size();
    }

    private void upsertApiPackageClasses(final ApiPackage apiPackage, final ApiPackage savedApiPackage) {
        for(int i = 0 ; i < apiPackage.getClassesList().size(); i++) {
            final ApiPackageClasses apiPackageClasses =  apiPackage.getClassesList().get(i);
            apiPackageClasses.setApiPackage(savedApiPackage);
            LOG.info(apiPackageClasses.getName());
            final ApiPackageClasses savedApiPackageClasses = this.apiPackageClassesRepository.getByName(apiPackageClasses.getName());
            if (savedApiPackageClasses == null) {
                LOG.info(apiPackageClasses.getName());
                this.apiPackageClassesRepository.save(apiPackageClasses);
            } else {
                savedApiPackageClasses.setReference(apiPackageClasses.getReference());
                savedApiPackageClasses.setDescription(apiPackageClasses.getDescription());
                savedApiPackageClasses.setApiPackage(apiPackageClasses.getApiPackage());
                this.apiPackageClassesRepository.save(savedApiPackageClasses);
            }
        }
    }

    private void upsertApiPackageInterfaces(final ApiPackage apiPackage, final ApiPackage savedApiPackage) {
        for(int i = 0 ; i < apiPackage.getInterfacesList().size(); i++) {
            final ApiPackageInterfaces apiPackageInterfaces =  apiPackage.getInterfacesList().get(i);
            apiPackageInterfaces.setApiPackage(savedApiPackage);
            final ApiPackageInterfaces savedApiPackageInterfaces = this.apiPackageInterfacesRepository.getByName(apiPackageInterfaces.getName());
            if (savedApiPackageInterfaces == null) {
                this.apiPackageInterfacesRepository.save(apiPackageInterfaces);
            } else {
                savedApiPackageInterfaces.setReference(apiPackageInterfaces.getReference());
                savedApiPackageInterfaces.setDescription(apiPackageInterfaces.getDescription());
                savedApiPackageInterfaces.setApiPackage(apiPackageInterfaces.getApiPackage());
                this.apiPackageInterfacesRepository.save(savedApiPackageInterfaces);
            }
        }
    }

    
    private ApiPackage upserApiPackage(final ApiPackage apiPackage) {
        final ApiPackage savedApiPackage = this.apiPackageRepository.getByPackageName(apiPackage.getPackageName());
        if (savedApiPackage == null ) {
            return this.apiPackageRepository.save(apiPackage);
        } else {
            savedApiPackage.setClassesList(apiPackage.getClassesList());
            savedApiPackage.setInterfacesList(apiPackage.getInterfacesList());
            savedApiPackage.setDescription(apiPackage.getDescription());
            savedApiPackage.setReference(apiPackage.getReference());
            return this.apiPackageRepository.save(savedApiPackage);
        }
    }

    public Page<ApiPackage> searchAPIPackages(final PageRequest pageRequest) {
        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());
        List<String> frameWorksList ;
        frameWorksList = Stream.of(FrameWork.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if (pageRequest.getFrameWorks() != null && pageRequest.getFrameWorks().size() >= 0) {
            frameWorksList = new ArrayList<>();
            for(int i = 0; i < pageRequest.getFrameWorks().size(); i++) {
             frameWorksList.add(pageRequest.getFrameWorks().get(i).name());   
            }
        } 
        LOG.info(pageRequest.getFrameWorks());
        if (pageRequest.getApiLevel() != -1) {
            return this.apiPackageRepository.searchAPIsWithVersionAdded(pageRequest.getQuery(),
                    frameWorksList,pageRequest.getApiLevel(), pageable);
        }
        final Page<ApiPackage> page = this.apiPackageRepository.searchAPIs(pageRequest.getQuery(), 
                frameWorksList, pageable);
        return page;
    }
    
    public Page<ApiPackage> getAPIPackagesByVersionTimeLine(final PageRequest pageRequest) throws Exception {

        if (pageRequest.getFrameWorks().size() > 1 ) {
            throw new Exception("Please select only one framework at a time");
        } 
        if (pageRequest.getFrameWorks() == null || pageRequest.getFrameWorks().size() == 0) {
            throw new Exception("Framework not selected");
        }

        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());
        final String operator = pageRequest.getOperator();
        LOG.info(operator + " " + pageRequest.getFrameWorks().get(0) );
        if ( operator.trim().equals("<") 
                || operator.trim().equalsIgnoreCase("lt") 
                || operator.trim().equalsIgnoreCase("less than")) {
            return this.apiPackageRepository.findAllByVersionAddedBeforeAndFrameWork(pageRequest.getVersionAdded(), 
                    pageRequest.getFrameWorks().get(0).name(), 
                    pageable);
        } else if ( operator.trim().equals(">")
                || operator.trim().equalsIgnoreCase("gt")
                || operator.trim().equalsIgnoreCase("greater than")) {
            return this.apiPackageRepository.findAllByVersionAddedAfterAndFrameWork(pageRequest.getVersionAdded(),
                    pageRequest.getFrameWorks().get(0).name(),
                    pageable);
        } else if (operator.trim().equals("=")
                || operator.trim().equalsIgnoreCase("eq")
                || operator.trim().equalsIgnoreCase("equals")) {
            return this.apiPackageRepository.findAllByVersionAddedAndFrameWork(pageRequest.getVersionAdded(),
                    pageRequest.getFrameWorks().get(0).name(),
                    pageable);
        } else 
            throw new Exception("Invalid operator");
    }

    public Page<ApiClassesInfo> searchAPIPackagesClasses(final PageRequest pageRequest) {
        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());
        
        LOG.info(pageRequest.getFrameWorks());
        final Page<ApiClassesInfo> page = this.apiPackageClassesRepository.searchAPIs(pageRequest.getQuery(),
                pageable);
        return page;
    }

    public Page<ApiClassesInfo> getAPIPackagesClassesByVersionTimeLine(final PageRequest pageRequest) throws Exception{

        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());
        final String operator = pageRequest.getOperator();
        LOG.info(operator + " operator " );
        if ( operator.trim().equals("<")
                || operator.trim().equalsIgnoreCase("lt")
                || operator.trim().equalsIgnoreCase("less than")) {
            return this.apiPackageClassesRepository.findAllByVersionAddedBefore(pageRequest.getVersionAdded(),
                    pageable);
        } else if ( operator.trim().equals(">")
                || operator.trim().equalsIgnoreCase("gt")
                || operator.trim().equalsIgnoreCase("greater than")) {
            return this.apiPackageClassesRepository.findAllByVersionAddedAfter(pageRequest.getVersionAdded(),
                    pageable);
        } else if (operator.trim().equals("=")
                || operator.trim().equalsIgnoreCase("eq")
                || operator.trim().equalsIgnoreCase("equals")) {
            return this.apiPackageClassesRepository.findAllByVersionAdded(pageRequest.getVersionAdded(),
                    pageable);
        } else
            throw new Exception("Invalid operator");
    }

    public Page<ApiInterfacesInfo> searchAPIPackagesInterfaces(final PageRequest pageRequest) {
        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());

        LOG.info(pageRequest.getFrameWorks());
        final Page<ApiInterfacesInfo> page = this.apiPackageInterfacesRepository.searchAPIs(pageRequest.getQuery(),
                pageable);
        return page;
    }

    public Page<ApiInterfacesInfo> getAPIPackagesInterfacesByVersionTimeLine(final PageRequest pageRequest) throws Exception {
        final Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.Direction.fromString(pageRequest.getSortOrder().name()),
                pageRequest.getSortField());
        final String operator = pageRequest.getOperator();
        LOG.info(operator + " operator " );
        if ( operator.trim().equals("<")
                || operator.trim().equalsIgnoreCase("lt")
                || operator.trim().equalsIgnoreCase("less than")) {
            return this.apiPackageInterfacesRepository.findAllByVersionAddedBefore(pageRequest.getVersionAdded(),
                    pageable);
        } else if ( operator.trim().equals(">")
                || operator.trim().equalsIgnoreCase("gt")
                || operator.trim().equalsIgnoreCase("greater than")) {
            return this.apiPackageInterfacesRepository.findAllByVersionAddedAfter(pageRequest.getVersionAdded(),
                    pageable);
        } else if (operator.trim().equals("=")
                || operator.trim().equalsIgnoreCase("eq")
                || operator.trim().equalsIgnoreCase("equals")) {
            return this.apiPackageInterfacesRepository.findAllByVersionAdded(pageRequest.getVersionAdded(),
                    pageable);
        } else
            throw new Exception("Invalid operator");
    }
    
}
