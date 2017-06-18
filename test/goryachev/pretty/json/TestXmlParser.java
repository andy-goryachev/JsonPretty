// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


public class TestXmlParser
	extends ParserTestBase
{
	public static void main(String[] args)
	{
		TF.run();
	}
	

	@Test
	public void regressionTests()
	{
		t(XB, "<p>", XT, "yo", XE, "</p>");
		t(XB, "<p>", XT, "yo", XE, "</p>", XC, "<!-- \n yo <xml> </xml> -->", XN, "<a />", IG, "ignore");
	}

	
	@Test
	public void testXml()
	{
		// <?xml version="1.0" encoding="UTF-8"?>
	}
}
