package com.ecfront.dew.rule;


import com.ecfront.dew.rule.entity.Rule;
import com.ecfront.dew.rule.entity.RuleSet;

import java.util.List;

public interface RuleStorage {

    String addRule(Rule rule);

    void removeRule(String code);

    void updateRule(Rule rule);

    String addRuleSet(RuleSet ruleSet);

    void removeRuleSet(String code);

    void updateRuleSet(RuleSet ruleSet);

    List<Rule> findRule();

    Rule getRules(String code);

    List<Rule> getRulesByRuleSetCode(String ruleSetCode);

    List<RuleSet> findRuleSet();

    RuleSet getRuleSet(String code);
}
