package com.example;

import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

public class QuoteWidget extends AppWidgetProvider 
{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		context.startService(new Intent(context,UpdateService.class));
	}

	public static class UpdateService extends Service
	{	
		@Override
		public void onStart(Intent intent, int startId) 
		{
			//build widget update for today
			RemoteViews views=buildUpdate(this);
			
			//push update for widget to the Home activity
			ComponentName widget = new ComponentName(this,QuoteWidget.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(widget, views);
			
		}

		public RemoteViews buildUpdate(Context ctx)
		{
			Random random = new Random();
			Resources resources = ctx.getResources();
			//get all of will's quotes
			String[] willQuotes = resources.getStringArray(R.array.will_rogers_quotes);
			
			//get a random quote to render
			String quote=willQuotes[random.nextInt(willQuotes.length-1)];
			
			RemoteViews view = new RemoteViews(ctx.getPackageName(),R.layout.will_quotes);
			view.setTextViewText(R.id.first, quote);
			//when a user clicks the widget, show 'em a google search page with will roger results...
			Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://en.wikipedia.org/wiki/Will_Rogers"));
			
			//no request code and no flags for this example
			PendingIntent pender = PendingIntent.getActivity(ctx, 0, intent, 0);
			view.setOnClickPendingIntent(R.id.widget, pender);
			return view;
		}
		
		@Override
		public IBinder onBind(Intent arg0) 
		{
			return null;
		}
		
	}
}
