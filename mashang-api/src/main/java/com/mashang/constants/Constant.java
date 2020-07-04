package com.mashang.constants;

public class Constant {
	
	public static final String 菜单类型_一级菜单 = "menu_1";
	
	public static final String 菜单类型_二级菜单 = "menu_2";
	
	public static final String 菜单类型_按钮 = "btn";
	
	public static final String 已删除账号ID = "DELETED_ACCOUNT_ID";
	
	public static final String 收款通道_京东 = "jdpay";
	
	public static final String 收款通道_微信 = "wechat";

	public static final String 收款通道_支付宝 = "alipay";

	public static final String 收款通道_银行卡 = "bankCard";
	
	public static final String 收款通道_微信手机转账 = "wechatMobile";
	
	public static final String 收款通道_支付宝转账 = "alipayTransfer";
	
	public static final String 收款通道_支付宝id转账 = "alipayIdTransfer";
	
	public static final String 收款通道_小钱袋 = "xqd";
	
	public static final String 收款通道_数字货币 = "digiccy";
	
	public static final String 商户订单确认方式_人工确认 = "1";
	
	public static final String 商户订单确认方式_监控app确认 = "2";

	public static final String 系统_会员端 = "member";

	public static final String 系统_商户端 = "merchant";

	public static final String 系统_后台管理 = "admin";

	public static final String 登录提示_登录成功 = "登录成功";

	public static final String 登录提示_不是管理员无法登陆后台 = "该账号不是管理员,无法登陆到后台";

	public static final String 登录提示_用户名或密码不正确 = "账号或密码不正确";

	public static final String 登录提示_验证码不正确 = "验证码不正确";
	
	public static final String 登录提示_谷歌验证码不正确 = "谷歌验证码不正确";

	public static final String 登录提示_账号已被禁用 = "你的账号已被禁用";

	public static final String 登录提示_用户名不存在 = "用户名不存在";
	
	public static final String 登录提示_后台账号不能登录会员端 = "后台账号不能登录会员端";

	public static final String 登录状态_成功 = "1";

	public static final String 登录状态_失败 = "0";

	public static final String 接单状态_正在接单 = "1";

	public static final String 接单状态_停止接单 = "2";
	
	public static final String 接单状态_禁止接单 = "3";

	public static final String 商户订单状态_等待接单 = "1";

	public static final String 商户订单状态_已接单 = "2";

	public static final String 商户订单状态_已支付 = "4";

	public static final String 商户订单状态_超时取消 = "5";

	public static final String 商户订单状态_人工取消 = "6";

	public static final String 商户订单状态_未确认超时取消 = "7";

	public static final String 商户订单状态_取消订单退款 = "9";

	public static final String 账号类型_管理员 = "admin";

	public static final String 账号类型_会员 = "member";
	
	public static final String 商户账号类型_普通商户 = "merchant";
	
	public static final String 商户账号类型_商户代理 = "merchantAgent";

	public static final String 账号状态_启用 = "1";

	public static final String 账号状态_禁用 = "0";

	public static final Integer 充值订单默认有效时长 = 10;

	public static final String 充值订单_已支付订单单号 = "RECHARGE_ORDER_PAID_ORDER_NO";

	public static final String 充值订单状态_待支付 = "1";

	public static final String 充值订单状态_已支付 = "2";

	public static final String 充值订单状态_已结算 = "3";

	public static final String 充值订单状态_超时取消 = "4";

	public static final String 充值订单状态_人工取消 = "5";

	public static final String 账变日志类型_账号充值 = "1";

	public static final String 账变日志类型_接单扣款 = "2";

	public static final String 账变日志类型_接单收益 = "3";

	public static final String 账变日志类型_账号提现 = "4";

	public static final String 账变日志类型_提现不符退款 = "5";

	public static final String 账变日志类型_取消订单退款 = "6";

	public static final String 账变日志类型_团队收益 = "7";

	public static final String 账变日志类型_退还冻结资金 = "8";
	
	public static final String 账变日志类型_后台调整余额 = "9";

	public static final String 商户账变日志类型_已支付订单实收金额 = "1";
	
	public static final String 商户账变日志类型_手工增可提现金额 = "2";

	public static final String 商户账变日志类型_手工减可提现金额 = "3";
	
	public static final String 商户账变日志类型_提现结算 = "4";
	
	public static final String 商户账变日志类型_提现不符退款 = "5";

	public static final String 提现记录状态_发起提现 = "1";

	public static final String 提现记录状态_审核通过 = "2";

	public static final String 提现记录状态_审核不通过 = "3";

	public static final String 提现记录状态_已到账 = "4";

	public static final String 提现方式_银行卡 = "bankCard";

	public static final String 商户订单ID = "MERCHANT_ORDER_ID";

	public static final String 派单订单ID = "DISPATCH_ORDER_ID";

	public static final String 异步通知订单ID = "ASYN_NOTICE_ORDER_ID";

	public static final String 订单返点ID = "ORDER_REBATE_ID";

	public static final String 实收金额记录ID = "ACTUAL_INCOME_RECORD_ID";

	public static final String 商户订单支付通知状态_未通知 = "1";

	public static final String 商户订单支付通知状态_通知成功 = "2";

	public static final String 商户订单支付通知状态_通知失败 = "3";

	public static final String 商户订单支付成功 = "1";

	public static final String 商户订单通知成功返回值 = "success";

	public static final String 支付类别_银行卡入款 = "bankCard";

	public static final String 支付类别_电子钱包 = "virtualWallet";

	public static final String 支付类别_收款码 = "gatheringCode";

	public static final String 商户结算状态_审核中 = "1";

	public static final String 商户结算状态_审核通过 = "2";

	public static final String 商户结算状态_审核不通过 = "3";

	public static final String 商户结算状态_已到账 = "4";

	public static final String 收款码状态_正常 = "1";

	public static final String 收款码状态_待审核 = "2";
	
	public static final String 收款码状态_收款异常 = "3";

	public static final String 收款码审核类型_新增 = "1";

	public static final String 收款码审核类型_删除 = "2";

	public static final String 邀请码状态_未使用 = "1";

	public static final String 邀请码状态_已使用 = "2";

	public static final String 邀请码状态_已失效 = "3";

	public static final String 冻结记录ID = "FREEZE_RECORD_ID";

}
