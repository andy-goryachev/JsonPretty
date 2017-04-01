// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;
import research.fx.edit.internal.CaretLocation;
import research.fx.edit.internal.Markers;


/**
 * FxEditor Layout.
 */
public class FxEditorLayout
{
	private final int offsety;
	private final int topLine;
	private final CList<LineBox> lines = new CList<>();
	

	public FxEditorLayout(int topLine, int offsety)
	{
		this.topLine = topLine;
		this.offsety = offsety;
	}
	
	
	public CList<LineBox> lines()
	{
		return lines;
	}
	
	
	/** returns text position at the screen coordinates, or null */
	public Marker getTextPos(double screenx, double screeny, Markers markers)
	{
		for(LineBox line: lines)
		{
			Region box = line.getBox();
			Point2D p = box.screenToLocal(screenx, screeny);
			double y = p.getY();
			
			if(y > 0)
			{
				if(y < box.getHeight())
				{
					if(box instanceof CTextFlow)
					{
						CHitInfo hit = ((CTextFlow)box).getHit(p.getX(), y);
						if(hit != null)
						{
							return markers.newMarker(line.getLineNumber(), hit.getIndex(), hit.isLeading());
						}
					}
				}
			}
			else
			{
				break;
			}
		}
		return null;
	}
	
	
	public LineBox getLineBox(int line)
	{
		line -= topLine;
		if((line >= 0) && (line < lines.size()))
		{
			return lines.get(line);
		}
		return null;
	}
	
	
	public CaretLocation getCaretLocation(Region parent, Marker pos)
	{
		if(pos != null)
		{
			LineBox b = getLineBox(pos.getLine());
			if(b != null)
			{
				Region box = b.getBox();
				if(box instanceof CTextFlow)
				{
					PathElement[] es = ((CTextFlow)box).getCaretShape(pos.getLineOffset(), pos.isLeading());
					if(es != null)
					{
						return EditorTools.translateCaretLocation(parent, box, es);
					}
				}
			}
		}
		return null;
	}
	

	protected void addLineBox(LineBox b)
	{
		lines.add(b);
	}
	
	
	public void removeFrom(Pane p)
	{
		ObservableList<Node> cs = p.getChildren();
		for(LineBox b: lines)
		{
			cs.remove(b.getBox());
		}
	}


	public int startLine()
	{
		return topLine;
	}


	public int getVisibleLineCount()
	{
		return lines.size();
	}
}