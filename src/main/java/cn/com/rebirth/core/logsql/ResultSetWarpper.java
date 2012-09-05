/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: ResultSetWarpper.java 2011-5-16 11:25:18 l.xue.nong$$
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
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * The Class ResultSetWarpper.
 */
public class ResultSetWarpper implements ResultSet {

	/** The real result set. */
	private ResultSet realResultSet;

	/** The parent. */
	private StatementWarpper parent;

	/**
	 * Gets the real result set.
	 * 
	 * @return the real result set
	 */
	public ResultSet getRealResultSet() {
		return realResultSet;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public StatementWarpper getParent() {
		return parent;
	}

	/**
	 * Instantiates a new result set warpper.
	 * 
	 * @param parent
	 *            the parent
	 * @param realResultSet
	 *            the real result set
	 */
	public ResultSetWarpper(StatementWarpper parent, ResultSet realResultSet) {
		if (realResultSet == null) {
			throw new IllegalArgumentException(
					"Must provide a non null real ResultSet");
		}
		this.realResultSet = realResultSet;
		this.parent = parent;
	}

	// forwarding methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnName, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRow()
	 */
	@Override
	public int getRow() throws SQLException {
		return (realResultSet.getRow());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	@Override
	public void cancelRowUpdates() throws SQLException {
		realResultSet.cancelRowUpdates();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(int)
	 */
	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return (realResultSet.getTime(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String columnName) throws SQLException {
		return (realResultSet.getTime(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return (realResultSet.getTime(columnIndex, cal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return (realResultSet.getTime(columnName, cal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#absolute(int)
	 */
	@Override
	public boolean absolute(int row) throws SQLException {
		return (realResultSet.absolute(row));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return (realResultSet.getTimestamp(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return (realResultSet.getTimestamp(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		return (realResultSet.getTimestamp(columnIndex, cal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		return (realResultSet.getTimestamp(columnName, cal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	@Override
	public void moveToInsertRow() throws SQLException {
		realResultSet.moveToInsertRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#relative(int)
	 */
	@Override
	public boolean relative(int rows) throws SQLException {
		return (realResultSet.relative(rows));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#previous()
	 */
	@Override
	public boolean previous() throws SQLException {
		return (realResultSet.previous());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	@Override
	public void moveToCurrentRow() throws SQLException {
		realResultSet.moveToCurrentRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRef(int)
	 */
	@Override
	public Ref getRef(int i) throws SQLException {
		return (realResultSet.getRef(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		realResultSet.updateRef(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	@Override
	public Ref getRef(String colName) throws SQLException {
		return (realResultSet.getRef(colName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	@Override
	public void updateRef(String columnName, Ref x) throws SQLException {
		realResultSet.updateRef(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	@Override
	public Blob getBlob(int i) throws SQLException {
		return (realResultSet.getBlob(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		realResultSet.updateBlob(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(String colName) throws SQLException {
		return (realResultSet.getBlob(colName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	@Override
	public void updateBlob(String columnName, Blob x) throws SQLException {
		realResultSet.updateBlob(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getClob(int)
	 */
	@Override
	public Clob getClob(int i) throws SQLException {
		return (realResultSet.getClob(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		realResultSet.updateClob(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(String colName) throws SQLException {
		return (realResultSet.getClob(colName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	@Override
	public void updateClob(String columnName, Clob x) throws SQLException {
		realResultSet.updateClob(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return (realResultSet.getBoolean(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		return (realResultSet.getBoolean(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getArray(int)
	 */
	@Override
	public Array getArray(int i) throws SQLException {
		return (realResultSet.getArray(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		realResultSet.updateArray(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	@Override
	public Array getArray(String colName) throws SQLException {
		return (realResultSet.getArray(colName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	@Override
	public void updateArray(String columnName, Array x) throws SQLException {
		realResultSet.updateArray(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return (realResultSet.getRowId(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getRowId(java.lang.String)
	 */
	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return (realResultSet.getRowId(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
	 */
	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		realResultSet.updateRowId(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
	 */
	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		realResultSet.updateRowId(columnLabel, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		return (realResultSet.getHoldability());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return (realResultSet.isClosed());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNString(int, java.lang.String)
	 */
	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		realResultSet.updateNString(columnIndex, nString);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		realResultSet.updateNString(columnLabel, nString);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
	 */
	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		realResultSet.updateNClob(columnIndex, nClob);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
	 */
	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, nClob);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return (realResultSet.getNClob(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNClob(java.lang.String)
	 */
	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return (realResultSet.getNClob(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return (realResultSet.getSQLXML(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getSQLXML(java.lang.String)
	 */
	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return (realResultSet.getSQLXML(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		realResultSet.updateSQLXML(columnIndex, xmlObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		realResultSet.updateSQLXML(columnLabel, xmlObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNString(int)
	 */
	@Override
	public String getNString(int columnIndex) throws SQLException {
		return (realResultSet.getNString(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNString(java.lang.String)
	 */
	@Override
	public String getNString(String columnLabel) throws SQLException {
		return (realResultSet.getNString(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return (realResultSet.getNCharacterStream(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return (realResultSet.getNCharacterStream(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 * java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		realResultSet.updateNCharacterStream(columnLabel, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream,
	 * long)
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnLabel, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream, long)
	 */
	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		realResultSet.updateBinaryStream(columnLabel, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		realResultSet.updateCharacterStream(columnLabel, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		realResultSet.updateBlob(columnIndex, inputStream, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream,
	 * long)
	 */
	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		realResultSet.updateBlob(columnLabel, inputStream, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		realResultSet.updateClob(columnIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader,
	 * long)
	 */
	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		realResultSet.updateClob(columnLabel, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		realResultSet.updateNClob(columnIndex, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader,
	 * long)
	 */
	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(int columnIndex, Reader reader)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String,
	 * java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnLabel, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
	 * java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		realResultSet.updateAsciiStream(columnLabel, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		realResultSet.updateBinaryStream(columnLabel, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateCharacterStream(columnLabel, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
	 */
	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		realResultSet.updateBlob(columnIndex, inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		realResultSet.updateBlob(columnLabel, inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
	 */
	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		realResultSet.updateClob(columnIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateClob(columnLabel, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
	 */
	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		realResultSet.updateNClob(columnIndex, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return (realResultSet.isBeforeFirst());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getShort(int)
	 */
	@Override
	public short getShort(int columnIndex) throws SQLException {
		return (realResultSet.getShort(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	@Override
	public short getShort(String columnName) throws SQLException {
		return (realResultSet.getShort(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 */
	@Override
	public int getInt(int columnIndex) throws SQLException {
		return (realResultSet.getInt(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String columnName) throws SQLException {
		return (realResultSet.getInt(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#close()
	 */
	@Override
	public void close() throws SQLException {
		realResultSet.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return (realResultSet.getMetaData());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getType()
	 */
	@Override
	public int getType() throws SQLException {
		return (realResultSet.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return (realResultSet.getDouble(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String columnName) throws SQLException {
		return (realResultSet.getDouble(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#deleteRow()
	 */
	@Override
	public void deleteRow() throws SQLException {
		realResultSet.deleteRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	@Override
	public int getConcurrency() throws SQLException {
		return (realResultSet.getConcurrency());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	@Override
	public boolean rowUpdated() throws SQLException {
		return (realResultSet.rowUpdated());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(int)
	 */
	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return (realResultSet.getDate(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String columnName) throws SQLException {
		return (realResultSet.getDate(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return (realResultSet.getDate(columnIndex, cal));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return (realResultSet.getDate(columnName, cal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#last()
	 */
	@Override
	public boolean last() throws SQLException {
		return (realResultSet.last());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowInserted()
	 */
	@Override
	public boolean rowInserted() throws SQLException {
		return (realResultSet.rowInserted());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	@Override
	public boolean rowDeleted() throws SQLException {
		return (realResultSet.rowDeleted());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	@Override
	public void updateNull(int columnIndex) throws SQLException {
		realResultSet.updateNull(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	@Override
	public void updateNull(String columnName) throws SQLException {
		realResultSet.updateNull(columnName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		realResultSet.updateShort(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	@Override
	public void updateShort(String columnName, short x) throws SQLException {
		realResultSet.updateShort(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		realResultSet.updateBoolean(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	@Override
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		realResultSet.updateBoolean(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		realResultSet.updateByte(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	@Override
	public void updateByte(String columnName, byte x) throws SQLException {
		realResultSet.updateByte(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		realResultSet.updateInt(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	@Override
	public void updateInt(String columnName, int x) throws SQLException {
		realResultSet.updateInt(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(int)
	 */
	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return (realResultSet.getObject(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String columnName) throws SQLException {
		return (realResultSet.getObject(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	@Override
	public Object getObject(String colName, Map<String, Class<?>> map)
			throws SQLException {
		return (realResultSet.getObject(colName, map));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#next()
	 */
	@Override
	public boolean next() throws SQLException {
		return (realResultSet.next());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		realResultSet.updateLong(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	@Override
	public void updateLong(String columnName, long x) throws SQLException {
		realResultSet.updateLong(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		realResultSet.updateFloat(columnIndex, x);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	@Override
	public void updateFloat(String columnName, float x) throws SQLException {
		realResultSet.updateFloat(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		realResultSet.updateDouble(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	@Override
	public void updateDouble(String columnName, double x) throws SQLException {
		realResultSet.updateDouble(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getStatement()
	 */
	@Override
	public Statement getStatement() throws SQLException {
		Statement s = realResultSet.getStatement();
		if (s == null) {
			return s;
		} else {
			return new StatementWarpper(
					new ConnectionWarpper(s.getConnection()), s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		return (realResultSet.getObject(columnIndex, map));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		realResultSet.updateString(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateString(String columnName, String x) throws SQLException {
		realResultSet.updateString(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return (realResultSet.getAsciiStream(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	@Override
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return (realResultSet.getAsciiStream(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		realResultSet.updateBigDecimal(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getURL(int)
	 */
	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return (realResultSet.getURL(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String,
	 * java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		realResultSet.updateBigDecimal(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(String columnName) throws SQLException {
		return (realResultSet.getURL(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		realResultSet.updateBytes(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	@Override
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		realResultSet.updateBytes(columnName, x);
	}

	/**
	 * Gets the unicode stream.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return the unicode stream
	 * @throws SQLException
	 *             the sQL exception
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("dep-ann")
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return (realResultSet.getUnicodeStream(columnIndex));
	}

	/**
	 * Gets the unicode stream.
	 * 
	 * @param columnName
	 *            the column name
	 * @return the unicode stream
	 * @throws SQLException
	 *             the sQL exception
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("dep-ann")
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return (realResultSet.getUnicodeStream(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		realResultSet.updateDate(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public void updateDate(String columnName, Date x) throws SQLException {
		realResultSet.updateDate(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		return (realResultSet.getFetchSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return (realResultSet.getWarnings());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return (realResultSet.getBinaryStream(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	@Override
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return (realResultSet.getBinaryStream(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		realResultSet.clearWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		realResultSet.updateTimestamp(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String,
	 * java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		realResultSet.updateTimestamp(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#first()
	 */
	@Override
	public boolean first() throws SQLException {
		return (realResultSet.first());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCursorName()
	 */
	@Override
	public String getCursorName() throws SQLException {
		return (realResultSet.getCursorName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	@Override
	public int findColumn(String columnName) throws SQLException {
		return (realResultSet.findColumn(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#wasNull()
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return (realResultSet.wasNull());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
	 * java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnName, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getString(int)
	 */
	@Override
	public String getString(int columnIndex) throws SQLException {
		return (realResultSet.getString(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	@Override
	public String getString(String columnName) throws SQLException {
		return (realResultSet.getString(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return (realResultSet.getCharacterStream(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(String columnName) throws SQLException {
		return (realResultSet.getCharacterStream(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		realResultSet.setFetchDirection(direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
	 * java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		realResultSet.updateCharacterStream(columnName, reader, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getByte(int)
	 */
	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return (realResultSet.getByte(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(String columnName) throws SQLException {
		return (realResultSet.getByte(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		realResultSet.updateTime(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	@Override
	public void updateTime(String columnName, Time x) throws SQLException {
		realResultSet.updateTime(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return (realResultSet.getBytes(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(String columnName) throws SQLException {
		return (realResultSet.getBytes(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		return (realResultSet.isAfterLast());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	@Override
	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		realResultSet.updateObject(columnIndex, x, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		realResultSet.updateObject(columnIndex, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object,
	 * int)
	 */
	@Override
	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		realResultSet.updateObject(columnName, x, scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void updateObject(String columnName, Object x) throws SQLException {
		realResultSet.updateObject(columnName, x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		return (realResultSet.getFetchDirection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getLong(int)
	 */
	@Override
	public long getLong(int columnIndex) throws SQLException {
		return (realResultSet.getLong(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String columnName) throws SQLException {
		return (realResultSet.getLong(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isFirst()
	 */
	@Override
	public boolean isFirst() throws SQLException {
		return (realResultSet.isFirst());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#insertRow()
	 */
	@Override
	public void insertRow() throws SQLException {
		realResultSet.insertRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return (realResultSet.getFloat(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String columnName) throws SQLException {
		return (realResultSet.getFloat(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#isLast()
	 */
	@Override
	public boolean isLast() throws SQLException {
		return (realResultSet.isLast());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int rows) throws SQLException {
		realResultSet.setFetchSize(rows);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#updateRow()
	 */
	@Override
	public void updateRow() throws SQLException {
		realResultSet.updateRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws SQLException {
		realResultSet.beforeFirst();
	}

	/**
	 * Gets the big decimal.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @param scale
	 *            the scale
	 * @return the big decimal
	 * @throws SQLException
	 *             the sQL exception
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("dep-ann")
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return (realResultSet.getBigDecimal(columnIndex, scale));
	}

	/**
	 * Gets the big decimal.
	 * 
	 * @param columnName
	 *            the column name
	 * @param scale
	 *            the scale
	 * @return the big decimal
	 * @throws SQLException
	 *             the sQL exception
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("dep-ann")
	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		return (realResultSet.getBigDecimal(columnName, scale));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return (realResultSet.getBigDecimal(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return (realResultSet.getBigDecimal(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#afterLast()
	 */
	@Override
	public void afterLast() throws SQLException {
		realResultSet.afterLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#refreshRow()
	 */
	@Override
	public void refreshRow() throws SQLException {
		realResultSet.refreshRow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return (iface != null && (iface == ResultSet.class)) ? (T) this
				: realResultSet.unwrap(iface);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface != null && (iface == ResultSet.class))
				|| realResultSet.isWrapperFor(iface);
	}
}
