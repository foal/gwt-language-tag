package org.jresearch.locale.langtag.parser;

@FunctionalInterface
public interface CharPosPredicate {
	boolean test(char c, int pos);
}
