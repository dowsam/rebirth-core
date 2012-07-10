/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core JaxbMapper.java 2012-2-3 9:57:23 l.xue.nong$$
 */
package cn.com.rebirth.core.mapper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import cn.com.rebirth.commons.utils.ExceptionUtils;

/**
 * 使用Jaxb2.0实现XML<->Java Object的Mapper.
 * 
 * 在创建时需要设定所有需要序列化的Root对象的Class.
 * 特别支持Root对象是Collection的情形.
 * 
 * @author l.xue.nong
 */
public class JaxbMapper {
	//多线程安全的Context.
	/** The jaxb context. */
	private JAXBContext jaxbContext;

	/**
	 * Instantiates a new jaxb mapper.
	 *
	 * @param rootTypes 所有需要序列化的Root对象的Class.
	 */
	public JaxbMapper(Class<?>... rootTypes) {
		try {
			jaxbContext = JAXBContext.newInstance(rootTypes);
		} catch (JAXBException e) {
			ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Java Object->Xml without encoding.
	 *
	 * @param root the root
	 * @return the string
	 */
	public String toXml(Object root) {
		return toXml(root, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 *
	 * @param root the root
	 * @param encoding the encoding
	 * @return the string
	 */
	public String toXml(Object root, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Java Object->Xml without encoding, 特别支持Root Element是Collection的情形.
	 *
	 * @param root the root
	 * @param rootName the root name
	 * @return the string
	 */
	public String toXml(Collection<?> root, String rootName) {
		return toXml(root, rootName, null);
	}

	/**
	 * Java Object->Xml with encoding, 特别支持Root Element是Collection的情形.
	 *
	 * @param root the root
	 * @param rootName the root name
	 * @param encoding the encoding
	 * @return the string
	 */
	public String toXml(Collection<?> root, String rootName, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Xml->Java Object.
	 *
	 * @param <T> the generic type
	 * @param xml the xml
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 创建Marshaller并设定encoding(可为null).
	 *
	 * @param encoding the encoding
	 * @return the marshaller
	 */
	public Marshaller createMarshaller(String encoding) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}

			return marshaller;
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 创建UnMarshaller.
	 *
	 * @return the unmarshaller
	 */
	public Unmarshaller createUnmarshaller() {
		try {
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 *
	 * @author l.xue.nong
	 */
	public static class CollectionWrapper {

		/** The collection. */
		@XmlAnyElement
		protected Collection<?> collection;
	}
}
