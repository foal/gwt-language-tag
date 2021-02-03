package org.jresearch.locale.langtag.parser;

import java.util.function.Predicate;

/**
 * Simple methods to work with ASCII String
 */
public class ParserUtil {

	public static final Predicate<String> LOW_STR = ParserUtil::isLow;

	private static final CharPredicate UP = ParserUtil::isUp;
	private static final CharPredicate LOW = ParserUtil::isLow;
	private static final CharPredicate NOT_UP = UP.negate();
	private static final CharPredicate NOT_LOW = LOW.negate();
	private static final CharPredicate ALPHA = UP.or(LOW);
	private static final CharPredicate NUM = ParserUtil::isNum;
	private static final CharPredicate ALPHA_NUM = ALPHA.or(NUM);

	private static char tit(char c, int p) {
		return p == 0 ? up(c) : low(c);
	}

	private static boolean isTit(char c, int p) {
		return p == 0 ? isUp(c) : isLow(c);
	}

	private static char up(char c) {
		return isLow(c) ? (char) (c - ' ') : c;
	}

	private static char low(char c) {
		return isUp(c) ? (char) (c + ' ') : c;
	}

	private static boolean isUp(char c) {
		return 'A' <= c && c <= 'Z';
	}

	private static boolean isLow(char c) {
		return 'a' <= c && c <= 'z';
	}

	public static boolean isNum(char c) {
		return '0' <= c && c <= '9';
	}

	public static boolean isAlphaNum(char c) {
		return ALPHA_NUM.test(c);
	}

	public static boolean isAlpha(char c) {
		return ALPHA.test(c);
	}

	public static boolean equalsIgnoreCase(char c1, char c2) {
		return c1 == c2 || low(c1) == low(c2);
	}

	public static String tit(String str) {
		return mapPos(str, ParserUtil::isTit, ParserUtil::tit);
	}

	public static boolean isTit(String str) {
		return isPos(str, ParserUtil::isTit);
	}

	public static String low(String str) {
		return map(str, NOT_UP, ParserUtil::low);
	}

	public static String up(String str) {
		return map(str, NOT_LOW, ParserUtil::up);
	}

	public static boolean isAlpha(String str) {
		return is(str, ALPHA);
	}

	public static boolean isNum(String str) {
		return is(str, NUM);
	}

	public static boolean isAlphaNum(String str) {
		return is(str, ALPHA_NUM);
	}

	public static boolean isLow(String str) {
		return is(str, NOT_UP);
	}

	public static boolean isUp(String str) {
		return is(str, NOT_LOW);
	}

	private static boolean is(String str, CharPredicate check) {
		return isPos(str, (c, p) -> check.test(c));
	}

	private static boolean isPos(String str, CharPosPredicate check) {
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (!check.test(str.charAt(i), i)) {
				return false;
			}
		}
		return true;
	}

	static String map(String str, CharPredicate check, CharFunction mapper) {
		return mapPos(str, (c, p) -> check.test(c), (c, p) -> mapper.apply(c));
	}

	private static String mapPos(String str, CharPosPredicate check, CharPosFunction mapper) {
		int len = str.length();
		int i = 0;
		while (i < len && check.test(str.charAt(i), i)) {
			i++;
		}
		if (i == len) {
			return str;
		}
		char[] buf = str.toCharArray();
		while (i < len) {
			buf[i] = mapper.apply(buf[i++], i);
		}
		return new String(buf);
	}

}
