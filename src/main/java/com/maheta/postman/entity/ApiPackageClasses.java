package com.maheta.postman.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name = "api_package_classes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property  = "id",
        scope     = Long.class)
public class ApiPackageClasses {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "version_added")
    private int versionAdded;

    //private List<APIPackageMethod> apiPackageMethods;

    @Column(name = "description")
    private String description;

    @Column(name = "reference")
    private String reference;

    @ManyToOne
    @JoinColumn(name="api_package_id", nullable=false)
    private ApiPackage apiPackage;
    
    public ApiPackageClasses() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getVersionAdded() {
        return this.versionAdded;
    }

    public void setVersionAdded(final int versionAdded) {
        this.versionAdded = versionAdded;
    }

//    public List<APIPackageMethod> getApiPackageMethods() {
//        return this.apiPackageMethods;
//    }
//
//    public void setApiPackageMethods(final List<APIPackageMethod> apiPackageMethods) {
//        this.apiPackageMethods = apiPackageMethods;
//    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public ApiPackage getApiPackage() {
        return this.apiPackage;
    }

    public void setApiPackage(final ApiPackage apiPackage) {
        this.apiPackage = apiPackage;
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof ApiPackageClasses){
            ApiPackageClasses toCompare = (ApiPackageClasses) obj;
            return this.name.equals(toCompare.name);
        }
        return false;
    }
}
