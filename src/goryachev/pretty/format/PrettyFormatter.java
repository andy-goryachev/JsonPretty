// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.format;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;


/**
 * JSON/XML Pretty Formatter.
 */
public class PrettyFormatter
{
	private final List<Segment> input;
	private final CList<Segment> result;
	private int indent;
	private boolean insertIndent;
	private Type prev = Type.IGNORE;
	private boolean egyptian = false; // TODO
	protected static Log log = Log.get("PrettyFormatter");
	
	
	public PrettyFormatter(List<Segment> input)
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
			return false;
		}
		return true;
	}
	
	
	// going backwards, skips whitespace, line break, and indents until the specified tag is found
	// and returns what effectively is the new result array size.
	// return value of -1 means that we don't have this condition
	protected int skipBack(Type type)
	{
		for(int i=result.size()-1; i>=0; i--)
		{
			Segment s = result.get(i);
			Type t = s.getType();
			
			if(t == type)
			{
				insertIndent = false;
				return i + 1;
			}
			
			switch(t)
			{
			case LINEBREAK:
			case WHITESPACE:
			case INDENT:
				continue;
			default:
				return -1;
			}
		}
		
		return -1;
	}
	
	
	// going backwards, skips whitespace, line break, and indents until the specified tag is found
	// then skip any additional whitespace, line breaks, and indents
	// and returns what effectively is the new result array size.
	// -1 means that we don't have this condition
	protected int skipBack_OLD(Type type)
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
			case INDENT:
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
		
		switch(lastType())
		{
		case LINEBREAK:
		case INDENT:
			return false;
		}
		
		// TODO perhaps insert an extra line break between two objects or arrays
		
		return true;
	}
	
	
	protected void indent(int delta)
	{
		indent += delta;
		if(indent < 0)
		{
			indent = 0;
		}
	}


	public CList<Segment> format()
	{
		int sz = input.size();
		for(int i=0; i<sz; i++)
		{
			Segment s = input.get(i);
			Type t = s.getType();
			
			log.print(t, s);
			
			switch(t)
			{
			case ARRAY_BEGIN:
			case OBJECT_BEGIN:
				if(needLineBreakBeforeArrayOrObject())
				{
					insertLineBreak();
				}
				addSegment(s);
				indent(1);
				insertLineBreak();
				break;
				
			case ARRAY_END:
				indent(-1);
				if(processArrayEnd())
				{
					insertLineBreak();
				}
				addSegment(s);
				break;
				
			case OBJECT_END:
				indent(-1);
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
				indent = 0;
				break;
				
			case XML_COMMENT:
			case XML_TAG_EMPTY:
				insertLineBreak();
				addSegment(s);
				break;
				
			case XML_TAG_OPEN:
				insertLineBreak();
				indent(1);
				addSegment(s);
				break;
				
			case XML_TAG_CLOSING:
				indent(-1);
				insertLineBreak();
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
	
	
	protected void addSegment(Type t, String text)
	{
		addIndentConditional(t);
		addSegmentPrivate(new Segment(t, text));
	}
	
	
	protected void addSegment(Segment s)
	{
		addIndentConditional(s.getType());
		addSegmentPrivate(s);
	}
	
	
	protected void addIndentConditional(Type t)
	{
		if(insertIndent)
		{
			if(t != Type.LINEBREAK)
			{
				if(indent > 0)
				{
					addSegmentPrivate(new Segment(Type.INDENT, CKit.tabs(indent)));
				}
				insertIndent = false;
			}
		}
	}
	
	
	protected void addSegmentPrivate(Segment s)
	{
		log.print("     add:", s);
		
		result.add(s);
		prev = s.getType();
	}
	
	
	protected void insertLineBreak()
	{
		addSegment(new Segment(Type.LINEBREAK, "\n"));
		insertIndent = true;
	}
	
	
	protected void addSpace(int spaces)
	{
		if(spaces > 0)
		{
			addSegment(new Segment(Type.WHITESPACE, CKit.spaces(spaces)));
		}
	}
	
	
//	protected void addIndent(int tabs)
//	{
//		if(tabs > 0)
//		{
//			addSegment(new Segment(Type.INDENT, CKit.tabs(tabs)));
//		}
//	}
}
