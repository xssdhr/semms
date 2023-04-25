package com.xssdhr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName account_application_sub_mcn
 */
@TableName(value ="account_application_sub_mcn")
@Data
public class AccountApplicationSubMcn implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父表ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 公司名称
     */
    @TableField(value = "company")
    private String company;

    /**
     * 统一社会信用代码
     */
    @TableField(value = "credit_code")
    private String creditCode;

    /**
     * 营业执照图片
     */
    @TableField(value = "certificate_image")
    private String certificateImage;

    /**
     * 证件有效期
     */
    @TableField(value = "validity_date")
    private Date validityDate;

    /**
     * 法人/经营者
     */
    @TableField(value = "legal_person")
    private String legalPerson;

    /**
     * 注册地
     */
    @TableField(value = "registration_place")
    private String registrationPlace;

    /**
     * 注册地(省-0)
     */
    @TableField(value = "registration_place_province")
    private String registrationPlaceProvince;

    /**
     * 注册地(市-1)
     */
    @TableField(value = "registration_place_city")
    private String registrationPlaceCity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}