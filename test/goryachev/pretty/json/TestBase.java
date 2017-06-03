// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonXmlParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


/**
 * Test Base.
 */
public class TestBase
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
	public static final Type XT = Type.XMLTAG;

	
	public static void t(Object ... parts)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<parts.length; i++)
		{
			sb.append((String)parts[++i]);
		}
		String text = sb.toString();
		
		RecursiveJsonXmlParser p = new RecursiveJsonXmlParser(text);
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
}
