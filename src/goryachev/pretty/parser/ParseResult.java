// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CList;
import java.util.List;


/** 
 * Encapsulates result of a parsing operation.
 */
public class ParseResult
{
	private final CList<Segment> segments = new CList<>(128);
	
	
	public ParseResult()
	{
	}
	
	
	public List<Segment> getSegments()
	{
		return segments;
	}

		
	public void addSegment(Segment c)
	{
		segments.add(c);
	}
	
	
	boolean isInArray()
	{
		for(int i=segments.size() - 1; i>=0; i--)
		{
			Segment seg = segments.get(i);
			switch(seg.getType())
			{
			case ARRAY_BEGIN:
				return true;
			case OBJECT_BEGIN:
				return false;
			}
		}
		return false;
	}
}
