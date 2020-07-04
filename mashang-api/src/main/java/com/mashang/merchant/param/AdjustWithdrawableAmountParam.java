package com.mashang.merchant.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdjustWithdrawableAmountParam {

	@NotBlank
	private String merchantId;

	@NotBlank
	private String accountChangeTypeCode;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double accountChangeAmount;

}
