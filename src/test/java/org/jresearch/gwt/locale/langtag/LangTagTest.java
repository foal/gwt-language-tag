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

package org.jresearch.gwt.locale.langtag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the language tag class.
 */
@SuppressWarnings({ "static-method", "nls" })
public class LangTagTest {

	@Test
	public void testConstructorSimple() {
		LangTag lt = LangTag.fromLang("en");
		assertEquals("en", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("en", lt.language());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testConstructorSimpleCanonicalFormat() {
		LangTag lt = LangTag.fromLang("EN");
		assertEquals("en", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("en", lt.language());
		assertEquals("en", lt.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNull() {
		LangTag.fromLang(null);
	}

	@Test
	public void testConstructorExtended() {
		LangTag lt = LangTag.fromLang("zh", "cmn");
		assertEquals("zh", lt.primaryLanguage());
		assertNotNull(lt.languageSubtags());
		assertEquals("cmn", lt.languageSubtags().get(0));
		assertEquals("zh-cmn", lt.language());
		assertEquals("zh-cmn", lt.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorExtendedNull() {
		LangTag.fromLang(null, new String[] {});
	}

	@Test
	public void testConstructorExtendedMultiple() throws Exception {

		LangTag lt = LangTag.fromLang("zh", "cmn", "xyz");
		assertEquals("zh", lt.primaryLanguage());
		assertNotNull(lt.languageSubtags());
		assertEquals(2, lt.languageSubtags().size());
		assertEquals("cmn", lt.languageSubtags().get(0));
		assertEquals("xyz", lt.languageSubtags().get(1));
		assertEquals("zh-cmn-xyz", lt.language());
		assertEquals("zh-cmn-xyz", lt.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorExtendedSubtagOnly() {
		LangTag.fromLang(null, "cmn");
	}

	@Test
	public void testEquality() {

		LangTag lt1 = ImmutableLangTag.builder().primaryLanguage("en").region("us").build();
		LangTag lt2 = ImmutableLangTag.builder().primaryLanguage("EN").region("US").build();

		assertTrue(lt1.equals(lt2));
	}

	@Test
	public void testScript() throws Exception {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("sr").script("Cyrl").build();

		assertEquals("sr", lt.primaryLanguage());
		assertEquals("sr", lt.language());
		assertEquals("Cyrl", lt.script());
		assertEquals("sr-Cyrl", lt.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testScriptNull() {
		ImmutableLangTag.builder().primaryLanguage("sr").script((String) null).build();
	}

	@Test
	public void testRegionISO3166() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").region("US").build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertEquals("US", lt.region());
		assertEquals("en-US", lt.toString());
	}

	@Test
	public void testRegionUNM49() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").region("123").build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertEquals("123", lt.region());
		assertEquals("en-123", lt.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testRegionNull() {
		ImmutableLangTag.builder().primaryLanguage("en").region((String) null).build();
	}

	@Test
	public void testVariants() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").addVariants("2012").build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertEquals(1, lt.variants().size());
		assertEquals("2012", lt.variants().get(0));
		assertEquals("en-2012", lt.toString());
	}

	@Test
	public void testVariantsEmpty() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").addVariants().build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertTrue(lt.variants().isEmpty());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testExtensions() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").addExtensions("a-cal").build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertEquals(1, lt.extensions().size());
		assertEquals("a-cal", lt.extensions().get(0));
		assertEquals("en-a-cal", lt.toString());
	}

	@Test
	public void testExtensionsEmpty() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").addExtensions().build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertTrue(lt.extensions().isEmpty());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testPrivateUse() {

		LangTag lt = ImmutableLangTag.builder().primaryLanguage("en").privateUse("x-private").build();

		assertEquals("en", lt.primaryLanguage());
		assertEquals("en", lt.language());
		assertEquals("x-private", lt.privateUse());
		assertEquals("en-x-private", lt.toString());
	}

	@Test
	public void testParse1() {

		LangTag lt = LangTag.parse("de");

		assertEquals("de", lt.primaryLanguage());
		assertEquals("de", lt.language());
		assertEquals("de", lt.toString());
	}

	@Test
	public void testParse2() {

		LangTag lt = LangTag.parse("zh-Hant");

		assertEquals("zh", lt.primaryLanguage());
		assertEquals("zh", lt.language());
		assertEquals("Hant", lt.script());
		assertEquals("zh-Hant", lt.toString());
	}

	@Test
	public void testParse3() {

		LangTag lt = LangTag.parse("zh-cmn-Hans-CN");

		assertEquals("zh", lt.primaryLanguage());
		assertEquals("cmn", lt.languageSubtags().get(0));
		assertEquals("zh-cmn", lt.language());
		assertEquals("Hans", lt.script());
		assertEquals("CN", lt.region());
		assertEquals("zh-cmn-Hans-CN", lt.toString());
	}

	@Test
	public void testParse4() {

		LangTag lt = LangTag.parse("zh-yue-HK");

		assertEquals("zh", lt.primaryLanguage());
		assertEquals("yue", lt.languageSubtags().get(0));
		assertEquals("zh-yue", lt.language());
		assertEquals("HK", lt.region());
		assertEquals("zh-yue-HK", lt.toString());
	}

	@Test
	public void testParse5() {

		LangTag lt = LangTag.parse("yue-HK");

		assertEquals("yue", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("yue", lt.language());
		assertEquals("HK", lt.region());
		assertEquals("yue-HK", lt.toString());
	}

	@Test
	public void testParse6() {

		LangTag lt = LangTag.parse("sr-Latn-RS");

		assertEquals("sr", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("sr", lt.language());
		assertEquals("Latn", lt.script());
		assertEquals("RS", lt.region());
		assertEquals("sr-Latn-RS", lt.toString());
	}

	@Test
	public void testParse7() {

		LangTag lt = LangTag.parse("sl-rozaj");

		assertEquals("sl", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("sl", lt.language());
		assertTrue(lt.script().isEmpty());
		assertTrue(lt.region().isEmpty());

		assertNotNull(lt.variants());
		assertEquals(1, lt.variants().size());
		assertEquals("rozaj", lt.variants().get(0));
		assertEquals("sl-rozaj", lt.toString());
	}

	@Test
	public void testParse8() {

		LangTag lt = LangTag.parse("de-CH-1901");

		assertEquals("de", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("de", lt.language());
		assertTrue(lt.script().isEmpty());
		assertEquals("CH", lt.region());

		assertNotNull(lt.variants());
		assertEquals(1, lt.variants().size());
		assertEquals("1901", lt.variants().get(0));
		assertEquals("de-CH-1901", lt.toString());
	}

	@Test
	public void testParse9() {

		LangTag lt = LangTag.parse("hy-Latn-IT-arevela");

		assertEquals("hy", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("hy", lt.language());
		assertEquals("Latn", lt.script());
		assertEquals("IT", lt.region());

		assertNotNull(lt.variants());
		assertEquals(1, lt.variants().size());
		assertEquals("arevela", lt.variants().get(0));
		assertEquals("hy-Latn-IT-arevela", lt.toString());
	}

	@Test
	public void testParse10() {

		LangTag lt = LangTag.parse("en-US-u-islamcal");

		assertEquals("en", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("en", lt.language());
		assertTrue(lt.script().isEmpty());
		assertEquals("US", lt.region());

		assertNotNull(lt.extensions());
		assertEquals(1, lt.extensions().size());
		assertEquals("u-islamcal", lt.extensions().get(0));
		assertEquals("en-US-u-islamcal", lt.toString());
	}

	@Test
	public void testParse11() {

		LangTag lt = LangTag.parse("en-a-myext-b-another");

		assertEquals("en", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("en", lt.language());
		assertTrue(lt.script().isEmpty());
		assertTrue(lt.region().isEmpty());

		assertNotNull(lt.extensions());
		assertEquals(2, lt.extensions().size());
		assertEquals("a-myext", lt.extensions().get(0));
		assertEquals("b-another", lt.extensions().get(1));
		assertEquals("en-a-myext-b-another", lt.toString());
	}

	@Test
	public void testParse12() {

		LangTag lt = LangTag.parse("zh-CN-a-myext-x-private");

		assertEquals("zh", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("zh", lt.language());
		assertTrue(lt.script().isEmpty());
		assertEquals("CN", lt.region());

		assertNotNull(lt.extensions());
		assertEquals(1, lt.extensions().size());
		assertEquals("a-myext", lt.extensions().get(0));

		assertEquals("x-private", lt.privateUse());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParse13() {
		LangTag.parse("invalid-t");
	}

	@Test
	public void testParse14() {
		assertNull(LangTag.parse(null));
	}

	@Test
	public void testParse15() {
		LangTag lt = LangTag.parse("ja-JP-u-ca-japanese");

		assertEquals("ja", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("ja", lt.language());
		assertTrue(lt.script().isEmpty());
		assertEquals("JP", lt.region());

		assertNotNull(lt.extensions());
		assertEquals(1, lt.extensions().size());
		assertEquals("u-ca-japanese", lt.extensions().get(0));
	}

	@Test
	public void testParse16() {
		LangTag lt = LangTag.parse("th-TH-u-nu-thai");

		assertEquals("th", lt.primaryLanguage());
		assertTrue(lt.languageSubtags().isEmpty());
		assertEquals("th", lt.language());
		assertTrue(lt.script().isEmpty());
		assertEquals("TH", lt.region());

		assertNotNull(lt.extensions());
		assertEquals(1, lt.extensions().size());
		assertEquals("u-nu-thai", lt.extensions().get(0));
	}

}
