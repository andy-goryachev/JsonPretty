// Copyright © 2010-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.parser;


public enum Type
{
	ARRAY_BEGIN,
	ARRAY_END,
	COMMA,
	COMMA_ARRAY,
	COMMENT,
	ERROR,
	IGNORE,
	INDENT,
	LINEBREAK,
	NAME,
	NAME_BEGIN,
	NAME_END,
	OBJECT_BEGIN,
	OBJECT_END,
	SEPARATOR,
	STRING,
	STRING_BEGIN,
	STRING_END,
	TEXT,
	VALUE,
	WHITESPACE,
	XML_COMMENT,
	XML_TAG_CLOSING,
	XML_TAG_EMPTY,
	XML_TAG_OPEN,
	XML_TEXT
}
