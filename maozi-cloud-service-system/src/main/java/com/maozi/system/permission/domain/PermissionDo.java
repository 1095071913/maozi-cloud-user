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
 */
package com.maozi.system.permission.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maozi.base.AbstractBaseNameDomain;
import com.maozi.system.permission.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 权限
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("system_permission")
public class PermissionDo extends AbstractBaseNameDomain {

	/**
	 * 父ID
	 */
	private Long parentId;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 标识
	 */
	private String mark;

	/**
	 * 深度
	 */
	private Integer level;

	/**
	 * 路由
	 */
	private String route;

	/**
	 * 服务地址
	 */
	private String serviceUri;

	/**
	 * 类型
	 */
	private PermissionType type;

	/**
	 * 排序
	 */
	private Integer sort;
	
}
