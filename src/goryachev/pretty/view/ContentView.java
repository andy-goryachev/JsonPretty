// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.Log;
import goryachev.fx.Binder;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.Marker;
import goryachev.fx.edit.SelectionSegment;
import goryachev.pretty.CaretSpot;
import goryachev.pretty.parser.Segment;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.input.DataFormat;


/**
 * Content View based on FxEditor.
 */
public class ContentView
{
	public static CssStyle CONTENT_PANE = new CssStyle("ContentView_CONTENT_PANE");
	public static CssStyle CONTENT_TEXT = new CssStyle("ContentView_CONTENT_TEXT");

	public final FxEditor textField;
	public final SegmentEditorModel model;
	private final ReadOnlyObjectWrapper<CaretSpot> caretSpot = new ReadOnlyObjectWrapper();
	
	
	public ContentView()
	{
		model = new SegmentEditorModel();
		
		textField = new FxEditor(model);
		textField.setContentPadding(FX.insets(2.5, 4.5));
		FX.style(textField, CONTENT_TEXT);
		// TODO set single selection
		// TODO disable editing
		//textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		Binder.onChange(this::updateCaret, textField.selectionProperty());
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

	
	public void setWrapText(boolean on)
	{
		textField.setWrapText(on);
	}
	
	
	protected void updateCaret()
	{
		SelectionSegment sel = textField.getSelection().getSegment();
		if(sel != null)
		{
			String text;
			int pos;
			
			if(sel.isEmpty())
			{
				Marker m = sel.getCaret();
				text = textField.getTextOnLine(m.getLine());
				pos = m.getLineOffset();
			}
			else
			{
				try
				{
					text = textField.getSelectedText();
				}
				catch(Exception e)
				{
					Log.ex(e);
					text = "";
				}
				
				pos = 0;
			}
			
			caretSpot.set(new CaretSpot(pos, text));
		}
	}


	public void copy()
	{
		textField.copy();
	}
	
	
	public void copyHtml()
	{
		textField.copy(null, DataFormat.HTML);
	}
	
	
	public void copyRtf()
	{
		textField.copy(null, DataFormat.RTF);
	}
}
