// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CList;
import java.util.List;


/** 
 * Encapsulates result of a parsing operation.
 */
public class ParseResult
{
	private final CList<Chunk> chunks = new CList<>(128);
	
	
	public ParseResult()
	{
	}
	
	
	public List<Chunk> getChunks()
	{
		return chunks;
	}

		
	public void add(Chunk c)
	{
		chunks.add(c);
	}
}
