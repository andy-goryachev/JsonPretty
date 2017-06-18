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
	public void regressionTests()
	{
		t(OB, "{", OE, "}");
		t(AB, "[", AE, "]");
		t(OB, "{", WS, " ", OE, "}", WS, " ", IG, "xx");
		t(AB, "[", WS, " ", AE, "]", WS, " ", IG, "xx");
		t(OB, "{", WS, " ", OE, "}");
		t(AB, "[", WS, " ", AE, "]");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "null", OE, "}");
		t(IG, "hello");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "true", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", VA, "true", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", VA, "true", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\nname", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\r", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\r", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\rx", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\rx", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(AB, "[", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", CA, ",", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\u4fdex", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\n\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", WS, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", CO, ",", WS, "\n ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", WS, "\n ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", OB, "{", WS, "\n", NB, "\"", NA, "n", NE, "\"", SP, ":", VA, "true", OE, "}", CO, ",", WS, "\n ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", WS, "\n ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", WS, "\n", VA, "1", WS, " ", CA, ",", WS, " ", VA, "2", WS, " ", AE, "]", WS, " ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", WS, "\n", VA, "1", WS, " ", CA, ",", WS, " ", VA, "2", WS, " ", AE, "]", WS, " ", OE, "}");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", CA, ",", VA, "3", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n", IG, "ignore");
		t(AB, "[", WS, " ", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(AB, "[", WS, " ", VA, "1", CA, ",", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(OB, "{", ER, "{");
		t(IG, "ignore", LB, "\r\n", OB, "{", OE, "}");
		t(OB, "{", OE, "}", LB, "\r\n", IG, "ignore", OB, "{", OE, "}", LB, "\r\n");
		t(AB, "[", AB, "[", AB, "[", AE, "]", AE, "]", AE, "]");
	}


	@Test
	public void test()
	{
		t(OB, "{", NB, "\"", NA, "issue_002", NE, "\"", SP, ":", AB, "[", AB, "[", AB, "[", AE, "]", AE, "]", AE, "]", OE, "}");
	}
}
