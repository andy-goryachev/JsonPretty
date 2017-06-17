// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


public class TestJsonParser
	extends ParserTestBase
{
	public static void main(String[] args)
	{
		TF.run();
	}
	

	@Test
	public void testRegression()
	{
		t(OB, "{", OE, "}");
		t(AB, "[", AE, "]");
		t(OB, "{", WH, " ", OE, "}", WH, " ", IG, "xx");
		t(AB, "[", WH, " ", AE, "]", WH, " ", IG, "xx");
		t(OB, "{", WH, " ", OE, "}");
		t(AB, "[", WH, " ", AE, "]");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "null", OE, "}");
		t(IG, "hello");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "true", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\nname", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "x\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "x\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(AB, "[", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", CA, ",", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", LB, "\n");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fdex", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "\\n\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", OB, "{", WH, "\n", NB, "\"", NA, "n", NE, "\"", SP, ":", VA, "true", OE, "}", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", WH, "\n", VA, "1", WH, " ", CA, ",", WH, " ", VA, "2", WH, " ", AE, "]", WH, " ", OE, "}");
		t(IG, "xx", WH, "  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", WH, "\n", VA, "1", WH, " ", CA, ",", WH, " ", VA, "2", WH, " ", AE, "]", WH, " ", OE, "}");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", CA, ",", VA, "3", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", LB, "\n", IG, "ignore");
		t(AB, "[", WH, " ", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", LB, "\n");
		t(AB, "[", WH, " ", VA, "1", CA, ",", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", LB, "\n");
		t(OB, "{", ER, "{");
		t(IG, "ignore", LB, "\r\n", OB, "{", OE, "}");
		t(OB, "{", OE, "}", LB, "\r\n", IG, "ignore", OB, "{", OE, "}", LB, "\r\n");
		t(AB, "[", AB, "[", AB, "[", AE, "]", AE, "]", AE, "]");
	}


	@Test
	public void test()
	{
	}
}
