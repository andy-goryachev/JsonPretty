// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.D;
import goryachev.common.util.Rex;


/**
 * A recursive descent parser capable of handling malformed JSON, XML, and embedded text.
 */
public class RecursiveJsonXmlParser
{
	private static final int EOF = -1;
	
	protected final String text;
	private final int maxSameOffsetCount = 50;
	private int ch;
	private Type state;
	private int offset;
	private int startOffset;
	private int symbolLength;
	private ParseResult result;
	private int sameOffsetCount;
	private int prevOffset = -1;
	private int xmlLevel;
	
	
	public RecursiveJsonXmlParser(String text)
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

		for(;;)
		{
			int c = peek();
			if(c == EOF)
			{
				break;
			}
			
			switch(c)
			{
			case '{':
			case '[':
			case '<':
				readMain();
				if(xmlLevel > 0)
				{
					setState(Type.XML_TEXT);
				}
				else
				{
					setState(Type.IGNORE);
				}
				continue;
			case '\r':
			case '\n':
				setState(Type.LINEBREAK);
				break;
			default:
				if(xmlLevel > 0)
				{
					setState(Type.XML_TEXT);
				}
				else
				{
					setState(Type.IGNORE);
				}
				break;
			}
			
			next();
		}
		
		addSegment();
		
		return result;
	}
	
	
	protected void addSegment()
	{
		int off = Math.min(offset, text.length());
		if(off > startOffset)
		{
			String s = text.substring(startOffset, off);
			
			switch(state)
			{
			case XML_TAG_OPEN:
				if(s.endsWith("/>"))
				{
					state = Type.XML_TAG_EMPTY;
					xmlLevel--;
				}
				break;
			}
			
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
	
	
	protected void forceSegment()
	{
		addSegment();
	}
	
	
	protected void detectInfiniteLoop()
	{
		// this code detects errors in the parser that cause infinite loops
		if(offset == prevOffset)
		{
			sameOffsetCount++;
			if(sameOffsetCount == maxSameOffsetCount - 20)
			{
				D.print("oops:\n" + text.substring(0, offset + 1));
			}
			else if(sameOffsetCount > maxSameOffsetCount)
			{
				throw new Rex("stuck at offset " + offset);
			}
		}
		else
		{
			prevOffset = offset;
			sameOffsetCount = 0;
		}
	}
	
	
	protected int peek()
	{
		if(offset >= text.length())
		{
			return EOF;
		}
		
		detectInfiniteLoop();
		
		int c = text.codePointAt(offset);
		symbolLength = Character.charCount(c);
		return c;
	}
	
	
	protected boolean peek(String token)
	{
		if(offset + token.length() >= text.length())
		{
			return false;
		}
		
		detectInfiniteLoop();
		
		return token.equals(text.substring(offset, offset + token.length()));
	}
	
	
	protected void next()
	{
		offset += symbolLength;
	}
	
	
	protected void next(int count)
	{
		for(int i=0; i<count; i++)
		{
			next();
		}
	}
	
	
	protected String nextString(int len)
	{
		len = Math.max(0, Math.min(len, text.length() - offset));
		return text.substring(offset, offset + len);
	}
	
	
	protected void skipWhitespace()
	{
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case '\r':
			case '\n':
				setState(Type.LINEBREAK);
				break;
			case ' ':
			case '\t':
				setState(Type.WHITESPACE);
				break;
			case EOF:
				return;
			default:
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
		
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case '}':
				setState(Type.OBJECT_END);
				next();
				forceSegment();
				return;
			case ',':
				setState(Type.COMMA);
				next();
				skipWhitespace();
				break;
			case '"':
				setState(Type.NAME);
				readString(true);
				skipWhitespace();
				setState(Type.SEPARATOR);
				expect(':');
				skipWhitespace();
				
				// read field value
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
				break;
			case EOF:
				return;
			default:
				// TODO backtrack to { and turn everything in between into an error
				setState(Type.ERROR);
				next();
				break;
			}
		}
	}
	
	
	protected void readArray()
	{
		setState(Type.ARRAY_BEGIN);
		expect('[');
		
		for(;;)
		{
			skipWhitespace();
			
			int c = peek();
			if(c == ']')
			{
				setState(Type.ARRAY_END);
				next();
				break;
			}
			else if(c == EOF)
			{
				break;
			}
			
			switch(c)
			{
			case ',':
				setState(Type.COMMA_ARRAY);
				next();
				break;
			case '[':
				readArray();
				break;
			case '{':
				readObject();
				break;
			default:
				readValue();
				if(state == Type.ERROR)
				{
					return;
				}
				break;
			}
		}
		setState(Type.ARRAY_END);
		expect(']');
	}
	
	
	protected void readString(boolean name)
	{
		setState(name ? Type.NAME_BEGIN : Type.STRING_BEGIN);
		expect('"');
		
		for(;;)
		{
			setState(name ? Type.NAME : Type.STRING);
			
			int c = peek();
			if(c == '"')
			{
				break;
			}
			else if(c == EOF)
			{
				break;
			}
			
			switch(c)
			{
			case '\\':
				next();
				readEscape();
				break;
			default:
				next();
				break;
			}
		}
		
		setState(name ? Type.NAME_END : Type.STRING_END);
		expect('"');
	}
	
	
	protected void readEscape()
	{
		int c = peek();
		switch(c)
		{
		case '"':
		case '\\':
		case '/':
		case 'b':
		case 'f':
		case 'n':
		case 'r':
		case 't':
			next();
			return;
		case 'u':
			next();
			readHex();
			return;
		case EOF:
			return;
		}
	}
	
	
	protected void readHex()
	{
		String hex = nextString(4);
		if(hex.length() != 4)
		{
			setState(Type.ERROR);
		}
		
		for(int i=0; i<hex.length(); i++)
		{
			next();
		}
	}
	
	
	protected void readValue()
	{
		setState(Type.VALUE);
		int count = 0;
		
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case ',':
			case ' ':
			case '\n':
			case '\r':
			case '\f':
			case '}':
			case ']':
				if(count == 0)
				{
					setState(Type.ERROR);
				}
				return;
			case EOF:
				return;
			case '"':
				readString(false);
				count++;
				return;
			}
			
			next();
			count++;
		}
	}
	
	
	protected void readXmlComment()
	{
		setState(Type.XML_COMMENT);
		
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case '-':
				if(peek("-->"))
				{
					next(3);
					return;
				}
				break;
			}
			
			next();
		}
	}
	
	
	protected void readXmlTag()
	{
		if(peek("<!--"))
		{
			readXmlComment();
			return;
		}
		
		if(peek("</"))
		{
			setState(Type.XML_TAG_CLOSING);
			xmlLevel--;
		}
		else
		{
			setState(Type.XML_TAG_OPEN);
			xmlLevel++;
		}
		expect('<');
		
		for(;;)
		{
			int c = peek();
			switch(c)
			{
			case '>':
				next();
				if(xmlLevel > 0)
				{
					setState(Type.XML_TEXT);
				}
				else
				{
					setState(Type.IGNORE);
				}
				return;
			default:
				next();
				break;
			}
		}
	}
	
	
	protected void readMain()
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
		case '<':
			readXmlTag();
			break;
		case EOF:
		default:
			// FIX? error?
			break;
		}
		return;
	}
}
