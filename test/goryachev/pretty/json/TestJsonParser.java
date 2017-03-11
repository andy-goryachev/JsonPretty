// Copyright (c) 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.json;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.pretty.parser.Chunk;
import goryachev.pretty.parser.ChunkType;
import goryachev.pretty.parser.ResilientJsonParser;
import java.util.List;


public class TestJsonParser
{
	public static final ChunkType I = ChunkType.IDENTIFIER;
	public static final ChunkType L = ChunkType.LINEBREAK;
	public static final ChunkType V = ChunkType.VALUE;
	public static final ChunkType X = ChunkType.IGNORE;
	
	
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	public static void t(Object ... parts) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<parts.length; i++)
		{
			sb.append((String)parts[++i]);
		}
		String text = sb.toString();
		
		ResilientJsonParser p = new ResilientJsonParser(text);
		p.parse();
		List<Chunk> chunks = p.getParsedDocument().getChunks();
		
		int sz = parts.length / 2;
		Chunk[] expected = new Chunk[sz];
		int ix = 0;
		for(int i=0; i<sz; i++)
		{
			ChunkType type = (ChunkType)parts[ix++];
			String part = (String)parts[ix++];
			expected[i] = new Chunk(type, part);
		}
		
		if(!CKit.equals(chunks, expected))
		{
			TF.print("FAIL");
			TF.print("parsed:");
			TF.list(chunks);
			TF.print("expected:");
			TF.list(expected);
			throw new Exception();
		}
	}
	
	
	@Test
	public void testParser() throws Exception
	{
		t(X, "{", L, "\n", X, " [ \"", I, "I \\\" I", X, "\": ", V, "1", X, " ] }", L, "\n");
		t(X, "{", L, "\n", X, " [ \"", I, "I \\\" I", X, "\": \"", I, "V \\\" V", X, "\" ] }", L, "\n");
		t(X, "{", L, "\n", X, "\"", I, "I \\\" I", X, "\": \"", I, "V \\\" V", X, "\" }", L, "\n");
		t(X, "{", L, "\n", X, "\"", I, "I I", X, "\": \"", I, "V V", X, "\" }", L, "\n");
		t(X, "{", L, "\n", X, "\"", I, "I", X, "\": \"", I, "V", X, "\" }", L, "\n");
	}
}
