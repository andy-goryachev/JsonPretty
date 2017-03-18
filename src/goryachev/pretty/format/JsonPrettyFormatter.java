// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.format;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.common.util.SB;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


/**
 * Json Pretty Formatter.
 */
public class JsonPrettyFormatter
{
	private final List<Segment> input;
	private final CList<Segment> result;
	private int indent;
	private Type prev = Type.IGNORE;
	private int tabSize = 4; // TODO option
	private boolean egyptian = false; // TODO
	
	
	public JsonPrettyFormatter(List<Segment> input)
	{
		this.input = input;
		result = new CList<>(input.size() + 128);
	}
	
	
	// returns true if new line is needed before the ARRAY_END token
	protected boolean processArrayEnd()
	{
		int ix = skipBack(Type.ARRAY_BEGIN);
		if(ix >= 0)
		{
			result.prune(ix);
			addSpace(1);
			addSegment(new Segment(Type.ARRAY_BEGIN, "["));
			return false;
		}
		return true;
	}
	
	
	// returns true if new line is needed before the OBJECT_END token
	protected boolean processObjectEnd()
	{
		int ix = skipBack(Type.OBJECT_BEGIN);
		if(ix >= 0)
		{
			result.prune(ix);
			addSpace(1);
			addSegment(new Segment(Type.OBJECT_BEGIN, "{"));
			return false;
		}
		return true;
	}
	
	
	// skip whitespace and newlines until the specified tag is found
	// then skip any additional whitespace and newline and return 
	// what effectively is the new result array size.
	// -1 means that we don't have this condition
	protected int skipBack(Type type)
	{
		boolean leading = false;
		
		for(int i=result.size()-1; i>=0; i--)
		{
			Segment s = result.get(i);
			Type t = s.getType();
			
			if(!leading)
			{
				if(t == type)
				{
					leading = true;
					continue;
				}
			}
			
			switch(t)
			{
			case LINEBREAK:
			case WHITESPACE:
				continue;
			default:
				if(leading)
				{
					return i + 1;
				}
				else
				{
					return -1;
				}
			}
		}
		
		if(leading)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	
	
	protected Type lastType()
	{
		if(result.size() > 0)
		{
			return result.getLast().getType();
		}
		return Type.IGNORE;
	}
	
	
	protected boolean needLineBreakBeforeArrayOrObject()
	{
		if(result.size() == 0)
		{
			return false;
		}
		
		if(lastType() == Type.LINEBREAK)
		{
			return false;
		}
		
		// TODO perhaps insert an extra line break between two objects or arrays
		
		return true;
	}


	public CList<Segment> format()
	{
		int sz = input.size();
		for(int i=0; i<sz; i++)
		{
			Segment s = input.get(i);
			Type t = s.getType();
			switch(t)
			{
			case ARRAY_BEGIN:
			case OBJECT_BEGIN:
				if(needLineBreakBeforeArrayOrObject())
				{
					insertLineBreak();
				}
				addSegment(s);
				indent++;
				insertLineBreak();
				break;
				
			case ARRAY_END:
				indent--;
				if(processArrayEnd())
				{
					insertLineBreak();
				}
				addSegment(s);
				break;
				
			case OBJECT_END:
				indent--;
				if(processObjectEnd())
				{
					insertLineBreak();
				}
				addSegment(s);
				break;
				
			case COMMA_ARRAY:
			case COMMA:
				addSegment(s);
				break;
				
			case SEPARATOR:
				addSpace(1);
				addSegment(s);
				break;
				
			case WHITESPACE:
			case LINEBREAK:
				switch(prev)
				{
				case IGNORE:
					addSegment(s);
					break;
				default:
					continue;
				}
				break;
				
			case IGNORE:
				switch(prev)
				{
				case OBJECT_END:
					insertLineBreak();
				}
				addSegment(s);
				break;
				
			default:
				switch(prev)
				{
				case COMMA:
				case COMMA_ARRAY:
					insertLineBreak();
					break;
				case SEPARATOR:
					addSpace(1);
					break;
				}
				addSegment(s);
				break;
			}
		}
		return result;
	}
	
	
	protected void addSegment(Segment s)
	{
		result.add(s);
		prev = s.getType();
	}
	
	
	protected void insertLineBreak()
	{
		addSegment(new Segment(Type.LINEBREAK, "\n"));
		addSpace(indent * tabSize);
	}
	
	
	protected void addSpace(int spaces)
	{
		SB sb = new SB(spaces);
		sb.sp(spaces);
		addSegment(new Segment(Type.WHITESPACE, sb.toString()));
	}
}
