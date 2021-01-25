package com.maheta.postman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maheta.postman.FrameWork;
import com.maheta.postman.entity.ApiPackage;

@Repository
public interface ApiPackageRepository extends JpaRepository<ApiPackage, Integer> {

    public ApiPackage getByPackageName(final String name);

    
    List<ApiPackage> findAllByFrameWork( String frameWork);

    @Transactional
    public int deleteAllByFrameWork(final String framework);

    @Query(
            value = "SELECT apiPackage "
            
            + "FROM ApiPackage apiPackage "
            + "WHERE frameWork in (:frameWorks)  AND (LOWER(packageName) LIKE (concat('%', :query, '%' )) "
            + "OR LOWER(description) LIKE (concat('%', :query, '%' ))) "
            //+  "AND "
    )
    Page<ApiPackage> searchAPIs(String query, List<String> frameWorks, Pageable pageable);
    
    Page<ApiPackage> findAllByVersionAddedAfterAndFrameWork(int versionAdded,  String framework, Pageable pageable);

    Page<ApiPackage> findAllByVersionAddedBeforeAndFrameWork(int versionAdded,  String framework, Pageable pageable);

    Page<ApiPackage> findAllByVersionAddedAndFrameWork(int versionAdded,  String framework, Pageable pageable);

    @Query(
            value = "SELECT apiPackage "

                    + "FROM ApiPackage apiPackage "
                    + "WHERE frameWork in (:frameWorks)  AND (LOWER(packageName) LIKE (concat('%', :query, '%' )) "
                    + "OR LOWER(description) LIKE (concat('%', :query, '%' ))) AND versionAdded =  :apiLevel"
            //+  "AND "
    )
    Page<ApiPackage> searchAPIsWithVersionAdded(String query, List<String> frameWorks, final int apiLevel, Pageable pageable);
}
