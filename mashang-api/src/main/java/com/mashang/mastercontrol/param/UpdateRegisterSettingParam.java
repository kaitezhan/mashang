package com.mashang.mastercontrol.param;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateRegisterSettingParam {
	
	@NotNull
	private Boolean registerFun;
	
	@NotNull
	private Boolean inviteRegisterMode;

}
