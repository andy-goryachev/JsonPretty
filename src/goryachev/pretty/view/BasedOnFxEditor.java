// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.pretty.IContentView;
import goryachev.pretty.parser.Segment;
import java.util.List;
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

	
	public BasedOnFxEditor()
	{
		model = new SegmentEditorModel();
		
		textField = new FxEditor(model);
		// TODO disable editing
		//textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
	}


	public Node getNode()
	{
		return textField;
	}


	public void setParsedSegments(List<Segment> formatted)
	{
		model.setSegments(formatted);
	}
}
