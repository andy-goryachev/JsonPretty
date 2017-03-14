// Copyright (c) 2007-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;


public class Segment
{
	private final Type type;
	private final String text;
	
	
	public Segment(Type type, String text)
	{
		this.type = type;
		this.text = text;
	}
	
	
	public Type getType()
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
		else if(x instanceof Segment)
		{
			Segment c = (Segment)x;
			return (type == c.type) && CKit.equals(text, c.text);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(Segment.class);
		h = FH.hash(h, type);
		h = FH.hash(h, text);
		return h;
	}
	
	
	public String toString()
	{
		return "Segment." + getType() + "<" + getText() + ">";
	}
}