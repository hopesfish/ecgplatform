package com.ainia.ecgApi.domain;

import java.io.Serializable;

/**
 * <p>所有domain 需实现接口</P>
 * domain 标记接口同时用于规范必须实现的 equals 及 hashCode等方法
 * Copyright: Copyright (c) 2013
 * Company:   
 * @author  pengqin
 * @createDate 2013-6-21
 * @version 0.1
 */
public interface Domain extends Serializable {
    
	/**
	 * <p>唯一标识设置</p>
	 * @param id
	 * @return void
	 */
	public Serializable getId();
}
