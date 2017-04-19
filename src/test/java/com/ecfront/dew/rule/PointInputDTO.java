package com.ecfront.dew.rule;

import java.math.BigDecimal;
import java.util.Date;

public class PointInputDTO {

    private String app_id;
    private String prov;
    private Date apply_ts;
    private BigDecimal amt;

    public static PointInputDTO apply(String app_id, String prov, Date apply_ts, BigDecimal amt) {
        PointInputDTO dto = new PointInputDTO();
        dto.app_id = app_id;
        dto.prov = prov;
        dto.apply_ts = apply_ts;
        dto.amt = amt;
        return dto;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public Date getApply_ts() {
        return apply_ts;
    }

    public void setApply_ts(Date apply_ts) {
        this.apply_ts = apply_ts;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

}
