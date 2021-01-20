package com.maheta.postman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maheta.postman.entity.ApiClassesInfo;
import com.maheta.postman.entity.ApiPackageClasses;

public interface ApiPackageClassesRepository extends JpaRepository<ApiPackageClasses, Integer> {
    
    public ApiPackageClasses getByName(final String name);

    @Query(
            value = "SELECT apiPackageClasses.name as name,  apiPackageClasses.id as id , "
                    + "apiPackageClasses.versionAdded as versionAdded, apiPackageClasses.description as description, "
                    + "apiPackageClasses.reference as reference, apiPackageClasses.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageClasses apiPackageClasses "
                    + "WHERE LOWER(apiPackageClasses.name) LIKE (concat('%', :query, '%' )) "
                    + "OR LOWER(apiPackageClasses.description) LIKE (concat('%', :query, '%' )) "
    )
    Page<ApiClassesInfo> searchAPIs(String query, Pageable pageable);

    @Query(
            value = "SELECT apiPackageClasses.name as name,  apiPackageClasses.id as id , "
                    + "apiPackageClasses.versionAdded as versionAdded, apiPackageClasses.description as description, "
                    + "apiPackageClasses.reference as reference, apiPackageClasses.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageClasses apiPackageClasses "
                    + "WHERE apiPackageClasses.versionAdded < :versionAdded"
    )
    Page<ApiClassesInfo> findAllByVersionAddedBefore(int versionAdded, Pageable pageable);

    @Query(
            value = "SELECT apiPackageClasses.name as name,  apiPackageClasses.id as id , "
                    + "apiPackageClasses.versionAdded as versionAdded, apiPackageClasses.description as description, "
                    + "apiPackageClasses.reference as reference, apiPackageClasses.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageClasses apiPackageClasses "
                    + "WHERE apiPackageClasses.versionAdded > :versionAdded"
    )
    Page<ApiClassesInfo> findAllByVersionAddedAfter(int versionAdded, Pageable pageable);

    @Query(
            value = "SELECT apiPackageClasses.name as name,  apiPackageClasses.id as id , "
                    + "apiPackageClasses.versionAdded as versionAdded, apiPackageClasses.description as description, "
                    + "apiPackageClasses.reference as reference, apiPackageClasses.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageClasses apiPackageClasses "
                    + "WHERE apiPackageClasses.versionAdded = :versionAdded"
    )
    Page<ApiClassesInfo> findAllByVersionAdded(int versionAdded, Pageable pageable);
}
