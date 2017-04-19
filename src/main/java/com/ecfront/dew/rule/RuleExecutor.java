package com.ecfront.dew.rule;


import com.ecfront.dew.rule.dto.OutputDTO;
import com.ecfront.dew.rule.entity.RuleSet;

import java.util.List;

public interface RuleExecutor {

    void init(RuleSet ruleSet) throws Exception;

    <I> List when(String ruleSetCode, String ruleCode, String instanceCode, List<I> input, String whenEL, Class<I> inputClass) throws Exception;

    <I> Object when(String ruleSetCode, String ruleCode, String instanceCode, I input, String whenEL, Class<I> inputClass) throws Exception;

    <O extends OutputDTO> O then(String ruleSetCode, String ruleCode, String instanceCode, Object record, String thenEL, Class<O> outputClass) throws Exception;

}
