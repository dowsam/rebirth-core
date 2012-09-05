/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: Utilities.java 2011-5-16 11:18:36 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

/**
 * The Class Utilities.
 */
class Utilities {
	/**
	 * Right justify.
	 *
	 * @param fieldSize the field size
	 * @param tempfield the tempfield
	 * @return the string
	 */
	public static String rightJustify(int fieldSize, String tempfield) {
		String field=tempfield;
		if (field == null) {
			field = "";
		}
		StringBuffer output = new StringBuffer();
		for (int i = 0, j = fieldSize - field.length(); i < j; i++) {
			output.append(' ');
		}
		output.append(field);
		return output.toString();
	}
}
