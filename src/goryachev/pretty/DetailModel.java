// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.fxeditor.CTextFlow;
import goryachev.fxeditor.Edit;
import goryachev.fxeditor.FxEditorModel;
import goryachev.fxeditor.FxEditorModel.LoadInfo;
import goryachev.text.analysis.AnalysisReport;
import goryachev.fxeditor.LineBox;
import javafx.geometry.Insets;
import javafx.scene.text.Text;


/**
 * Detail Model.
 */
public class DetailModel
	extends FxEditorModel
{
	private final CList<Line> lines = new CList();
	private static final Insets HEADING_INSETS = new Insets(0, 0, 0, 0);
	private static final Insets LINE_INSETS = new Insets(0, 0, 0, 20);
	
	
	public DetailModel()
	{
	}
	
	
	public void setReport(AnalysisReport rep)
	{
		if(rep == null)
		{
			lines.clear();
		}
		else
		{
			CList<String> names = rep.getSectionNames();
			CSorter.sort(names);
			
			CList<Line> rv = new CList<>();
			for(String k: names)
			{
				Object x = rep.getSection(k);
				if(x != null)
				{
					rv.add(new Line(true, k));
					
					if(x instanceof String)
					{
						rv.add(new Line(false, (String)x));
					}
					else if(x instanceof String[])
					{
						for(String s: (String[])x)
						{
							rv.add(new Line(false, s));
						}
					}
				}
			}
			
			lines.setAll(rv);
		}
		
		fireAllChanged();
	}


	public LoadInfo getLoadInfo()
	{
		return null;
	}


	public int getLineCount()
	{
		return lines.size();
	}
	
	
	protected Line getLine(int ix)
	{
		return lines.get(ix);
	}


	public String getPlainText(int ix)
	{
		return getLine(ix).text;
	}


	public LineBox getLineBox(int ix)
	{
		Line line = getLine(ix);
		
		Text t = new Text(line.text);
		t.setFill(ColorScheme.getDetailColor(line.heading));
		
		LineBox b = new LineBox();
		CTextFlow flow = b.flow();
		flow.add(t);
		flow.setPadding(line.heading ? HEADING_INSETS : LINE_INSETS);
		return b;
	}
	

	public Edit edit(Edit ed) throws Exception
	{
		throw new Exception();
	}
	
	
	//
	
	
	protected static class Line
	{
		public final boolean heading;
		public final String text;
		
		
		public Line(boolean heading, String text)
		{
			this.heading = heading;
			this.text = text;
		}
	}
}
