package com.mashang.merchant.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Merchant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String userName;

	private String merchantNum;

	private String merchantName;

	private String secretKey;

	private String notifyUrl;

	private String returnUrl;

	private String loginPwd;

	/**
	 * 资金密码
	 */
	private String moneyPwd;

	/**
	 * 可提现金额
	 */
	private Double withdrawableAmount;

	private String googleSecretKey;

	private Date googleAuthBindTime;

	/**
	 * 状态
	 */
	private String state;
	
	private String accountType;

	private Boolean appointCodeBusinessman;

	private Date createTime;

	/**
	 * 最近登录时间
	 */
	private Date latelyLoginTime;

	/**
	 * 逻辑删除
	 */
	private Boolean deletedFlag;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;
	
	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inviter_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Merchant inviter;

	public void bindGoogleAuth(String googleSecretKey) {
		this.setGoogleSecretKey(googleSecretKey);
		this.setGoogleAuthBindTime(new Date());
	}
}
