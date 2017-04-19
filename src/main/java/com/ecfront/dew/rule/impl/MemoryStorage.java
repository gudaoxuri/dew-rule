package com.ecfront.dew.rule.impl;

import com.ecfront.dew.rule.RuleStorage;
import com.ecfront.dew.rule.entity.Rule;
import com.ecfront.dew.rule.entity.RuleSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStorage implements RuleStorage {

    private static Map<String, Rule> RULE_CONTAINER = new HashMap<>();
    private static Map<String, RuleSet> CONTAINER = new HashMap<>();

    @Override
    public String addRule(Rule rule) {
        RULE_CONTAINER.put(rule.getCode(), rule);
        return rule.getCode();
    }

    @Override
    public void removeRule(String code) {
        RULE_CONTAINER.remove(code);
    }

    @Override
    public void updateRule(Rule rule) {
        RULE_CONTAINER.put(rule.getCode(), rule);
    }

    @Override
    public String addRuleSet(RuleSet ruleSet) {
        CONTAINER.put(ruleSet.getCode(), ruleSet);
        return ruleSet.getCode();
    }

    @Override
    public void removeRuleSet(String code) {
        CONTAINER.remove(code);
    }

    @Override
    public void updateRuleSet(RuleSet ruleSet) {
        CONTAINER.put(ruleSet.getCode(), ruleSet);
    }

    @Override
    public List<Rule> findRule() {
        return new ArrayList<>(RULE_CONTAINER.values());
    }

    @Override
    public Rule getRules(String code) {
        return RULE_CONTAINER.get(code);
    }

    @Override
    public List<Rule> getRulesByRuleSetCode(String ruleSetCode) {
        return getRuleSet(ruleSetCode).getRules();
    }

    @Override
    public List<RuleSet> findRuleSet() {
        return new ArrayList<>(CONTAINER.values());
    }

    @Override
    public RuleSet getRuleSet(String code) {
        return CONTAINER.get(code);
    }
}
