// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


public class TestRecursiveJsonParser
{
	public static final Type AB = Type.ARRAY_BEGIN;
	public static final Type AE = Type.ARRAY_END;
	public static final Type CA = Type.COMMA_ARRAY;
	public static final Type CO = Type.COMMA;
	public static final Type IG = Type.IGNORE;
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
	
	
	public static void t(Object ... parts)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<parts.length; i++)
		{
			sb.append((String)parts[++i]);
		}
		String text = sb.toString();
		
		RecursiveJsonParser p = new RecursiveJsonParser(text);
		ParseResult r = p.parse();
		List<Segment> segments = r.getSegments();
		
		int sz = parts.length / 2;
		CList<Segment> expected = new CList(sz);
		int ix = 0;
		for(int i=0; i<sz; i++)
		{
			Type type = (Type)parts[ix++];
			String part = (String)parts[ix++];
			expected.add(new Segment(type, part));
		}
		
		if(!CKit.equals(segments, expected))
		{
			TF.print("FAIL");
			TF.print("parsed:");
			TF.list(segments);
			TF.print("expected:");
			TF.list(expected);
			TF.print("JSON:\n" + text + "\n");
			throw new Rex("Mismatch at index " + findMismatchIndex(segments, expected));
		}
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
			if(CKit.notEquals(a, b))
			{
				return i;
			}
		}
		return -1;
	}


	@Test
	public void testRegression()
	{
		t(OB, "{", OE, "}");
		t(AB, "[", AE, "]");
		t(OB, "{", WH, " ", OE, "}", IG, " xx");
		t(AB, "[", WH, " ", AE, "]", IG, " xx");
		t(OB, "{", WH, " ", OE, "}");
		t(AB, "[", WH, " ", AE, "]");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "null", OE, "}");
		t(IG, "hello");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "true", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n", OE, "}");
		
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\nname", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fdex", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\n\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		
		
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", OB, "{", WH, "\n", NB, "\"", NA, "n", NE, "\"", SP, ":", VA, "true", OE, "}", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", WH, "\n", VA, "1", WH, " ", CA, ",", WH, " ", VA, "2", WH, " ", AE, "]", WH, " ", OE, "}");
//		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", WH, "\n", VA, "1", WH, " ", CA, ",", WH, " ", VA, "2", WH, " ", AE, "]", WH, " ", OE, "}");
//		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", AE, "]");
//		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "s", SE, "\"", CA, ",", VA, "3", AE, "]");
//		t(AB, "[", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", WH, "\n");
//		t(AB, "[", WH, " ", VA, "1", CA, ",", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", WH, "\n");
//		t(AB, "[", WH, " ", VA, "1", CA, ",", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", WH, "\n");
	}


	@Test
	public void test()
	{
//		t(AB, "[", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", CA, ",", WH, " ", SB, "\"", ST, "1", SE, "\"", WH, " ", AE, "]", WH, "\n");
	}
}
