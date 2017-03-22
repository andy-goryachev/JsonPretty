// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.FX;
import goryachev.pretty.parser.Type;
import javafx.scene.paint.Color;


/**
 * Color Scheme.
 */
public class ColorScheme
{
	// TODO from settings
//	public static Color commentColor = Color.RED;
//	public static Color errorColor = Color.MAGENTA;
//	public static Color ignoreColor = Color.GRAY;
//	public static Color nameColor = Color.DARKGREEN;
//	public static Color stringColor = Color.BLUE;
//	public static Color textColor = Color.BLACK;
//	public static Color valueColor = FX.rgb(0x7d2727); // FX.rgb(0x8d71cf);
	
	public static Color commentColor = Color.RED;
	public static Color errorColor = Color.MAGENTA;
	public static Color ignoreColor = Color.GRAY;
	public static Color nameColor = FX.rgb(0xa6e22e);
	public static Color stringColor = FX.rgb(0x66d9ef);
	public static Color textColor = Color.BLACK;
	public static Color valueColor = FX.rgb(0xae81ff);

	
	public static Color getColor(Type t)
	{
		switch(t)
		{
		case COMMENT:
			return commentColor;
		case ERROR:
			return errorColor;
		case IGNORE:
			return ignoreColor;
		case NAME:
		case NAME_BEGIN:
		case NAME_END:
			return nameColor;
		case STRING_BEGIN:
		case STRING:
		case STRING_END:
			return stringColor;
		case VALUE:
			return valueColor;
		}
		return textColor;
	}
}
