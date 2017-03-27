// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view.fxeditor;
import goryachev.fx.FX;
import goryachev.pretty.CaretSpot;
import goryachev.pretty.IContentView;
import goryachev.pretty.parser.Segment;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import research.fx.edit.FxEditor;


/**
 * IContentView based on FxEditor.
 */
public class BasedOnFxEditor
	implements IContentView
{
	public final FxEditor textField;
	public final SegmentEditorModel model;
	private final ReadOnlyObjectWrapper<CaretSpot> caretSpot = new ReadOnlyObjectWrapper();
	
	
	public BasedOnFxEditor()
	{
		model = new SegmentEditorModel();
		
		textField = new FxEditor(model);
		FX.style(textField, FX.insets(2.5, 4.5));
		// TODO disable editing
		//textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		// TODO selection listener
	}


	public Node getNode()
	{
		return textField;
	}


	public void setTextSegments(List<Segment> formatted)
	{
		model.setSegments(formatted);
	}


	public ReadOnlyObjectProperty<CaretSpot> caretSpotProperty()
	{
		return caretSpot.getReadOnlyProperty();
	}


	public void setCaretVisible(boolean on)
	{
		textField.setCaretVisible(on);
	}
}
