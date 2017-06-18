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
		test1();
		test2();
		test_issue001();
		test_issue002();
		test_issue007();
		test_issue007_1();
	}
	
	
	// fixed @Test
	public void test_issue001()
	{
		// {"issue_001":{"a":{"b":{"c":{"d": false}}}}}
		t
		(
			"{\"issue_001\":{\"a\":{\"b\":{\"c\":{\"d\": false}}}}}",
			OB, LB,
			IN, NB, NA, NE, SP, LB,
			IN, OB, LB,
			IN, NB, NA, NE, SP, LB,
			IN, OB, LB,
			IN, NB, NA, NE, SP, LB,
			IN, OB, LB,
			IN, NB, NA, NE, SP, LB,
			IN, OB, LB,
			IN, NB, NA, NE, SP, WS, VA, LB,
			IN, OE, LB,
			IN, OE, LB,
			IN, OE, LB,
			IN, OE, LB,
			OE
		);
	}
	
	
	// fixed @Test
	public void test_issue002()
	{
		// {"issue_002":[[[]]]}
		t
		(
			"{\"issue_002\":[[[]]]}",
			OB, LB,
			IN, NB, NA, NE, SP, LB,
			IN, AB, LB,
			IN, AB, LB,
			IN, AB, WS, AE, LB,
			IN, AE, LB,
			IN, AE, LB,
			OE
		);
	}
	
	
	// fixed @Test
	public void test_issue007()
	{
		// [[[]]]
		t
		(
			"[[[]]]",
			AB, LB,
			IN, AB, LB,
			IN, AB, WS, AE, LB,
			IN, AE, LB,
			AE
		);
	}
	
	
	// fixed @Test
	public void test_issue007_1()
	{
		// [[]]
		t
		(
			"[[]]",
			AB, LB,
			IN, AB, WS, AE, LB,
			AE
		);
	}


	// fixed @Test
	public void test1()
	{
		// { "array":[ { }] }
		t
		(
			"{ \"array\":[ { }] }", 
			OB, LB, 
			IN, NB, NA, NE, SP, LB, 
			IN, AB, LB, 
			IN, OB, WS, OE, LB, 
			IN, AE, LB, 
			OE
		);
	}
	
	
	// fixed @Test
	public void test2()
	{
		// { "array":[{ "n":null }] }
		t
		(
			"{ \"array\":[{ \"n\":null }] }",
			OB, LB, 
			IN, NB, NA, NE, SP, LB, 
			IN, AB, LB,
			IN, OB, LB, 
			IN, NB, NA, NE, SP, WS, VA, LB, 
			IN, OE, LB, 
			IN, AE, LB, 
			OE
		);
	}
}
