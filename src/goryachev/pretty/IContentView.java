// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CssStyle;
import goryachev.pretty.parser.Segment;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;


/**
 * Displays parsed text.
 */
public interface IContentView
{
	public static CssStyle CONTENT_PANE = new CssStyle("IContentView_CONTENT_PANE");
	public static CssStyle CONTENT_TEXT = new CssStyle("IContentView_CONTENT_TEXT");
	
	
	/** returns the view FX node */
	public Node getNode();
	
	
	/** replaces current content with the list of parsed and formatted segments */
	public void setTextSegments(List<Segment> segments);
	
	
	public ReadOnlyObjectProperty<CaretSpot> caretSpotProperty();


	public void setCaretVisible(boolean on);
	
	
	public void setWrapText(boolean on);
}
