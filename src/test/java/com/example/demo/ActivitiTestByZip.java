package com.example.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @Title: ActivitiTestByZip
 * @ProjectName ActivityDemo
 * @Description: TODO
 * @Author Li_jiaMeng
 * @Date Created in 20:59 2020/12/22
 */
public class ActivitiTestByZip {
    /**
     * 流程定义部署(通过Zip压缩包形式就行部署文件)
     *
     * 背后影响的表:
     *      act_re_deployment  流程部署表 部署信息
     *      act_re_procdef    流程定义表 流程定义的一些信息
     *      act_ge_bytearray 存放静态资源 存储图片、bpmn流程图文件
     */
    @Test
    public void activitiByZip() {
        //1.创建ProcessEngine(进程引擎)对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RepositoryService(资源管理类)实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.转化为 InputStream 输入流的形式
        InputStream inputStream = ActivitiTestByZip.class.getClassLoader().getResourceAsStream("bpmn/bpmn.zip");

        //4.再将 InputStream 流转化为 ZipInputStream 流
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        //5.进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("请假申请单流程")
                .deploy();
    }
}
