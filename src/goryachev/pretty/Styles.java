// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CommonStyles;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.Theme;
import goryachev.pretty.view.ContentView;


/**
 * Application style sheet.
 */
public class Styles
	extends FxStyleSheet
{
	public Styles()
	{
		Theme theme = Theme.current();
		
		add
		(
			new CommonStyles(),
			
			new Selector(ContentView.CONTENT_TEXT).defines
			(
				fontSize("100%")
			),
			
			new Selector(DetailPane.PANE).defines
			(
				fontFamily("monospace")
			)
			
//			new Selector(".menu").defines
//			(
//				padding(spaces(2, 5, 2, 5))
//			),
//			
//			new Selector(".menu .button").defines
//			(
//				padding(spaces(2, 5, 2, 5))
//			)
		);
	}
}
