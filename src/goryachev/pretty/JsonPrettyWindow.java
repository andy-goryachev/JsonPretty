// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;


/**
 * JsonPrettyWindow.
 */
public class JsonPrettyWindow
	extends FxWindow
{
	public JsonPrettyWindow()
	{
		super("JsonPrettyWindow");
		
		setTitle("Pretty Print JSON/XML " + Version.VERSION);
//		setCenter(ed);
		setSize(600, 700);
		
		FxDump.attach(this);
	}
}