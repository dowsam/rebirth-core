/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core MimeMailService.java 2012-2-2 17:27:41 l.xue.nong$$
 */
package cn.com.rebirth.core.email;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import cn.com.rebirth.commons.utils.ReflectionUtils;
import cn.com.rebirth.core.template.TemplateEngine;

import com.google.common.collect.Maps;

/**
 * 邮件发送器模板，附件.
 *
 * @author xuenong_li
 */
public class MimeMailService {
	/** The Constant DEFAULT_ENCODING. */
	private static final String DEFAULT_ENCODING = "utf-8";

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(MimeMailService.class);

	/** The mail sender. */
	private JavaMailSender mailSender;

	/** The velocity engine. */
	private TemplateEngine templateEngine;

	/**
	 * Send.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 */
	public void send(String from, String to, String subject) {
		send(from, to, subject, null, null);
	}

	/**
	 * Send.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 */
	public void send(String from, String to, String subject, String templateName, Map<String, Object> map) {
		send(from, new String[] { to }, subject, templateName, map, null);
	}

	/**
	 * Send.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 */
	public void send(String from, String[] to, String subject, String templateName, Map<String, Object> map) {
		send(from, to, subject, templateName, map, null);
	}

	/**
	 * Send.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @param fileName the file name
	 */
	public void send(String from, String to, String subject, String templateName, Map<String, Object> map,
			String fileName) {
		send(from, new String[] { to }, subject, templateName, map, fileName);
	}

	/**
	 * Send.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @param fileName the file name
	 */
	public void send(String from, String[] to, String subject, String templateName, Map<String, Object> map,
			String fileName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, DEFAULT_ENCODING);
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			if (templateName == null || "".equals(templateName)) {
				templateName = "base.ftl";
			}
			if (map == null || map.isEmpty()) {
				map = Maps.newHashMap();
			}
			String context = templateEngine.renderFile(templateName, map);
			messageHelper.setText(context, true);
			if (!(fileName == null || "".equals(fileName.trim()))) {
				Map<String, Object> fileMap = generateAttachment(fileName);
				messageHelper.addAttachment((String) fileMap.get("fileName"), (File) fileMap.get("file"));
			}
			mailSender.send(message);
			logger.info("HTML版邮件已发送至{}", StringUtils.arrayToCommaDelimitedString(to));
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}

	}

	/**
	 * Generate attachment.
	 *
	 * @param fileName the file name
	 * @return the map
	 * @throws MessagingException the messaging exception
	 */
	private Map<String, Object> generateAttachment(String fileName) throws MessagingException {
		Map<String, Object> map = Maps.newHashMap();
		try {
			Resource resource = new UrlResource(fileName);
			map.put("file", resource.getFile());
			map.put("fileName", resource.getFilename());
			return map;
		} catch (IOException e) {
			logger.error("构造邮件失败,附件文件不存在", e);
			throw new MessagingException("附件文件不存在", e);
		}
	}

	/**
	 * Spring的MailSender.
	 *
	 * @param mailSender the new mail sender
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

}
