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
	
	
	public Segment getSegment(int ix)
	{
		return segments.get(ix);
	}
	
	
	public Type getType(int ix)
	{
		return getSegment(ix).getType();
	}

		
	public void addSegment(Type type, String text)
	{
		segments.add(new Segment(type, text));
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


	public int size()
	{
		return segments.size();
	}


	public void replaceTail(int ix, Type t, String text)
	{
		segments.prune(ix);
		addSegment(t, text);
	}
}
