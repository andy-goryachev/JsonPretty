// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;


/**
 * A JSON parser capable of handling malformed JSON (and later, XML).
 */
public class ResilientJsonParser
{
	protected final String text;
	private Type state;
	private CList<Type> states = new CList<>();
	private int offset;
	private int startOffset;
	private ParseResult result;
	
	
	public ResilientJsonParser(String text)
	{
		this.text = text;
	}
	
	
	public ParseResult parse()
	{
		if(result != null)
		{
			throw new Rex("parser can be called only once");
		}
		
		result = new ParseResult();
		state = Type.IGNORE;
		for(offset=0; offset<text.length(); )
		{
			int c = text.codePointAt(offset);
			Type newState = processSymbol(c);
			if(newState != null)
			{
				if(newState != state)
				{
					addSegment();
					state = newState;
				}
			}
			offset += Character.charCount(c);
		}
		addSegment();
		
		return result;
	}
	
	
	protected void pushState()
	{
		states.add(state);
	}
	
	
	protected void popState()
	{
	}
	
	
	protected void addSegment()
	{
		if(offset > startOffset)
		{
			String s = text.substring(startOffset, offset);
			Segment ch = new Segment(state, s);
			result.addSegment(ch);

			startOffset = offset;
		}
	}
	
	
	protected Type processSymbol(int c)
	{
		switch(state)
		{
		case ARRAY_BEGIN:
			return inArrayBegin(c);
		case ARRAY_END:
			return inArrayEnd(c);
		case COMMENT:
			return inComment(c);
		case ERROR:
			return inError(c);
		case IGNORE:
			return inIgnore(c);
		case NAME:
			return inName(c);
		case NAME_BEGIN:
			return inNameBegin(c);
		case NAME_END:
			return inNameEnd(c);
		case OBJECT_BEGIN:
			return inObjectBegin(c);
		case OBJECT_END:
			return inObjectEnd(c);
		case SEPARATOR:
			return inSeparator(c);
		case STRING:
			return inString(c);
		case STRING_BEGIN:
			return inStringBegin(c);
		case STRING_END:
			return inStringEnd(c);
		case VALUE:
			return inValue(c);
		case WHITESPACE:
			return inWhitespace(c);
		default:
			throw new Rex("unknown state " + state);
		}
	}
	
	
	protected Type inArrayBegin(int c)
	{
		if(Character.isWhitespace(c))
		{
			pushState();
			return Type.WHITESPACE;
		}
		
		switch(c)
		{
		case '"':
			return Type.STRING_BEGIN;
		case ']':
			return Type.ARRAY_END;
		}
		
		return Type.VALUE;
	}
	
	
	protected Type inArrayEnd(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inComment(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inError(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inIgnore(int c)
	{
		switch(c)
		{
		case '{':
			return Type.OBJECT_BEGIN;
		case '[':
			return Type.ARRAY_BEGIN;
		}
		return null;
	}
	
	
	protected Type inName(int c)
	{
		switch(c)
		{
		case '"':
			return Type.NAME_END;
		}
		
		return null;
	}
	
	
	protected Type inNameBegin(int c)
	{
		switch(c)
		{
		case '"':
			return Type.NAME_END;
		}
		
		return Type.NAME;
	}
	
	
	protected Type inNameEnd(int c)
	{
		if(Character.isWhitespace(c))
		{
			pushState();
			return Type.WHITESPACE;
		}
		
		switch(c)
		{
		case ':':
			return Type.SEPARATOR;
		}
		
		return Type.ERROR;
	}
	
	
	protected Type inObjectBegin(int c)
	{
		if(Character.isWhitespace(c))
		{
			pushState();
			return Type.WHITESPACE;
		}
		
		switch(c)
		{
		case '"':
			return Type.NAME_BEGIN;
		case '}':
			return Type.OBJECT_END;
		}
		
		return Type.ERROR;
	}
	
	
	protected Type inObjectEnd(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inSeparator(int c)
	{
		if(Character.isWhitespace(c))
		{
			pushState();
			return Type.WHITESPACE;
		}
		
		switch(c)
		{
		case '"':
			return Type.STRING;
		default:
			return Type.VALUE;
		}
	}
	
	
	protected Type inString(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inStringBegin(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inStringEnd(int c)
	{
		throw new Rex();
	}
	
	
	protected Type inValue(int c)
	{
		if(Character.isWhitespace(c))
		{
			pushState();
			return Type.WHITESPACE;
		}
		
		switch(c)
		{
		case ',':
			return Type.COMMA;
		case '}':
			return Type.OBJECT_END;
		}
		
		return null;
	}
	
	
	protected Type inWhitespace(int c)
	{
		throw new Rex();
	}
}
