// Copyright (c) 2007-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;
import goryachev.common.util.Log;
import goryachev.common.util.TextTools;


public class Chunk
{
	private ChunkType type;
	private String text;
	protected static final Log log = Log.get("Chunk");
	
	
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
	
	
	public String getTypeString()
	{
		return getType().getName();
	}
	
	
	public String getTag()
	{
		if(isTag())
		{
			String s = TextTools.beforeSpace(getText());
			if(s.startsWith("<"))
			{
				s = s.substring(1);
			}
			if(s.endsWith(">"))
			{
				s = s.substring(0, s.length()-1);
			}
			return s;
		}
		return null;
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
	

	public boolean isAttribute()   { return (getType() == ChunkType.ATTR); }
	public boolean isComment()     { return (getType() == ChunkType.COMMENT); }
	public boolean isData()        { return (getType() == ChunkType.DATA); }
	public boolean isDelimiter()   { return (getType() == ChunkType.DELIMITER); }
	public boolean isError()       { return (getType() == ChunkType.ERROR); }
	public boolean isIgnore()      { return (getType() == ChunkType.IGNORE); }
	public boolean isIdentifier()  { return (getType() == ChunkType.IDENTIFIER); }
	public boolean isKeyword()     { return (getType() == ChunkType.KEYWORD); }
	public boolean isLinebreak()   { return (getType() == ChunkType.LINEBREAK); }
	public boolean isNormal()      { return (getType() == ChunkType.NORMAL); }
	public boolean isNumber()      { return (getType() == ChunkType.NUMBER); }
	public boolean isPartialTag()  { return (getType() == ChunkType.TAG_PART); }
	public boolean isPunctuation() { return (getType() == ChunkType.PUNCT); }
	public boolean isScript()      { return (getType() == ChunkType.SCRIPT); }
	public boolean isString()      { return (getType() == ChunkType.STRING); }
	public boolean isTag()         { return (getType() == ChunkType.TAG); }
	public boolean isText()        { return (getType() == ChunkType.TEXT); }
	public boolean isValue()       { return (getType() == ChunkType.VALUE); }
	public boolean isWhitespace()  { return (getType() == ChunkType.WHITESPACE); }
	

	public String getLowerCaseTag()
	{
		if(getType() == ChunkType.TAG)
		{
			int start = isClosingTag() ? 2 : 1;
			for(int i=start; i<text.length(); i++)
			{
				char c = text.charAt(i);
				if(!Character.isLetterOrDigit(c))
				{
					return text.substring(start, i).toLowerCase();
				}
			}
			
			return text.substring(start).toLowerCase();
		}

		throw new RuntimeException("not a tag: " + text);
	}
	
	
	public String getCompactTag()
	{
		if(getType() == ChunkType.TAG)
		{
			if(isClosingTag())
			{
				return text.toLowerCase();
			}
			else
			{
				for(int i=1; i<text.length(); i++)
				{
					char c = text.charAt(i);
					if(!Character.isLetter(c))
					{
						return text.substring(0, i).toLowerCase() + ">";
					}
				}
				throw new RuntimeException("tag has no closing >: " + text);
			}
		}
		throw new RuntimeException("not a tag: " + text);
	}
	
	
	public boolean isClosingTag()
	{
		if(getType() == ChunkType.TAG)
		{
			if(text.length() > 1)
			{
				return text.charAt(1) == '/';
			}
		}
		return false;
		//throw new RuntimeException("not a tag: " + original);
	}
	
	
	public String toString()
	{
		return "Chunk." + getTypeString() + "[" + getText() + "]";
	}


	public static String[] toStringArray(Chunk[] cs)
	{
		if(cs == null)
		{
			return null;
		}
		
		int sz = cs.length;
		if(sz == 0)
		{
			return null;
		}
		
		String[] ss = new String[sz + sz];
		int ix = 0;
		for(int i=0; i<sz; i++)
		{
			Chunk ch = cs[i];
			ss[ix++] = ch.getType().getCode();
			ss[ix++] = ch.getText();
		}
		return ss;
	}
	
	
	public static Chunk[] toChunkArray(Object x)
	{
		if(x instanceof String[])
		{
			String[] ss = (String[])x;
			int sz = ss.length;
			Chunk[] cs = new Chunk[sz/2];
			
			int ix = 0;
			for(int i=0; i<cs.length; i++)
			{
				String code = ss[ix++];
				ChunkType t = ChunkType.parse(code);
				if(t == null)
				{
					log.print("unknown chunk:", code);
					return null;
				}
				
				String text = ss[ix++];
				cs[i] = new Chunk(t, text);
			}
			return cs;
		}
		else
		{
			return null;
		}
	}
}