// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.ResilientJsonParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


public class TestJsonParser
{
	public static final Type AB = Type.ARRAY_BEGIN;
	public static final Type AE = Type.ARRAY_END;
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
		
		ResilientJsonParser p = new ResilientJsonParser(text);
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
			throw new Rex();
		}
	}
	
	
	@Test
	public void testRegression()
	{
		t(OB, "{", OE, "}");
		t(AB, "[", AE, "]");
		t(IG, "hello");
		t(OB, "{", NB, "\"", NA, "name", NE, "\"", SP, ":", VA, "true", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\nname", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\r", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\rx", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "x\\u4fdex", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "\\n\\u4fde", NE, "\"", WH, "  ", SP, ":", WH, "  ", SB, "\"", ST, "a string", SE, "\"", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", VA, "true", WH, "\n", OE, "}");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n", OE, "}");
		t(OB, "{", WH, " ", OE, "}");
		t(AB, "[", WH, " ", AE, "]");
		t(OB, "{", WH, " ", OE, "}", WH, " ", IG, "xx");
		t(AB, "[", WH, " ", AE, "]", WH, " ", IG, "xx");
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
	}


	@Test
	public void test()
	{
		t(IG, "xx  ", OB, "{", WH, " ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", OB, "{", WH, "\n", NB, "\"", NA, "n", NE, "\"", SP, ":", VA, "true", OE, "}", CO, ",", WH, "\n ", NB, "\"", NA, "name", NE, "\"", WH, "  ", SP, ":", WH, "  ", AB, "[", AE, "]", WH, "\n ", OE, "}");
	}
}
