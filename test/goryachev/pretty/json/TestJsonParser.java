// Copyright (c) 2013-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.Log;


public class TestJsonParser
	extends ParserTestBase
{
	public static void main(String[] args)
	{
//		Log.connect("RecursiveJsonXmlParser", "console");
		TF.run();
	}
	

//	@Test
	public void regressionTests()
	{
		testNormal();
		testErrors();
	}
	
	
//	@Test
	public void testNormal()
	{
		t(IG, "]");
		t(OB, "{", OE, "}");
		t(AB, "[", AE, "]");
		t(OB, "{", WS, " ", OE, "}", WS, " ", IG, "xx");
		t(AB, "[", WS, " ", AE, "]", WS, " ", IG, "xx");
		t(OB, "{", WS, " ", OE, "}");
		t(AB, "[", WS, " ", AE, "]");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "null", OE, "}");
		t(IG, "hello");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "true", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", VA, "true", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\nname", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\r", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\r", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\rx", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\rx", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(AB, "[", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", CA, ",", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "x\\u4fdex", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "\\n\\u4fde", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", LB, "\n", VA, "1", WS, " ", CA, ",", WS, " ", VA, "2", WS, " ", AE, "]", WS, " ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", LB, "\n", VA, "1", WS, " ", CA, ",", WS, " ", VA, "2", WS, " ", AE, "]", WS, " ", OE, "}");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", CA, ",", VA, "3", AE, "]");
		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n", IG, "ignore");
		t(AB, "[", WS, " ", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(AB, "[", WS, " ", VA, "1", CA, ",", WS, " ", SB, "\"", ST, "1", SE, "\"", WS, " ", AE, "]", LB, "\n");
		t(IG, "ignore", LB, "\r\n", OB, "{", OE, "}");
		t(OB, "{", OE, "}", LB, "\r\n", IG, "ignore", OB, "{", OE, "}", LB, "\r\n");
		t(AB, "[", AB, "[", AB, "[", AE, "]", AE, "]", AE, "]");
		t(IG, "a", WS, "\t", IG, "b");
		t(IG, "[info]");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", VA, "true", LB, "\n", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", SB, "\"", ST, "a string", SE, "\"", LB, "\n", OE, "}");
		t(AB, "[", WS, " ", OB, "{", WS, " ", OE, "}", AE, "]");
		t(OB, "{", WS, " ", NB, "\"", NA, "issue_008", NE, "\"", SP, ":", AB, "[", WS, " ", OB, "{", WS, " ", OE, "}", AE, "]", WS, " ", OE, "}");
		t(OB, "{", NB, "\"", NA, "issue_002", NE, "\"", SP, ":", AB, "[", AB, "[", AB, "[", AE, "]", AE, "]", AE, "]", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", OB, "{", LB, "\n", NB, "\"", NA, "n", NE, "\"", SP, ":", VA, "true", OE, "}", CO, ",", LB, "\n", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", LB, "\n", WS, " ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", CO, ",", LB, "\n", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", LB, "\n", WS, " ", OE, "}");
		t(IG, "xx", WS, "  ", OB, "{", WS, " ", NB, "\"", NA, "name", NE, "\"", WS, "  ", SP, ":", WS, "  ", AB, "[", AE, "]", LB, "\n", OE, "}");
	}
	
	
//	@Test
	public void testErrors()
	{
		t(IG, "{{");
	}


	@Test
	public void test()
	{
	}
}
