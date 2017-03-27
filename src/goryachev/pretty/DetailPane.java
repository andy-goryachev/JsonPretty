// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CPane;
import goryachev.pretty.view.BasedOnTextFlow;
import goryachev.pretty.view.fxeditor.BasedOnFxEditor;
import javafx.scene.web.WebView;


/**
 * Detail Pane.
 */
public class DetailPane
	extends CPane
{
	public final IContentView view;
	
	
	public DetailPane()
	{
		view = Config.USE_FX_EDITOR ? new BasedOnFxEditor() : new BasedOnTextFlow();
		
		setCenter(view.getNode());
	}
}
