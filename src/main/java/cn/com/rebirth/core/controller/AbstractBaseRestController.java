/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AbstractBaseRestController.java 2012-2-11 16:24:50 l.xue.nong$$
 */
package cn.com.rebirth.core.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 定义标准的rest方法以对应实体对象的操作,以达到统一rest的方法名称, 还可以避免子类需要重复编写@RequestMapping annotation.
 * 子类要实现某功能只需覆盖下面的方法即可. 注意: 覆盖时请使用@Override,以确保不会发生错误
 * <pre>
 * /userinfo                => index()
 * /userinfo/new            => _new()
 * /userinfo/{id}           => show()
 * /userinfo/{id}/edit      => edit()
 * /userinfo        POST    => create()
 * /userinfo/{id}   PUT     => update()
 * /userinfo/{id}   DELETE  => delete()
 * /userinfo        DELETE  => batchDelete()
 * </pre>
 * @param <T> the generic type
 * @param <PK> the generic type
 * @author l.xue.nong
 */
public abstract class AbstractBaseRestController<T, PK extends Serializable> extends AbstractBaseController {
	/**
	 * binder用于bean属性的设置.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}

	/**
	 * 增加了@ModelAttribute的方法可以在本controller方法调用前执行,可以存放一些共享变量,如枚举值,或是一些初始化操作.
	 *
	 * @param model the model
	 */
	@ModelAttribute
	public void init(ModelMap model) {
		model.put("now", new java.sql.Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Index.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * _new.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param entity the entity
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/new")
	public String _new(ModelMap model, HttpServletRequest request, HttpServletResponse response, T entity)
			throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Show.
	 *
	 * @param model the model
	 * @param id the id
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/{id}")
	public String show(ModelMap model, @PathVariable PK id) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Edits the.
	 *
	 * @param model the model
	 * @param id the id
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/{id}/edit")
	public String edit(ModelMap model, @PathVariable PK id) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Creates the.
	 *
	 * @param model the model
	 * @param entity the entity
	 * @param errors the errors
	 * @param request the request
	 * @param response the response
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String create(ModelMap model, @Valid T entity, BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Update.
	 *
	 * @param model the model
	 * @param id the id
	 * @param entity the entity
	 * @param errors the errors
	 * @param request the request
	 * @param response the response
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(ModelMap model, @PathVariable PK id, @Valid T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Delete.
	 *
	 * @param model the model
	 * @param id the id
	 * @return the string
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(ModelMap model, @PathVariable PK id) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Batch delete.
	 *
	 * @param model the model
	 * @param items the items
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public String batchDelete(ModelMap model, @RequestParam("items") PK[] items) {
		throw new UnsupportedOperationException("not yet implement");
	}
}
