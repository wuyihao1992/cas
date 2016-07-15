package org.jasig.cas.support.pms.bean;

import java.util.Date;

/**
 * Copyright (c) 2016 Hengte Technology Co.,Ltd.
 * All Rights Reserved.<br />
 * created on 7/14/16
 *
 * @author lcs
 * @version 1.0
 */


public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**主键*/
	private Long userId;
	/**用户编码*/
	private String userCode;
	/**姓名*/
	private String name;
	/**出生日期*/
	private Date birthday;
	/**登录名*/
	private String userName;
	/**登录密码*/
	private String password;
	/**手机号码*/
	private String telphone;
	/**家庭电话*/
	private String homephone;
	/**办公电话*/
	private String officephone;
	/**证件类型*/
	private String credentialType;
	/**证件号码*/
	private String credentialNo;
	/**入职日期*/
	private Date employmentDate;
	/**离职日期*/
	private Date departureDate;
	/**工作状态，1-工作、2-休假、3-离职*/
	private String workStatus;
	/**状态，0-在用 1-停用 2-锁定*/
	private String status;
	/**性别，1代表男、0代表女*/
	private String sex;
	/**值班状态，1代表值班、2代表未值班*/
	private String onDuty;
	/**用户类型，0代表超级管理员:admin，1代表各组织机构管理员，2代表各业务操作人员*/
	private String userType;
	/**职能类别：1-项目人员、2-职能机关、3-前介项目*/
	private String functionType;
	/**直接上级id*/
	private Long leaderId;
	/**删除状态,0-未删除,1-删除*/
	private String isDel;
	/**拥有角色*/

}