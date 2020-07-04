package com.mashang.merchant.param;

import com.mashang.common.param.PageParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GatheringChannelRebateQueryCondParam extends PageParam {

	private String channelId;

}
