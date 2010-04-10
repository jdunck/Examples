package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.TextElementListener;
import android.util.Xml;
import android.util.Xml.Encoding;

public class BuzzParser extends BaseParser {
	public interface OnParsedListener {
		void onParsed();

		void onException(Exception e);
	}

	public static final class Item {
		public String title;
		public String description;

		public Item copy() {
			Item newvalue = new Item();
			newvalue.description = this.description;
			newvalue.title = this.title;
			return newvalue;
		}
	}

	static final String ROOT = "feed";
	static final String ITEM = "entry";
	static final String TITLE = "title";
	static final String DESC = "content";

	private RootElement feed;
	private final ArrayList<Item> items;
	private final Item currentItem;

	public BuzzParser() 
	{
		currentItem = new Item();
		items = new ArrayList<Item>();

		feed = new RootElement(ROOT);

		Element item = feed.getChild(ITEM);
		item.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end() 
			{
				items.add(currentItem.copy());
			}
		});
		item.getChild(TITLE).setTextElementListener(new TextElementListener()
		{
			@Override
			public void start(Attributes attributes) {}

			@Override
			public void end(String body) 
			{
				currentItem.title=body;
			}
		});
		item.getChild(DESC).setTextElementListener(new TextElementListener()
		{
			@Override
			public void start(Attributes attributes) {}

			@Override
			public void end(String body) 
			{
				currentItem.description=body;
			}
		});
	}

	public List<Item> getItems() 
	{
		return items;
	}

	public void Parse(String xmldata) 
	{
		Parse(xmldata, null);
	}

	public void Parse(String xmldata, OnParsedListener listener) 
	{
		items.clear();
		try {
			Xml.parse(xmldata, feed.getContentHandler());
			if (listener != null)
				listener.onParsed();
		} catch (SAXException e) {
			if (listener != null)
				listener.onException(e);
		}
	}

	public void Parse(InputStream value) 
	{
		Parse(value, null);
	}

	public void Parse(InputStream value, OnParsedListener listener) 
	{
		items.clear();
		try {
			Xml.parse(value, Encoding.UTF_8, feed.getContentHandler());
			if (listener != null)
				listener.onParsed();
		} catch (SAXException e) {
			if (listener != null)
				listener.onException(e);
		} catch (IOException e) {
			if (listener != null)
				listener.onException(e);
		}
	}

	public void Parse(Reader value) 
	{
		Parse(value, null);
	}

	public void Parse(Reader value, OnParsedListener listener) 
	{
		items.clear();
		try {
			Xml.parse(value, feed.getContentHandler());
			if (listener != null)
				listener.onParsed();
		} catch (SAXException e) {
			if (listener != null)
				listener.onException(e);
		} catch (IOException e) {
			if (listener != null)
				listener.onException(e);
		}
	}

	public void Parse(URL endpoint) 
	{
		Parse(endpoint, null);
	}

	public void Parse(URL endpoint, OnParsedListener listener) 
	{
		try {
			Parse((InputStream) endpoint.getContent(), listener);
		} catch (IOException e) {
			if (listener != null)
				listener.onException(e);
		}
	}
}