// Copyright (c) 2010-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;


public enum ChunkType
{
	ATTR, // html tag attribute
	COMMENT,
	DELIMITER,
	ERROR,
	IDENTIFIER,
	IGNORE,
	KEYWORD,
	LINEBREAK,
	NORMAL,
	NUMBER,
	PUNCT,
	SEPARATOR,
	SCRIPT,
	STRING,
	TAG,
	TAG_PART,
	TERMINATOR, // prompt terminator symbol (; in localizable strings)
	TEXT,
	VALUE, // html tag value
	WHITESPACE
	;
}
