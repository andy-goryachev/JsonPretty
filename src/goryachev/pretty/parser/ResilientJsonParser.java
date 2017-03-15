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
	private int symbolLength;
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
			symbolLength = Character.charCount(c);
				
			Type newState = processSymbol(c);
			if(newState != null)
			{
				if(newState != state)
				{
					addSegment();
					state = newState;
				}
			}
			offset += symbolLength;
		}
		addSegment();
		
		return result;
	}
	
	
	protected void pushState()
	{
		states.add(state);
	}
	
	
	protected Type popState()
	{
		return states.remove(states.size() - 1);
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
	
	
	// returns length of the escaped symbols, or -1 if not a valid sequence
	protected int getEscapeSequenceLength()
	{
		int c = text.codePointAt(offset + symbolLength);
		int len = Character.charCount(c);
		
		switch(c)
		{
		case 'b':
		case 'f':
		case 'n':
		case 'r':
		case 't':
		case '"':
		case '\\':
		case '/':
			return len;
		case 'u':
			// unicode escape
			return len += 3;
		default:
			// not a valid escape sequence
			return -1;
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
		// stay in error state
		return null;
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
		case '\\':
			int n = getEscapeSequenceLength();
			if(n < 0)
			{
				return Type.ERROR;
			}
			else
			{
				symbolLength += n;
			}
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
		switch(c)
		{
		case '\\':
			// TODO escape
			throw new Rex("esc"); 
		case '"':
			return Type.STRING_END;
		}
		
		return null;
	}
	
	
	protected Type inStringBegin(int c)
	{
		switch(c)
		{
		case '\\':
			// TODO escape
			throw new Rex("esc"); 
		case '"':
			return Type.STRING_END;
		}
		
		return Type.STRING;
	}
	
	
	protected Type inStringEnd(int c)
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
		case ']':
			return Type.ARRAY_END; // FIX?
		}
		
		return Type.ERROR;
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
		if(Character.isWhitespace(c))
		{
			return null;
		}
		
		Type prev = popState();
		switch(c)
		{
		case '"':
			switch(prev)
			{
			case OBJECT_BEGIN:
				return Type.NAME_BEGIN;
			case SEPARATOR:
				return Type.STRING_BEGIN;
			}
			// TODO
			throw new Rex("c=" + (char)c + " " + prev);
			
		case ':':
			switch(prev)
			{
			case NAME_END:
				return Type.SEPARATOR;
			}
			// TODO
			throw new Rex("c=" + (char)c + " " + prev);
			
		case '}':
			switch(prev)
			{
			case STRING_END:
			case VALUE:
				return Type.OBJECT_END;
			}
			// TODO
			throw new Rex("c=" + (char)c + " " + prev);
		}
		
		switch(prev)
		{
		case SEPARATOR:
			return Type.VALUE;
		}
		
		throw new Rex("c=" + (char)c + " " + prev);
	}
}
