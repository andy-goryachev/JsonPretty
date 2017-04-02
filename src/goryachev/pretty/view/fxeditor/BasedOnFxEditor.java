// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view.fxeditor;
import goryachev.fx.Binder;
import goryachev.fx.FX;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.Marker;
import goryachev.fx.edit.SelectionSegment;
import goryachev.pretty.CaretSpot;
import goryachev.pretty.IContentView;
import goryachev.pretty.parser.Segment;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;


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
		FX.style(textField, FX.insets(2.5, 4.5), CONTENT_TEXT);
		// TODO set single selection
		// TODO disable editing
		//textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		Binder.onChange(this::updateCaret, textField.getSelectionModel().getSelection());
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
	
	
	public void setWrapText(boolean on)
	{
		textField.setWrapText(on);
	}
	
	
	protected void updateCaret()
	{
		SelectionSegment sel = textField.getSelectionModel().getLastSegment();
		if(sel != null)
		{
			Marker m = sel.getEnd();
			String text = textField.getTextOnLine(m.getLine());
			caretSpot.set(new CaretSpot(m.getLineOffset(), text));
		}
	}
}
