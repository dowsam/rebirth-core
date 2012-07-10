package cn.com.rebirth.core.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.SwingUtilities;

/**
 * 异步处理,核心类 处理,异常,与成功,处理.
 *
 * @param <T> the generic type
 * @author Administrator
 */
public class Token<T> {

	/** The Constant DEFAULT_TOKEN_GROUP. */
	public static final String DEFAULT_TOKEN_GROUP = "default";

	/** The token id sequence. */
	private static AtomicLong tokenIdSequence = new AtomicLong(1);

	// tokenGroup tokenName tokenId
	/** The token group. */
	private String tokenGroup = DEFAULT_TOKEN_GROUP;

	/** The token name. */
	private String tokenName;

	/** The token id. */
	private long tokenId;

	/** The _responders. */
	private List<Responder> _responders = new ArrayList<Responder>(2);

	/** The uncaught exception handler. */
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	/** The _result. */
	private T _result;

	/** The _fault. */
	private Exception _fault;

	/** The _is fired result. */
	private boolean _isFiredResult;

	/** The await result signal. */
	private CountDownLatch awaitResultSignal = null;

	/**
	 * Instantiates a new async token.
	 */
	public Token() {
		this(null);
	}

	/**
	 * Instantiates a new async token.
	 *
	 * @param uncaughtExceptionHandler the uncaught exception handler
	 */
	public Token(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this(DEFAULT_TOKEN_GROUP, null);
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	/**
	 * Instantiates a new async token.
	 *
	 * @param tokenGroup the token group
	 * @param tokenName the token name
	 */
	public Token(String tokenGroup, String tokenName) {
		setTokenGroup(tokenGroup);
		setTokenName(tokenName);
		this.tokenId = tokenIdSequence.getAndIncrement();
	}

	/**
	 * Gets the token group.
	 *
	 * @return the token group
	 */
	public String getTokenGroup() {
		return tokenGroup;
	}

	/**
	 * Sets the token group.
	 *
	 * @param tokenGroup the new token group
	 */
	public void setTokenGroup(String tokenGroup) {
		if (tokenGroup == null)
			throw new IllegalArgumentException("'tokenGroup' must be not null");
		this.tokenGroup = tokenGroup;
	}

	/**
	 * Gets the token name.
	 *
	 * @return the token name
	 */
	public String getTokenName() {
		return tokenName;
	}

	/**
	 * Sets the token name.
	 *
	 * @param tokenName the new token name
	 */
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	/**
	 * Gets the token id.
	 *
	 * @return the token id
	 */
	public long getTokenId() {
		return tokenId;
	}

	/**
	 * 增加监听器 addResponder(responder,false);.
	 *
	 * @param responder the responder
	 */
	public void addResponder(final Responder responder) {
		addResponder(responder, false);
	}

	/**
	 * 增加监听器,如果AsyncToken已经拥有token的执行结果,
	 * 则token会根据invokeResponderInOtherThread参数决定是否在异步线程调用responder.
	 *
	 * @param responder 监听器
	 * @param invokeResponderInOtherThread true则另起线程调用responder
	 */
	public void addResponder(final Responder responder, boolean invokeResponderInOtherThread) {
		_responders.add(responder);

		if (_isFiredResult) {
			if (invokeResponderInOtherThread) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireResult2Responder(responder);
					}
				});
			} else {
				fireResult2Responder(responder);
			}
		}
	}

	/**
	 * Gets the responders.
	 *
	 * @return the responders
	 */
	public List<Responder> getResponders() {
		return _responders;
	}

	/**
	 * Checks for responder.
	 *
	 * @return true, if successful
	 */
	public boolean hasResponder() {
		return _responders != null && _responders.size() > 0;
	}

	/**
	 * Gets the uncaught exception handler.
	 *
	 * @return the uncaught exception handler
	 */
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	/**
	 * Sets the uncaught exception handler.
	 *
	 * @param uncaughtExceptionHandler the new uncaught exception handler
	 */
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	/**
	 * Fire result2 responder.
	 *
	 * @param responder the responder
	 */
	private void fireResult2Responder(Responder responder) {
		try {
			if (_fault != null) {
				responder.onFault(_fault);
			} else {
				responder.onResult(_result);
			}
		} catch (RuntimeException e) {
			if (getUncaughtExceptionHandler() != null) {
				getUncaughtExceptionHandler().uncaughtException(responder, e);
			} else {
				throw e;
			}
		} catch (Error e) {
			if (getUncaughtExceptionHandler() != null) {
				getUncaughtExceptionHandler().uncaughtException(responder, e);
			} else {
				throw e;
			}
		}
	}

	/**
	 * Fire result2 responders.
	 */
	private void fireResult2Responders() {
		synchronized (this) {
			_isFiredResult = true;
			if (awaitResultSignal != null) {
				awaitResultSignal.countDown();
			}
		}

		for (Responder r : _responders) {
			fireResult2Responder(r);
		}
	}

	/**
	 * Sets the complete.
	 */
	public void setComplete() {
		setComplete(null);
	}

	/**
	 * Sets the complete.
	 *
	 * @param result the new complete
	 */
	public void setComplete(T result) {
		if (_isFiredResult)
			throw new IllegalStateException("token already fired");
		this._result = result;
		fireResult2Responders();
	}

	/**
	 * Sets the fault.
	 *
	 * @param fault the new fault
	 */
	public void setFault(Exception fault) {
		if (fault == null)
			throw new NullPointerException();
		if (_isFiredResult)
			throw new IllegalStateException("token already fired");
		this._fault = fault;
		fireResult2Responders();
	}

	/**
	 * Checks if is done.
	 *
	 * @return true, if is done
	 */
	public boolean isDone() {
		synchronized (this) {
			return _isFiredResult;
		}
	}

	/**
	 * 等待得到token结果,测试一般使用此方法,因为jdk有相同功能的Future.get()可以使用
	 *
	 * @return the object
	 * @throws InterruptedException the interrupted exception
	 * @throws Exception the exception
	 * @see Future
	 */
	@Deprecated
	public Object waitForResult() throws InterruptedException, Exception {
		return waitForResult(-1, null);
	}

	/**
	 * 等待得到token结果,测试一般使用此方法,因为jdk有相同功能的Future.get()可以使用
	 *
	 * @param timeout the timeout
	 * @param timeUnit the time unit
	 * @return the object
	 * @throws InterruptedException the interrupted exception
	 * @throws Exception the exception
	 * @see Future
	 */
	@Deprecated
	public Object waitForResult(long timeout, TimeUnit timeUnit) throws InterruptedException, Exception {
		synchronized (this) {
			if (_isFiredResult) {
				if (_fault != null) {
					throw _fault;
				} else {
					return _result;
				}
			}

			awaitResultSignal = new CountDownLatch(1);
		}

		if (timeout > 0) {
			awaitResultSignal.await(timeout, timeUnit);
		} else {
			awaitResultSignal.await();
		}

		if (_fault != null) {
			throw _fault;
		} else {
			return _result;
		}
	}
}
