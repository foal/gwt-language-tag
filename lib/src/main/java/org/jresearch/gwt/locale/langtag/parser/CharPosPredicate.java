package org.jresearch.gwt.locale.langtag.parser;

@FunctionalInterface
public interface CharPosPredicate {
	boolean test(char c, int pos);
}
