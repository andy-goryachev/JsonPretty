// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CssStyle;
import goryachev.pretty.parser.Segment;
import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Node;


/**
 * Displays parsed text.
 */
public interface IContentView
{
	public static CssStyle CONTENT_PANE = new CssStyle("IContentView_CONTENT_PANE");
	
	
	/** returns the view FX node */
	public Node getNode();
	
	
	/** replaces current content with the list of parsed and formatted segments */
	public void setParsedSegments(List<Segment> segments);
	
	
	public ReadOnlyIntegerProperty selectionIndexProperty();


	public String getLineAtCaret();
}
