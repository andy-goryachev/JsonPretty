// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
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
		List<Segment> parsed = r.getSegments();
		
		int sz = parts.length / 2;
		CList<Segment> expected = new CList(sz);
		int ix = 0;
		for(int i=0; i<sz; i++)
		{
			Type type = (Type)parts[ix++];
			String part = (String)parts[ix++];
			expected.add(new Segment(type, part));
		}
		
		if(!CKit.equals(parsed, expected))
		{
			TF.print("FAIL JSON:\n" + text + "\n");
			throw TF.printDiff("parsed:", parsed, "expected:", expected, (a,b) -> CKit.equals(a, b));
		}
	}
}
