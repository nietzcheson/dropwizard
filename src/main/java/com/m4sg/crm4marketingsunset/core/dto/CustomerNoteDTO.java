package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.m4c.model.entity.CustomerNote;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by desarrollo1 on 07/05/2015.
 */
public class CustomerNoteDTO extends ValidateConstraint {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final @Valid
    AuditDTO audit ;
    private  Long id;
    private  Long idCliente;
    @NotNull
    private String description;
    private Date date;
    public String remindTOusername;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long customer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long pk;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private  String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private  Long noteType;
    private  String type;
    @NotNull
    private  String mood;
    private String specialReq;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date contactDate;
    private  Long note_noteId;

    public CustomerNoteDTO(){
        this.id=null;
        this.idCliente=null;
        this.type=null;
        this.customer=null;
        this.mood=null;
        this.number=null;
        this.pk=null;
        this.status=null;
        this.noteType=null;
        this.description=null;
        this.username=null;
        this.audit=null;
        this.note_noteId=null;
    }

    public Long getCustomer() {
        return customer;
    }

    public Integer getNumber() {
        return number;
    }

    public Long getPk() {
        return pk;
    }

    public String getStatus() {
        return status;
    }

    public Long getNoteType() {
        return noteType;
    }



    public String getSpecialReq() {
        return specialReq;
    }

    public void setSpecialReq(String specialReq) {
        this.specialReq = specialReq;
    }
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
    public Date getContactDate() {
        return contactDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNoteType(Long noteType) {
        this.noteType = noteType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }


    public Long getId() {
        return id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public String getType() {
        return type;
    }

    public String getMood() {
        return mood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemindTOusername() {
        return remindTOusername;
    }

    public void setRemindTOusername(String remindTOusername) {
        this.remindTOusername = remindTOusername;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public Long getNote_noteId() {
        return note_noteId;
    }

    public void setNote_noteId(Long note_noteId) {
        this.note_noteId = note_noteId;
    }

    @JsonIgnore
    @Override
    public List<ErrorRepresentation> validate() {
        return validationMessages;
    }
}
