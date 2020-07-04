package com.mashang.mastercontrol.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReceiveOrderRiskSettingParam {

	@NotNull
	private Boolean auditGatheringCode;

	@NotNull
	private Boolean banReceiveRepeatOrder;
	
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer noOpsStopReceiveOrder;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer waitConfirmOrderUpperLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer gatheringCodeReceiveOrderInterval;

	@NotNull
	private Boolean gatheringCodeEverydayUsedUpperLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long gatheringCodeUsedUpperLimit;

	@NotNull
	private Boolean gatheringCodeEverydayGatheringUpperLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double gatheringCodeGatheringUpperLimit;

	@NotNull
	private Boolean floatAmountMode;

	@NotBlank
	private String floatAmountDirection;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer minFloatAmount;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer maxFloatAmount;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer continuationGatheringFailOffLine;

}
