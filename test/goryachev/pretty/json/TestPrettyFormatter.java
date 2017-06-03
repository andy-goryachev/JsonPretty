// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * tests JsonPrettyFormatter
 */
public class TestPrettyFormatter
	extends FormatterTestBase
{
	public static void main(String[] args)
	{
		TF.run();
	}

	
	@Test
	public void testRegression()
	{
	}


	@Test
	public void test1()
	{
		t
		(
			"{ \"array\":[ { }] }", 
			OB, LB, WH, NB, NA, NE, WH, SP, LB, WH, AB,
			// LB // FIX must be here
			WH, OB, OE, LB, WH, AE, LB, OE
		);
	}
	
	
	@Test
	public void test2()
	{
		t
		(
			"{ \"array\":[{ \"n\":null }] }",
			OB, LB, WH, NB, NA, NE, WH, SP, LB, WH, AB, LB,
			WH, LB, // FIX should not be here 
			WH, OB, LB, WH, NB, NA, NE, WH, SP, WH, VA, LB, WH, OE, LB, WH, AE, LB, OE
		);
	}
}
