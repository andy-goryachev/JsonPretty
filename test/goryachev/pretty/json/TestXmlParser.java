// Copyright (c) 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


public class TestXmlParser
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
	}
	
	
	// part of the regression test
	public void testNormal()
	{
		t(XB, "<p>", XT, "yo", XE, "</p>");
		t(XB, "<p>", XT, "yo", XE, "</p>", XC, "<!-- \n yo <xml> </xml> -->", XN, "<a />", IG, "ignore");
		t(XN, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}

	
	@Test
	public void testXml()
	{
	}
}
