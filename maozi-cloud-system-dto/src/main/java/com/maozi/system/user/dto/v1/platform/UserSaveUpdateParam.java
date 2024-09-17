/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.maozi.system.user.dto.v1.platform;

import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveUpdateParam extends AbstractBaseDtomain {
	
	@NotNull(message = "账号不能为空")
	@Schema(description = "账号",required = true)
	private String username;
	
	@NotNull(message = "名称不能为空")
	@Schema(description = "名称")
	private String name;
	
	@NotNull(message = "密码不能为空")
	@Schema(description = "密码")
	private String password;
	
	@NotNull(message = "客户端不能为空")
	@Schema(description = "客户端")
	private Long clientId;
	
	@NotNull(message = "图标不能为空")
	@Schema(description = "图标",required = true)
	private String icon;
	
	@NotNull(message = "状态不能为空")
	@Schema(description = "状态",required = true)
	private Status status;
	
	@Schema(description = "绑定角色列表")
	private List<Long> bindRoleIds;
	
	@Schema(description = "解绑角色列表")
	private List<Long> unbindRoleIds;
	
}
