package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class BuzzGrabber 
{
	private static final String TAG="BuzzGrabber";
	private static final String formatUri = "http://buzz.googleapis.com/feeds/%s/public/posted";
	private static ScheduledExecutorService svc;
	private Context context;
	private final String accountName;
	private volatile boolean canRun;
	
	static
	{
		if(svc == null || svc.isShutdown())
			svc = Executors.newScheduledThreadPool(2);
	}
	
	public BuzzGrabber(Context ctx,String name)
	{
		accountName=name;
		context=ctx;
		canRun=false;
	}
	public void start()
	{
		canRun=true;
		Runnable runner = new Runnable()
		{
			@Override
			public void run() 
			{
				if(!canRun)
					return;
				
				ResponseHandler<String> responder = new ResponseHandler<String>()
				{
					@Override
					public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException 
					{
						int status = response.getStatusLine().getStatusCode();
						if(status == 200)
						{
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							StreamUtil.copy(response.getEntity().getContent(), baos, 512);
							return new String(baos.toByteArray());
						}
						else
						{
							return "";
						}
					}
				};
				
				String uri = String.format(formatUri,accountName);
				HttpClient client = HttpUtil.getHttpClient(HttpUtil.DEFAULT_USER_AGENT);
				HttpGet httpGet = new HttpGet(uri);
				try 
				{
					String xml = client.execute(httpGet,responder);
					BuzzParser parser = new BuzzParser();
					//a cheat... spend time to pretty this later maybe
					String tmp = xml.replace("<feed xmlns='http://www.w3.org/2005/Atom'", "<feed ");
					parser.Parse(tmp);
					//parser.Parse(new URL(uri));
					ContentResolver resolver = context.getContentResolver();
					//since this example just uses one account, and the buzz updates are complete records,
					//there's no need to keep any data in the store
					
					resolver.delete(BlurtProvider.CONTENT_URI, null, null);
					
					//now, just loop through the items and insert them
					List<BuzzParser.Item> items = parser.getItems();
					for(BuzzParser.Item item:items)
					{
						ContentValues values = new ContentValues();
						values.put(BlurtColumns.TITLE, item.title);
						values.put(BlurtColumns.DETAIL, item.description);
						resolver.insert(BlurtProvider.CONTENT_URI, values);
					}
				} 
				catch (ClientProtocolException e) 
				{
					Log.e(TAG, e.getMessage());
				} 
				catch (IOException e) 
				{
					Log.e(TAG,e.getMessage());
				}
				catch(Exception e)
				{
					Log.e(TAG,e.getMessage());
				}
			}
		};
		
		svc.scheduleAtFixedRate(runner, 3, 30, TimeUnit.SECONDS);
	}
	
	public void stop()
	{
		canRun=false;
	}
	
	public void dispose()
	{
		svc.shutdown();
	}	
}
