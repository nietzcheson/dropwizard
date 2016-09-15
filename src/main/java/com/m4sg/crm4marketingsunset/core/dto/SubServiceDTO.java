package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Fernando on 11/08/2015.
 */
public class SubServiceDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idsubservicio ;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private Double amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer adults;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer minor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuditDTO audit;


    public SubServiceDTO() {
    }

    public SubServiceDTO(Long id, String name, Double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public void setAudit(AuditDTO audit) {
        this.audit = audit;
    }

    public Long getIdsubservicio() {
        return idsubservicio;
    }

    public void setIdsubservicio(Long idsubservicio) {
        this.idsubservicio = idsubservicio;
    }
}
