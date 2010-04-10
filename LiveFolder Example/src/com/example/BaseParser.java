package com.example;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public abstract class BaseParser 
{
	public abstract void Parse(String xmldata);

	public abstract void Parse(InputStream value);

	public abstract void Parse(Reader value);

	public abstract void Parse(URL uri);
}