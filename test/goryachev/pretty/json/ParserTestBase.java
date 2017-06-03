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
 * Parser Test Base Class.
 */
public class ParserTestBase
	implements TestTypes
{
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
