package com.maheta.postman.service;

import java.io.IOException;
import java.util.List;

import com.maheta.postman.entity.ApiPackage;

public interface FetchService {
    List<ApiPackage> getApiPackages() throws Exception;

    String getDescriptions(String reference) throws IOException;
}
