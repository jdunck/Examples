package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class StreamUtil 
{
	public interface CopyProgressListener
	{
		void onProgress(String key, int value);
	}
	
	public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException
	{
		if(bufferSize < 1)
			bufferSize=32*1024;
		
		byte[] buffer=new byte[bufferSize];
		int br=-1;
		while(true)
		{
			br = input.read(buffer);
			if(br < 0 ) break;
			output.write(buffer,0,br);
		}
	}
	
	public static void copy(InputStream input, OutputStream output, int bufferSize, String key, CopyProgressListener listener) 
	throws IOException
	{
		if(bufferSize < 1)
			bufferSize=32*1024;
		
		byte[] buffer=new byte[bufferSize];
		int br=-1;
		int tbr = 0;
		boolean canAlert = listener == null?false:true;
		while(true)
		{
			br = input.read(buffer);
			if(br < 0 ) break;
			tbr+=br;
			output.write(buffer,0,br);
			if(canAlert) listener.onProgress(key, tbr);
		}
	}
		
	public static void deleteFiles(String businessEnd)
	{
		if(!businessEnd.startsWith("/"))
			businessEnd = "/" + businessEnd;
		File path = new File(String.format("%s%s",sdcardPrefix,businessEnd));
		final ArrayList<File> files = new ArrayList<File>();
		final ArrayList<File> dirs = new ArrayList<File>();
		FileTraversal ft = new FileTraversal()
		{
			@Override
			public void onDirectory(File dir) 
			{
				dirs.add(dir);
			}

			@Override
			public void onFile(File file) 
			{
				files.add(file);
			}		
		};
		try
		{
			ft.traverse(path);
			Iterator<File> iterf = files.iterator();
			Iterator<File> iterd = dirs.iterator();
			while(iterf.hasNext())
			{
				iterf.next().delete();
				iterf.remove();
			}
			while(iterd.hasNext())
			{
				iterd.next().delete();
				iterd.remove();
			}
		}
		catch(IOException e)
		{
		}
	}
	
	private static final String sdcardPrefix = "sdcard/com_example_gtug";
	public static File constructFile(String businessEnd)
	{
		File dir = new File(sdcardPrefix);
		if(!dir.exists()) dir.mkdirs();
		if(!businessEnd.startsWith("/"))
			businessEnd = "/"+businessEnd;
		File retval = new File(String.format("%s%s",sdcardPrefix,businessEnd));
		File tmp = retval.getParentFile();
		if(!tmp.exists())
			tmp.mkdirs();
		return retval;
	}
}