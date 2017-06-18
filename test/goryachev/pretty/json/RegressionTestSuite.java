// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Regression Test Suite.
 */
public class RegressionTestSuite
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	@Test
	public void test()
	{
		new TestJsonParser().regressionTests();
		new TestXmlParser().regressionTests();
		new TestPrettyFormatter().regressionTests();
	}
}
