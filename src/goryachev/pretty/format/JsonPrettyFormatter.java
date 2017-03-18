// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.format;
import goryachev.common.util.CList;
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
				insertLineBreak();
				addSegment(s);
				indent++;
				insertLineBreak();
				break;
				
			case ARRAY_END:
				indent--;
				switch(prev)
				{
				case ARRAY_BEGIN:
					break;
				default:
					insertLineBreak();
					break;
				}
				addSegment(s);
				break;
				
			case OBJECT_END:
				indent--;
				switch(prev)
				{
				case OBJECT_BEGIN:
					break;
				default:
					insertLineBreak(); // FIX conditional
					break;
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
				continue;
				
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
