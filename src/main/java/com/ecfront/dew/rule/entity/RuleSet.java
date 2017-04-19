package com.ecfront.dew.rule.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RuleSet {

    private String code;
    private String name;
    private String desc;
    private Date startTime;
    private Date endTime;
    private List<Rule> rules;

    public static RuleSet apply(String name, List<Rule> rules) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.code = UUID.randomUUID().toString().replace("-","");
        ruleSet.name = name;
        ruleSet.desc = "";
        ruleSet.startTime = null;
        ruleSet.endTime = null;
        ruleSet.rules = rules;
        return ruleSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

}
