
package com.app.smartwatchapplication.Modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable
{

    @SerializedName("v_id")
    @Expose
    private String vId;
    @SerializedName("d_name")
    @Expose
    private String dName;
    @SerializedName("d_mobile")
    @Expose
    private String dMobile;
    @SerializedName("d_address")
    @Expose
    private String dAddress;
    @SerializedName("d_age")
    @Expose
    private String dAge;
    @SerializedName("d_licenseno")
    @Expose
    private String dLicenseno;
    @SerializedName("d_license_expdate")
    @Expose
    private String dLicenseExpdate;
    @SerializedName("d_doj")
    @Expose
    private String dDoj;
    @SerializedName("d_ref")
    @Expose
    private String dRef;
    @SerializedName("v_registration_no")
    @Expose
    private String vRegistrationNo;
    @SerializedName("v_name")
    @Expose
    private String vName;
    @SerializedName("v_model")
    @Expose
    private String vModel;
    @SerializedName("v_chassis_no")
    @Expose
    private String vChassisNo;
    @SerializedName("v_engine_no")
    @Expose
    private String vEngineNo;
    @SerializedName("v_manufactured_by")
    @Expose
    private String vManufacturedBy;
    @SerializedName("v_type")
    @Expose
    private String vType;
    @SerializedName("v_color")
    @Expose
    private String vColor;
    @SerializedName("v_mileageperlitre")
    @Expose
    private Object vMileageperlitre;
    @SerializedName("v_is_active")
    @Expose
    private String vIsActive;
    @SerializedName("v_group")
    @Expose
    private String vGroup;
    @SerializedName("v_school")
    @Expose
    private String vSchool;
    @SerializedName("v_reg_exp_date")
    @Expose
    private String vRegExpDate;
    @SerializedName("v_api_url")
    @Expose
    private String vApiUrl;
    @SerializedName("v_api_username")
    @Expose
    private String vApiUsername;
    @SerializedName("v_api_password")
    @Expose
    private String vApiPassword;
    @SerializedName("v_created_by")
    @Expose
    private String vCreatedBy;
    @SerializedName("v_created_date")
    @Expose
    private String vCreatedDate;
    @SerializedName("v_modified_date")
    @Expose
    private String vModifiedDate;

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdMobile() {
        return dMobile;
    }

    public void setdMobile(String dMobile) {
        this.dMobile = dMobile;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getdAge() {
        return dAge;
    }

    public void setdAge(String dAge) {
        this.dAge = dAge;
    }

    public String getdLicenseno() {
        return dLicenseno;
    }

    public void setdLicenseno(String dLicenseno) {
        this.dLicenseno = dLicenseno;
    }

    public String getdLicenseExpdate() {
        return dLicenseExpdate;
    }

    public void setdLicenseExpdate(String dLicenseExpdate) {
        this.dLicenseExpdate = dLicenseExpdate;
    }

    public String getdDoj() {
        return dDoj;
    }

    public void setdDoj(String dDoj) {
        this.dDoj = dDoj;
    }

    public String getdRef() {
        return dRef;
    }

    public void setdRef(String dRef) {
        this.dRef = dRef;
    }

    public String getvRegistrationNo() {
        return vRegistrationNo;
    }

    public void setvRegistrationNo(String vRegistrationNo) {
        this.vRegistrationNo = vRegistrationNo;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvModel() {
        return vModel;
    }

    public void setvModel(String vModel) {
        this.vModel = vModel;
    }

    public String getvChassisNo() {
        return vChassisNo;
    }

    public void setvChassisNo(String vChassisNo) {
        this.vChassisNo = vChassisNo;
    }

    public String getvEngineNo() {
        return vEngineNo;
    }

    public void setvEngineNo(String vEngineNo) {
        this.vEngineNo = vEngineNo;
    }

    public String getvManufacturedBy() {
        return vManufacturedBy;
    }

    public void setvManufacturedBy(String vManufacturedBy) {
        this.vManufacturedBy = vManufacturedBy;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvColor() {
        return vColor;
    }

    public void setvColor(String vColor) {
        this.vColor = vColor;
    }

    public Object getvMileageperlitre() {
        return vMileageperlitre;
    }

    public void setvMileageperlitre(Object vMileageperlitre) {
        this.vMileageperlitre = vMileageperlitre;
    }

    public String getvIsActive() {
        return vIsActive;
    }

    public void setvIsActive(String vIsActive) {
        this.vIsActive = vIsActive;
    }

    public String getvGroup() {
        return vGroup;
    }

    public void setvGroup(String vGroup) {
        this.vGroup = vGroup;
    }

    public String getvSchool() {
        return vSchool;
    }

    public void setvSchool(String vSchool) {
        this.vSchool = vSchool;
    }

    public String getvRegExpDate() {
        return vRegExpDate;
    }

    public void setvRegExpDate(String vRegExpDate) {
        this.vRegExpDate = vRegExpDate;
    }

    public String getvApiUrl() {
        return vApiUrl;
    }

    public void setvApiUrl(String vApiUrl) {
        this.vApiUrl = vApiUrl;
    }

    public String getvApiUsername() {
        return vApiUsername;
    }

    public void setvApiUsername(String vApiUsername) {
        this.vApiUsername = vApiUsername;
    }

    public String getvApiPassword() {
        return vApiPassword;
    }

    public void setvApiPassword(String vApiPassword) {
        this.vApiPassword = vApiPassword;
    }

    public String getvCreatedBy() {
        return vCreatedBy;
    }

    public void setvCreatedBy(String vCreatedBy) {
        this.vCreatedBy = vCreatedBy;
    }

    public String getvCreatedDate() {
        return vCreatedDate;
    }

    public void setvCreatedDate(String vCreatedDate) {
        this.vCreatedDate = vCreatedDate;
    }

    public String getvModifiedDate() {
        return vModifiedDate;
    }

    public void setvModifiedDate(String vModifiedDate) {
        this.vModifiedDate = vModifiedDate;
    }

}
