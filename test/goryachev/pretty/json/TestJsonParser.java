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
	public static final Type N = Type.NAME;
	public static final Type OB = Type.OBJECT_BEGIN;
	public static final Type OE = Type.OBJECT_END;
	public static final Type V = Type.VALUE;
	public static final Type X = Type.IGNORE;
	
	
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
	
	
//	@Test
//	public void testParser() throws Exception
//	{
//		t(X, "{", L, "\n", X, " [ \"", N, "I \\\" I", X, "\": ", V, "1", X, " ] }", L, "\n");
//		t(X, "{", L, "\n", X, " [ \"", N, "I \\\" I", X, "\": \"", N, "V \\\" V", X, "\" ] }", L, "\n");
//		t(X, "{", L, "\n", X, "\"", N, "I \\\" I", X, "\": \"", N, "V \\\" V", X, "\" }", L, "\n");
//		t(X, "{", L, "\n", X, "\"", N, "I I", X, "\": \"", N, "V V", X, "\" }", L, "\n");
//		t(X, "{", L, "\n", X, "\"", N, "I", X, "\": \"", N, "V", X, "\" }", L, "\n");
//	}
	
	
	@Test
	public void test()
	{
		t(OB, "{", OE, "}"); 
	}
	
	
	@Test
	public void testIgnore()
	{
		t(X, "hello"); 
	}
}
