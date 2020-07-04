package com.mashang.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.mashang.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LowerLevelAccountReceiveOrderQueryCondParam extends PageParam {

	@NotBlank
	private String currentAccountId;

	private String userName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	private String gatheringChannelCode;

	private String orderState;

}
