// Copyright (c) 2007-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;


public class Chunk
{
	private final ChunkType type;
	private final String text;
	
	
	public Chunk(ChunkType type, String text)
	{
		this.type = type;
		this.text = text;
	}
	
	
	public ChunkType getType()
	{
		return type;
	}
	
	
	public String getText()
	{
		return text;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Chunk)
		{
			Chunk c = (Chunk)x;
			return (type == c.type) && CKit.equals(text, c.text);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(Chunk.class);
		h = FH.hash(h, type);
		h = FH.hash(h, text);
		return h;
	}
	
	
	public String toString()
	{
		return "Chunk." + getType() + "[" + getText() + "]";
	}
}