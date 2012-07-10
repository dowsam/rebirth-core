/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core BeanMapper.java 2012-2-3 9:14:52 l.xue.nong$$
 */
package cn.com.rebirth.core.mapper;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.
 * 
 * @author l.xue.nong
 */
public class BeanMapper {

	/**
	 * Instantiates a new bean mapper.
	 */
	private BeanMapper() {
	}

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param destinationClass the destination class
	 * @return the t
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 *
	 * @param <T> the generic type
	 * @param sourceList the source list
	 * @param destinationClass the destination class
	 * @return the list
	 */
	public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 *
	 * @param source the source
	 * @param destinationObject the destination object
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
}