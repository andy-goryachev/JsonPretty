// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;


/**
 * Analysis Report.
 */
public class AnalysisReport
{
	private final CMap<String,Object> rep = new CMap();
	
	
	public AnalysisReport()
	{
	}


	public CList<String> getSectionNames()
	{
		return rep.keys();
	}
	
	
	public Object getSection(String name)
	{
		return rep.get(name);
	}
	
	
	public void addSection(String name, String[] lines)
	{
		rep.put(name, lines);
	}
	
	
	public void addSection(String name, String line)
	{
		rep.put(name, line);
	}
}