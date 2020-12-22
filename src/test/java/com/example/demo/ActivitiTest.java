package com.example.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @Title: ActivitiTest
 * @ProjectName ActivityDemo
 * @Description: TODO
 * @Author Li_jiaMeng
 * @Date Created in 15:34 2020/12/20
 */
public class ActivitiTest {

    /**
     *
     * 1. 生成Activiti表
     *
     */
    @Test
    public void testGenTable() {
        //1.创建ProcessEngineConfiguration对象  第一个参数:配置文件名称  第二个参数是processEngineConfiguration的bean的id
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //2.创建ProcesEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();

        //3.输出processEngine对象
        System.out.println(processEngine);
    }


    /**
     * 2. 流程定义部署
     *
     * bpmn文件指: 流程定义
     *
     * 背后影响的表:
     *      act_re_deployment  流程部署表 部署信息
     *      act_re_procdef    流程定义表 流程定义的一些信息
     *      act_ge_bytearray 存放静态资源 存储图片、bpmn流程图文件
     */
    @Test
    public void depoloy() {
        //1.创建ProcessEngine(进程引擎)对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RepositoryService(资源管理类)实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/holiday.bpmn")  //添加bpmn资源
                .addClasspathResource("bpmn/holiday.png")
                .name("请假申请单流程")
                .deploy();

        //4.输出部署的一些信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());
    }

    /**
     * 3. 流程实例
     *
     *
     * 流程定义好比Java中写好的一个类
     * 流程实例好比Java中写好的一个实例(对象)
     * (一个流程定义可以对应多个流程实例,好比流程定义为：请假,流程实例则为 多人都可请假)
     *
     * 背后影响的表:
     *     act_hi_actinst         已完成的活动信息记录
     *     act_hi_identitylink    参与者信息
     *     act_hi_procinst        流程实例
     *     act_hi_taskinst        任务实例
     *     act_ru_execution       执行表
     *     act_ru_identitylink    参与者信息
     *     act_ru_task            记录当前任务表
     */
    @Test
    public void instance() {
        //1.创建ProcessEngine(进程引擎)对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService(运行资源管理类)实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.创建流程实例并进行部署
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday");

        //4.输出流程实例的一些信息
        System.out.println("流程部署的ID:" + holiday.getDeploymentId());
        System.out.println("流程定义的ID:" + holiday.getProcessDefinitionId());
        System.out.println("流程实例的ID:" + holiday.getId());
        System.out.println("当前活动的ID:" + holiday.getActivityId());

    }

    /**
     * 4. 查询当前用户(zhangsan)的任务列表
     *
     */
    @Test
    public void searchZhangsan() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.根据流程定义的key,负责人 Assignee 来实现当前用户的任务列表查询
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();

        //4.输出任务列表的一些信息
        for (Task task : taskList) {
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务负责人:" + task.getAssignee());
            System.out.println("任务名称:" + task.getName());
        }
    }


    /**
     * 5. zhangsan完成自己的任务(处理当前用户的任务)
     *
     * 受影响的表:
     *     act_hi_actinst       流程实例信息
     *     act_hi_identitylink  任务参与者
     *     act_hi_taskinst      当前任务流程
     *     act_ru_execution
     *     act_ru_identitylink  新任务涉及用户
     *     act_ru_task          添加新任务并删除旧任务
     */
    @Test
    public void zhangsanComplete() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.处理任务,结合当前用户任务列表的查询操作的话 , 任务ID(act_hi_actinst:Task_ID):2505
        taskService.complete("2505");
    }


    /**
     * 6. lisi完成自己任务列表的查询
     *
     */
    @Test
    public void searchLisi() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.根据流程定义的key,负责人assignee来实现当前用户的任务列表查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("lisi")
                .singleResult();

        //4.任务列表的展示
        System.out.println("流程实例ID:" + task.getProcessInstanceId());
        System.out.println("任务ID:" + task.getId());
        System.out.println("任务负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());
    }


    /**
     * 7. lisi完成自己的任务(处理当前用户的任务)
     *
     * 受影响的表:
     *     act_hi_actinst       流程实例信息
     *     act_hi_identitylink  任务参与者
     *     act_hi_taskinst      当前任务流程
     *     act_ru_execution
     *     act_ru_identitylink  新任务涉及用户
     *     act_ru_task          添加新任务并删除旧任务
     */
    @Test
    public void lisiComplete() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.处理任务,结合当前用户任务列表的查询操作的话,任务ID(act_hi_actinst:Task_ID):5002
        taskService.complete("5002");
    }


    /**
     * 8. wangwu完成自己任务列表的查询
     *
     */
    @Test
    public void searchWangwu() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.根据流程定义的key,负责人assignee来实现当前用户的任务列表查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("wangwu")
                .singleResult();

        //4.任务列表的展示
        System.out.println("流程实例ID:" + task.getProcessInstanceId());
        System.out.println("任务ID:" + task.getId());  //5002
        System.out.println("任务负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());
    }


    /**
     * 9. wangwu完成自己的任务(处理当前用户的任务)
     *
     * 受影响的表:
     *     act_hi_actinst       流程实例信息
     *     act_hi_identitylink  任务参与者
     *     act_hi_taskinst      当前任务流程
     *     act_ru_execution
     *     act_ru_identitylink  新任务涉及用户
     *     act_ru_task          添加新任务并删除旧任务
     */
    @Test
    public void wangwuComplete() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("wangwu")
                .singleResult();

        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID(act_hi_actinst:Task_ID):task.getId()
        taskService.complete(task.getId());

        //5.输出任务的id
        System.out.println(task.getId());
    }
}
