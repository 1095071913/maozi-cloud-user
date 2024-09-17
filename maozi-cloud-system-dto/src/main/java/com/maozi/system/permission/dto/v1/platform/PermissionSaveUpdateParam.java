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

package com.maozi.system.permission.dto.v1.platform;

import com.maozi.base.AbstractBaseDtomain;
import com.maozi.system.permission.enums.PermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionSaveUpdateParam extends AbstractBaseDtomain {
	
	@Schema(description = "上级ID")
	private Long parentId;
	
	@Schema(description = "名称")
	@NotNull(message = "名称不能为空")
	private String name;
	
	@Schema(description = "图标")
	@NotNull(message = "图标不能为空")
	private String icon;

	@Schema(description = "标识")
	@NotNull(message = "标识不能为空")
	private String mark;
	
	@Schema(description = "深度")
	@NotNull(message = "深度不能为空")
	private Integer level;
	
	@Schema(description = "路由")
	private String route;
	
	@Schema(description = "服务地址")
	private String serviceUri;
	
	@Schema(description = "类型")
	@NotNull(message = "类型不能为空")
	private PermissionType type;
	
	@Schema(description = "排序")
	private Integer sort;
	
}
