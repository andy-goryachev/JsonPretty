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
			// TODO use previos to add spaces after comma,
			// to add newline after OE
			
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
			case OBJECT_END:
				indent--;
				insertLineBreak();
				addSegment(s);
//				insertLineBreak();
				break;
			case COMMA_ARRAY:
			case COMMA:
				addSegment(s);
				insertLineBreak();
				break;
			case SEPARATOR:
				addSpace(1);
				addSegment(s);
				addSpace(1); // FIX conditional
				break;
			case WHITESPACE:
				// ignore
				continue;
			default:
				addSegment(s);
				break;
			}
		}
		return result;
	}
	
	
	protected void addSegment(Segment s)
	{
		result.add(s);
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
