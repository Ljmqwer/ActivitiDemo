package com.example.demo;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Title: HistoryQuery
 * @ProjectName ActivityDemo
 * @Description: TODO
 * @Author Li_jiaMeng
 * @Date Created in 13:55 2020/12/21
 * <p>
 * 需求：
 * 历史数据的查看
 */
public class HistoryQuery {
    @Test
    public static void main(String[] args) throws IOException {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到HistoryService
        HistoryService historyService = processEngine.getHistoryService();

        //3.得到HistoricActivitiInstanceQuery对象
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        historicActivityInstanceQuery.processInstanceId("22501");//设置流程实例的id

        //4.执行查询
        List<HistoricActivityInstance> list = historicActivityInstanceQuery
                .orderByHistoricActivityInstanceStartTime().asc().list();//排序StartTime

        //5.遍历查询结果
        for (HistoricActivityInstance instance : list) {
            System.out.println(instance.getActivityId());
            System.out.println(instance.getActivityName());
            System.out.println(instance.getProcessDefinitionId());
            System.out.println(instance.getProcessInstanceId());
            System.out.println("=============================");
        }
    }
}
