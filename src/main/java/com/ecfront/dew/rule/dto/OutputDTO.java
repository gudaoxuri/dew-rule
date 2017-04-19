package com.ecfront.dew.rule.dto;

import java.util.Date;

public abstract class OutputDTO {

    private String hitRuleCode;
    private String hitRuleName;
    private Date hitDate;
    private String hitDesc;

    public String getHitRuleCode() {
        return hitRuleCode;
    }

    public void setHitRuleCode(String hitRuleCode) {
        this.hitRuleCode = hitRuleCode;
    }

    public String getHitRuleName() {
        return hitRuleName;
    }

    public void setHitRuleName(String hitRuleName) {
        this.hitRuleName = hitRuleName;
    }

    public Date getHitDate() {
        return hitDate;
    }

    public void setHitDate(Date hitDate) {
        this.hitDate = hitDate;
    }

    public String getHitDesc() {
        return hitDesc;
    }

    public void setHitDesc(String hitDesc) {
        this.hitDesc = hitDesc;
    }
}
