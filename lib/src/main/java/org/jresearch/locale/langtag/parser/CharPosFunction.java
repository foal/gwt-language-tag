package org.jresearch.locale.langtag.parser;

@FunctionalInterface
public interface CharPosFunction {
	char apply(char c, int pos);
}
