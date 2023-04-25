package com.xssdhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xssdhr.entity.*;
import com.xssdhr.service.*;
import com.xssdhr.util.DateUtil;
import com.xssdhr.util.IPTimeStamp;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xssdhr
 * 账号申请
 * @18/4/2023 下午10:09
 * @Company 南京点子跳跃传媒有限公司
 */
@RestController
@RequestMapping("/sys/application")
public class AccountApplicationController {

    @Value("${web.certificateImageFilePath}")
    private String certificateImageFilePath;


    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private AccountApplicationSubMcnService accountApplicationSubMcnService;

    @Autowired
    private AccountApplicationSubCreatorService accountApplicationSubCreatorService;


    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AccountMcnService accountMcnService;

    @Autowired
    private AccountCreatorService accountCreatorService;

    @Value("${default.defaultAccountApplyReceiverId}")
    private Long defaultAccountApplyReceiverId;

    @Value("${default.defaultAccountApplyReceiverName}")
    private String defaultAccountApplyReceiverName;

    @Value("${businessId.accountApplyId}")
    private String accountApplyId;

    @Value("${businessId.accountApplyName}")
    private String accountApplyName;
    /**
     * 上传公司营业执照图片
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadCertificateImage")
    @PreAuthorize("hasAuthority('acc:application')")
    public Map<String,Object> uploadCertificateImage(MultipartFile file)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(certificateImageFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }


    /**
     * 账号申请提交(MCN)
     * @param accountApplication
     * @return
     * @throws Exception
     */
    @PostMapping("/mcnSubmit")
    @PreAuthorize("hasAuthority('acc:application')")
    public R mcnAccountApplicationCommit(@RequestBody AccountApplication accountApplication){
        if(accountApplication.getRole().equals("2")){
            IPTimeStamp ipTimeStamp = new IPTimeStamp(getIp());
            String serialCode = "账号申请-mcn-"+ipTimeStamp.getTimeStamp();
            accountApplication.setCreateTime(new Date());
            accountApplication.setUpdateTime(new Date());
//            accountApplication.setStatus(1);
            accountApplication.setSerialCode(serialCode);
            if(accountApplicationService.save(accountApplication)){
                saveWorkflow(accountApplication);
                //存储子表
                Long pid = accountApplicationService.getOne(new QueryWrapper<AccountApplication>().eq("serial_code",serialCode)).getId();
                accountApplication.getAccountApplicationSubMcn().setParentId(pid);
                accountApplicationSubMcnService.save(accountApplication.getAccountApplicationSubMcn());
                return R.ok();
            }
            return R.error("上传主表失败");
        }
        return R.error("上传数据为空");
    }

    /**
     * 账号申请提交(creator)
     * @param accountApplication
     * @return
     * @throws Exception
     */
    @PostMapping("/creatorSubmit")
    @PreAuthorize("hasAuthority('acc:application')")
    public R creatorAccountApplicationCommit(@RequestBody AccountApplication accountApplication){
        if(accountApplication.getRole().equals("1")){
            IPTimeStamp ipTimeStamp = new IPTimeStamp(getIp());
            String serialCode = "账号申请-达人-"+ipTimeStamp.getTimeStamp();
            accountApplication.setCreateTime(new Date());
            accountApplication.setUpdateTime(new Date());
//            accountApplication.setStatus(1);
            accountApplication.setSerialCode(serialCode);
            if(accountApplicationService.save(accountApplication)){
                saveWorkflow(accountApplication);
                Long pid = accountApplicationService.getOne(new QueryWrapper<AccountApplication>().eq("serial_code",serialCode)).getId();
                for(AccountApplicationSubCreator accountApplicationSubCreator :accountApplication.getAccountApplicationSubCreatorList()){
                    accountApplicationSubCreator.setParentId(pid);
                    accountApplicationSubCreatorService.save(accountApplicationSubCreator);
                }
                return R.ok();
            }
            return R.error("上传主表失败");
        }
        return R.error("上传数据为空");
    }

    /**
     * 保存用户信息
     * @param accountApplication
     * @return
     * @throws Exception
     */
    @PostMapping("/saveAccount")
    @PreAuthorize("hasAuthority('acc:application')")
    public R saveAccount(@RequestBody AccountApplication accountApplication) {
        if(saveAccountDetail(accountApplication)){
            boolean success = true;
            //user表中是否授权0->1
            SysUser sysUser = sysUserService.getById(accountApplication.getUserId());
            sysUser.setAuthorize("1");
            sysUser.setUpdateTime(new Date());
            success = sysUserService.updateById(sysUser);

            //更新workflow
            Workflow workflow = workflowService.getOne(new QueryWrapper<Workflow>().eq("serial_code",accountApplication.getSerialCode()));
            workflow.setUpdateTime(new Date());
            workflow.setOpinion(accountApplication.getRemark());
            //更新状态为通过
            workflow.setStatus(2);
            success = workflowService.updateById(workflow);

            if(accountApplication.getRole().equals("1")){
                success = saveAccountCreator(accountApplication);
            }else if(accountApplication.getRole().equals("2")){
                success = saveAccountMcn(accountApplication);
            }else{
                success = false;
            }
            if(success) return R.ok();
            else return R.error("更新失败");
        }
        return R.error("上传数据为空");
    }

    //获取请求对象
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            return requestAttributes.getRequest();
        }
    }

    //获取请求的ip地址
    public static String getIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "127.0.0.1";
        } else {
            return request.getRemoteHost();
        }
    }
    private boolean saveWorkflow(AccountApplication accountApplication){
        //workflow表新增一条数据
        Workflow workflow = new Workflow();
        workflow.setSenderId(accountApplication.getUserId());
        workflow.setSenderName(accountApplication.getUserName());
        workflow.setReceiverId(defaultAccountApplyReceiverId);
        workflow.setReceiverName(defaultAccountApplyReceiverName);
        workflow.setSerialCode(accountApplication.getSerialCode());
        workflow.setBusinessId(accountApplyId);
        workflow.setBusinessName(accountApplyName);
        workflow.setCreateTime(new Date());
        workflow.setUpdateTime(new Date());
        workflow.setStatus(1);
        return workflowService.save(workflow);
    }
    private boolean saveAccountDetail(AccountApplication accountApplication){
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setUserId(accountApplication.getUserId());
        accountDetail.setContacts(accountApplication.getContacts());
        accountDetail.setPhoneNumber(accountApplication.getPhoneNumber());
        accountDetail.setEmail(accountApplication.getEmail());
        accountDetail.setIdCard(accountApplication.getIdCard());
        accountDetail.setBankName(accountApplication.getBankName());
        accountDetail.setBankAccount(accountApplication.getBankAccount());
        accountDetail.setAccountName(accountApplication.getAccountName());
        accountDetail.setCreateTime(new Date());
        accountDetail.setUpdateTime(new Date());
        return accountDetailService.save(accountDetail);
    }
    private boolean saveAccountMcn(AccountApplication accountApplication){
        AccountMcn accountMcn = new AccountMcn();
        AccountApplicationSubMcn accountApplicationSubMcn = accountApplication.getAccountApplicationSubMcn();
        if(accountApplicationSubMcn != null){
            accountMcn.setUserId(accountApplication.getUserId());
            accountMcn.setCompany(accountApplicationSubMcn.getCompany());
            accountMcn.setCreditCode(accountApplicationSubMcn.getCreditCode());
            accountMcn.setCertificateImage(accountApplicationSubMcn.getCertificateImage());
            accountMcn.setValidityDate(accountApplicationSubMcn.getValidityDate());
            accountMcn.setLegalPerson(accountApplicationSubMcn.getLegalPerson());
            accountMcn.setRegistrationPlace(accountApplicationSubMcn.getRegistrationPlace());
            accountMcn.setRegistrationPlaceProvince(accountApplicationSubMcn.getRegistrationPlaceProvince());
            accountMcn.setRegistrationPlaceCity(accountApplicationSubMcn.getRegistrationPlaceCity());
            accountMcn.setCreateTime(new Date());
            accountMcn.setUpdateTime(new Date());
            return accountMcnService.save(accountMcn);
        }
        return false;
    }

    private boolean saveAccountCreator(AccountApplication accountApplication){
        if(accountApplication.getAccountApplicationSubCreatorList()!=null){
            boolean success = true;
            for(AccountApplicationSubCreator accountApplicationSubCreator :accountApplication.getAccountApplicationSubCreatorList()){
                AccountCreator accountCreator = new AccountCreator();
                accountCreator.setUserId(accountApplication.getUserId());
                accountCreator.setPlatform(accountApplicationSubCreator.getPlatform());
                accountCreator.setName(accountApplicationSubCreator.getName());
                accountCreator.setArea(accountApplicationSubCreator.getArea());
                accountCreator.setAreaProvince(accountApplicationSubCreator.getAreaProvince());
                accountCreator.setAreaCity(accountApplicationSubCreator.getAreaCity());
                accountCreator.setFans(accountApplicationSubCreator.getFans());
                accountCreator.setLove(accountApplicationSubCreator.getLove());
                accountCreator.setPrice(accountApplicationSubCreator.getPrice());
                accountCreator.setNotes(accountApplicationSubCreator.getNotes());
                accountCreator.setLink(accountApplicationSubCreator.getLink());
                accountCreator.setCreateTime(new Date());
                accountCreator.setUpdateTime(new Date());
                success = success&&(accountCreatorService.save(accountCreator));
            }
            return success;
        }
        return false;
    }

}




