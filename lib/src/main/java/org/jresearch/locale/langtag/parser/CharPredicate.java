package org.jresearch.locale.langtag.parser;

@FunctionalInterface
public interface CharPredicate {
	boolean test(char c);

	default CharPredicate or(CharPredicate other) {
		return v -> test(v) || other.test(v);
	}

	default CharPredicate negate() {
		return (t) -> !test(t);
	}
}
