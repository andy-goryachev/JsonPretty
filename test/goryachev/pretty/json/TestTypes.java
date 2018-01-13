// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.pretty.parser.Type;


/**
 * Test Types.
 */
public interface TestTypes
{
	public static final Type AB = Type.ARRAY_BEGIN;
	public static final Type AE = Type.ARRAY_END;
	public static final Type CA = Type.COMMA_ARRAY;
	public static final Type CO = Type.COMMA;
	public static final Type ER = Type.ERROR;
	public static final Type IG = Type.IGNORE;
	public static final Type IN = Type.INDENT;
	public static final Type LB = Type.LINEBREAK;
	public static final Type NA = Type.NAME;
	public static final Type NB = Type.NAME_BEGIN;
	public static final Type NE = Type.NAME_END;
	public static final Type OB = Type.OBJECT_BEGIN;
	public static final Type OE = Type.OBJECT_END;
	public static final Type SP = Type.SEPARATOR;
	public static final Type ST = Type.STRING;
	public static final Type SB = Type.STRING_BEGIN;
	public static final Type SE = Type.STRING_END;
	public static final Type VA = Type.VALUE;
	public static final Type WS = Type.WHITESPACE;
	public static final Type XB = Type.XML_TAG_OPEN;
	public static final Type XC = Type.XML_COMMENT;
	public static final Type XE = Type.XML_TAG_CLOSING;
	public static final Type XN = Type.XML_TAG_EMPTY;
	public static final Type XT = Type.XML_TEXT;
}
