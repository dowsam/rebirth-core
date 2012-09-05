/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: BasicSqlFormatter.java 2011-5-16 11:16:59 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * The Class BasicSqlFormatter.
 */
public class BasicSqlFormatter {
	/** The Constant WHITESPACE. */
	public static final String WHITESPACE = " \n\r\f\t";

	/** The Constant BEGIN_CLAUSES. */
	private static final Set<String> BEGIN_CLAUSES = new HashSet<String>();
	
	/** The Constant END_CLAUSES. */
	private static final Set<String> END_CLAUSES = new HashSet<String>();
	
	/** The Constant LOGICAL. */
	private static final Set<String> LOGICAL = new HashSet<String>();
	
	/** The Constant QUANTIFIERS. */
	private static final Set<String> QUANTIFIERS = new HashSet<String>();
	
	/** The Constant DML. */
	private static final Set<String> DML = new HashSet<String>();
	
	/** The Constant MISC. */
	private static final Set<String> MISC = new HashSet<String>();

	static {
		BEGIN_CLAUSES.add("left");
		BEGIN_CLAUSES.add("right");
		BEGIN_CLAUSES.add("inner");
		BEGIN_CLAUSES.add("outer");
		BEGIN_CLAUSES.add("group");
		BEGIN_CLAUSES.add("order");

		END_CLAUSES.add("where");
		END_CLAUSES.add("set");
		END_CLAUSES.add("having");
		END_CLAUSES.add("join");
		END_CLAUSES.add("from");
		END_CLAUSES.add("by");
		END_CLAUSES.add("join");
		END_CLAUSES.add("into");
		END_CLAUSES.add("union");

		LOGICAL.add("and");
		LOGICAL.add("or");
		LOGICAL.add("when");
		LOGICAL.add("else");
		LOGICAL.add("end");

		QUANTIFIERS.add("in");
		QUANTIFIERS.add("all");
		QUANTIFIERS.add("exists");
		QUANTIFIERS.add("some");
		QUANTIFIERS.add("any");

		DML.add("insert");
		DML.add("update");
		DML.add("delete");

		MISC.add("select");
		MISC.add("on");
	}

	/** The Constant indentString. */
	static final String indentString = "    ";
	
	/** The Constant initial. */
	static final String initial = "\n    ";

	/**
	 * Format.
	 *
	 * @param source the source
	 * @return the string
	 */
	public String format(String source) {
		return new FormatProcess(source).perform();
	}

	/**
	 * The Class FormatProcess.
	 *
	 * @author l.xue.nong
	 */
	private static class FormatProcess {
		
		/** The begin line. */
		boolean beginLine = true;
		
		/** The after begin before end. */
		boolean afterBeginBeforeEnd = false;
		
		/** The after by or set or from or select. */
		boolean afterByOrSetOrFromOrSelect = false;
		
		/** The after on. */
		boolean afterOn = false;
		
		/** The after between. */
		boolean afterBetween = false;
		
		/** The after insert. */
		boolean afterInsert = false;
		
		/** The in function. */
		int inFunction = 0;
		
		/** The parens since select. */
		int parensSinceSelect = 0;
		
		/** The paren counts. */
		private LinkedList<Integer> parenCounts = new LinkedList<Integer>();
		
		/** The after by or from or selects. */
		private LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<Boolean>();

		/** The indent. */
		int indent = 1;

		/** The result. */
		StringBuffer result = new StringBuffer();
		
		/** The tokens. */
		StringTokenizer tokens;
		
		/** The last token. */
		String lastToken;
		
		/** The token. */
		String token;
		
		/** The lc token. */
		String lcToken;

		/**
		 * Instantiates a new format process.
		 *
		 * @param sql the sql
		 */
		public FormatProcess(String sql) {
			tokens = new StringTokenizer(sql, "()+*/-=<>'`\"[]," + WHITESPACE, true);
		}

		/**
		 * Perform.
		 *
		 * @return the string
		 */
		@SuppressWarnings("synthetic-access")
		public String perform() {

			result.append(initial);

			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken();
				lcToken = token.toLowerCase();

				if ("'".equals(token)) {
					String t;
					do {
						t = tokens.nextToken();
						token += t;
					} while (!"'".equals(t) && tokens.hasMoreTokens()); // cannot handle single quotes
				} else if ("\"".equals(token)) {
					String t;
					do {
						t = tokens.nextToken();
						token += t;
					} while (!"\"".equals(t));
				}

				if (afterByOrSetOrFromOrSelect && ",".equals(token)) {
					commaAfterByOrFromOrSelect();
				} else if (afterOn && ",".equals(token)) {
					commaAfterOn();
				}

				else if ("(".equals(token)) {
					openParen();
				} else if (")".equals(token)) {
					closeParen();
				}

				else if (BEGIN_CLAUSES.contains(lcToken)) {
					beginNewClause();
				}

				else if (END_CLAUSES.contains(lcToken)) {
					endNewClause();
				}

				else if ("select".equals(lcToken)) {
					select();
				}

				else if (DML.contains(lcToken)) {
					updateOrInsertOrDelete();
				}

				else if ("values".equals(lcToken)) {
					values();
				}

				else if ("on".equals(lcToken)) {
					on();
				}

				else if (afterBetween && lcToken.equals("and")) {
					misc();
					afterBetween = false;
				}

				else if (LOGICAL.contains(lcToken)) {
					logical();
				}

				else if (isWhitespace(token)) {
					white();
				}

				else {
					misc();
				}

				if (!isWhitespace(token)) {
					lastToken = lcToken;
				}

			}
			return result.toString();
		}

		/**
		 * Comma after on.
		 */
		private void commaAfterOn() {
			out();
			indent--;
			newline();
			afterOn = false;
			afterByOrSetOrFromOrSelect = true;
		}

		/**
		 * Comma after by or from or select.
		 */
		private void commaAfterByOrFromOrSelect() {
			out();
			//			newline();
		}

		/**
		 * Logical.
		 */
		private void logical() {
			if ("end".equals(lcToken)) {
				indent--;
			}
			newline();
			out();
			beginLine = false;
		}

		/**
		 * On.
		 */
		private void on() {
			indent++;
			afterOn = true;
			newline();
			out();
			beginLine = false;
		}

		/**
		 * Misc.
		 */
		private void misc() {
			out();
			if ("between".equals(lcToken)) {
				afterBetween = true;
			}
			if (afterInsert) {
				newline();
				afterInsert = false;
			} else {
				beginLine = false;
				if ("case".equals(lcToken)) {
					indent++;
				}
			}
		}

		/**
		 * White.
		 */
		private void white() {
			if (!beginLine) {
				result.append(" ");
			}
		}

		/**
		 * Update or insert or delete.
		 */
		private void updateOrInsertOrDelete() {
			out();
			indent++;
			beginLine = false;
			if ("update".equals(lcToken)) {
				newline();
			}
			if ("insert".equals(lcToken)) {
				afterInsert = true;
			}
		}

		/**
		 * Select.
		 */
		private void select() {
			out();
			indent++;
			newline();
			parenCounts.addLast(new Integer(parensSinceSelect));
			afterByOrFromOrSelects.addLast(Boolean.valueOf(afterByOrSetOrFromOrSelect));
			parensSinceSelect = 0;
			afterByOrSetOrFromOrSelect = true;
		}

		/**
		 * Out.
		 */
		private void out() {
			result.append(token);
		}

		/**
		 * End new clause.
		 */
		private void endNewClause() {
			if (!afterBeginBeforeEnd) {
				indent--;
				if (afterOn) {
					indent--;
					afterOn = false;
				}
				newline();
			}
			out();
			if (!"union".equals(lcToken)) {
				indent++;
			}
			newline();
			afterBeginBeforeEnd = false;
			afterByOrSetOrFromOrSelect = "by".equals(lcToken) || "set".equals(lcToken) || "from".equals(lcToken);
		}

		/**
		 * Begin new clause.
		 */
		private void beginNewClause() {
			if (!afterBeginBeforeEnd) {
				if (afterOn) {
					indent--;
					afterOn = false;
				}
				indent--;
				newline();
			}
			out();
			beginLine = false;
			afterBeginBeforeEnd = true;
		}

		/**
		 * Values.
		 */
		private void values() {
			indent--;
			newline();
			out();
			indent++;
			newline();
		}

		/**
		 * Close paren.
		 */
		private void closeParen() {
			parensSinceSelect--;
			if (parensSinceSelect < 0) {
				indent--;
				parensSinceSelect = parenCounts.removeLast().intValue();
				afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast().booleanValue();
			}
			if (inFunction > 0) {
				inFunction--;
				out();
			} else {
				if (!afterByOrSetOrFromOrSelect) {
					indent--;
					newline();
				}
				out();
			}
			beginLine = false;
		}

		/**
		 * Open paren.
		 */
		private void openParen() {
			if (isFunctionName(lastToken) || inFunction > 0) {
				inFunction++;
			}
			beginLine = false;
			if (inFunction > 0) {
				out();
			} else {
				out();
				if (!afterByOrSetOrFromOrSelect) {
					indent++;
					newline();
					beginLine = true;
				}
			}
			parensSinceSelect++;
		}

		/**
		 * Checks if is function name.
		 *
		 * @param tok the tok
		 * @return true, if is function name
		 */
		@SuppressWarnings("synthetic-access")
		private static boolean isFunctionName(String tok) {
			final char begin = tok.charAt(0);
			final boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;
			return isIdentifier && !LOGICAL.contains(tok) && !END_CLAUSES.contains(tok) && !QUANTIFIERS.contains(tok)
					&& !DML.contains(tok) && !MISC.contains(tok);
		}

		/**
		 * Checks if is whitespace.
		 *
		 * @param token the token
		 * @return true, if is whitespace
		 */
		private static boolean isWhitespace(String token) {
			return WHITESPACE.indexOf(token) >= 0;
		}

		/**
		 * Newline.
		 */
		private void newline() {
			result.append("\n");
			for (int i = 0; i < indent; i++) {
				result.append(indentString);
			}
			beginLine = true;
		}
	}
}
