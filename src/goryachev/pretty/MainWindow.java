// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxButton;
import goryachev.fx.FxCheckMenuItem;
import goryachev.fx.FxDump;
import goryachev.fx.FxMenuBar;
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
		FxMenuBar m = new FxMenuBar();
		// file
		m.menu("File");
		m.item("Save As...");
		m.separator();
		m.item("Preferences");
		m.separator();
		m.item("Quit", FX.exitAction());
		// edit
		m.menu("Edit");
		m.item("Copy", pane.copyAction);
		m.item("Copy HTML", pane.copyHtmlAction);
		m.item("Copy RTF", pane.copyRtfAction);
		m.separator();
		m.item("Save Selection As...");
		m.separator();
		m.item("Select All", pane.view.textField.selectAllAction);
		m.separator();
		m.add(new FxCheckMenuItem("Monitor Clipboard", pane.monitorClipboardProperty));
		m.item("Paste from Clipboard", pane.pasteAction);
		// view
		m.menu("View");
		m.add(new FxCheckMenuItem("Detail Pane on a Side", pane.horizontalSplit));
		// help
		m.menu("Help");
		m.item("About");
		
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
		p.add(0, 0, m);
		p.add(1, 0, pane.monitorClipboardCheckbox);
		p.add(2, 0, new FxButton("Paste", pane.pasteAction));
		
		return p;
	}
	
	
	protected Node createStatusBar()
	{
		HPane p = new HPane();
		p.fill();
		p.add(FX.label("copyright © 2018 andy goryachev", Color.GRAY, new Insets(1, 10, 1, 2)));
		return p;
	}
}