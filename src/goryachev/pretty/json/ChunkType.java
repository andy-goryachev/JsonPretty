// Copyright (c) 2010-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.util.CMap;


public enum ChunkType
{
	ATTR // html tag attribute
	{
		public String getCode() { return "a"; }
		public String getName() { return "Attribute"; }
	},
	COMMENT
	{
		public String getCode() { return "C"; }
		public String getName() { return "Comment"; }
	},
	DATA // text data in cvs
	{
		public String getCode() { return "A"; }
		public String getName() { return "Data"; }
	},
	DELIMITER
	{
		public String getCode() { return "D"; }
		public String getName() { return "Delimiter"; }
	},
	ERROR
	{
		public String getCode() { return "E"; }
		public String getName() { return "Error"; }
	},
	IDENTIFIER
	{
		public String getCode() { return "K"; }
		public String getName() { return "Identifier"; }
	},
	IGNORE
	{
		public String getCode() { return "I"; }
		public String getName() { return "Ignore"; }
	},
	KEYWORD
	{
		public String getCode() { return "Y"; }
		public String getName() { return "Keyword"; }
	},
	LINEBREAK
	{
		public String getCode() { return "L"; }
		public String getName() { return "Linebreak"; }
	},
	NORMAL
	{
		public String getCode() { return "o"; }
		public String getName() { return "Normal"; }
	},
	NUMBER
	{
		public String getCode() { return "N"; }
		public String getName() { return "Number"; }
	},
	PUNCT
	{
		public String getCode() { return "P"; }
		public String getName() { return "Punctuation"; }
	},
	SEPARATOR
	{
		public String getCode() { return ","; }
		public String getName() { return "Separator"; }
	},
	SCRIPT
	{
		public String getCode() { return "R"; }
		public String getName() { return "Script"; }
	},
	STRING
	{
		public String getCode() { return "S"; }
		public String getName() { return "String"; }
	},
	TAG
	{
		public String getCode() { return "G"; }
		public String getName() { return "Tag"; }
	},
	TAG_PART
	{
		public String getCode() { return "p"; }
		public String getName() { return "TagPart"; }
	},
	TERMINATOR // prompt terminator symbol (; in localizable strings)
	{
		public String getCode() { return ";"; }
		public String getName() { return "Terminator"; }
	},
	TEXT
	{
		public String getCode() { return "T"; }
		public String getName() { return "Text"; }
	},
	VALUE // html tag value
	{
		public String getCode() { return "v"; }
		public String getName() { return "Value"; }
	},
	WHITESPACE
	{
		public String getCode() { return "W"; }
		public String getName() { return "Whitespace"; }
	},
	;
	
	//
	
	public abstract String getCode();
	public abstract String getName();
	
	//
	
	private static CMap<String,ChunkType> types;
	
	
	public synchronized static ChunkType parse(Object x)
	{
		if(types == null)
		{
			types = new CMap();
			
			for(ChunkType t: values())
			{
				types.put(t.getCode(), t);
			}
		}
		
		ChunkType t = types.get(x);
		return t;
	}
	
	
	public static ChunkType lookup(String code)
	{
		ChunkType t = parse(code);
		if(t == null)
		{
			return IGNORE;
		}
		return t;
	}
}
