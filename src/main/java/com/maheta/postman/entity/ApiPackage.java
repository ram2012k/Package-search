package com.maheta.postman.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "api_package")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property  = "id",
        scope     = Long.class)
public class ApiPackage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "version_added")
    private int versionAdded;
    
    @Column(name = "package_name")
    private String packageName;

    @Column(name = "reference")
    private String reference;

    @OneToMany(mappedBy="apiPackage", cascade = CascadeType.ALL)
    private List<ApiPackageInterfaces> interfacesList;

    @OneToMany(mappedBy="apiPackage", cascade = CascadeType.ALL)
    private List<ApiPackageClasses> classesList;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "framework")
    private String frameWork;

    public ApiPackage() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getVersionAdded() {
        return this.versionAdded;
    }

    public void setVersionAdded(final int versionAdded) {
        this.versionAdded = versionAdded;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public List<ApiPackageInterfaces> getInterfacesList() {
        return this.interfacesList;
    }

    public void setInterfacesList(final List<ApiPackageInterfaces> interfacesList) {
        this.interfacesList = interfacesList;
    }

    public List<ApiPackageClasses> getClassesList() {
        return this.classesList;
    }

    public void setClassesList(final List<ApiPackageClasses> classesList) {
        this.classesList = classesList;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFrameWork() {
        return this.frameWork;
    }

    public void setFrameWork(final String frameWork) {
        this.frameWork = frameWork;
    }
}
