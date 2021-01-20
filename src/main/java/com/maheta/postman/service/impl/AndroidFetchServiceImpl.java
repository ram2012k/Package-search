package com.maheta.postman.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
public class AndroidFetchServiceImpl implements FetchService {

    public static Logger LOG = Logger.getLogger(AndroidFetchServiceImpl.class);
    static int maxApiLevel ;

    public static final String CLASSES = "Classes";
    public static final String INTERFACES = "Interfaces";
    public static final String ROOT_URL = "https://developer.android.com";
    public static final String url2 = ROOT_URL + "/reference/packages";
    
    @PostConstruct
    public void init() {
        LOG.setLevel(Level.INFO);
        BasicConfigurator.configure();
    }
    
    
    @Override
    public List<ApiPackage> getApiPackages() throws IOException {
        Document doc = this.getDocument(url2);

        Elements packageElementsList = doc.select("li[data-version-added].devsite-nav-expandable");

        Elements packageInfoList = doc.select("div.devsite-article-body.clearfix.devsite-no-page-title > table > tbody > tr");

        maxApiLevel = doc.select("select.devsite-version-selector-select > option").size();

        Map<String, String> packageOverview = getPackagesOverview(packageInfoList);

        LOG.info(packageOverview.size());

        List<ApiPackage> apiPackages = getPackagesInfo(packageElementsList);

        for(int i = 0; i < apiPackages.size(); i++) {
            apiPackages.get(i).setDescription(packageOverview.get(apiPackages.get(i).getPackageName()));
        }
        return apiPackages;
    }

    private Document getDocument(final String url2) throws IOException {
        LOG.info("Getting Document");
        return Jsoup.connect(url2)
                .userAgent("PostmanRuntime/7.20.1")
                .referrer("www.google.com")
                .maxBodySize(0)
                .timeout(10000)
                .get();
        
    }

    private List<ApiPackage> getPackagesInfo(final Elements packageElementsList) throws IOException {
        List<ApiPackage> apiPackageList = new ArrayList<>();

        for(int i = 0; i < packageElementsList.size(); i++ ) {
            Element element = packageElementsList.get(i);

            final Elements parts = element.select("devsite-expandable-nav > div > span");
            final String name = parts.first().text();
            final String versionAdded = element.attr("data-version-added");
            final String reference = element.select("devsite-expandable-nav > ul.devsite-nav-section > li.devsite-nav-item > a")
                    .attr("href");


            ApiPackage apiPackage = new ApiPackage();
            apiPackage.setPackageName(name);
            if (versionAdded.equals("REL")) {
                apiPackage.setVersionAdded(maxApiLevel);
            } else {
                apiPackage.setVersionAdded(Integer.parseInt(versionAdded));
            }

            apiPackage.setReference(ROOT_URL + reference);
            apiPackage.setFrameWork(FrameWork.ANDROID.name());

            for(int part = 1; part < parts.size(); part++) {
                if (parts.get(part).text().equals(INTERFACES)) {
                    apiPackage.setInterfacesList(this.getInterfaces(parts.get(part), apiPackage));
                }
                if (parts.get(part).text().equals(CLASSES)) {
                    apiPackage.setClassesList(this.getClasses(parts.get(part), apiPackage));
                }
            }

            apiPackageList.add(apiPackage);

        }
        return apiPackageList;
    }

    @Override
    public String getDescriptions(final String reference) throws IOException {
        Document doc = getDocument(reference);
        final StringBuilder stringBuilder = new StringBuilder();
        Elements para = doc.select("br + hr + p");
        while(para != null && para.first() != null && para.first().tagName().equals("p")) {
            stringBuilder.append(para.first().text());
            stringBuilder.append("\n");
            para = para.next();
        }
        return stringBuilder.toString();
    }

    private List<ApiPackageClasses> getClasses(final Element element, final ApiPackage apiPackage) throws IOException{
        final Element parent = element.parent().parent();
        final Elements classes = parent.select("ul > li");
        List<ApiPackageClasses> apiPackageClassesList = new ArrayList<>();

        for(int i = 0; i < classes.size(); i++) {
            ApiPackageClasses apiPackageClasses = new ApiPackageClasses();
            apiPackageClasses.setName(classes.get(i).select("a > span").text());
            apiPackageClasses.setReference(ROOT_URL + classes.get(i).select("a").attr("href"));
            final String versionAdded = classes.get(i).attr("data-version-added");
            if (versionAdded.equals("REL")) {
                apiPackageClasses.setVersionAdded(maxApiLevel);
            } else {
                if (versionAdded.trim().length() == 0 ) {
                    apiPackageClasses.setVersionAdded(1);
                }
                else apiPackageClasses.setVersionAdded(Integer.parseInt(versionAdded));
            }
            
            apiPackageClasses.setApiPackage(apiPackage);
            //apiPackageClasses.setDescription(getDescriptions(apiPackageClasses.getReference()));
            apiPackageClassesList.add(apiPackageClasses);
        }
        return apiPackageClassesList;
    }

    private List<ApiPackageInterfaces> getInterfaces(final Element element, final ApiPackage apiPackage) throws IOException {
        final Element parent = element.parent().parent();
        final Elements classes = parent.select("ul > li");
        List<ApiPackageInterfaces> apiPackageInterfacesList = new ArrayList<>();

        for(int i = 0; i < classes.size(); i++) {
            ApiPackageInterfaces apiPackageInterfaces = new ApiPackageInterfaces();
            apiPackageInterfaces.setName(classes.get(i).select("a > span").text());
            apiPackageInterfaces.setReference(ROOT_URL + classes.get(i).select("a").attr("href"));
            final String versionAdded = classes.get(i).attr("data-version-added");
            if (versionAdded.equals("REL")) {
                apiPackageInterfaces.setVersionAdded(maxApiLevel);
            } else {
                if (versionAdded.trim().length() == 0 ) {
                    apiPackageInterfaces.setVersionAdded(1);
                }
                apiPackageInterfaces.setVersionAdded(Integer.parseInt(versionAdded));
            }

            apiPackageInterfaces.setApiPackage(apiPackage);
            //apiPackageInterfaces.setDescription(getDescriptions(apiPackageInterfaces.getReference()));
            apiPackageInterfacesList.add(apiPackageInterfaces);
        }
        return apiPackageInterfacesList;
    }

    private static Map<String, String> getPackagesOverview(final Elements packageInfoElements) {
        Map<String, String> packagesInfo = new HashMap<>();

        for(int i = 0; i < packageInfoElements.size(); i++) {
            Element element = packageInfoElements.get(i);
            String packageName = element.select("td.jd-linkcol > a").text();
            String packageOverview = element.select("td.jd-descrcol > p, td.jd-descrcol").text();
            packagesInfo.put(packageName, packageOverview);
        }

        return packagesInfo;
    }
}


/*
remaining item 
update table properly on reindex ^
update description classes and interfaces 
 */