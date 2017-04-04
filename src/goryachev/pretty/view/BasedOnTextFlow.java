// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CList;
import goryachev.fx.Binder;
import goryachev.fx.FX;
import goryachev.fx.edit.StyledTextPane;
import goryachev.pretty.CaretSpot;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.IContentView;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


/**
 * IContentView based on FX TextFlow.
 * 
 * (to be replaced with FxEditor)
 */
public class BasedOnTextFlow
	implements IContentView
{
	public final StyledTextPane textField;
	public final ScrollPane scroll;
	private final ReadOnlyObjectWrapper<CaretSpot> caretSpot = new ReadOnlyObjectWrapper();

	
	public BasedOnTextFlow()
	{
		textField = new StyledTextPane();
		Binder.onChange(this::updateCaret, textField.selectionIndexProperty());
		FX.style(textField, FX.insets(2.5, 4.5), CONTENT_TEXT);
		textField.setPrefWidth(Region.USE_COMPUTED_SIZE);
		
		scroll = new ScrollPane(textField);
		FX.style(scroll, CONTENT_PANE);
	}
	
	
	protected void updateCaret()
	{
		int ix = textField.selectionIndexProperty().get();
		String text = textField.getText();
		caretSpot.set(new CaretSpot(ix, text));
	}
	
	
	public ReadOnlyObjectProperty<CaretSpot> caretSpotProperty()
	{
		return caretSpot.getReadOnlyProperty();
	}


	public Node getNode()
	{
		return scroll;
	}


	public void setTextSegments(List<Segment> formatted)
	{
		CList<Node> rv = new CList<>(formatted.size());
		for(Segment c: formatted)
		{
			rv.add(createText(c.getText(), c.getType()));
		}
		
		textField.setText(rv);
		scroll.setVvalue(0);
	}
	
	
	protected Text createText(String text, Type type)
	{
		Text t = new Text(text);
		t.setFill(ColorScheme.getColor(type));
		return t;
	}
	
	
	public void setWrapText(boolean on)
	{
		scroll.setFitToWidth(on);
	}
}
