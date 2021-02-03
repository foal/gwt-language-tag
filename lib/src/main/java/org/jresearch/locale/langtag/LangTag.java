/*
 * Copyright 2020, Stanislav Spiridonov
 */

/*
 * lang-tag
 *
 * Copyright 2012-2016, Connect2id Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.jresearch.locale.langtag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.immutables.value.Value.Check;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.jresearch.locale.langtag.ImmutableLangTag.Builder;
import org.jresearch.locale.langtag.parser.ParserUtil;

/**
 * Language tag according to <a href="http://tools.ietf.org/html/rfc5646">RFC
 * 5646</a>.
 *
 * <p>
 * Supports normal language tags. Special private language tags beginning with
 * "x" and grandfathered tags beginning with "i" are not supported.
 *
 * <p>
 * To construct a new language tag from scratch:
 *
 * <pre>
 * // English as used in the United States
 * LangTag tag = new LangTag("en");
 * tag.setRegion("US");
 *
 * // Returns "en-US"
 * tag.toString();
 * </pre>
 *
 * <p>
 * To parse a language tag:
 *
 * <pre>
 * // Chinese, Mandarin, Simplified script, as used in China
 * LangTag tag = LangTag.parse("zh-cmn-Hans-CN");
 *
 * // Returns "zh"
 * tag.getPrimaryLanguage();
 *
 * // Returns "cmn"
 * tag.getExtendedLanguageSubtags()[0];
 *
 * // Returns "zh-cmn"
 * tag.getLanguage();
 *
 * // Returns "Hans"
 * tag.getScript();
 *
 * // Returns "CN"
 * tag.getRegion();
 * </pre>
 *
 * <p>
 * See <a href="http://tools.ietf.org/html/rfc5646">RFC 5646</a>.
 */
@Immutable
@SuppressWarnings({ "static-method", "nls" })
public abstract class LangTag {

	private static final char SEP = '-';
	private static final char PRIVATE_USE = 'x';
	private static final String UNDETERMINED = "und";

	/**
	 * The primary language, as the shortest ISO 639 code (2*3ALPHA). Must always be
	 * defined, unless sufficient language subtags exist.
	 */
	@Default
	public String primaryLanguage() {
		return "";
	}

	/**
	 * Optional extended language subtags, as three-letter ISO-639-3 codes.
	 */
	public abstract List<String> languageSubtags();

	/**
	 * Optional script, (4ALPHA) ISO 15924 code.
	 */
	@Default
	public String script() {
		return "";
	}

	/**
	 * Optional region, (2ALPHA) ISO 3166-1 code or (3DIGIT) UN M.49 code.
	 */
	@Default
	public String region() {
		return "";
	}

	/**
	 * Optional variants, (5*8alphanum) or (DIGIT 3alphanum).
	 */
	public abstract List<String> variants();

	/**
	 * Optional extensions.
	 */
	public abstract List<String> extensions();

	/**
	 * Optional private use subtag.
	 */
	@Default
	public String privateUse() {
		return "";
	}

	public String language() {
		if (primaryLanguage().isEmpty()) {
			return UNDETERMINED;
		}
		StringBuilder language = new StringBuilder(primaryLanguage());
		add(languageSubtags(), language);
		return language.toString();
	}

	@Override
	public String toString() {
		if (!primaryLanguage().isEmpty()) {
			StringBuilder tag = new StringBuilder(primaryLanguage());
			add(languageSubtags(), tag);
			add(this::script, tag);
			add(this::region, tag);
			add(variants(), tag);
			add(extensions(), tag);
			add(this::privateUse, tag);
			return tag.toString();
		} else if (!privateUse().isEmpty()) {
			return privateUse();
		}
		return UNDETERMINED;
	}

	private void add(Supplier<String> supplier, StringBuilder builder) {
		String str = supplier.get();
		if (!str.isEmpty()) {
			builder.append(SEP).append(str);
		}
	}

	private void add(List<String> supplier, StringBuilder builder) {
		supplier.forEach(v -> builder.append(SEP).append(v));
	}

	@Check
	protected LangTag normalize() {

		// Primary language
		if (!ParserUtil.isLow(primaryLanguage())) {
			return ImmutableLangTag.copyOf(this).withPrimaryLanguage(ParserUtil.low(primaryLanguage()));
		}

		// language subtags
		if (languageSubtags().stream().anyMatch(ParserUtil.LOW_STR.negate())) {
			List<String> subtags = languageSubtags().stream()
					.map(ParserUtil::low)
					.collect(Collectors.toList());
			return ImmutableLangTag.copyOf(this).withLanguageSubtags(subtags);
		}

		// script
		if (!ParserUtil.isTit(script())) {
			return ImmutableLangTag.copyOf(this).withScript(ParserUtil.tit(script()));
		}

		// region
		if (!ParserUtil.isUp(region())) {
			return ImmutableLangTag.copyOf(this).withRegion(ParserUtil.up(region()));
		}

		// variants
		if (variants().stream().anyMatch(ParserUtil.LOW_STR.negate())) {
			List<String> variants = variants().stream()
					.map(ParserUtil::low)
					.collect(Collectors.toList());
			return ImmutableLangTag.copyOf(this).withVariants(variants);
		}

		// extension
		if (extensions().stream().anyMatch(ParserUtil.LOW_STR.negate())) {
			List<String> extensions = extensions().stream()
					.map(ParserUtil::low)
					.collect(Collectors.toList());
			return ImmutableLangTag.copyOf(this).withExtensions(extensions);
		}

		// private use
		if (!ParserUtil.isLow(privateUse())) {
			return ImmutableLangTag.copyOf(this).withPrivateUse(ParserUtil.low(privateUse()));
		}

		return this;
	}

	@Check
	protected void check() {
		boolean pl = !primaryLanguage().isEmpty();
		boolean ls = !languageSubtags().isEmpty();
		boolean sc = !script().isEmpty();
		boolean re = !region().isEmpty();
		boolean va = !variants().isEmpty();
		boolean ex = !extensions().isEmpty();
		boolean pu = !privateUse().isEmpty();

		// Primary language specified or only private use part exists
		if (!pl) {
			if (!pu || ls || sc || re || va || ex) {
				throw new IllegalArgumentException("Primary language can't be empty in such tag: " + toString());
			}
		} else {
			if (!isPrimaryLanguage(primaryLanguage())) {
				throw new IllegalArgumentException("Wrong primary language part: " + primaryLanguage() + ", tag: " + toString());
			}
		}

		// language subtags
		for (String languageSubtag : languageSubtags()) {
			if (!isExtendedLanguageSubtag(languageSubtag)) {
				throw new IllegalArgumentException("Wrong extended language subtag part: " + languageSubtag + ", tag: " + toString());
			}
		}

		// script
		if (sc && !isScript(script())) {
			throw new IllegalArgumentException("Wrong script part: " + script() + ", tag: " + toString());
		}

		// region
		if (re && !isRegion(region())) {
			throw new IllegalArgumentException("Wrong region part: " + region() + ", tag: " + toString());
		}

		// variants
		for (String variant : variants()) {
			if (!isVariant(variant)) {
				throw new IllegalArgumentException("Wrong variant part: " + variant + ", tag: " + toString());
			}
		}

		// extension
		extensions().forEach(LangTag::checkExtension);

		// private use
		if (pu) {
			checkPrivateUse(privateUse());
		}

	}

	/**
	 * Parses the specified string representation of a language tag.
	 *
	 * @param s The string to parse. May be {@code null}.
	 *
	 * @return The language tag. {@code null} if the string was empty or
	 *         {@code null}.
	 *
	 * @throws LangTagException If the string has invalid language tag syntax.
	 */
	public static ImmutableLangTag parse(final String s) {
		if (s == null || s.trim().isEmpty())
			return null;

		final String[] subtags = s.split(String.valueOf(SEP));

		int pos = 0;

		// Parse primary lang + ext lang subtags
		if (!isPrimaryLanguage(subtags[0])) {
			throw new IllegalArgumentException("Can't parce tag " + s + ". The value " + subtags[0] + " is not a primary language");
		}
		String primaryLang = subtags[pos++];

		// Multiple ext lang subtags possible
		List<String> extLangSubtags = new ArrayList<>();
		while (pos < subtags.length && isExtendedLanguageSubtag(subtags[pos])) {
			extLangSubtags.add(subtags[pos++]);
		}

		Builder builder = ImmutableLangTag.builder()
				.primaryLanguage(primaryLang)
				.languageSubtags(extLangSubtags);

		// Parse script
		if (pos < subtags.length && isScript(subtags[pos]))
			builder.script(subtags[pos++]);

		// Parse region
		if (pos < subtags.length && isRegion(subtags[pos]))
			builder.region(subtags[pos++]);

		// Parse variants
		List<String> variantSubtags = new ArrayList<>();
		while (pos < subtags.length && isVariant(subtags[pos])) {
			variantSubtags.add(subtags[pos++]);
		}
		builder.variants(variantSubtags);

		// Parse extensions, e.g. u-usercal
		pos = parseExtension(subtags, pos, builder);

		// Parse private use, e.g. x-abc
		pos = parsePrivateUse(subtags, pos, builder);

		// End of tag?
		if (pos < subtags.length) {
			throw new IllegalArgumentException("Invalid language tag " + s + ". There is something after expected end. Position is " + pos);
		}

		return builder.build();
	}

	// Parse extensions, e.g. u-usercal
	private static int parseExtension(final String[] subtags, int position, Builder builder) {
		int pos = position;
		while (pos < subtags.length && isExtensionSingleton(subtags[pos])) {
			String singleton = subtags[pos++];
			if (pos == subtags.length) {
				throw new IllegalArgumentException("There is no extension after extension singelton. Position is " + pos);
			}

			String part = subtags[pos++];
			if (!isExtensionSubtag(part)) {
				throw new IllegalArgumentException("The value " + part + " is not a extension. Position is " + pos);
			}
			String subtag = singleton + SEP + part;
			while (pos < subtags.length && !subtags[pos].equals("x") && !isExtensionSingleton(subtags[pos])) {
				part = subtags[pos++];
				if (!isExtensionSubtag(part)) {
					throw new IllegalArgumentException("The value " + part + " is not a extension. Position is " + pos);
				}
				subtag += SEP + part;
			}
			if (builder != null) {
				builder.addExtensions(subtag);
			}
		}
		return pos;
	}

	private static void checkPrivateUse(String privateUse) {
		parseExtension(privateUse.split(String.valueOf(SEP)), 0, null);
	}

	// Parse private use, e.g. x-abc
	private static int parsePrivateUse(final String[] subtags, int position, Builder builder) {
		int pos = position;
		if (pos < subtags.length && isPrivateUseSingleton(subtags[pos])) {
			if (++pos == subtags.length) {
				throw new IllegalArgumentException("There is no privat use part after private use singelton. Position is " + pos);
			}
			String part = subtags[pos++];
			if (!isPrivateUse(part)) {
				throw new IllegalArgumentException("The value " + part + " is not a pricate use part. Position is " + pos);
			}
			String subtag = "" + PRIVATE_USE + SEP + part;
			while (pos < subtags.length) {
				part = subtags[pos++];
				if (!isPrivateUse(part)) {
					throw new IllegalArgumentException("The value " + part + " is not a pricate use part. Position is " + pos);
				}
				subtag += SEP + part;
			}
			if (builder != null) {
				builder.privateUse(subtag);
			}
		}
		return pos;
	}

	private static void checkExtension(String extension) {
		parseExtension(extension.split(String.valueOf(SEP)), 0, null);
	}

	/**
	 * Checks if the specified string has a valid primary language subtag syntax.
	 *
	 * @param s The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isPrimaryLanguage(final String str) {
		return is(str, 2, 8, ParserUtil::isAlpha);
	}

	/**
	 * Checks if the specified string has a valid extended language subtag syntax.
	 *
	 * @param s The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isExtendedLanguageSubtag(final String str) {
		return is(str, 3, ParserUtil::isAlpha);
	}

	/**
	 * Checks if the specified string has a valid script subtag syntax.
	 *
	 * @param s The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isScript(final String str) {
		return is(str, 4, ParserUtil::isAlpha);
	}

	/**
	 * Checks if the specified string has a valid region subtag syntax.
	 *
	 * @param s The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isRegion(final String str) {
		return is(str, 2, ParserUtil::isAlpha) || is(str, 3, ParserUtil::isNum);
	}

	/**
	 * Checks if the specified string has a valid variant subtag syntax.
	 *
	 * @param s The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isVariant(final String str) {
		return is(str, 4, v -> ParserUtil.isNum(v.charAt(0))
				&& ParserUtil.isAlphaNum(v.charAt(1))
				&& ParserUtil.isAlphaNum(v.charAt(2))
				&& ParserUtil.isAlphaNum(v.charAt(3)))
				||
				is(str, 5, 8, ParserUtil::isAlphaNum);
	}

	/**
	 * Checks if the specified string has a valid extension singleton syntax.
	 *
	 * @param str The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isExtensionSingleton(final String str) {
		return is(str, 1, v -> !ParserUtil.equalsIgnoreCase(PRIVATE_USE, v.charAt(0)) && ParserUtil.isAlpha(v.charAt(0)));
	}

	/**
	 * Checks if the specified string has a valid extension subtag syntax.
	 *
	 * @param str The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isExtensionSubtag(final String str) {
		return is(str, 2, 8, ParserUtil::isAlphaNum);
	}

	/**
	 * Checks if the specified string has a valid private use singleton syntax.
	 *
	 * @param str The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isPrivateUseSingleton(final String str) {
		return is(str, 1, v -> ParserUtil.equalsIgnoreCase(PRIVATE_USE, v.charAt(0)));
	}

	/**
	 * Checks if the specified string has a valid private use subtag syntax.
	 *
	 * @param str The string to check. Must not be {@code null}.
	 *
	 * @return {@code true} if the syntax is correct, else {@code false}.
	 */
	private static boolean isPrivateUse(final String str) {
		return is(str, 1, 8, ParserUtil::isAlphaNum);
	}

	private static boolean is(final String str, int min, int max, Predicate<String> check) {
		int len = str.length();
		return min <= len && len <= max && check.test(str);
	}

	private static boolean is(final String str, int len, Predicate<String> check) {
		return str.length() == len && check.test(str);
	}

	/**
	 * Creates a new simple language tag.
	 *
	 * <p>
	 * Use for simple language tags such as "en" (English), "fr" (French) or "pt"
	 * (Portuguese).
	 *
	 * @param primaryLanguage The primary language, as the shortest two or
	 *                        three-letter ISO 639 code. Must not be {@code null}.
	 *
	 * @throws LangTagException If the primary language syntax is invalid.
	 */
	public static LangTag fromLang(final String primaryLanguage) {
		return ImmutableLangTag.builder().primaryLanguage(primaryLanguage).build();
	}

	/**
	 * Creates a new extended language tag.
	 *
	 * <p>
	 * Use for extended language tags such as "zh-cmn" (Mandarin Chinese) or
	 * "zh-yue" (Cantonese Chinese).
	 *
	 * @param primaryLanguage The primary language, as the shortest two or
	 *                        three-letter ISO 639 code. May be {@code null} if the
	 *                        subtags are sufficient to identify the language.
	 * @param languageSubtags One or more extended language subtags, as three-letter
	 *                        ISO 639-3 codes. {@code null} if none.
	 *
	 * @throws LangTagException If the primary or extended language syntax is
	 *                          invalid.
	 */
	public static LangTag fromLang(final String primaryLanguage, final String... languageSubtags) {
		return ImmutableLangTag.builder().primaryLanguage(primaryLanguage).addLanguageSubtags(languageSubtags).build();
	}

}
