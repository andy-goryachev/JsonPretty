// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CKit;


/*
 * A JSON parser capable of handling malformed JSON (and later, XML).
 * 
 * Generates the following Chunks:
 *    LINEBREAK
 *    IGNORE     - whitespace, braces, punctuation
 *    IDENTIFIER - for identifier names and quoted values
 *    VALUE      - for unquoted values
 */
public class ResilientJsonParser
{
	protected final String text;
	private int line;
	private int offset;
	private int startOffset;
	private boolean skipLF;
	private boolean inQuotes;
	private boolean escape;
	private ChunkType state;
	private ParseResult result;
	
	
	public ResilientJsonParser(String text)
	{
		this.text = text;
	}
	
	
	public ParseResult getParsedDocument()
	{
		return result;
	}
	
	
	protected void newLine()
	{
		++line;
	}
	
	
	protected void addChunk()
	{
		if(offset > startOffset)
		{
			String s = text.substring(startOffset, offset);
			Chunk ch = new Chunk(state, s);
			result.add(ch);

			startOffset = offset;
		}
	}
	
	
	protected void setState(ChunkType t)
	{
		if(state != t)
		{
			addChunk();
			state = t;
		}
	}
	
	
	protected boolean isValue(char c)
	{
		if(CKit.isBlank(c))
		{
			return false;
		}
		
		switch(c)
		{
		case '{':
		case '}':
		case '[':
		case ']':
		case ':':
			return false;
		}
		return true;
	}
	
	
	public ParseResult parse()
	{
		if(result != null)
		{
			return result;
		}
		
		result = new ParseResult();
		state = ChunkType.IGNORE;
		
		int length = text.length();
		for(offset=0; offset<length; offset++)
		{
			char c = text.charAt(offset);
			switch(c)
			{
			case '\r':
				setState(ChunkType.LINEBREAK);
				newLine();
				skipLF = true;
				escape = false;
				break;

			case '\n':
				if(skipLF)
				{
					skipLF = false;
				}
				else
				{
					setState(ChunkType.LINEBREAK);
					newLine();
				}
				escape = false;
				break;
				
			case '\\':
				escape = true;
				break;
				
			case '"':
				if(inQuotes)
				{
					if(!escape)
					{
						setState(ChunkType.IGNORE);
						inQuotes = false;
					}
				}
				else
				{
					if(!escape)
					{
						if(state != ChunkType.IGNORE)
						{
							setState(ChunkType.IGNORE);
						}
						inQuotes = true;
					}
				}
				escape = false;
				break;
				
			default:
				switch(state)
				{
				case IGNORE:
				case LINEBREAK:
					{
						if(inQuotes)
						{
							setState(ChunkType.IDENTIFIER);
						}
						else
						{
							if(isValue(c))
							{
								setState(ChunkType.VALUE);
							}
							else
							{
								if(state != ChunkType.IGNORE)
								{
									setState(ChunkType.IGNORE);
								}
							}
						}
					}
					break;
				case VALUE:
					if(!inQuotes)
					{
						if(CKit.isBlank(c))
						{
							setState(ChunkType.IGNORE);
						}
					}
					break;
				}
				escape = false;
			}
		}
		
		setState(null);
		
		return result;
	}
}


