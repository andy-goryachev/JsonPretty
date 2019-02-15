// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.edit.FxEditor;
import goryachev.pretty.analysis.AnalysisReport;


/**
 * Detail Pane.
 */
public class DetailPane
	extends CPane
{
	public static final CssStyle PANE = new CssStyle("DetailPane_PANE");
	public final DetailModel model;
	public final FxEditor textField;
	
	
	public DetailPane()
	{
		FX.style(this, PANE);
		
		model = new DetailModel();
		
		textField = new FxEditor(model);
		textField.setDisplayCaret(false);
		textField.setWordWrap(true);
		textField.setContentPadding(FX.insets(2.5, 4.5));
		
		setCenter(textField);
	}


	public void setReport(AnalysisReport rep)
	{
		model.setReport(rep);
	}


	public void clear()
	{
		model.setReport(null);
	}
}
