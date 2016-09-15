package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Fernando on 30/03/2015.
 */
public class CustomerDTO extends ValidateConstraint {
    @NotNull
    private  Long title;
    @NotEmpty
    private  String firstName;
    @NotEmpty
    private  String lastName;
    private  @Valid
    @NotNull SaleDTO sale;
    @javax.validation.constraints.Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    private  String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @javax.validation.constraints.Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    private  String email2;
   /* @NotBlank
    @Length(min = 2, max = 255)*/
    private Long id;
    private  String phone;
    private  String phone2;
    private  String phone3;
    @NotEmpty
    private  String countryCode;

    private  Long state;
    private  String city;
    private  Integer dOBDay;
    private  Integer dOBMonth;
    private  Integer dOBYear;


    private  String zip;
    private  String address;
    private  Integer[] featureLst;
    @NotEmpty
    private  String userName;
    private  String birthdate;

    private  String occupation;

    private  String statusMarital;
    private Boolean sendMail;
    private EmailDTO emailDTO;


    public CustomerDTO() {
        title = 0l;
        firstName = null;
        lastName = null;
        sale = null;
        email = null;
        phone = null;
        phone2 = null;
        phone3 = null;
        countryCode = null;
        state = 0l;
        city = null;
        zip = null;
        address = null;
        featureLst = null;
        userName = null;
        birthdate = null;
        occupation = null;
        statusMarital=null;
        dOBDay=null;
        dOBMonth=null;
        dOBYear=null;
        emailDTO=null;
        sendMail=null;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SaleDTO getSale() {
        return sale;
    }

    public String getEmail() {
        return email;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPhone() {
        return phone;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPhone2() {
        return phone2;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPhone3() {
        return phone3;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getCountryCode() {
        return countryCode;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getState() {
        return state;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getCity() {
        return city;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getZip() {
        return zip;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getAddress() {
        return address;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer[] getFeatureLst() {
        return featureLst;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getUserName() {
        return userName;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getBirthdate() {
        return birthdate;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getOccupation() {
        return occupation;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getStatusMarital() {
        return statusMarital;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getdOBDay() {
        return dOBDay;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getdOBMonth() {
        return dOBMonth;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getdOBYear() {
        return dOBYear;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public EmailDTO getEmailDTO() {
        return emailDTO;
    }

    public void setEmailDTO(EmailDTO emailDTO) {
        this.emailDTO = emailDTO;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getSendMail() {
        return sendMail;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public void setTitle(Long title) {
        this.title = title;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSale(SaleDTO sale) {
        this.sale = sale;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setdOBDay(Integer dOBDay) {
        this.dOBDay = dOBDay;
    }

    public void setdOBMonth(Integer dOBMonth) {
        this.dOBMonth = dOBMonth;
    }

    public void setdOBYear(Integer dOBYear) {
        this.dOBYear = dOBYear;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFeatureLst(Integer[] featureLst) {
        this.featureLst = featureLst;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setStatusMarital(String statusMarital) {
        this.statusMarital = statusMarital;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        return null;
    }
}
