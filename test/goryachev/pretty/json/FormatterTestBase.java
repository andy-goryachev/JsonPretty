// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;
import goryachev.pretty.format.PrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonXmlParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


/**
 * Formatter Test Base Class.
 */
public class FormatterTestBase
	implements TestTypes
{
	public static void t(String text, Object ... parts)
	{
		RecursiveJsonXmlParser p = new RecursiveJsonXmlParser(text);
		ParseResult r = p.parse();
		List<Segment> segments = r.getSegments();
		
		PrettyFormatter fm = new PrettyFormatter(segments);
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
			TF.print("formatted:");
			TF.list(formatted);
			TF.print("expected:");
			TF.list(expected);
			TF.print("JSON:\n" + text + "\n");
			
			int ix = findMismatchIndex(formatted, expected);
			throw new Rex("Mismatch at index " + ix + ": " + formatted.get(ix) + " " + expected.get(ix));
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
}
