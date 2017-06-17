// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CButton;
import goryachev.fx.CCheckMenuItem;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fx.HPane;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.paint.Color;


/**
 * JsonPretty Window.
 */
public class MainWindow
	extends FxWindow
{
	public final MainPane pane;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		pane = new MainPane();
								
		setTitle("Pretty Print JSON/XML " + Version.VERSION);
		setTop(createMenu());
		setCenter(pane);
		setBottom(createStatusBar());
		setSize(600, 700);

		// preferences
		bind("HSPLIT", pane.horizontalSplit);
		bind("MONITOR_CLIPBOARD", pane.monitorClipboardProperty);
		
		pane.updateClipboardMonitoring();

		// debugging
		FxDump.attach(this);
	}
	
	
	protected Node createMenu()
	{
		CMenu m;
		CMenuBar mb = new CMenuBar();
		// file
		mb.add(m = new CMenu("File"));
		m.add("Save As...");
		m.separator();
		m.add("Preferences");
		m.separator();
		m.add("Quit", FX.exitAction());
		// edit
		mb.add(m = new CMenu("Edit"));
		m.add("Copy", pane.copyAction);
		m.add("Copy HTML", pane.copyHtmlAction);
		m.add("Copy RTF", pane.copyRtfAction);
		m.separator();
		m.add("Save Selection As...");
		m.separator();
		m.add("Select All", pane.view.textField.selectAllAction);
		m.separator();
		m.add(new CCheckMenuItem("Monitor Clipboard", pane.monitorClipboardProperty));
		m.add("Paste from Clipboard", pane.pasteAction);
		// view
		mb.add(m = new CMenu("View"));
		m.add(new CCheckMenuItem("Detail Pane on a Side", pane.horizontalSplit));
		// help
		mb.add(m = new CMenu("Help"));
		m.add("About");
		
//		mb.addFill();
//		mb.add(monitorClipboardCheckbox);
//		mb.add(new CButton("Paste", pasteAction));
//		return mb;
		
		CPane p = new CPane();
		p.setPadding(1);
		p.setHGap(10);
		p.addColumns
		(
			CPane.FILL,
			CPane.PREF,
			CPane.PREF
		);
		p.add(0, 0, mb);
		p.add(1, 0, pane.monitorClipboardCheckbox);
		p.add(2, 0, new CButton("Paste", pane.pasteAction));
		
		return p;
	}
	
	
	protected Node createStatusBar()
	{
		HPane p = new HPane();
		p.fill();
		p.add(FX.label("copyright © 2017 andy goryachev", Color.GRAY, new Insets(1, 10, 1, 2)));
		return p;
	}
}