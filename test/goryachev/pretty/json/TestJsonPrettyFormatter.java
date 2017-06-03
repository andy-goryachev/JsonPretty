// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;
import goryachev.pretty.format.JsonPrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonXmlParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


/**
 * tests JsonPrettyFormatter
 */
public class TestJsonPrettyFormatter
{
	public static final Type AB = Type.ARRAY_BEGIN;
	public static final Type AE = Type.ARRAY_END;
	public static final Type CA = Type.COMMA_ARRAY;
	public static final Type CO = Type.COMMA;
	public static final Type ER = Type.ERROR;
	public static final Type IG = Type.IGNORE;
	public static final Type LB = Type.LINEBREAK;
	public static final Type NA = Type.NAME;
	public static final Type NB = Type.NAME_BEGIN;
	public static final Type NE = Type.NAME_END;
	public static final Type OB = Type.OBJECT_BEGIN;
	public static final Type OE = Type.OBJECT_END;
	public static final Type SP = Type.SEPARATOR;
	public static final Type ST = Type.STRING;
	public static final Type SB = Type.STRING_BEGIN;
	public static final Type SE = Type.STRING_END;
	public static final Type VA = Type.VALUE;
	public static final Type WH = Type.WHITESPACE;
	
	
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	public static void t(String text, Object ... parts)
	{
		RecursiveJsonXmlParser p = new RecursiveJsonXmlParser(text);
		ParseResult r = p.parse();
		List<Segment> segments = r.getSegments();
		
		JsonPrettyFormatter fm = new JsonPrettyFormatter(segments);
		List<Segment> formatted = fm.format();
		
		CList<Segment> expected = new CList();
		for(int i=0; i<parts.length; i++)
		{
			Object x = parts[i];
			if(x instanceof String)
			{
				i++;
				Type t = (Type)parts[i];
				expected.add(new Segment(t, (String)x));
			}
			else if(x instanceof Type)
			{
				expected.add(new Segment((Type)x, null));
			}
			else
			{
				throw new Rex("?" + x);
			}
		}
		
		if(!isSame(formatted, expected))
		{
			TF.print("FAIL");
			TF.print("parsed:");
			TF.list(formatted);
			TF.print("expected:");
			TF.list(expected);
			TF.print("JSON:\n" + text + "\n");
			throw new Rex("Mismatch at index " + findMismatchIndex(formatted, expected));
		}
	}
	
	
	/** returns true if two lists are the same, optionally ignoring text if the expected segment text is null */ 
	private static boolean isSame(List<Segment> segments, List<Segment> expected)
	{
		if(segments.size() != expected.size())
		{
			return false;
		}
		
		for(int i=0; i<segments.size(); i++)
		{
			Segment seg = segments.get(i);
			Segment exp = expected.get(i);
			if(!isSame(seg, exp))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/** returns true if two segments are the same, optionally ignoring text if the expected segment text is null */ 
	private static boolean isSame(Segment segment, Segment expected)
	{
		if(segment.getType() == expected.getType())
		{
			if(expected.getText() == null)
			{
				return true;
			}
			else
			{
				return segment.getText().equals(expected.getText());
			}
		}
		return false;
	}
	
	
	private static int findMismatchIndex(List<Segment> segments, List<Segment> expected)
	{
		for(int i=0; i<10000; i++)
		{
			if((i >= segments.size()) || (i >= expected.size()))
			{
				return i;
			}
			
			Segment a = segments.get(i);
			Segment b = expected.get(i);
			if(!isSame(a, b))
			{
				return i;
			}
		}
		return -1;
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
