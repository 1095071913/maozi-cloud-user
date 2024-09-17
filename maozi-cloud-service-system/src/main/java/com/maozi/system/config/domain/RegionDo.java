package com.maozi.system.config.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maozi.base.AbstractBaseNameDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 地区
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("system_config_region")
public class RegionDo extends AbstractBaseNameDomain {

	/**
	 * 父级ID
	 */
	private Long parentId;

	/**
	 * 简称
	 */
	private String sname;

	/**
	 * 级别
	 */
	private Integer level;

	/**
	 * 编码
	 */
	private String cityCode;

	/**
	 * 邮政编码
	 */
	private String mailCode;

	/**
	 * 组合名称
	 */
	private String mername;

	/**
	 * 经度
	 */
	private Float Lng;

	/**
	 * 维度
	 */
	private Float Lat;

	/**
	 * 拼音
	 */
	private String pinyin;

}
