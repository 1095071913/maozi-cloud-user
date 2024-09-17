package com.maozi.system.config.api.impl;

import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.common.BaseCommon;
import com.maozi.system.config.api.SystemService;
import com.maozi.system.config.properties.SystemProperties;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl extends BaseCommon<SystemErrorCode> implements SystemService {
	
	@Resource
	protected SystemProperties systemProperties;

}
