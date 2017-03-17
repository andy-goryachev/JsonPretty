// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
import goryachev.fx.FxStyleSheet;
import javafx.scene.paint.Color;


/**
 * ReqTraq style sheet.
 */
public class Styles
	extends FxStyleSheet
{
	public Styles()
	{
		// TODO themes
		
		add
		(
			// basic styles
			new Selector(".root").defines
			(
				prop("-fx-accent", Color.RED),
				prop("-fx-focus-color", Color.RED),
				prop("-fx-faint-focus-color", Color.BLACK)
			),
			
			new Selector(".text").defines
			(
				prop("-fx-font-smoothing-type", "gray")
			),
			
			new Selector(".scroll-pane > .viewport").defines
			(
				backgroundColor(Color.WHITE)
			),
			
			// common fx styles
			new CommonStyles()
		);
	}
}
