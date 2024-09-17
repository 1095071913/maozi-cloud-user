package com.maozi.system.permission.api.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.api.impl.BaseServiceImpl;
import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.system.permission.api.RoleService;
import com.maozi.system.permission.api.UserRoleService;
import com.maozi.system.permission.domain.UserRoleDo;
import com.maozi.system.permission.mapper.UserRoleMapper;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper,UserRoleDo,AbstractBaseDtomain, SystemErrorCode> implements UserRoleService {
	
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@Override
	protected String getAbbreviationModelName() {return "【用户角色关系】";}

	@Override
	public void checkUserBindRoleByRole(Long roleId) {
		
		MPJLambdaWrapper<UserRoleDo> wrapper = MPJWrappers.lambdaJoin(UserRoleDo.builder().roleId(roleId).build());
		
		checkBoolThrowError(count(wrapper) < 1 , "【角色】已被用户绑定");
		
	}

	@Override
	public void updateBind(Long userId, List<Long> bindRoleIds, List<Long> unbindRoleIds) {
		
		if(collectionIsNotEmpty(bindRoleIds)) {

			Consumer<Long> consumer = wrapConsumer((roleId)->{

				UserRoleDo domainSave = UserRoleDo.builder().userId(userId).roleId(roleId).build();

				if(count(MPJWrappers.lambdaJoin(domainSave)) < 1 && roleService.has(roleId)) {
					save(domainSave);
				}

			});
			
			bindRoleIds.parallelStream().forEach(consumer);
			
		}
		
		if(collectionIsNotEmpty(unbindRoleIds)) {

			Consumer<Long> consumer = wrapConsumer((roleId)->{

				remove(MPJWrappers.lambdaJoin(UserRoleDo.builder().userId(userId).roleId(roleId).build()));

			});
			
			unbindRoleIds.parallelStream().forEach(consumer);
			
		}
		
	}

	@Override
	public List<Long> getRolesByUser(Long userId) {
		
		MPJLambdaWrapper<UserRoleDo> wrapper = MPJWrappers.lambdaJoin(UserRoleDo.builder().userId(userId).build());
		
		wrapper.select(UserRoleDo::getRoleId);
		
		return list(wrapper).stream().map(UserRoleDo::getRoleId).collect(Collectors.toList());
		
	}

	@Override
	public void hasUserBindRole(Long userId, Long roleId) {
		
		MPJLambdaWrapper<UserRoleDo> wrapper = MPJWrappers.lambdaJoin(UserRoleDo.builder().userId(userId).roleId(roleId).build());
		
		checkBoolThrowError(count(wrapper) > 0 , "【用户】未绑定该角色");
		
	}

	@Override
	public List<Long> getUsersByRole(Long roleId) {
		
		MPJLambdaWrapper<UserRoleDo> wrapper = MPJWrappers.lambdaJoin(UserRoleDo.builder().roleId(roleId).build());
		
		wrapper.select(UserRoleDo::getUserId);
		
		return list(wrapper).stream().map(UserRoleDo::getUserId).collect(Collectors.toList());
		
	}

	@Override
	public void userUnbind(Long userId) {
		remove(Wrappers.lambdaQuery(UserRoleDo.builder().userId(userId).build()));
	}

}