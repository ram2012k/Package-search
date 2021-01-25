package com.maheta.postman.controller;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.maheta.postman.FrameWork;
import com.maheta.postman.SortOrder;

public class PageRequest {

    private static final int MAX_PAGE_SIZE = 1000000;
    private static final int MAX_PAGE_NUMBER = MAX_PAGE_SIZE;

    @Min(value = 0)
    @Max(value = MAX_PAGE_SIZE)
    protected int pageSize;

    @Min(value = 0)
    @Max(value = MAX_PAGE_NUMBER)
    protected int pageNumber;

    @Size(min = 1, max = 320)
    protected String sortField;

    protected SortOrder sortOrder;

    @Size(max = 2000)
    protected String query = "";
    
    protected List<FrameWork> frameWorks;
    
    protected String operator;

    @Min(value = 0)
    protected int versionAdded;

    @Min(value = 0)
    protected int apiLevel = -1;
    

    public PageRequest() {
        this(MAX_PAGE_SIZE, SortOrder.ASC, "=", "id");
    }

    public PageRequest(final int pageSize, final SortOrder sortOrder, final String operator, final String sortField) {
        this.pageSize = pageSize;
        this.sortOrder = sortOrder;
        this.operator = operator;
        this.sortField = sortField;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(final String sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setSortOrder(final String sortOrder) {
        this.sortOrder = SortOrder.valueOf(sortOrder.toUpperCase());
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<FrameWork> getFrameWorks() {
        return this.frameWorks;
    }

    public void setFrameWorks(final List<FrameWork> frameWorks) {
        this.frameWorks = frameWorks;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public int getVersionAdded() {
        return this.versionAdded;
    }

    public void setVersionAdded(final int versionAdded) {
        this.versionAdded = versionAdded;
    }

    public int getApiLevel() {
        return this.apiLevel;
    }

    public void setApiLevel(final int apiLevel) {
        this.apiLevel = apiLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pageSize", pageSize)
                .append("pageNumber", pageNumber)
                .append("sortField", sortField)
                .append("sortOrder", sortOrder)
                .append("query", query)
                .toString();
    }
}
