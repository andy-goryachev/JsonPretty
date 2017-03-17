// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.Rex;


/**
 * A recursive descent JSON parser capable of handling malformed JSON (and later, XML).
 * 
 * WARNING: this parser lacks a formal proof of correctness.
 * I am too tired to do better at the moment, perhaps some other time.
 * Sorry.
 */
public class RecursiveJsonParser
{
	private static final int EOF = -1;
	
	protected final String text;
	private int ch;
	private Type state;
	private int offset;
	private int startOffset;
	private int symbolLength;
	private ParseResult result;
	
	
	public RecursiveJsonParser(String text)
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

		while(ch != EOF)
		{
			skipIgnore();
			jsonObject();
		}
		
		addSegment();
		
		return result;
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
	
	
	protected void setState(Type t)
	{
		if(t != null)
		{
			if(t != state)
			{
				addSegment();
				state = t;
			}
		}
	}
	
	
	protected int peek()
	{
		if(offset >= text.length())
		{
			return EOF;
		}
		
		int c = text.codePointAt(offset);
		symbolLength = Character.charCount(c);
		return c;
	}
	
	
	protected void next()
	{
		offset += symbolLength;
	}
	
	
	protected void skipIgnore()
	{
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case '{':
			case '[':
				return;
			}
		}
	}
	
	
	protected void skipWhitespace()
	{
		for(;;)
		{
			int c = peek();
			if(!Character.isWhitespace(c))
			{
				return;
			}
			
			setState(Type.WHITESPACE);
			next();
		}
	}
	
	
	protected void expect(char expected)
	{
		int c = peek();
		if(c == expected)
		{
			next();
		}
		else
		{
			setState(Type.ERROR);
		}
	}
	
	
	protected void readObject()
	{
		setState(Type.OBJECT_BEGIN);
		expect('{');
		skipWhitespace();
		
		int c = peek();
		if(c != '}')
		{
			setState(Type.NAME);
			readString();
			skipWhitespace();
			setState(Type.SEPARATOR);
			expect(':');
			skipWhitespace();
			
			c = peek();
			switch(c)
			{
			case '[':
				readArray();
				break;
			case '{':
				readObject();
				break;
			default:
				readValue();
				break;
			}
			
			skipWhitespace();
		}
		setState(Type.OBJECT_END);
		expect('}');
	}
	
	
	protected void readArray()
	{
		// TODO
		throw new Rex();
	}
	
	
	protected void readString()
	{
		// TODO
		throw new Rex();
	}
	
	
	protected void readValue()
	{
		// TODO
		throw new Rex();
	}
	
	
	protected void jsonObject()
	{
		skipWhitespace();
		
		int c = peek();
		switch(c)
		{
		case '{':
			readObject();
			break;
		case '[':
			readArray();
			break;
		default:
			// FIX? error?
			break;
		}
		return;
	}
}
