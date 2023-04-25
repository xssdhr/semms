package com.xssdhr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName account_application_sub_creator
 */
@TableName(value ="account_application_sub_creator")
@Data
public class AccountApplicationSubCreator implements Serializable {
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
     * 平台
     */
    @TableField(value = "platform")
    private String platform;

    /**
     * 昵称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 地区
     */
    @TableField(value = "area")
    private String area;

    /**
     * 地区(省)
     */
    @TableField(value = "area_province")
    private String areaProvince;

    /**
     * 地区(市)
     */
    @TableField(value = "area_city")
    private String areaCity;

    /**
     * 粉丝数
     */
    @TableField(value = "fans")
    private String fans;

    /**
     * 赞藏
     */
    @TableField(value = "love")
    private String love;

    /**
     * 报价
     */
    @TableField(value = "price")
    private String price;

    /**
     * 备注
     */
    @TableField(value = "notes")
    private String notes;

    /**
     * 链接
     */
    @TableField(value = "link")
    private String link;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}