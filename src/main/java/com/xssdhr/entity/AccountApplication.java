package com.xssdhr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName account_application
 */
@TableName(value ="account_application")
@Data
public class AccountApplication  extends BaseEntity implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 流水号
     */
    @TableField(value = "serial_code")
    private String serialCode;

    /**
     * 角色：1-达人/创作者  2-MCN机构
     */
    @TableField(value = "role")
    private String role;

    /**
     * 联系人真实姓名
     */
    @TableField(value = "contacts")
    private String contacts;

    /**
     * 联系人电话
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 联系人邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 联系人身份证号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 收款开户行名称
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 收款银行账号
     */
    @TableField(value = "bank_account")
    private String bankAccount;

    /**
     * 收款账户名称
     */
    @TableField(value = "account_name")
    private String accountName;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * mcn子表 存储公司认证信息
     */
    @TableField(exist = false)
    private AccountApplicationSubMcn accountApplicationSubMcn;

    /**
     * creater子表 存储账户信息
     */
    @TableField(exist = false)
    private List<AccountApplicationSubCreator> accountApplicationSubCreatorList = new ArrayList<>();

}