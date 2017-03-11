// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.util.CList;


/** 
 * Encapsulates result of a parsing operation.
 */
public class ParseResult
{
	private Chunk[] chunks;
	
	
	public ParseResult()
	{
	}
	
	
	public Chunk[] getChunks()
	{
		return chunks;
	}

		
	public void setChunks(Chunk[] cs)
	{
		chunks = cs;
	}
	
	
	public void setChunks(CList<Chunk> cs)
	{
		setChunks(cs == null ? null : cs.toArray(new Chunk[cs.size()]));
	}
}
