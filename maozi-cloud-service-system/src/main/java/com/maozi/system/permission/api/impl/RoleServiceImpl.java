package com.maozi.system.permission.api.impl;

import com.github.yulichang.toolkit.MPJWrappers;
import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.api.impl.BaseServiceImpl;
import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.oauth.token.api.rpc.v1.RpcOauthTokenServiceV1;
import com.maozi.system.permission.api.RolePermissionService;
import com.maozi.system.permission.api.RoleService;
import com.maozi.system.permission.api.UserRoleService;
import com.maozi.system.permission.domain.RoleDo;
import com.maozi.system.permission.mapper.RoleMapper;
import com.maozi.system.role.dto.v1.platform.RoleSaveUpdateParam;
import com.maozi.system.user.api.UserService;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper,RoleDo,AbstractBaseDtomain, SystemErrorCode> implements RoleService{
	
	@Resource(name = "userServiceImpl")
	protected UserService userService;

	@Resource(name = "userRoleServiceImpl")
	protected UserRoleService userRoleService;
	
	@Resource(name = "rolePermissionServiceImpl")
	protected RolePermissionService rolePermissionService;
	
	@DubboReference
	protected RpcOauthTokenServiceV1 rpcOauthTokenService;
	
	@Override
	protected String getAbbreviationModelName() {return "【角色】";}

	@Override
	protected void checkBind(Long id) {
		userRoleService.checkUserBindRoleByRole(id);
	}
	
	@Override
	protected void unbind(Long id) {
		rolePermissionService.roleUnbind(id);
	}

	@Override
	public boolean has(Long id) {
		return count(MPJWrappers.lambdaJoin(RoleDo.builder().id(id).build())) > 0;
	}
	
	protected Long restSaveUpdate(Long id, RoleSaveUpdateParam param) {
		
		id = saveUpdate(id,param);
		
		rolePermissionService.updateBind(id, param.getBindPermissionIds(), param.getUnbindPermissionIds());
		
		return id;
		
	}

}
