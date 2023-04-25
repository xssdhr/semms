package com.xssdhr.controller.workflowController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xssdhr.entity.*;
import com.xssdhr.service.AccountApplicationService;
import com.xssdhr.service.AccountApplicationSubCreatorService;
import com.xssdhr.service.AccountApplicationSubMcnService;
import com.xssdhr.service.WorkflowService;
import com.xssdhr.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xssdhr
 * 工作流Controller(账号申请)
 * @20/4/2023 上午11:52
 * @Company 南京点子跳跃传媒有限公司
 */
@RestController
@RequestMapping("/workflow/acc")
@CrossOrigin
public class AccWorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private AccountApplicationSubMcnService accountApplicationSubMcnService;

    @Autowired
    private AccountApplicationSubCreatorService accountApplicationSubCreatorService;


    /**
     * “我的申请加载工作流列表”
     * 分页获取用户工作流申请列表
     * @param pageBean
     * @return R
     */
    @PostMapping("/senderList")
    @PreAuthorize("hasAuthority('workflow:apply')")
    public R loadWorkflowSenderList(@RequestBody PageBean pageBean){
        String query = pageBean.getQuery().trim();
        String queryId = pageBean.getQueryId().trim();
        boolean isHide = pageBean.isHide();
        QueryWrapper<Workflow> queryWrapper = new QueryWrapper<Workflow>();
        queryWrapper.like(StringUtil.isNotEmpty(query),"serial_code",query);
        queryWrapper.eq(StringUtil.isNotEmpty(queryId),"sender_id",queryId);
        if(isHide){
            queryWrapper.in("status",1,2);
        }
        Page<Workflow> pageResult = workflowService.page(new Page<>(pageBean.getPageNum(),pageBean.getPageSize()),queryWrapper);
        //返回分页后的数据集合
        List<Workflow> workflowList = pageResult.getRecords();
        Map<String,Object> resultMap = new HashMap<>();
        //每页数据
        resultMap.put("workflowList",workflowList);
        //总计路数
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    @PostMapping("/rejectWorkflow")
    @PreAuthorize("hasAuthority('workflow:apply')")
    public R rejectWorkflow(@RequestBody Workflow workflow){
        workflow.setStatus(3);
        workflow.setUpdateTime(new Date());
        workflow.setOpinion(workflow.getOpinion());
        if(workflowService.updateById(workflow))
            return R.ok();
        else return R.error("更新数据失败");
    }

    /**
     * “我的申请加载工作流列表”
     * 分页获取用户工作流申请列表
     * @param pageBean
     * @return R
     */
    @PostMapping("/receiverList")
    @PreAuthorize("hasAuthority('workflow:apply')")
    public R loadWorkflowReceiverList(@RequestBody PageBean pageBean){
        String query = pageBean.getQuery().trim();
        String queryId = pageBean.getQueryId().trim();
        boolean isHide = pageBean.isHide();
        QueryWrapper<Workflow> queryWrapper = new QueryWrapper<Workflow>();
        queryWrapper.like(StringUtil.isNotEmpty(query),"serial_code",query);
        queryWrapper.eq(StringUtil.isNotEmpty(queryId),"receiver_id",queryId);
        if(isHide){
            queryWrapper.in("status",1,2);
        }
        Page<Workflow> pageResult = workflowService.page(new Page<>(pageBean.getPageNum(),pageBean.getPageSize()),queryWrapper);
        //返回分页后的数据集合
        List<Workflow> workflowList = pageResult.getRecords();
        Map<String,Object> resultMap = new HashMap<>();
        //每页数据
        resultMap.put("workflowList",workflowList);
        //总计路数
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    /**
     * 账号申请Dialog详情加载
     * @return R
     */
    @GetMapping("/initAccWorkflowDialog/{id}")
    @PreAuthorize("hasAuthority('workflow:apply')")
    public R initAccountApplicationWorkflowDialog(@PathVariable(value = "id")Long id){
        if(id!=null&&id!=-1L){
            Workflow workflow = workflowService.getById(id);
            AccountApplication accountApplication = accountApplicationService.getOne(new QueryWrapper<AccountApplication>().eq("serial_code",workflow.getSerialCode()));
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("workflow",workflow);
            resultMap.put("accountApplication",accountApplication);
            if(accountApplication.getSerialCode().contains("达人")){
                QueryWrapper queryWrapperCreator = new QueryWrapper<List<AccountApplicationSubCreator>>().eq("parent_id",accountApplication.getId());
                List<AccountApplicationSubCreator> accountApplicationSubCreator = accountApplicationSubCreatorService.list(queryWrapperCreator);
                resultMap.put("accountApplicationSubCreator",accountApplicationSubCreator);
                resultMap.put("role","creator");
            }else{
                AccountApplicationSubMcn accountApplicationSubMcn = accountApplicationSubMcnService.getOne(new QueryWrapper<AccountApplicationSubMcn>().eq("parent_id",accountApplication.getId()));
                resultMap.put("accountApplicationSubMcn",accountApplicationSubMcn);
                resultMap.put("role","mcn");
            }
            return R.ok(resultMap);
        }
        return R.error();
    }
}
