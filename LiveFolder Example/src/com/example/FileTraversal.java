package com.example;

import java.io.File;
import java.io.IOException;

public class FileTraversal 
{
	public final void traverse(final File value) throws IOException
	{
		if(value.isDirectory())
		{
			onDirectory(value);
			final File[] files = value.listFiles();
			for(int i=0;i<files.length;i++)
			{
				traverse(files[i]);
			}
			return;
		}
		onFile(value);
	}
	
	public void onDirectory(final File dir){};
	public void onFile(final File file){};
}
