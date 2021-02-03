package org.jresearch.gwt.locale.langtag.parser;

@FunctionalInterface
public interface CharPosFunction {
	char apply(char c, int pos);
}
