// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CList;
import goryachev.fx.FX;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.IContentView;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * IContentView based on FX TextFlow.
 * 
 * (to be replaced with FxEditor)
 */
public class BasedOnTextFlow
	implements IContentView
{
	public final TextFlow textField;
	public final ScrollPane scroll;

	
	public BasedOnTextFlow()
	{
		textField = new TextFlow();
		FX.style(textField, FX.insets(2.5, 4.5));
		textField.setPrefWidth(Region.USE_COMPUTED_SIZE);
		// unnecessary
		//textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		
		scroll = new ScrollPane(textField);
		FX.style(scroll, CONTENT_PANE);
	}


	public Node getNode()
	{
		return scroll;
	}


	public void setParsedSegments(List<Segment> formatted)
	{
		CList<Node> rv = new CList<>(formatted.size());
		for(Segment c: formatted)
		{
			rv.add(createText(c.getText(), c.getType()));
		}
		
		textField.getChildren().setAll(rv);
		scroll.setVvalue(0);
	}
	
	
	protected Text createText(String text, Type type)
	{
		Text t = new Text(text);
		t.setFill(ColorScheme.getColor(type));
		return t;
	}
}
