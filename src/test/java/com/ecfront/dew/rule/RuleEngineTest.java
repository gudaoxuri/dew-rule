package com.ecfront.dew.rule;

import com.ecfront.dew.rule.entity.Rule;
import com.ecfront.dew.rule.entity.RuleSet;
import com.ecfront.dew.rule.impl.JSRuleExecutor;
import com.ecfront.dew.rule.impl.MemoryStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class RuleEngineTest {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void init() {
        RuleEngine.setExecutorImpl(new JSRuleExecutor());
        RuleEngine.setStorageImpl(new MemoryStorage());
    }

    public String addPointRule() {
        Rule r1 = Rule.matchAll("订单成交基础规则", 1, false, true, "{'point':100,'hitDesc':'订单号：'+input.app_id}");
        Rule r2 = Rule.matchWhen("元旦翻倍", 3, false, true,
                " var d= new Date(input.apply_ts);\r\n" +
                        "d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate() == '2016-1-1'?input:null; ",
                "{'point':200,'hitDesc':'订单号：'+input.app_id+',时间：'+new Date(input.apply_ts)}");
        Rule r3 = Rule.matchWhen("特殊地区奖励", 2, false, true,
                "input.prov == '贵州省'?input:null; ",
                "{'point':200,'hitDesc':'订单号：'+input.app_id+',地区：'+input.prov}");
        Rule r4 = Rule.matchWhen("订单数阶梯奖励", 4, true, false,
                "input.length>1? [{'count':input.length}]:[];",
                "if(input.count>5){" +
                        " result = {'point':500,'hitDesc':'订单数：'+input.count}" +
                        "}else if(input.count>1){" +
                        " result = {'point':50,'hitDesc':'订单数：'+input.count}" +
                        "}\r\n" +
                        "result;");
        Rule r5 = Rule.matchAll("不应该命中的规则", 8, false, true,
                "{'point':500,'hitDesc':'订单数：'+input.count}");
        RuleEngine.Manage.addRule(r1);
        RuleEngine.Manage.addRule(r2);
        RuleEngine.Manage.addRule(r3);
        RuleEngine.Manage.addRule(r4);
        RuleEngine.Manage.addRule(r5);
        return RuleEngine.Manage.addRuleSet(RuleSet.apply("积分奖励", new ArrayList<Rule>() {{
            add(r1);
            add(r2);
            add(r3);
            add(r4);
            add(r5);
        }}));
    }

    @Test
    public void execute() throws Exception {
        String ruleSetCode = addPointRule();
        List<PointInputDTO> input = new ArrayList<PointInputDTO>() {{
            add(PointInputDTO.apply("1", "浙江省", df.parse("2016-01-01 00:00:00"), new BigDecimal(1000)));
            add(PointInputDTO.apply("2", "贵州省", df.parse("2016-02-01 00:00:00"), new BigDecimal(2000)));
        }};
        List<PointOutputDTO> result = RuleEngine.execute(input, ruleSetCode, PointInputDTO.class, PointOutputDTO.class);
        result.forEach(record -> System.out.println(String.format("规则 [%s] : 积分[%s] 说明：%s", record.getHitRuleName(), record.getPoint(), record.getHitDesc())));
        Assert.assertEquals(5, result.size());
    }

    private Object t = new Object();

    @Test
    public void lock() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (t) {
                        System.out.println("1");
                        t.wait();
                        t.notify();
                        System.out.println("2");
                        t.wait();
                        t.notify();
                        System.out.println("3");
                        t.wait();
                        t.notify();
                        System.out.println("4");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (t) {
                    try {
                        System.out.println("A");
                        t.notify();
                        t.wait();
                        System.out.println("B");
                        t.notify();
                        t.wait();
                        System.out.println("C");
                        t.notify();
                        t.wait();
                        System.out.println("D");
                        t.notify();
                        t.wait();
                    }catch (Exception e){}
                }
            }
        }).start();
        new CountDownLatch(1).await();
    }

}