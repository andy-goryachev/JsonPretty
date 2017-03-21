// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.D;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import research.fx.edit.CTextFlow;
import research.fx.edit.TextPos;


/**
 * Mouse Controller.
 */
public class MouseController
{
	public static final CssStyle CARET = new CssStyle("MouseController_CARET");
	protected final ReadOnlyIntegerWrapper selectionIndex = new ReadOnlyIntegerWrapper(-1);
	protected final CTextFlow editor;
	protected final Path caret;
	protected final Timeline caretTimeline;


	public MouseController(CTextFlow ed)
	{
		this.editor = ed;
		
		caret = new Path();
		FX.style(caret, CARET);
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
		
		ed.getChildren().add(caret);
		
		caretTimeline = new Timeline();
		caretTimeline.setCycleCount(Animation.INDEFINITE);
		updateBlinkRate(Duration.millis(500));
		
		ed.addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> handleKeyPressed(ev));
		ed.addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> handleKeyReleased(ev));
		ed.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyTyped(ev));
		ed.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> handleMousePressed(ev));
		ed.addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> handleMouseReleased(ev));
		ed.addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
	}
	

	protected void handleKeyPressed(KeyEvent ev)
	{
		// TODO
//		switch(ev.getCode())
//		{
//		case PAGE_DOWN:
//		}
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void updateBlinkRate(Duration d)
	{
		Duration period = d.multiply(2);
		
		caretTimeline.stop();
		caretTimeline.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretTimeline.play();
	}
	
	
	protected TextPos getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		Point2D p = editor.screenToLocal(x, y);
		return editor.getTextPos(0, p.getX(), p.getY());
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		TextPos pos = getTextPos(ev);
		int ix = pos.getInsertionIndex();
		D.print(ix);
		setCaret(ix);
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
	}
	
	
	public void setCaret(int ix)
	{
		if(ix < 0)
		{
			caret.getElements().clear();
			setCaretVisible(false);
		}
		else
		{
			if(caret.getParent() == null)
			{
				editor.getChildren().add(caret);
			}
			
			PathElement[] es = editor.getCaretShape(ix, true);
			caret.getElements().setAll(es);
			setCaretVisible(true);
		}
		selectionIndex.set(ix);
	}
	
	
	protected void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
	public ReadOnlyIntegerProperty selectionIndexProperty()
	{
		return selectionIndex.getReadOnlyProperty();
	}
}