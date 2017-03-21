// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CPane;
import javafx.scene.web.WebView;


/**
 * Detail Pane.
 */
public class DetailPane
	extends CPane
{
	public final WebView view;
	
	
	public DetailPane()
	{
		view = new WebView();
		
		setCenter(view);
	}
}
