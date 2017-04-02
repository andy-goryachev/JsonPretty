// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CPane;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.view.BasedOnTextFlow;
import goryachev.pretty.view.fxeditor.BasedOnFxEditor;
import java.util.List;


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
		view.setDisplayCaret(false);
		view.setWrapText(true);
		
		setCenter(view.getNode());
	}


	public void setTextSegments(List<Segment> segments)
	{
		view.setTextSegments(segments);
	}
}
