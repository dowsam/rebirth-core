/**
 * Copyright (c) 2005-2011 www.china-cti.com
 * Id: LogSqlEntity.java 2011-5-16 11:10:53 l.xue.nong$$
 */
package cn.com.rebirth.core.logsql;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * The Class LogSqlEntity.
 *
 * @author l.xue.nong
 */
public class LogSqlEntity extends SqlParamEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5849485321906124164L;
	/** The time. */
	private Long time;

	/** The user id. */
	private Long userId;

	/** The create date. */
	private Date createDate;

	/** The method name. */
	private String methodName;

	/** The request ip. */
	private String requestIp;

	/** The stack trace element. */
	private String stackTraceElement;

	/** The server ip. */
	private String serverIp;

	/** The server port. */
	private Integer serverPort;

	/** The sql type enum. */
	private SqlTypeEnum sqlTypeEnum;
	
	/** The context. */
	protected Map<String, Object> context = Maps.newHashMap();//传递上下文

	/**
	 * Instantiates a new log sql entity.
	 */
	public LogSqlEntity() {
		super();
	}

	/**
	 * Instantiates a new log sql entity.
	 * 
	 * @param id
	 *            the id
	 * @param sql
	 *            the sql
	 * @param time
	 *            the time
	 * @param userId
	 *            the user id
	 * @param createDate
	 *            the create date
	 * @param methodName
	 *            the method name
	 */
	public LogSqlEntity(Long id, String sql, Long time, Long userId, Date createDate, String methodName) {
		super();
		super.setId(id);
		super.setSql(sql);
		this.time = time;
		this.userId = userId;
		this.createDate = createDate;
		this.methodName = methodName;
	}

	/**
	 * Gets the request ip.
	 * 
	 * @return the request ip
	 */
	public String getRequestIp() {
		return requestIp;
	}

	/**
	 * Sets the request ip.
	 * 
	 * @param requestIp
	 *            the new request ip
	 */
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 * 
	 * @param time
	 *            the new time
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createDate
	 *            the new creates the date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the method name.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets the method name.
	 * 
	 * @param methodName
	 *            the new method name
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Gets the stack trace element.
	 * 
	 * @return the stack trace element
	 */
	public String getStackTraceElement() {
		return stackTraceElement;
	}

	/**
	 * Sets the stack trace element.
	 * 
	 * @param stackTraceElement
	 *            the new stack trace element
	 */
	public void setStackTraceElement(String stackTraceElement) {
		this.stackTraceElement = stackTraceElement;
	}

	/**
	 * Gets the server ip.
	 *
	 * @return the server ip
	 */
	public String getServerIp() {
		return serverIp;
	}

	/**
	 * Sets the server ip.
	 *
	 * @param serverIp the new server ip
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * Gets the server port.
	 *
	 * @return the server port
	 */
	public Integer getServerPort() {
		return serverPort;
	}

	/**
	 * Sets the server port.
	 *
	 * @param serverPort the new server port
	 */
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Gets the sql type enum.
	 *
	 * @return the sql type enum
	 */
	public SqlTypeEnum getSqlTypeEnum() {
		return sqlTypeEnum;
	}

	/**
	 * Sets the sql type enum.
	 *
	 * @param sqlTypeEnum the new sql type enum
	 */
	public void setSqlTypeEnum(SqlTypeEnum sqlTypeEnum) {
		this.sqlTypeEnum = sqlTypeEnum;
	}

	/**
	 * The Enum SqlTypeEnum.
	 *
	 * @author l.xue.nong
	 */
	public static enum SqlTypeEnum {

		/** The SELECT. */
		SELECT,
		/** The UPDATE. */
		UPDATE,
		/** The DELETE. */
		DELETE,
		/** The CALL. */
		CALL,
		/** The NULL. */
		NULL,
		/** The INSERT. */
		INSERT,
		/** The drop. */
		DROP,
		/** The create. */
		CREATE;
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 *
	 * @param context the context
	 */
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

}
