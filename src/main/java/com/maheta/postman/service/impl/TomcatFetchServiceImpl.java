package com.maheta.postman.service.impl;

import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.print.Doc;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.maheta.postman.FrameWork;
import com.maheta.postman.entity.ApiPackage;
import com.maheta.postman.entity.ApiPackageClasses;
import com.maheta.postman.entity.ApiPackageInterfaces;
import com.maheta.postman.service.FetchService;

@Service
public class TomcatFetchServiceImpl implements FetchService {

    public static Logger LOG = Logger.getLogger(TomcatFetchServiceImpl.class);
    static int maxApiLevel ;

    public static final String CLASSES = "Classes";
    public static final String INTERFACES = "Interfaces";
    public static final String ROOT_URL = "https://tomcat.apache.org/";
    public static final String OVERVIEW_URL = "https://tomcat.apache.org/{0}/api/overview-summary.html";
    public static final String ALL_CLASSES_URL = "https://tomcat.apache.org/{0}/api/allclasses-frame.html";
    public static final String INDEX_ALL = "https://tomcat.apache.org/{0}/api/index-all.html";
    
    public static final List<Integer> API_VERSIONS = Arrays.asList(new Integer[]{7, 8, 9, 10 });
    public static final List<String> API_VERSIONS_ADDRESS = Arrays.asList(new String[]{"tomcat-7.0-doc", "tomcat-8.5-doc",
       "tomcat-9.0-doc","tomcat-10.0-doc" });
    public static final String url = "/api/index.html";
    public static final String index = "index";
    

    private Document getDocument(final String url2) throws IOException {
        LOG.info("Getting Document " + url2);
        return Jsoup.connect(url2)
                .userAgent("PostmanRuntime/7.20.1")
                .referrer("www.google.com")
                .maxBodySize(0)
                .timeout(20000)
                .get();

    }

    @Override
    public List<ApiPackage> getApiPackages() throws Exception {
        List<ApiPackage> apiPackageList = new ArrayList<>();
        
        for(int version = 0; version < API_VERSIONS.size() ; version++) {
            final int apiVersion = API_VERSIONS.get(version);
            
            final MessageFormat format = new MessageFormat(OVERVIEW_URL);
            final String location = API_VERSIONS_ADDRESS.get(version);
            final Document overview = getDocument(format.format(new String[]{location}));
            
            final Elements packageNameList = overview.select("table.overviewSummary > tbody > tr.altColor,  tr.rowColor ");
            LOG.info(packageNameList.size());
            for(Element element : packageNameList) {
                final String packageName = element.select("td.colFirst").text();
                final String reference = element.select("td.colFirst > a").attr("href");
                final String description =  element.select("td.colLast").text();
                 ApiPackage apiPackage = new ApiPackage();
                 
                 apiPackage.setPackageName(packageName);
                 apiPackage.setVersionAdded(apiVersion);
                 apiPackage.setReference(ROOT_URL + location + "/api/" + reference);
                 apiPackage.setDescription(description);
                 apiPackage.setFrameWork(FrameWork.TOMCAT.name());
                 
                // LOG.info(apiPackage.getPackageName());
                if (apiPackageList.contains(apiPackage) == false) {
                    apiPackageList.add(apiPackage);
                } 
                
            } // apipackage scan

            final MessageFormat classUrlFormat = new MessageFormat(ALL_CLASSES_URL);
            final  Document classesInterface = getDocument(classUrlFormat.format(new String[]{location}));
            setClasses(classesInterface, apiPackageList, apiVersion, location);
            setInterfaces(classesInterface, apiPackageList, apiVersion, location);
            
            final MessageFormat descriptionURLFormat = new MessageFormat(INDEX_ALL);
            final Document description = getDocument(descriptionURLFormat.format(new String[]{location}));
            LOG.info("Document downloaded");
            setDescription(description, apiPackageList);

        }
        
        return apiPackageList;
    }

    private void setDescription(final Document document, final List<ApiPackage> apiPackageList) {
        final Elements dts = document.select("dt");
        
        for(Element dt : dts) {
            final Element dd = dt.nextElementSibling();
            
            if (dt.text().contains("- Class in")) {
                //LOG.info("dd " + dd.text().replace("\u00a0",""));
                final String name = dt.select("span.strong").text();
                final String packageName = dt.select("a[href*=package-summary.html]").text();
                final String description = dd.text().replace("\u00a0","");
                
                final ApiPackageClasses apiPackageClasses = new ApiPackageClasses();
                apiPackageClasses.setName(name);
                final ApiPackage apiPackage = new ApiPackage();
                apiPackage.setPackageName(packageName);
                final ApiPackage originalPackage = apiPackageList.get(apiPackageList.indexOf(apiPackage));
                if (originalPackage.getClassesList().indexOf(apiPackageClasses) != -1) {
                    originalPackage.getClassesList().get(originalPackage.getClassesList().indexOf(apiPackageClasses))
                            .setDescription(description);
                }
                
                //LOG.info("packageName "  +packageName);
            } else if (dt.text().contains("- Interface in")) {
               // LOG.info("dd " + dd.text().replace("\u00a0",""));
                final String name = dt.select("span.strong").text();
                final String packageName = dt.select("a[href*=package-summary.html]").text();
                final String description = dd.text().replace("\u00a0","");

                final ApiPackageInterfaces apiPackageInterfaces = new ApiPackageInterfaces();
                apiPackageInterfaces.setName(name);
                final ApiPackage apiPackage = new ApiPackage();
                apiPackage.setPackageName(packageName);
                final ApiPackage originalPackage = apiPackageList.get(apiPackageList.indexOf(apiPackage));
                
                if (originalPackage.getInterfacesList().indexOf(apiPackageInterfaces) != -1) {
                    originalPackage.getInterfacesList().get(originalPackage.getInterfacesList().indexOf(apiPackageInterfaces))
                            .setDescription(description);
                }
                
                //LOG.info("packageName "  +packageName);

            }
        }
        
       
    }

    private void setClasses(final Document document, final List<ApiPackage> apiPackage, final int apiVersion, final String location) throws IOException{
        
        final Elements classes = document.select("div.indexContainer > ul > li");
        

        for(int i = 0; i < classes.size(); i++) {
            ApiPackageClasses apiPackageClasses = new ApiPackageClasses();
            apiPackageClasses.setVersionAdded(apiVersion);
            apiPackageClasses.setName(classes.get(i).text());
            apiPackageClasses.setReference(ROOT_URL + location + "/api/" +  classes.get(i).select("a").attr("href"));
            final String text = classes.get(i).select("a").attr("title");
            
            if (text.startsWith("class in ") == true) {
                final String packageName = text.trim().substring(("class in ").length());
                final ApiPackage pkg = new ApiPackage();
                pkg.setPackageName(packageName);

                final ApiPackage origPackage = apiPackage.get(apiPackage.indexOf(pkg));
                apiPackageClasses.setApiPackage(origPackage);
                //LOG.info(origPackage.getPackageName());
                if (origPackage.getClassesList() == null) {
                    origPackage.setClassesList(new ArrayList<>());
                }
                if (origPackage.getClassesList().contains(apiPackageClasses) == false) {
                    origPackage.getClassesList().add(apiPackageClasses);
                }
            }
            
            //apiPackageClasses.setDescription(getDescriptions(apiPackageClasses.getReference()));
        }
       
    }

    private void setInterfaces(final Document document, final List<ApiPackage> apiPackageList, final int apiVersion, final String location) throws IOException {
        final Elements interfaces = document.select("div.indexContainer > ul > li");


        for(int i = 0; i < interfaces.size(); i++) {
            ApiPackageInterfaces apiPackageInterfaces = new ApiPackageInterfaces();
            apiPackageInterfaces.setVersionAdded(apiVersion);
            apiPackageInterfaces.setName(interfaces.get(i).text());
            apiPackageInterfaces.setReference(ROOT_URL + location + "/api/" +  interfaces.get(i).select("a").attr("href"));
            final String text = interfaces.get(i).select("a").attr("title");

            if (text.startsWith("interface in ") == true) {
                final String packageName = text.trim().substring(("interface in ").length());
                final ApiPackage pkg = new ApiPackage();
                pkg.setPackageName(packageName);

                final ApiPackage origPackage = apiPackageList.get(apiPackageList.indexOf(pkg));
                apiPackageInterfaces.setApiPackage(origPackage);
                if (origPackage.getInterfacesList() == null) {
                    origPackage.setInterfacesList(new ArrayList<>());
                }
                if (origPackage.getInterfacesList().contains(apiPackageInterfaces) == false) {
                    origPackage.getInterfacesList().add(apiPackageInterfaces);
                }
            }

            //apiPackageClasses.setDescription(getDescriptions(apiPackageClasses.getReference()));
        }

    }

    @Override
    public String getDescriptions(final String reference) throws IOException {
        return null;
    }
}
