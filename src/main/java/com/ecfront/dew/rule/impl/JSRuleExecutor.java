package com.ecfront.dew.rule.impl;

import com.ecfront.dew.common.JsonHelper;
import com.ecfront.dew.rule.RuleExecutor;
import com.ecfront.dew.rule.dto.OutputDTO;
import com.ecfront.dew.rule.entity.RuleSet;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JS引擎
 *
 * 引入Underscore包
 *
 * @link http://underscorejs.org/
 */
public class JSRuleExecutor implements RuleExecutor {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();

    private static Map<String, Invocable> JS_CACHE = new HashMap<>();
    private static Map<String, Integer> JS_CACHE_CHANGE_MONITOR = new HashMap<>();

    @Override
    public void init(RuleSet ruleSet) throws Exception {
        ScriptEngine engine = SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
        if (JS_CACHE_CHANGE_MONITOR.containsKey(ruleSet.getCode())
                && JS_CACHE_CHANGE_MONITOR.get(ruleSet.getCode()) != JsonHelper.toJsonString(ruleSet).hashCode()) {
            JS_CACHE_CHANGE_MONITOR.remove(ruleSet.getCode());
            JS_CACHE.remove(ruleSet.getCode());
        }
        if (!JS_CACHE.containsKey(ruleSet.getCode())) {
            List<String> codes = ruleSet.getRules().stream().map(
                    rule -> {
                        String method = rule.getCode();
                        String whenCodeWrap = "";
                        if (!rule.isWhenAll()) {
                            String[] whenCode = rule.getWhenEL().split("\r\n");
                            whenCode[whenCode.length - 1] = "var __result=" + whenCode[whenCode.length - 1];
                            whenCodeWrap = "function __" + method + "_when(input){\r\n" +
                                    "input=JSON.parse(input);\r\n" +
                                    String.join("\r\n", whenCode) + "\r\n" +
                                    "return JSON.stringify(__result);\r\n" +
                                    "}\r\n";
                        }
                        String[] thenCode = rule.getThenEL().split("\r\n");
                        thenCode[thenCode.length - 1] = "var __result=" + thenCode[thenCode.length - 1];
                        String thenCodeWrap = "function __" + method + "_then(input){\r\n" +
                                "input=JSON.parse(input);\r\n" +
                                String.join("\r\n", thenCode) + "\r\n" +
                                "return JSON.stringify(__result);\r\n" +
                                "}\r\n";
                        return whenCodeWrap + thenCodeWrap;
                    }
            ).collect(Collectors.toList());
            String js = String.join("\r\n", codes);
            engine.eval(new InputStreamReader(getClass().getResourceAsStream("/underscore.js")));
            engine.eval(js);
            JS_CACHE.put(ruleSet.getCode(), (Invocable) engine);
            JS_CACHE_CHANGE_MONITOR.put(ruleSet.getCode(), JsonHelper.toJsonString(ruleSet).hashCode());
        }
    }

    @Override
    public <I> List when(String ruleSetCode, String ruleCode, String instanceCode, List<I> input, String whenEL, Class<I> inputClass) throws Exception {
        String result = (String) JS_CACHE.get(ruleSetCode).invokeFunction("__" + ruleCode + "_when", JsonHelper.toJsonString(input));
        return JsonHelper.toList(result, Object.class);
    }

    @Override
    public <I> Object when(String ruleSetCode, String ruleCode, String instanceCode, I input, String whenEL, Class<I> inputClass) throws Exception {
        String result = (String) JS_CACHE.get(ruleSetCode).invokeFunction("__" + ruleCode + "_when", JsonHelper.toJsonString(input));
        return JsonHelper.toObject(result, Object.class);
    }

    @Override
    public <O extends OutputDTO> O then(String ruleSetCode, String ruleCode, String instanceCode, Object record, String thenEL, Class<O> outputClass) throws Exception {
        String result = (String) JS_CACHE.get(ruleSetCode).invokeFunction("__" + ruleCode + "_then", JsonHelper.toJsonString(record));
        return JsonHelper.toObject(result, outputClass);
    }

}
