// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


public class TestXmlParser
	extends TestBase
{
	public static void main(String[] args)
	{
		TF.run();
	}
	

//	@Test
	public void testRegression()
	{
		t(XB, "<p>", IG, "yo", XE, "</p>");
	}

	
	@Test
	public void testXml()
	{
		t(XB, "<p>", IG, "yo", XE, "</p>", XC, "<!-- \n yo <xml> </xml> -->", XN, "<a />");
	}
}
