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
		t(XT, "<p>", IG, "yo", XT, "</p>");
	}

	
	@Test
	public void testXml()
	{
		t(XT, "<p>", IG, "yo", XT, "</p>", XC, "<!-- \n yo <xml> </xml> -->", XT, "<a />");
	}
}
