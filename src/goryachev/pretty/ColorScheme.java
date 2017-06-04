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
	
	// too light
//	private Color commentColor = Color.RED;
//	private Color errorColor = Color.MAGENTA;
//	private Color ignoreColor = Color.GRAY;
//	private Color nameColor = FX.rgb(0xa6e22e);
//	private Color stringColor = FX.rgb(0x66d9ef);
//	private Color textColor = Color.BLACK;
//	private Color valueColor = FX.rgb(0xae81ff);
	
	private Color commentColor = Color.RED;
	private Color errorColor = Color.MAGENTA;
	private Color ignoreColor = Color.GRAY;
	private Color nameColor = FX.rgb(0xa6e22e);
	private Color stringColor = FX.rgb(0x66d9ef);
	private Color textColor = Color.BLACK;
	private Color valueColor = FX.rgb(0xae81ff);
	private Color xmlTagColor = FX.rgb(0xa6e22e); //Color.GREEN; //FX.rgb(0xae81ff);

	
	//
	
	private final String name;
	private static ColorScheme current;
	
	
	public ColorScheme(String name)
	{
		this.name = name;
	}
	
	
	private static ColorScheme getScheme()
	{
		if(current == null)
		{
			// TODO from settings
			current = createSolarizedScheme();
		}
		return current;
	}
	
	
	private static ColorScheme createSolarizedScheme()
	{
		ColorScheme s = new ColorScheme("Solarized");
		s.commentColor = FX.rgb(0xdc322f);; // red
		s.errorColor = FX.rgb(0xd33682); // magenta
		s.ignoreColor = FX.rgb(0x657b83); // base00
		s.nameColor = FX.rgb(0x859900); // green
		s.stringColor = FX.rgb(0x6c71c4); // violet
		s.textColor = FX.rgb(0x002b36); // base03
		s.valueColor = FX.rgb(0xcb4b16); // orange
		return s;
	}

	
	public static Color getColor(Type t)
	{
		ColorScheme s = getScheme();
		
		switch(t)
		{
		case COMMENT:
		case XML_COMMENT:
			return s.commentColor;
		case ERROR:
			return s.errorColor;
		case IGNORE:
			return s.ignoreColor;
		case NAME:
		case NAME_BEGIN:
		case NAME_END:
			return s.nameColor;
		case STRING_BEGIN:
		case STRING:
		case STRING_END:
			return s.stringColor;
		case VALUE:
			return s.valueColor;
		case XML_TAG_CLOSING:
		case XML_TAG_EMPTY:
		case XML_TAG_OPEN:
			return s.xmlTagColor;
		}
		return s.textColor;
	}
	
	
	public static Color getDetailColor(boolean heading)
	{
		ColorScheme s = getScheme();
		return heading ? s.nameColor : s.textColor;
	}
}
