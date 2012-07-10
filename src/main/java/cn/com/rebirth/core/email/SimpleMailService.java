package cn.com.rebirth.core.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

/**
 * 纯文本邮件发送器.
 *
 * @author xuenong_li
 */
public class SimpleMailService {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

	/** The mail sender. */
	private JavaMailSender mailSender;// spring 封装邮件发送器

	/**
	 * 文本发送器，单用户.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param context the context
	 */
	public void send(String from, String to, String subject, String context) {
		send(from, new String[] { to }, subject, context);
	}

	/**
	 * 文本发送器，多邮箱.
	 *
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param context the context
	 */
	public void send(String from, String[] to, String subject, String context) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(context);

		try {
			mailSender.send(simpleMailMessage);
			logger.info("纯文本邮件已发送至{}", StringUtils.arrayToCommaDelimitedString(simpleMailMessage.getTo()));
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
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

}
