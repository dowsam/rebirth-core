/**
 * Copyright (c) 2005-2011 www.china-cti.com
 * Id: PreparedStatementWarpper.java 2011-5-16 11:24:04 l.xue.nong$$
 */
package cn.com.rebirth.core.logsql;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The Class PreparedStatementWarpper.
 */
public class PreparedStatementWarpper extends StatementWarpper implements
		PreparedStatement {

	/** The sql. */
	private String sql;

	/** The real prepared statement. */
	protected PreparedStatement realPreparedStatement;

	/** The rdbms. */
	protected Rdbms rdbms;

	/** The arg trace. */
	protected final List<String> argTrace = Lists.newArrayList();

	/** The arg trace value. */
	protected final List<Object> argTraceValue = Lists.newArrayList();

	/** The Constant showTypeHelp. */
	private static final boolean showTypeHelp = false;

	/**
	 * Gets the real prepared statement.
	 * 
	 * @return the real prepared statement
	 */
	public PreparedStatement getRealPreparedStatement() {
		return realPreparedStatement;
	}

	/**
	 * Instantiates a new prepared statement warpper.
	 * 
	 * @param sql
	 *            the sql
	 * @param connectionWarpper
	 *            the connection warpper
	 * @param realPreparedStatement
	 *            the real prepared statement
	 */
	public PreparedStatementWarpper(String sql,
			ConnectionWarpper connectionWarpper,
			PreparedStatement realPreparedStatement) {
		super(connectionWarpper, realPreparedStatement);
		this.sql = sql;
		this.realPreparedStatement = realPreparedStatement;
		rdbms = connectionWarpper.getRdbms();
	}

	/**
	 * Arg trace set.
	 * 
	 * @param i
	 *            the i
	 * @param typeHelper
	 *            the type helper
	 * @param arg
	 *            the arg
	 * @param arValue
	 *            the ar value
	 */
	protected void argTraceSet(int i, String typeHelper, Object arg,
			Object arValue) {
		int tmpeI = i;
		String tracedArg;
		try {
			tracedArg = rdbms.formatParameterObject(arg);
		} catch (Throwable t) {
			tracedArg = arg == null ? "null" : arg.toString();
		}

		tmpeI--; // make the index 0 based
		synchronized (argTrace) {
			while (tmpeI >= argTrace.size()) {
				argTrace.add(argTrace.size(), null);
			}
			if (!showTypeHelp) {
				argTrace.set(tmpeI, tracedArg);
			} else {
				argTrace.set(tmpeI, typeHelper + tracedArg);
			}
		}
		synchronized (argTraceValue) {
			while (tmpeI >= argTraceValue.size()) {
				argTraceValue.add(argTraceValue.size(), null);
			}
			argTraceValue.set(tmpeI, arValue);
		}
	}

	/**
	 * Dumped sql.
	 * 
	 * @return the string
	 */
	protected String dumpedSql() {
		StringBuffer dumpSql = new StringBuffer();
		int lastPos = 0;
		int Qpos = sql.indexOf('?', lastPos); // find position of first question
												// mark
		int argIdx = 0;
		String arg;

		while (Qpos != -1) {
			// get stored argument
			synchronized (argTrace) {
				try {
					arg = argTrace.get(argIdx);
				} catch (IndexOutOfBoundsException e) {
					arg = "?";
				}
			}
			if (arg == null) {
				arg = "?";
			}

			argIdx++;

			dumpSql.append(sql.substring(lastPos, Qpos)); // dump segment of sql
															// up to question
															// mark.
			lastPos = Qpos + 1;
			Qpos = sql.indexOf('?', lastPos);
			dumpSql.append(arg);
		}
		if (lastPos < sql.length()) {
			dumpSql.append(sql.substring(lastPos, sql.length())); // dump last
																	// segment
		}

		return dumpSql.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	/**
	 * Adds the batch.
	 * 
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void addBatch() throws SQLException {
		currentBatch
				.add(new SqlParamEntity(dumpedSql(), this.sql,
						this.argTraceValue
								.toArray(new Object[this.argTraceValue.size()])) {
					private static final long serialVersionUID = -8287628246388023956L;

				});
		realPreparedStatement.addBatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	/**
	 * Clear parameters.
	 * 
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void clearParameters() throws SQLException {
		synchronized (argTrace) {
			argTrace.clear();
		}
		synchronized (argTraceValue) {
			argTraceValue.clear();
		}
		realPreparedStatement.clearParameters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#execute()
	 */
	/**
	 * Execute.
	 * 
	 * @return true, if successful
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public boolean execute() throws SQLException {
		String methodName = "execute()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		boolean result = realPreparedStatement.execute();
		long tsEnd = System.currentTimeMillis();
		action(methodName, dumpedSql, this.sql, (tsEnd - tstart),
				argTraceValue.toArray(new Object[argTraceValue.size()]));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	/**
	 * Execute query.
	 * 
	 * @return the result set
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public ResultSet executeQuery() throws SQLException {
		String methodName = "executeQuery()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		ResultSet r = realPreparedStatement.executeQuery();
		long tsEnd = System.currentTimeMillis();
		action(methodName, dumpedSql, this.sql, (tsEnd - tstart),
				argTraceValue.toArray(new Object[argTraceValue.size()]));
		return new ResultSetWarpper(this, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	/**
	 * Execute update.
	 * 
	 * @return the int
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public int executeUpdate() throws SQLException {
		String methodName = "executeUpdate()";
		String dumpedSql = dumpedSql();
		long tstart = System.currentTimeMillis();
		int result = realPreparedStatement.executeUpdate();
		long tsEnd = System.currentTimeMillis();
		action(methodName, dumpedSql, this.sql, (tsEnd - tstart),
				argTraceValue.toArray(new Object[argTraceValue.size()]));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	/**
	 * Gets the meta data.
	 * 
	 * @return the meta data
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return realPreparedStatement.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	/**
	 * Gets the parameter meta data.
	 * 
	 * @return the parameter meta data
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return realPreparedStatement.getParameterMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	/**
	 * Sets the array.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		argTraceSet(parameterIndex, "(Array)", "<Array>", x);
		realPreparedStatement.setArray(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	/**
	 * Sets the ascii stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		argTraceSet(parameterIndex, "(Ascii InputStream)",
				"<Ascii InputStream>", x);
		realPreparedStatement.setAsciiStream(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 * int)
	 */
	/**
	 * Sets the ascii stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Ascii InputStream)",
				"<Ascii InputStream of length " + length + ">", x);
		realPreparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 * long)
	 */
	/**
	 * Sets the ascii stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Ascii InputStream)",
				"<Ascii InputStream of length " + length + ">", x);
		realPreparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	/**
	 * Sets the big decimal.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		argTraceSet(parameterIndex, "(BigDecimal)", x, x);
		realPreparedStatement.setBigDecimal(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	/**
	 * Sets the binary stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		argTraceSet(parameterIndex, "(Binary InputStream)",
				"<Binary InputStream>", x);
		realPreparedStatement.setBinaryStream(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 * int)
	 */
	/**
	 * Sets the binary stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Binary InputStream)",
				"<Binary InputStream of length " + length + ">", x);
		realPreparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 * long)
	 */
	/**
	 * Sets the binary stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Binary InputStream)",
				"<Binary InputStream of length " + length + ">", x);
		realPreparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	/**
	 * Sets the blob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		argTraceSet(parameterIndex, "(Blob)", x == null ? null
				: "<Blob of size " + x.length() + ">", x);
		realPreparedStatement.setBlob(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	/**
	 * Sets the blob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param inputStream
	 *            the input stream
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		argTraceSet(parameterIndex, "(InputStream)", "<InputStream>",
				inputStream);
		realPreparedStatement.setBlob(parameterIndex, inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	/**
	 * Sets the blob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param inputStream
	 *            the input stream
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		argTraceSet(parameterIndex, "(InputStream)", "<InputStream of length "
				+ length + ">", inputStream);
		realPreparedStatement.setBlob(parameterIndex, inputStream, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	/**
	 * Sets the boolean.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		Boolean b = x ? Boolean.TRUE : Boolean.FALSE;
		argTraceSet(parameterIndex, "(boolean)", b, b);
		realPreparedStatement.setBoolean(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	/**
	 * Sets the byte.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		argTraceSet(parameterIndex, "(byte)", new Byte(x), new Byte(x));
		realPreparedStatement.setByte(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	/**
	 * Sets the bytes.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		argTraceSet(parameterIndex, "(byte[])", "<byte[]>", x);
		realPreparedStatement.setBytes(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	/**
	 * Sets the character stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader>", reader);
		realPreparedStatement.setCharacterStream(parameterIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 * int)
	 */
	/**
	 * Sets the character stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length
				+ ">", reader);
		realPreparedStatement
				.setCharacterStream(parameterIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 * long)
	 */
	/**
	 * Sets the character stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length
				+ ">", reader);
		realPreparedStatement
				.setCharacterStream(parameterIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	/**
	 * Sets the clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		argTraceSet(parameterIndex, "(Clob)", x == null ? null
				: "<Clob of size " + x.length() + ">", x);
		realPreparedStatement.setClob(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	/**
	 * Sets the clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader>", reader);
		realPreparedStatement.setClob(parameterIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	/**
	 * Sets the clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length
				+ ">", reader);
		realPreparedStatement.setClob(parameterIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	/**
	 * Sets the date.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		argTraceSet(parameterIndex, "(Date)", x, x);
		realPreparedStatement.setDate(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
	 * java.util.Calendar)
	 */
	/**
	 * Sets the date.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param cal
	 *            the cal
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		argTraceSet(parameterIndex, "(Date)", x, x);
		realPreparedStatement.setDate(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	/**
	 * Sets the double.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		argTraceSet(parameterIndex, "(double)", new Double(x), new Double(x));
		realPreparedStatement.setDouble(parameterIndex, x);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	/**
	 * Sets the float.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		argTraceSet(parameterIndex, "(float)", new Float(x), new Float(x));
		realPreparedStatement.setFloat(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	/**
	 * Sets the int.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		argTraceSet(parameterIndex, "(int)", new Integer(x), new Integer(x));
		realPreparedStatement.setInt(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	/**
	 * Sets the long.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		argTraceSet(parameterIndex, "(long)", new Long(x), new Long(x));
		realPreparedStatement.setLong(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	/**
	 * Sets the n character stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param value
	 *            the value
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader>", value);
		realPreparedStatement.setNCharacterStream(parameterIndex, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader,
	 * long)
	 */
	/**
	 * Sets the n character stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param value
	 *            the value
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length
				+ ">", value);
		realPreparedStatement
				.setNCharacterStream(parameterIndex, value, length);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	/**
	 * Sets the n clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param value
	 *            the value
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		argTraceSet(parameterIndex, "(NClob)", "<NClob>", value);
		realPreparedStatement.setNClob(parameterIndex, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	/**
	 * Sets the n clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader>", reader);
		realPreparedStatement.setNClob(parameterIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	/**
	 * Sets the n clob.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param reader
	 *            the reader
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Reader)", "<Reader of length " + length
				+ ">", reader);
		realPreparedStatement.setNClob(parameterIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	/**
	 * Sets the n string.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param value
	 *            the value
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		argTraceSet(parameterIndex, "(String)", value, value);
		realPreparedStatement.setNString(parameterIndex, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	/**
	 * Sets the null.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param sqlType
	 *            the sql type
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		argTraceSet(parameterIndex, null, null, null);
		realPreparedStatement.setNull(parameterIndex, sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	/**
	 * Sets the null.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param sqlType
	 *            the sql type
	 * @param typeName
	 *            the type name
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		argTraceSet(parameterIndex, null, null, null);
		realPreparedStatement.setNull(parameterIndex, sqlType, typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	/**
	 * Sets the object.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		argTraceSet(parameterIndex, getTypeHelp(x), x, x);
		realPreparedStatement.setObject(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	/**
	 * Sets the object.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param targetSqlType
	 *            the target sql type
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		argTraceSet(parameterIndex, getTypeHelp(x), x, x);
		realPreparedStatement.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * Gets the type help.
	 * 
	 * @param x
	 *            the x
	 * @return the type help
	 */
	private String getTypeHelp(Object x) {
		if (x == null) {
			return "(null)";
		} else {
			return "(" + x.getClass().getName() + ")";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int,
	 * int)
	 */
	/**
	 * Sets the object.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param targetSqlType
	 *            the target sql type
	 * @param scaleOrLength
	 *            the scale or length
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		argTraceSet(parameterIndex, getTypeHelp(x), x, x);
		realPreparedStatement.setObject(parameterIndex, x, targetSqlType,
				scaleOrLength);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	/**
	 * Sets the ref.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		argTraceSet(parameterIndex, "(Ref)", x, x);
		realPreparedStatement.setRef(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	/**
	 * Sets the row id.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		argTraceSet(parameterIndex, "(RowId)", x, x);
		realPreparedStatement.setRowId(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	/**
	 * Sets the sqlxml.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param xmlObject
	 *            the xml object
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		argTraceSet(parameterIndex, "(SQLXML)", xmlObject, xmlObject);
		realPreparedStatement.setSQLXML(parameterIndex, xmlObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	/**
	 * Sets the short.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		argTraceSet(parameterIndex, "(short)", new Short(x), new Short(x));
		realPreparedStatement.setShort(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	/**
	 * Sets the string.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		argTraceSet(parameterIndex, "(String)", x, x);
		realPreparedStatement.setString(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	/**
	 * Sets the time.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		argTraceSet(parameterIndex, "(Time)", x, x);
		realPreparedStatement.setTime(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
	 * java.util.Calendar)
	 */
	/**
	 * Sets the time.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param cal
	 *            the cal
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		argTraceSet(parameterIndex, "(Time)", x, x);
		realPreparedStatement.setTime(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	/**
	 * Sets the timestamp.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		argTraceSet(parameterIndex, "(Date)", x, x);
		realPreparedStatement.setTimestamp(parameterIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
	 * java.util.Calendar)
	 */
	/**
	 * Sets the timestamp.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param cal
	 *            the cal
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		argTraceSet(parameterIndex, "(Timestamp)", x, x);
		realPreparedStatement.setTimestamp(parameterIndex, x, cal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	/**
	 * Sets the url.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		argTraceSet(parameterIndex, "(URL)", x, x);
		realPreparedStatement.setURL(parameterIndex, x);
	}

	/**
	 * Sets the unicode stream.
	 * 
	 * @param parameterIndex
	 *            the parameter index
	 * @param x
	 *            the x
	 * @param length
	 *            the length
	 * @throws SQLException
	 *             the sQL exception
	 * @deprecated
	 */
	@SuppressWarnings("dep-ann")
	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		argTraceSet(parameterIndex, "(Unicode InputStream)",
				"<Unicode InputStream of length " + length + ">", x);
		realPreparedStatement.setUnicodeStream(parameterIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinacti.kb.logsql.StatementWarpper#unwrap(java.lang.Class)
	 */
	/**
	 * Unwrap.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param iface
	 *            the iface
	 * @return the t
	 * @throws SQLException
	 *             the sQL exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return (iface != null && (iface == PreparedStatement.class || iface == Statement.class)) ? (T) this
				: realPreparedStatement.unwrap(iface);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinacti.kb.logsql.StatementWarpper#isWrapperFor(java.lang.Class)
	 */
	/**
	 * Checks if is wrapper for.
	 * 
	 * @param iface
	 *            the iface
	 * @return true, if is wrapper for
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface != null && (iface == PreparedStatement.class || iface == Statement.class))
				|| realPreparedStatement.isWrapperFor(iface);
	}

}
