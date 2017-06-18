// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.Log;


public class TestMixed
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
	}


	@Test
	public void test()
	{
		// issue #5
		//t(IG, "embedded xml tags [ because it's malformed json\n[info]\ntext=[<xml>this should be <xml>yo</xml></xml>]\nignore");
		
		t(IG, "a[info]");
	}
}
