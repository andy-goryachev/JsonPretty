// Copyright (c) 2013-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.Log;


/**
 * tests JsonPrettyFormatter
 */
public class TestPrettyFormatter
	extends FormatterTestBase
{
	public static void main(String[] args)
	{
		Log.connect("PrettyFormatter", "console");
//		Log.connect("RecursiveJsonXmlParser", "console");
		TF.run();
	}
	
	
//	@Test
	public void regressionTests()
	{
		test1();
		test2();
		issue_001();
		issue002();
		issue006();
		issue007();
		issue007_1();
		issue_008();
		testXml();
		testXmlComment();
	}
	
	
	// fixed @Test
	public void issue_008()
	{
		// { "array":[],"object":{} }
		t
		(
			"{ \"array\":[],\"object\":{} }",
			OB, LB,
			IN, NB, NA, NE, SP, WS, AB, WS, AE, CO, LB,
			IN, NB, NA, NE, SP, WS, OB, WS, OE, LB,
			OE
		);
	}

	
	// fixed @Test
	public void issue_001()
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
	public void issue002()
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
	public void issue007()
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
	public void issue007_1()
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
	
	
//	@Test
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
	
	
	// fixed @Test
	public void issue006()
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
	
	
//	@Test
	public void testXml()
	{
		t
		(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<yo> </yo>\n",
			XN, LB,
			XB, LB,
			IN, XT, LB,
			XE
		);
	}
	
	
//	@Test
	public void testXmlComment()
	{
		/*
<!-- comment -->
<yo> </yo>

		 */
		t
		(
			"<!-- comment -->\n<yo> </yo>\n",
			XC, LB,
			XB, LB,
			IN, XT, LB,
			XE
		);
	}
}
