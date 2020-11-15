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
	public void testConstructorSimple() throws LangTagException {
		LangTag lt = new LangTag("en");
		assertEquals("en", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("en", lt.getLanguage());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testConstructorSimpleCanonicalFormat() throws LangTagException {
		LangTag lt = new LangTag("EN");
		assertEquals("en", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("en", lt.getLanguage());
		assertEquals("en", lt.toString());
	}

	@SuppressWarnings("unused")
	@Test(expected = LangTagException.class)
	public void testConstructorNull() throws LangTagException {
		new LangTag(null);
	}

	@Test
	public void testConstructorExtended() throws LangTagException {

		LangTag lt = new LangTag("zh", "cmn");
		assertEquals("zh", lt.getPrimaryLanguage());
		assertNotNull(lt.getExtendedLanguageSubtags());
		assertEquals("cmn", lt.getExtendedLanguageSubtags()[0]);
		assertEquals("zh-cmn", lt.getLanguage());
		assertEquals("zh-cmn", lt.toString());
	}

	@SuppressWarnings("unused")
	@Test(expected = LangTagException.class)
	public void testConstructorExtendedNull() throws LangTagException {
		new LangTag(null, new String[] {});
	}

	@Test
	public void testConstructorExtendedMultiple() throws Exception {

		LangTag lt = new LangTag("zh", "cmn", "xyz");
		assertEquals("zh", lt.getPrimaryLanguage());
		assertNotNull(lt.getExtendedLanguageSubtags());
		assertEquals(2, lt.getExtendedLanguageSubtags().length);
		assertEquals("cmn", lt.getExtendedLanguageSubtags()[0]);
		assertEquals("xyz", lt.getExtendedLanguageSubtags()[1]);
		assertEquals("zh-cmn-xyz", lt.getLanguage());
		assertEquals("zh-cmn-xyz", lt.toString());
	}

	@Test
	public void testConstructorExtendedSubtagOnly() throws LangTagException {

		LangTag lt = new LangTag(null, "cmn");
		assertNull(lt.getPrimaryLanguage());
		assertNotNull(lt.getExtendedLanguageSubtags());
		assertEquals("cmn", lt.getExtendedLanguageSubtags()[0]);
		assertEquals("cmn", lt.getLanguage());
		assertEquals("cmn", lt.toString());
	}

	@Test
	public void testEquality() throws LangTagException {

		LangTag lt1 = new LangTag("en");
		lt1.setRegion("us");

		LangTag lt2 = new LangTag("EN");
		lt2.setRegion("US");

		assertTrue(lt1.equals(lt2));
	}

	@Test
	public void testScript() throws Exception {

		LangTag lt = new LangTag("sr");
		lt.setScript("Cyrl");

		assertEquals("sr", lt.getPrimaryLanguage());
		assertEquals("sr", lt.getLanguage());
		assertEquals("Cyrl", lt.getScript());
		assertEquals("sr-Cyrl", lt.toString());
	}

	@Test
	public void testScriptNull() throws LangTagException {

		LangTag lt = new LangTag("sr");
		lt.setScript(null);

		assertEquals("sr", lt.getPrimaryLanguage());
		assertEquals("sr", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("sr", lt.toString());
	}

	@Test
	public void testRegionISO3166() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setRegion("US");

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertEquals("US", lt.getRegion());
		assertEquals("en-US", lt.toString());
	}

	@Test
	public void testRegionUNM49() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setRegion("123");

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertEquals("123", lt.getRegion());
		assertEquals("en-123", lt.toString());
	}

	@Test
	public void testRegionNull() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setRegion(null);

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertNull(lt.getRegion());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testVariants() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setVariants("2012");

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertEquals(1, lt.getVariants().length);
		assertEquals("2012", lt.getVariants()[0]);
		assertEquals("en-2012", lt.toString());
	}

	@Test
	public void testVariantsEmpty() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setVariants();

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertNull(lt.getVariants());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testExtensions() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setExtensions("a-cal");

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertEquals(1, lt.getExtensions().length);
		assertEquals("a-cal", lt.getExtensions()[0]);
		assertEquals("en-a-cal", lt.toString());
	}

	@Test
	public void testExtensionsEmpty() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setExtensions();

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertNull(lt.getExtensions());
		assertEquals("en", lt.toString());
	}

	@Test
	public void testPrivateUse() throws LangTagException {

		LangTag lt = new LangTag("en");
		lt.setPrivateUse("x-private");

		assertEquals("en", lt.getPrimaryLanguage());
		assertEquals("en", lt.getLanguage());
		assertEquals("x-private", lt.getPrivateUse());
		assertEquals("en-x-private", lt.toString());
	}

	@Test
	public void testParse1() throws LangTagException {

		LangTag lt = LangTag.parse("de");

		assertEquals("de", lt.getPrimaryLanguage());
		assertEquals("de", lt.getLanguage());
		assertEquals("de", lt.toString());
	}

	@Test
	public void testParse2() throws LangTagException {

		LangTag lt = LangTag.parse("zh-Hant");

		assertEquals("zh", lt.getPrimaryLanguage());
		assertEquals("zh", lt.getLanguage());
		assertEquals("Hant", lt.getScript());
		assertEquals("zh-Hant", lt.toString());
	}

	@Test
	public void testParse3() throws LangTagException {

		LangTag lt = LangTag.parse("zh-cmn-Hans-CN");

		assertEquals("zh", lt.getPrimaryLanguage());
		assertEquals("cmn", lt.getExtendedLanguageSubtags()[0]);
		assertEquals("zh-cmn", lt.getLanguage());
		assertEquals("Hans", lt.getScript());
		assertEquals("CN", lt.getRegion());
		assertEquals("zh-cmn-Hans-CN", lt.toString());
	}

	@Test
	public void testParse4() throws LangTagException {

		LangTag lt = LangTag.parse("zh-yue-HK");

		assertEquals("zh", lt.getPrimaryLanguage());
		assertEquals("yue", lt.getExtendedLanguageSubtags()[0]);
		assertEquals("zh-yue", lt.getLanguage());
		assertEquals("HK", lt.getRegion());
		assertEquals("zh-yue-HK", lt.toString());
	}

	@Test
	public void testParse5() throws LangTagException {

		LangTag lt = LangTag.parse("yue-HK");

		assertEquals("yue", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("yue", lt.getLanguage());
		assertEquals("HK", lt.getRegion());
		assertEquals("yue-HK", lt.toString());
	}

	@Test
	public void testParse6() throws LangTagException {

		LangTag lt = LangTag.parse("sr-Latn-RS");

		assertEquals("sr", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("sr", lt.getLanguage());
		assertEquals("Latn", lt.getScript());
		assertEquals("RS", lt.getRegion());
		assertEquals("sr-Latn-RS", lt.toString());
	}

	@Test
	public void testParse7() throws LangTagException {

		LangTag lt = LangTag.parse("sl-rozaj");

		assertEquals("sl", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("sl", lt.getLanguage());
		assertNull(lt.getScript());
		assertNull(lt.getRegion());

		assertNotNull(lt.getVariants());
		assertEquals(1, lt.getVariants().length);
		assertEquals("rozaj", lt.getVariants()[0]);
		assertEquals("sl-rozaj", lt.toString());
	}

	@Test
	public void testParse8() throws LangTagException {

		LangTag lt = LangTag.parse("de-CH-1901");

		assertEquals("de", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("de", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("CH", lt.getRegion());

		assertNotNull(lt.getVariants());
		assertEquals(1, lt.getVariants().length);
		assertEquals("1901", lt.getVariants()[0]);
		assertEquals("de-CH-1901", lt.toString());
	}

	@Test
	public void testParse9() throws LangTagException {

		LangTag lt = LangTag.parse("hy-Latn-IT-arevela");

		assertEquals("hy", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("hy", lt.getLanguage());
		assertEquals("Latn", lt.getScript());
		assertEquals("IT", lt.getRegion());

		assertNotNull(lt.getVariants());
		assertEquals(1, lt.getVariants().length);
		assertEquals("arevela", lt.getVariants()[0]);
		assertEquals("hy-Latn-IT-arevela", lt.toString());
	}

	@Test
	public void testParse10() throws LangTagException {

		LangTag lt = LangTag.parse("en-US-u-islamcal");

		assertEquals("en", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("en", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("US", lt.getRegion());

		assertNotNull(lt.getExtensions());
		assertEquals(1, lt.getExtensions().length);
		assertEquals("u-islamcal", lt.getExtensions()[0]);
		assertEquals("en-US-u-islamcal", lt.toString());
	}

	@Test
	public void testParse11() throws LangTagException {

		LangTag lt = LangTag.parse("en-a-myext-b-another");

		assertEquals("en", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("en", lt.getLanguage());
		assertNull(lt.getScript());
		assertNull(lt.getRegion());

		assertNotNull(lt.getExtensions());
		assertEquals(2, lt.getExtensions().length);
		assertEquals("a-myext", lt.getExtensions()[0]);
		assertEquals("b-another", lt.getExtensions()[1]);
		assertEquals("en-a-myext-b-another", lt.toString());
	}

	@Test
	public void testParse12() throws LangTagException {

		LangTag lt = LangTag.parse("zh-CN-a-myext-x-private");

		assertEquals("zh", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("zh", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("CN", lt.getRegion());

		assertNotNull(lt.getExtensions());
		assertEquals(1, lt.getExtensions().length);
		assertEquals("a-myext", lt.getExtensions()[0]);

		assertEquals("x-private", lt.getPrivateUse());
	}

	@Test(expected = LangTagException.class)
	public void testParse13() throws LangTagException {
		LangTag.parse("invalid-tag");
	}

	@Test
	public void testParse14() throws LangTagException {
		assertNull(LangTag.parse(null));
	}

	@Test
	public void testParse15() throws LangTagException {
		LangTag lt = LangTag.parse("ja-JP-u-ca-japanese");

		assertEquals("ja", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("ja", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("JP", lt.getRegion());

		assertNotNull(lt.getExtensions());
		assertEquals(1, lt.getExtensions().length);
		assertEquals("u-ca-japanese", lt.getExtensions()[0]);
	}

	@Test
	public void testParse16() throws LangTagException {
		LangTag lt = LangTag.parse("th-TH-u-nu-thai");

		assertEquals("th", lt.getPrimaryLanguage());
		assertNull(lt.getExtendedLanguageSubtags());
		assertEquals("th", lt.getLanguage());
		assertNull(lt.getScript());
		assertEquals("TH", lt.getRegion());

		assertNotNull(lt.getExtensions());
		assertEquals(1, lt.getExtensions().length);
		assertEquals("u-nu-thai", lt.getExtensions()[0]);
	}

}
