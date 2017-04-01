// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import research.fx.edit.internal.CaretLocation;
import research.fx.edit.internal.Markers;


/**
 * Fx Text Editor, intended to provide capabilities missing in JavaFX, such as:
 * <pre>
 * - text editor with syntax highlighing
 * - capability to display and edit large texts (up to 2 billion rows)
 * - capability to use external models (disk, net)
 * - capability to insert arbitrary row widgets
 */
public class FxEditor
	extends Pane
{
	/** caret style */
	public static final CssStyle CARET = new CssStyle("FxEditor_CARET");
	/** selection highlight */
	public static final CssStyle HIGHLIGHT = new CssStyle("FxEditor_HIGHLIGHT");
	/** panel style */
	public static final CssStyle PANEL = new CssStyle("FxEditor_PANEL");
	
	protected final SimpleBooleanProperty editable = new SimpleBooleanProperty(false); // TODO for now
	protected final ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	protected final ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<Boolean>(true)
	{
		protected void invalidated()
		{
			requestLayout();
		}
	};
	protected final ReadOnlyObjectWrapper<Boolean> singleSelection = new ReadOnlyObjectWrapper<>();
	protected final ReadOnlyObjectWrapper<Duration> blinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	// TODO multiple selection
	// TODO caret visible
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	/** index of the topmost visible line */
	protected int topLineIndex;
	/** defines horizontal shift */
	protected int offsetx;
	/** vertical shift applied to topmost line */
	protected int offsety;
	protected Markers markers = new Markers(32);
	protected final FxEditorSelectionShapes selection;
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;

	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		setModel(m);
		FX.style(this, PANEL);
		setBackground(FX.background(Color.WHITE));
		
		selection = new FxEditorSelectionShapes(this);
		
		getChildren().addAll(selection.highlight, vscroll(), selection.caretPath);
		
		initController();
	}
	
	
	/** override to provide your own controller */
	protected void initController()
	{
		FxEditorController h = new FxEditorController(this);
		
		addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> h.handleKeyPressed(ev));
		addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> h.handleKeyReleased(ev));
		addEventFilter(KeyEvent.KEY_TYPED, (ev) -> h.handleKeyTyped(ev));
		addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> h.handleMousePressed(ev));
		addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> h.handleMouseReleased(ev));
		addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> h.handleMouseDragged(ev));
		addEventFilter(ScrollEvent.ANY, (ev) -> h.handleScroll(ev));
	}
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(this);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(this);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getModel()
	{
		return model.get();
	}
	
	
	protected ScrollBar vscroll()
	{
		if(vscroll == null)
		{
			vscroll = createVScrollBar();
		}
		return vscroll;
	}
	
	
	protected ScrollBar hscroll()
	{
		if(hscroll == null)
		{
			hscroll = createHScrollBar();
		}
		return hscroll;
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setVerticalAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	// TODO
	protected ScrollBar createHScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.HORIZONTAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		//s.valueProperty().addListener((src,old,val) -> setHAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	protected void setVerticalAbsolutePosition(double pos)
	{
		// TODO account for visible line count
		int start = FX.round(getModel().getLineCount() * pos);
		setTopLineIndex(start);
	}
	
	
	protected void scrollRelative(double pixels)
	{
		if(pixels < 0)
		{
			double toScroll = pixels;
			int ix = getViewStartLine();
			int offsety = getOffsetY();
			
			LayoutOp op = newLayoutOp();
			
			// TODO
			// using the current layout, add lines until scrolled up to the necessary number of pixels
			// or the first/last line
//			while(toScroll > 0)
//			{
//				if(ix <= 0)
//				{
//					break;
//				}
//			}
		}
		else
		{
			
		}
	}
	
	
	public boolean isWrapText()
	{
		return wrap.get();
	}
	
	
	public void setWrapText(boolean on)
	{
		wrap.set(on);
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return model.getReadOnlyProperty();
	}
	
	
	protected void layoutChildren()
	{
		layout = updateLayout(layout);
	}
	
	
	protected void setTopLineIndex(int x)
	{
		topLineIndex = x;
		requestLayout();
		// FIX update selection
	}
	
	
	protected FxEditorLayout updateLayout(FxEditorLayout prev)
	{
		if(prev != null)
		{
			prev.removeFrom(getChildren());
		}
		
		double width = getWidth();
		double height = getHeight();
		
		// position the scrollbar(s)
		ScrollBar vscroll = vscroll();
		if(vscroll.isVisible())
		{
			double w = vscroll.prefWidth(-1);
			layoutInArea(vscroll, width - w, 0, w, height, 0, null, true, true, HPos.LEFT, VPos.TOP);
		}
		
		// TODO is loaded?
		FxEditorModel m = getModel();
		int lines = m.getLineCount();
		FxEditorLayout la = new FxEditorLayout(topLineIndex, offsety);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop();
		double x0 = pad.getLeft();
		double wid = width - x0 - pad.getRight() - vscroll.getWidth(); // TODO leading, trailing components
		boolean wrap = isWrapText();
		
		for(int ix=topLineIndex; ix<lines; ix++)
		{
			Region n = m.getDecoratedLine(ix);
			n.setManaged(true);
			
			double w = wrap ? wid : n.prefWidth(-1);
			n.setMaxWidth(wrap ? wid : Double.MAX_VALUE); 
			double h = n.prefHeight(w);
			
			LineBox b = new LineBox(ix, n);
			la.add(b);
			
			layoutInArea(n, x0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > maxy)
			{
				break;
			}
		}
		
		la.addTo(getChildren());
		
		return la;
	}
	
	
	/** returns text position at the specified screen coordinates */
	public TextPos getTextPos(double screenx, double screeny)
	{
		return layout.getTextPos(screenx, screeny);
	}
	
	
	protected CaretLocation getCaretLocation(TextPos pos)
	{
		return layout.getCaretLocation(this, pos);
	}
	
	
	protected int getOffsetX()
	{
		return offsetx;
	}
	
	
	protected int getOffsetY()
	{
		return offsety;
	}
	
	
	protected int getViewStartLine()
	{
		return layout.startLine();
	}
	
	
	public ReadOnlyObjectProperty<Duration> blinkRateProperty()
	{
		return blinkRate.getReadOnlyProperty();
	}
	
	
	public Duration getBlinkRate()
	{
		return blinkRate.get();
	}
	
	
	public void setBlinkRate(Duration d)
	{
		blinkRate.set(d);
	}
	
	
	public boolean isEditable()
	{
		return editable.get();
	}
	
	
	/** enables editing.  FIX not yet editable */
	public void setEditable(boolean on)
	{
		editable.set(on);
	}


	public LayoutOp newLayoutOp()
	{
		return new LayoutOp(layout);
	}


	public void setCaretVisible(boolean on)
	{
		// TODO
	}
	
	
	
	public void eventAllChanged()
	{
		selection.clear();
		
		if(vscroll != null)
		{
			vscroll.setValue(0);
		}
		
		if(hscroll != null)
		{
			hscroll.setValue(0);
		}
		
		requestLayout();
	}


	protected void eventLinesDeleted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesInserted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesModified(int start, int count)
	{
		// FIX
		D.print(start, count);
	}
}
