package com.ecfront.dew.rule;

import com.ecfront.dew.rule.dto.OutputDTO;
import com.ecfront.dew.rule.entity.Rule;
import com.ecfront.dew.rule.entity.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class RuleEngine {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    private static RuleStorage storage;
    private static RuleExecutor executor;

    public static <I, O extends OutputDTO> List<O> execute(I inputData, String ruleSetCode, Class<I> inputClass, Class<O> outputClass) {
        return execute(new ArrayList<I>() {{
            add(inputData);
        }}, ruleSetCode, inputClass, outputClass);
    }

    public static <I, O extends OutputDTO> List<O> execute(List<I> input, String ruleSetCode, Class<I> inputClass, Class<O> outputClass) {
        logger.info("Start prepare execute RuleSet [" + ruleSetCode + "]");
        List<O> output = new ArrayList<>();
        Optional<RuleSet> ruleSetR = getRuleSet(ruleSetCode);
        if (!ruleSetR.isPresent()) {
            return output;
        }
        RuleSet ruleSet = ruleSetR.get();
        Long startTime = new Date().getTime();
        logger.info("Start execute RuleSet [" + ruleSetCode + "]:" + ruleSet.getName());
        try {
            executor.init(ruleSet);
        } catch (Throwable e) {
            logger.error("Execute init error", e);
            return null;
        }
        for (Rule rule : ruleSet.getRules()) {
            String instanceId = ruleSet.getCode() + "_" + rule.getCode() + "_" + System.nanoTime();
            try {
                List filterInput = new ArrayList();
                if (rule.isWhenAll()) {
                    filterInput = input;
                } else {
                    if (rule.isSingle()) {
                        for (I i : input) {
                            Object o = executor.when(ruleSet.getCode(), rule.getCode(), instanceId, i, rule.getWhenEL(), inputClass);
                            if (o != null) {
                                filterInput.add(o);
                            }
                        }
                    } else {
                        filterInput = executor.when(ruleSet.getCode(), rule.getCode(), instanceId, input, rule.getWhenEL(), inputClass);
                    }
                }
                if (!filterInput.isEmpty()) {
                    final Throwable[] err = new Throwable[1];
                    filterInput.forEach(record -> {
                                try {
                                    O result = executor.then(ruleSet.getCode(), rule.getCode(), instanceId, record, rule.getThenEL(), outputClass);
                                    result.setHitDate(new Date());
                                    result.setHitRuleCode(rule.getCode());
                                    result.setHitRuleName(rule.getName());
                                    output.add(result);
                                } catch (Exception e) {
                                    err[0] = e;
                                }
                            }
                    );
                    if (err[0] != null) {
                        throw err[0];
                    }
                    if (rule.isBreaking()) {
                        break;
                    }
                }
            } catch (Throwable e) {
                logger.error("Execute rule error", e);
                return null;
            }
        }
        logger.info("Finish execute RuleSet [" + ruleSetCode + "]:" + ruleSet.getName() + " , Use times:" + (new Date().getTime() - startTime));
        return output;
    }

    private static Optional<RuleSet> getRuleSet(String ruleSetCode) {
        RuleSet ruleSet = Manage.getRuleSet(ruleSetCode);
        if (ruleSet == null) {
            logger.warn("RuleSet [" + ruleSetCode + "] not exist.");
            return Optional.empty();
        }
        if (ruleSet.getStartTime() != null && ruleSet.getStartTime().getTime() > new Date().getTime() || ruleSet.getEndTime() != null && ruleSet.getEndTime().getTime() < new Date().getTime()) {
            logger.warn("RuleSet [" + ruleSetCode + "] expiry date not match.");
            return Optional.empty();
        }
        if (ruleSet.getRules().isEmpty()) {
            logger.warn("RuleSet [" + ruleSetCode + "] rules is empty.");
            return Optional.empty();
        }
        ruleSet.setRules(ruleSet.getRules().stream().filter(Rule::isEnable).collect(Collectors.toList()));
        ruleSet.getRules().sort(Comparator.comparingInt(Rule::getPriority));
        return Optional.of(ruleSet);
    }

    public static void setStorageImpl(RuleStorage _storage) {
        storage = _storage;
    }

    public static void setExecutorImpl(RuleExecutor _executor) {
        executor = _executor;
    }

    public static class Manage {

        public static String addRule(Rule rule) {
            return RuleEngine.storage.addRule(rule);
        }

        public static void removeRule(String code) {
            RuleEngine.storage.removeRule(code);
        }

        public static void updateRule(Rule rule) {
            RuleEngine.storage.updateRule(rule);
        }

        public List<Rule> findRule() {
            return RuleEngine.storage.findRule();
        }

        public Rule getRules(String code) {
            return RuleEngine.storage.getRules(code);
        }

        public static List<Rule> getRulesByRuleSetCode(String ruleSetCode) {
            return RuleEngine.storage.getRulesByRuleSetCode(ruleSetCode);
        }

        public static String addRuleSet(RuleSet ruleSet) {
            return RuleEngine.storage.addRuleSet(ruleSet);
        }

        public static void removeRuleSet(String code) {
            RuleEngine.storage.removeRuleSet(code);
        }

        public static void updateRuleSet(RuleSet ruleSet) {
            RuleEngine.storage.updateRuleSet(ruleSet);
        }

        public static List<RuleSet> findRuleSet() {
            return RuleEngine.storage.findRuleSet();
        }

        public static RuleSet getRuleSet(String code) {
            return RuleEngine.storage.getRuleSet(code);
        }

    }

}
