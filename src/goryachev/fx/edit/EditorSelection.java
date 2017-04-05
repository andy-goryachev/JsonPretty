// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * An immutable object that represents text selection within FxEditor.
 */
public class EditorSelection
{
	public static final EditorSelection EMPTY = createEmpty();
	private final SelectionSegment[] segments;
	
	
	public EditorSelection(SelectionSegment[] segments)
	{
		this.segments = segments;
	}
	
	
	private static EditorSelection createEmpty()
	{
		Marker m = new Marker(0, 0, true);
		return new EditorSelection(new SelectionSegment[] { new SelectionSegment(m, m) });
	}


	/** returns original segment array */
	public SelectionSegment[] getSegments()
	{
		return segments;
	}
	
	
	/** returns last or the only selection segment */
	public SelectionSegment getSegment()
	{
		return segments[segments.length - 1];
	}
	
	
	public int getSegmentCount()
	{
		return segments.length;
	}
	
	
	public boolean isEmpty()
	{
		for(SelectionSegment s: segments)
		{
			if(!s.isEmpty())
			{
				return false;
			}
		}
		return false;
	}
	
	
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
	
	
	public boolean hasMultipleSegments()
	{
		return segments.length > 1;
	}
}
