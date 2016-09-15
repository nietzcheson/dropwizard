package com.m4sg.crm4marketingsunset.core.dto;

import java.util.List;

/**
 * Created by Fernando on 19/02/2015.
 */
public class PaginationDTO<E> {

    private final int currentPage;

    private final long totalElements;

    private final int totalPages;

    private final List<E> elements;

    public PaginationDTO(int currentPage, long totalElements, int totalPages, List<E> elements) {
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.elements = elements;
    }

    public PaginationDTO() {
        currentPage = 0;
        totalElements = 0;
        totalPages = 0;
        elements = null;
    }

    public List<E> getElements() {
        return elements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
