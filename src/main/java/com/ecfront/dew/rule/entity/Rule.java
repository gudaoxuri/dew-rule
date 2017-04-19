package com.ecfront.dew.rule.entity;

import java.util.UUID;

public class Rule {

    private String code;
    private String name;
    private String desc;
    private int priority;
    private boolean breaking;
    private boolean enable;
    private boolean whenAll;
    private boolean single;
    private String whenEL;
    private String thenEL;

    public static Rule matchAll(String name, Integer priority, boolean breaking, boolean single, String thenEL) {
        Rule rule = new Rule();
        rule.code = UUID.randomUUID().toString().replace("-", "");
        rule.name = name;
        rule.desc = "";
        rule.priority = priority;
        rule.breaking = breaking;
        rule.enable = true;
        rule.whenAll = true;
        rule.single = single;
        rule.whenEL = "";
        rule.thenEL = thenEL;
        return rule;
    }

    public static Rule matchWhen(String name, Integer priority, boolean breaking, boolean single, String whenEL, String thenEL) {
        Rule rule = new Rule();
        rule.code = UUID.randomUUID().toString().replace("-", "");
        rule.name = name;
        rule.desc = "";
        rule.priority = priority;
        rule.breaking = breaking;
        rule.enable = true;
        rule.whenAll = false;
        rule.single = single;
        rule.whenEL = whenEL;
        rule.thenEL = thenEL;
        return rule;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public void setBreaking(boolean breaking) {
        this.breaking = breaking;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isWhenAll() {
        return whenAll;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public void setWhenAll(boolean whenAll) {
        this.whenAll = whenAll;
    }

    public String getWhenEL() {
        return whenEL;
    }

    public void setWhenEL(String whenEL) {
        this.whenEL = whenEL;
    }

    public String getThenEL() {
        return thenEL;
    }

    public void setThenEL(String thenEL) {
        this.thenEL = thenEL;
    }
}
