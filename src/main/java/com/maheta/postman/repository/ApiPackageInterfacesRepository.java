package com.maheta.postman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maheta.postman.entity.ApiInterfacesInfo;
import com.maheta.postman.entity.ApiPackageClasses;
import com.maheta.postman.entity.ApiPackageInterfaces;

public interface ApiPackageInterfacesRepository extends JpaRepository<ApiPackageInterfaces, Integer> {

    public ApiPackageInterfaces getByName(final String name);

    @Query(
            value = "SELECT apiPackageInterfaces.name as name,  apiPackageInterfaces.id as id , "
                    + "apiPackageInterfaces.versionAdded as versionAdded, apiPackageInterfaces.description as description, "
                    + "apiPackageInterfaces.reference as reference, apiPackageInterfaces.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageInterfaces apiPackageInterfaces "
                    + "WHERE LOWER(apiPackageInterfaces.name) LIKE (concat('%', :query, '%' )) "
                    + "OR LOWER(apiPackageInterfaces.description) LIKE (concat('%', :query, '%' )) "
    )
    Page<ApiInterfacesInfo> searchAPIs(String query, Pageable pageable);


    @Query(
            value = "SELECT apiPackageInterfaces.name as name,  apiPackageInterfaces.id as id , "
                    + "apiPackageInterfaces.versionAdded as versionAdded, apiPackageInterfaces.description as description, "
                    + "apiPackageInterfaces.reference as reference, apiPackageInterfaces.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageInterfaces apiPackageInterfaces "
                    + "WHERE apiPackageInterfaces.versionAdded < :versionAdded"
    )
    Page<ApiInterfacesInfo> findAllByVersionAddedBefore(int versionAdded, Pageable pageable);

    @Query(
            value = "SELECT apiPackageInterfaces.name as name,  apiPackageInterfaces.id as id , "
                    + "apiPackageInterfaces.versionAdded as versionAdded, apiPackageInterfaces.description as description, "
                    + "apiPackageInterfaces.reference as reference, apiPackageInterfaces.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageInterfaces apiPackageInterfaces "
                    + "WHERE apiPackageInterfaces.versionAdded > :versionAdded"
    )
    Page<ApiInterfacesInfo> findAllByVersionAddedAfter(int versionAdded, Pageable pageable);

    @Query(
            value = "SELECT apiPackageInterfaces.name as name,  apiPackageInterfaces.id as id , "
                    + "apiPackageInterfaces.versionAdded as versionAdded, apiPackageInterfaces.description as description, "
                    + "apiPackageInterfaces.reference as reference, apiPackageInterfaces.apiPackage.packageName as apiPackageName "

                    + "FROM ApiPackageInterfaces apiPackageInterfaces "
                    + "WHERE apiPackageInterfaces.versionAdded = :versionAdded"
    )
    Page<ApiInterfacesInfo> findAllByVersionAdded(int versionAdded, Pageable pageable);
}
