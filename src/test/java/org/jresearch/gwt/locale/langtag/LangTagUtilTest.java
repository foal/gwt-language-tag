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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jresearch.gwt.locale.langtag.LangTag;
import org.jresearch.gwt.locale.langtag.LangTagException;
import org.jresearch.gwt.locale.langtag.LangTagUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the language tag utility class.
 */
@SuppressWarnings({ "static-method", "nls" })
public class LangTagUtilTest {

	@Test
	public void testStripWithLangTag() {

		assertEquals("name", LangTagUtils.strip("name#en-US"));

		assertEquals("profile", LangTagUtils.strip("profile#"));
	}

	@Test
	public void testStripWithNoLangTag() {

		assertEquals("name", LangTagUtils.strip("name"));

		assertEquals("", LangTagUtils.strip(""));
	}

	@Test
	public void testStripSet() {

		Set<String> set = new HashSet<>();
		set.add("name");
		set.add("name#en-US");

		Set<String> out = LangTagUtils.strip(set);

		assertTrue(out.contains("name"));
		assertEquals(1, out.size());
	}

	@Test
	public void testStripList() {

		List<String> list = new ArrayList<>();
		list.add("name");
		list.add("name#en-US");

		List<String> out = LangTagUtils.strip(list);

		assertEquals("name", out.get(0));
		assertEquals("name", out.get(1));
		assertEquals(2, out.size());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testStripWithNullArg() {

		assertNull(LangTagUtils.strip((String) null));
		assertNull(LangTagUtils.strip((Set) null));
		assertNull(LangTagUtils.strip((List) null));
	}

	@Test
	public void testExtractWithNullArg() throws LangTagException {

		assertNull(LangTagUtils.extract(null));
	}

	@Test
	public void testExtractWithNoLangTag() throws LangTagException {

		assertNull(LangTagUtils.extract("name"));
	}

	@Test
	public void testExtractWithEmptyLangTag() throws LangTagException {

		assertNull(LangTagUtils.extract("name#"));
	}

	@Test
	public void testExtractValid() throws LangTagException {

		assertEquals("bg-BG", LangTagUtils.extract("name#bg-BG").toString());
	}

	@Test(expected = LangTagException.class)
	public void testExtractInvalid() throws LangTagException {
		LangTagUtils.extract("name#nosuchlangtag");
	}

	@Test
	public void testFind() throws Exception {

		Map<String, String> map = new HashMap<>();

		map.put("month", "January");
		map.put("month#de", "Januar");
		map.put("month#fr", "janvier");
		map.put("month#pt", "janeiro");
		map.put("other key", "other value");

		Map<LangTag, String> result = LangTagUtils.find("month", map);

		assertEquals("January", result.get(null));
		assertEquals("Januar", result.get(new LangTag("de")));
		assertEquals("janvier", result.get(new LangTag("fr")));
		assertEquals("janeiro", result.get(new LangTag("pt")));

		assertEquals(4, result.size());
	}

	@Test
	public void testFindNone() {

		Map<String, String> map = new HashMap<>();

		Map<LangTag, String> result = LangTagUtils.find("month", map);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testFindNoneMatching() {

		Map<String, String> map = new HashMap<>();

		map.put("month", "January");
		map.put("month#de", "Januar");
		map.put("month#fr", "janvier");
		map.put("month#pt", "janeiro");
		map.put("other key", "other value");

		Map<LangTag, String> result = LangTagUtils.find("day", map);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testToStringList() throws LangTagException {

		List<LangTag> in = new LinkedList<>();
		in.add(LangTag.parse("en-GB"));
		in.add(LangTag.parse("bg-BG"));

		List<String> out = LangTagUtils.toStringList(in);

		assertEquals("en-GB", out.get(0));
		assertEquals("bg-BG", out.get(1));
		assertEquals(2, out.size());
	}

	@Test
	public void testToStringListNull() {

		assertNull(LangTagUtils.toStringList(null));
	}

	@Test
	public void testToStringArray() throws LangTagException {

		List<LangTag> in = new LinkedList<>();
		in.add(LangTag.parse("en-GB"));
		in.add(LangTag.parse("bg-BG"));

		String[] out = LangTagUtils.toStringArray(in);

		assertEquals("en-GB", out[0]);
		assertEquals("bg-BG", out[1]);
		assertEquals(2, out.length);
	}

	@Test
	public void testToStringArrayNull() {

		assertNull(LangTagUtils.toStringArray(null));
	}

	@Test
	public void testParseLangTagList() throws LangTagException {

		// From string list
		List<String> in = Arrays.asList("bg-BG", "de-DE");

		List<LangTag> out = LangTagUtils.parseLangTagList(in);
		assertEquals("bg-BG", out.get(0).toString());
		assertEquals("de-DE", out.get(1).toString());
		assertEquals(2, out.size());

		// From string varargs
		out = LangTagUtils.parseLangTagList("bg-BG", "de-DE");
		assertEquals("bg-BG", out.get(0).toString());
		assertEquals("de-DE", out.get(1).toString());
		assertEquals(2, out.size());
	}

	@Test
	public void testParseLangTagListNull() throws LangTagException {

		assertNull(LangTagUtils.parseLangTagList((String[]) null));
	}

	@Test
	public void testParseLangTagArray() throws LangTagException {

		String[] in = { "bg-BG", "fr-FR" };

		LangTag[] out = LangTagUtils.parseLangTagArray(in);
		assertEquals(in[0], out[0].toString());
		assertEquals(in[1], out[1].toString());
		assertEquals(in.length, out.length);
	}

	@Test
	public void testParseLangTagArrayNull() throws LangTagException {
		assertNull(LangTagUtils.parseLangTagArray((String[]) null));
	}

//TODO
	@Test
	@Ignore
	public void testParseLangTagStringNull() throws LangTagException {
		assertNull(LangTagUtils.parseLangTagArray((String) null));
	}

	@Test
	public void testSplit() throws LangTagException {

		Map.Entry<String, LangTag> pair = LangTagUtils.split("name#bg-BG");
		assertEquals("name", pair.getKey());
		assertEquals(LangTag.parse("bg-BG"), pair.getValue());
	}

	@Test
	public void testSplit_noLangTag() throws LangTagException {

		Map.Entry<String, LangTag> pair = LangTagUtils.split("name");
		assertEquals("name", pair.getKey());
		assertNull(pair.getValue());
	}

	@Test
	public void testSplit_empty() throws LangTagException {

		Map.Entry<String, LangTag> pair = LangTagUtils.split("");
		assertEquals("", pair.getKey());
		assertNull(pair.getValue());
	}

	@Test
	public void testSplit_hash() throws LangTagException {

		Map.Entry<String, LangTag> pair = LangTagUtils.split("#");
		assertEquals("#", pair.getKey());
		assertNull(pair.getValue());
	}

	@Test(expected = LangTagException.class)
	public void testSplit_invalidLangTag() throws LangTagException {
		LangTagUtils.split("name#invalid-tag");
	}

	@Test
	public void testSplit_null() throws LangTagException {
		assertNull(LangTagUtils.split(null));
	}
}
