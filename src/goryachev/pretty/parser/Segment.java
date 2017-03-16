// Copyright (c) 2007-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;


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
	
	
	/** escaped text, for debugging only (messes up \ char) */
	public String getTextEscaped()
	{
		SB sb = new SB(text.length() + 64);
		text.codePoints().forEach((c) ->
		{
			if(c < ' ')
			{
				String s = printable(c);
				if(s == null)
				{
					sb.append(Hex.toHexString((short)c));
				}
				else
				{
					sb.append(s);
				}
			}
			else
			{
				sb.appendCodePoint(c);
			}
		});
		return sb.toString();
	}
	
	
	protected String printable(int c)
	{
		switch(c)
		{
		case '\b': return "\\b";
		case '\f': return "\\f";
		case '\n': return "\\n";
		case '\r': return "\\r";
		case '\t': return "\\t";
		}
		return null;
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
		return "Segment." + getType() + "<" + getTextEscaped() + ">";
	}
}