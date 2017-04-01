// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * FxEditor Controller.
 */
public class FxEditorController
{
	protected final FxEditor editor;
	protected boolean dragging;


	public FxEditorController(FxEditor ed)
	{
		this.editor = ed;
	}
	
	
	public void moveCaret(boolean right)
	{
		// TODO
	}


	protected void handleKeyPressed(KeyEvent ev)
	{
		switch(ev.getCode())
		{
		case PAGE_DOWN:
			editor.scrollRelative(editor.getHeight());
			break;
		case LEFT:
			moveCaret(false);
			break;
		case PAGE_UP:
			editor.scrollRelative(-editor.getHeight());
			break;
		case RIGHT:
			moveCaret(true);
			break;
		}
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected TextPos getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		return editor.getTextPos(x, y);
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
			
		// TODO property: multiple selection
		TextPos pos = getTextPos(ev);
		
		if(ev.isShiftDown())
		{
			// FIX there might be a zero length segment (single caret)
			
			// expand selection from the anchor point to the current position
			// clearing existing (possibly multiple) selection
			editor.selection.clearAndExtendLastSegment(pos);
		}
		else if(ev.isShortcutDown())
		{
			if(editor.selection.isInsideSelection(pos))
			{
				// replace selection with a single caret
				editor.selection.clear();
				editor.selection.addSegment(pos, pos);
			}
			else
			{
				// FIX add a new caret
				editor.selection.addSegment(pos, pos);
			}
		}
		else
		{
			editor.selection.clear();
			if(pos != null)
			{
				editor.selection.addSegment(pos, pos);
			}
		}
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
		
		dragging = true;
		
		TextPos pos = getTextPos(ev);
		editor.selection.extendLastSegment(pos);
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
		
		dragging = false;
		
		D.print(editor.selection); // FIX
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		// TODO mouse wheel scroll
		D.print(ev);
	}
}