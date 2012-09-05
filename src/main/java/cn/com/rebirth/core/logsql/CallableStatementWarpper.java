/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: CallableStatementWarpper.java 2011-5-16 11:21:29 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * The Class CallableStatementWarpper.
 */
public class CallableStatementWarpper  extends PreparedStatementWarpper implements CallableStatement {

	/** The real callable statement. */
	private CallableStatement realCallableStatement;

	/**
	 * Gets the real callable statement.
	 *
	 * @return the real callable statement
	 */
	public CallableStatement getRealCallableStatement() {
		return realCallableStatement;
	}

	/**
	 * Instantiates a new callable statement warpper.
	 *
	 * @param sql the sql
	 * @param connectionWarpper the connection warpper
	 * @param realCallableStatement the real callable statement
	 */
	public CallableStatementWarpper(String sql, ConnectionWarpper connectionWarpper,
			CallableStatement realCallableStatement) {
		super(sql, connectionWarpper, realCallableStatement);
		this.realCallableStatement = realCallableStatement;
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDate(int)
	 */
	@Override
	public Date getDate(int parameterIndex) throws SQLException {
		return realCallableStatement.getDate(parameterIndex);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		return realCallableStatement.getDate(parameterIndex, cal);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getRef(java.lang.String)
	 */
	@Override
	public Ref getRef(String parameterName) throws SQLException {
		return realCallableStatement.getRef(parameterName);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String parameterName) throws SQLException {
		return realCallableStatement.getTime(parameterName);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
	 */
	@Override
	public void setTime(String parameterName, Time x) throws SQLException {
		realCallableStatement.setTime(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBlob(int)
	 */
	@Override
	public Blob getBlob(int i) throws SQLException {
		return realCallableStatement.getBlob(i);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getClob(int)
	 */
	@Override
	public Clob getClob(int i) throws SQLException {
		return realCallableStatement.getClob(i);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getArray(int)
	 */
	@Override
	public Array getArray(int i) throws SQLException {
		return realCallableStatement.getArray(i);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBytes(int)
	 */
	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException {
		return realCallableStatement.getBytes(parameterIndex);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDouble(int)
	 */
	@Override
	public double getDouble(int parameterIndex) throws SQLException {
		return realCallableStatement.getDouble(parameterIndex);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getInt(int)
	 */
	@Override
	public int getInt(int parameterIndex) throws SQLException {
		return realCallableStatement.getInt(parameterIndex);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#wasNull()
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return realCallableStatement.wasNull();
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTime(int)
	 */
	@Override
	public Time getTime(int parameterIndex) throws SQLException {
		return realCallableStatement.getTime(parameterIndex);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		return realCallableStatement.getTime(parameterIndex, cal);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		return (realCallableStatement.getTimestamp(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		realCallableStatement.setTimestamp(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getString(int)
	 */
	@Override
	public String getString(int parameterIndex) throws SQLException {
		return (realCallableStatement.getString(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(int, int)
	 */
	@Override
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
		argTraceSet(parameterIndex, null, "<OUT>", null);
		realCallableStatement.registerOutParameter(parameterIndex, sqlType);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
	 */
	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
		argTraceSet(parameterIndex, null, "<OUT>", null);
		realCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, java.lang.String)
	 */
	@Override
	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
		argTraceSet(paramIndex, null, "<OUT>", null);
		realCallableStatement.registerOutParameter(paramIndex, sqlType, typeName);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(String parameterName) throws SQLException {
		return (realCallableStatement.getByte(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String parameterName) throws SQLException {
		return (realCallableStatement.getDouble(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String parameterName) throws SQLException {
		return (realCallableStatement.getFloat(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String parameterName) throws SQLException {
		return (realCallableStatement.getInt(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String parameterName) throws SQLException {
		return (realCallableStatement.getLong(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getShort(java.lang.String)
	 */
	@Override
	public short getShort(String parameterName) throws SQLException {
		return (realCallableStatement.getShort(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String parameterName) throws SQLException {
		return (realCallableStatement.getBoolean(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(String parameterName) throws SQLException {
		return (realCallableStatement.getBytes(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
	 */
	@Override
	public void setByte(String parameterName, byte x) throws SQLException {
		realCallableStatement.setByte(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(String parameterName, double x) throws SQLException {
		realCallableStatement.setDouble(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
	 */
	@Override
	public void setFloat(String parameterName, float x) throws SQLException {
		realCallableStatement.setFloat(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int)
	 */
	@Override
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
		realCallableStatement.registerOutParameter(parameterName, sqlType);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setInt(java.lang.String, int)
	 */
	@Override
	public void setInt(String parameterName, int x) throws SQLException {
		realCallableStatement.setInt(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException {
		realCallableStatement.setNull(parameterName, sqlType);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, int)
	 */
	@Override
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
		realCallableStatement.registerOutParameter(parameterName, sqlType, scale);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setLong(java.lang.String, long)
	 */
	@Override
	public void setLong(String parameterName, long x) throws SQLException {
		realCallableStatement.setLong(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setShort(java.lang.String, short)
	 */
	@Override
	public void setShort(String parameterName, short x) throws SQLException {
		realCallableStatement.setShort(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		realCallableStatement.setBoolean(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
	 */
	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		realCallableStatement.setBytes(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException {
		return (realCallableStatement.getBoolean(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return (realCallableStatement.getTimestamp(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		realCallableStatement.setAsciiStream(parameterName, x, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		realCallableStatement.setBinaryStream(parameterName, x, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	@Override
	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		realCallableStatement.setCharacterStream(parameterName, reader, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String parameterName) throws SQLException {
		return (realCallableStatement.getObject(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setObject(String parameterName, Object x) throws SQLException {
		realCallableStatement.setObject(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		realCallableStatement.setObject(parameterName, x, targetSqlType);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int, int)
	 */
	@Override
	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		realCallableStatement.setObject(parameterName, x, targetSqlType, scale);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
		return (realCallableStatement.getTimestamp(parameterIndex, cal));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDate(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		return (realCallableStatement.getDate(parameterName, cal));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTime(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		return (realCallableStatement.getTime(parameterName, cal));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
		return (realCallableStatement.getTimestamp(parameterName, cal));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date, java.util.Calendar)
	 */
	@Override
	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		realCallableStatement.setDate(parameterName, x, cal);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time, java.util.Calendar)
	 */
	@Override
	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		realCallableStatement.setTime(parameterName, x, cal);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp, java.util.Calendar)
	 */
	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		realCallableStatement.setTimestamp(parameterName, x, cal);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getShort(int)
	 */
	@Override
	public short getShort(int parameterIndex) throws SQLException {
		return (realCallableStatement.getShort(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getLong(int)
	 */
	@Override
	public long getLong(int parameterIndex) throws SQLException {
		return (realCallableStatement.getLong(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getFloat(int)
	 */
	@Override
	public float getFloat(int parameterIndex) throws SQLException {
		return (realCallableStatement.getFloat(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getRef(int)
	 */
	@Override
	public Ref getRef(int i) throws SQLException {
		return (realCallableStatement.getRef(i));
	}

	/**
	 * Gets the big decimal.
	 *
	 * @param parameterIndex the parameter index
	 * @param scale the scale
	 * @return the big decimal
	 * @throws SQLException the sQL exception
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("dep-ann")
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
		return (realCallableStatement.getBigDecimal(parameterIndex, scale));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getURL(int)
	 */
	@Override
	public URL getURL(int parameterIndex) throws SQLException {
		return (realCallableStatement.getURL(parameterIndex));

	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		return (realCallableStatement.getBigDecimal(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getByte(int)
	 */
	@Override
	public byte getByte(int parameterIndex) throws SQLException {
		return (realCallableStatement.getByte(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getObject(int)
	 */
	@Override
	public Object getObject(int parameterIndex) throws SQLException {
		return (realCallableStatement.getObject(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return (realCallableStatement.getObject(i, map));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getString(java.lang.String)
	 */
	@Override
	public String getString(String parameterName) throws SQLException {
		return (realCallableStatement.getString(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
		realCallableStatement.registerOutParameter(parameterName, sqlType, typeName);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		realCallableStatement.setNull(parameterName, sqlType, typeName);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(String parameterName, String x) throws SQLException {
		realCallableStatement.setString(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		return (realCallableStatement.getBigDecimal(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
	 */
	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
		return (realCallableStatement.getObject(parameterName, map));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		realCallableStatement.setBigDecimal(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(String parameterName) throws SQLException {
		return (realCallableStatement.getURL(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getRowId(int)
	 */
	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
		return (realCallableStatement.getRowId(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getRowId(java.lang.String)
	 */
	@Override
	public RowId getRowId(String parameterName) throws SQLException {
		return (realCallableStatement.getRowId(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setRowId(java.lang.String, java.sql.RowId)
	 */
	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
		realCallableStatement.setRowId(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setNString(String parameterName, String value) throws SQLException {
		realCallableStatement.setNString(parameterName, value);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setNCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		realCallableStatement.setNCharacterStream(parameterName, reader, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.sql.NClob)
	 */
	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
		realCallableStatement.setNClob(parameterName, value);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		realCallableStatement.setClob(parameterName, reader, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		realCallableStatement.setBlob(parameterName, inputStream, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		realCallableStatement.setNClob(parameterName, reader, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNClob(int)
	 */
	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
		return (realCallableStatement.getNClob(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNClob(java.lang.String)
	 */
	@Override
	public NClob getNClob(String parameterName) throws SQLException {
		return (realCallableStatement.getNClob(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		realCallableStatement.setSQLXML(parameterName, xmlObject);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		return (realCallableStatement.getSQLXML(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getSQLXML(java.lang.String)
	 */
	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		return (realCallableStatement.getSQLXML(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNString(int)
	 */
	@Override
	public String getNString(int parameterIndex) throws SQLException {
		return (realCallableStatement.getNString(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNString(java.lang.String)
	 */
	@Override
	public String getNString(String parameterName) throws SQLException {
		return (realCallableStatement.getNString(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		return (realCallableStatement.getNCharacterStream(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getNCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		return (realCallableStatement.getNCharacterStream(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		return (realCallableStatement.getCharacterStream(parameterIndex));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
		return (realCallableStatement.getCharacterStream(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.sql.Blob)
	 */
	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
		realCallableStatement.setBlob(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.sql.Clob)
	 */
	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
		realCallableStatement.setClob(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
		realCallableStatement.setAsciiStream(parameterName, x, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
		realCallableStatement.setBinaryStream(parameterName, x, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		realCallableStatement.setCharacterStream(parameterName, reader, length);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		realCallableStatement.setAsciiStream(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		realCallableStatement.setBinaryStream(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		realCallableStatement.setCharacterStream(parameterName, reader);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(String parameterName, Reader reader) throws SQLException {
		realCallableStatement.setNCharacterStream(parameterName, reader);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setClob(String parameterName, Reader reader) throws SQLException {
		realCallableStatement.setClob(parameterName, reader);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		realCallableStatement.setBlob(parameterName, inputStream);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setNClob(String parameterName, Reader reader) throws SQLException {
		realCallableStatement.setNClob(parameterName, reader);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
	 */
	@Override
	public void setURL(String parameterName, URL val) throws SQLException {
		realCallableStatement.setURL(parameterName, val);
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getArray(java.lang.String)
	 */
	@Override
	public Array getArray(String parameterName) throws SQLException {
		return (realCallableStatement.getArray(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(String parameterName) throws SQLException {
		return (realCallableStatement.getBlob(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(String parameterName) throws SQLException {
		return (realCallableStatement.getClob(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String parameterName) throws SQLException {
		return (realCallableStatement.getDate(parameterName));
	}

	/* (non-Javadoc)
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public void setDate(String parameterName, Date x) throws SQLException {
		realCallableStatement.setDate(parameterName, x);
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.PreparedStatementWarpper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface != null && (iface == CallableStatement.class || iface == PreparedStatement.class || iface == Statement.class))
				|| realCallableStatement.isWrapperFor(iface);
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.PreparedStatementWarpper#unwrap(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return (iface != null && (iface == CallableStatement.class || iface == PreparedStatement.class || iface == Statement.class)) ? (T) this
				: realCallableStatement.unwrap(iface);
	}

}
