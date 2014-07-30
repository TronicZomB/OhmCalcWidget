package troniczomb.productions.ohmcalcwidget1_1;

import java.text.DecimalFormat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

public class OhmCalcWidget extends AppWidgetProvider {
	final static String PREFS = "Resistor Values";
	final static String FIRST_BAND = "troniczomb.productions.ohmcalcwidget.FIRST_BAND";
	final static String SECOND_BAND = "troniczomb.productions.ohmcalcwidget.SECOND_BAND";
	final static String MULTIPLIER = "troniczomb.productions.ohmcalcwidget.MULTIPLIER";
	final static String TOLERANCE = "troniczomb.productions.ohmcalcwidget.TOLERANCE";
	
	double baseValue;
	String resistance;
	
	enum BandType {
		FIRST, SECOND, MULTIPLIER, TOLERANCE
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, OhmCalcWidget.class);
		
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			
			//Intent appUpdate = new Intent(context, OhmCalcWidget.class);
			//appUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			//appUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			//appUpdate.setData(Uri.parse(appUpdate.toUri(Intent.URI_INTENT_SCHEME)));
			//PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context, widgetId, appUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
			
			
			Intent first_band_intent = new Intent(context, OhmCalcWidget.class);
			first_band_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			first_band_intent.setAction(FIRST_BAND);
			first_band_intent.setData(Uri.parse(first_band_intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, widgetId, first_band_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    remoteViews.setOnClickPendingIntent(R.id.first_band, pendingIntent1);
		    
		    Intent second_band_intent = new Intent(context, OhmCalcWidget.class);
		    second_band_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		    second_band_intent.setAction(SECOND_BAND);
		    second_band_intent.setData(Uri.parse(second_band_intent.toUri(Intent.URI_INTENT_SCHEME)));
		    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, widgetId, second_band_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    remoteViews.setOnClickPendingIntent(R.id.second_band, pendingIntent2);
		    
		    Intent multiplier_intent = new Intent(context, OhmCalcWidget.class);
		    multiplier_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		    multiplier_intent.setAction(MULTIPLIER);
		    multiplier_intent.setData(Uri.parse(multiplier_intent.toUri(Intent.URI_INTENT_SCHEME)));
		    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, widgetId, multiplier_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    remoteViews.setOnClickPendingIntent(R.id.multiplier, pendingIntent3);
		    
		    Intent tolerance_intent = new Intent(context, OhmCalcWidget.class);
		    tolerance_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		    tolerance_intent.setAction(TOLERANCE);
		    tolerance_intent.setData(Uri.parse(tolerance_intent.toUri(Intent.URI_INTENT_SCHEME)));
		    PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, widgetId, tolerance_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    remoteViews.setOnClickPendingIntent(R.id.tolerance, pendingIntent4);
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		int widgetID = appWidgetIds[0];
		String preferences = PREFS + Integer.toString(widgetID);
		SharedPreferences values = context.getSharedPreferences(preferences, 0);
		SharedPreferences.Editor editor = values.edit().clear();
		editor.commit();
		this.onDeleted(context, new int[] {widgetID});
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//super.onReceive(context, intent);
		SharedPreferences values = null;
		int widgetID = 0, firstBandValue = 0, secondBandValue = 0, multiplierValue = 0, toleranceValue = 0;

		try {
			widgetID = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
			String preferences = PREFS + Integer.toString(widgetID);
			values = context.getSharedPreferences(preferences, 0);
			firstBandValue = values.getInt("first_band_value", 1);
			secondBandValue = values.getInt("second_band_value", 0);
			multiplierValue = values.getInt("multiplier_value", 2);
			toleranceValue = values.getInt("tolerance_value", 1);
		} catch (NullPointerException npe) {
			return;
		}

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		if (intent.getAction().equals(FIRST_BAND)) {
			// nextBandColor() will set the proper band based on the next value and return an integer that is BandValue + 1
			firstBandValue = getNextBandColor(context, BandType.FIRST, firstBandValue);

			SharedPreferences.Editor editor = values.edit();
			editor.putInt("first_band_value", firstBandValue);
			editor.commit();
		}
		else if (intent.getAction().equals(SECOND_BAND)) {
			// nextBandColor() will return an integer that is BandValue + 1
			secondBandValue = getNextBandColor(context, BandType.SECOND, secondBandValue);

			SharedPreferences.Editor editor = values.edit();
			editor.putInt("second_band_value", secondBandValue);
			editor.commit();
		}
		else if (intent.getAction().equals(MULTIPLIER)) {
			// nextBandColor() will return an integer that is BandValue + 1
			multiplierValue = getNextBandColor(context, BandType.MULTIPLIER, multiplierValue);

			SharedPreferences.Editor editor = values.edit();
			editor.putInt("multiplier_value", multiplierValue);
			editor.commit();
		}
		else if (intent.getAction().equals(TOLERANCE)) {
			// nextBandColor() will return an integer that is BandValue + 1
			toleranceValue = getNextBandColor(context, BandType.TOLERANCE, toleranceValue);

			SharedPreferences.Editor editor = values.edit();
			editor.putInt("tolerance_value", toleranceValue);
			editor.commit();
		}
		else {
			super.onReceive(context, intent);
			return;
		}

		// Update the view items based on the new integer values
		updateBands(context, widgetID, firstBandValue, secondBandValue, multiplierValue, toleranceValue);

		// Concatenate the integer value of the first band value with the integer value of the second band color
		String value = Integer.toString(firstBandValue) + Integer.toString(secondBandValue);

		// Turn the concatenation of the two integers into a double value
		baseValue = Double.parseDouble(value);

		multiplyBaseValue(multiplierValue);

		String units = getUnitsAndAdjustBaseValue();
		String tolerance = getTolerance(toleranceValue);

		DecimalFormat df = new DecimalFormat("#.#");
		resistance = df.format(baseValue) + units + tolerance;

		remoteViews.setTextViewText(R.id.resistance_value, resistance);

		//TODO remove if testing goes as planned
		//ComponentName thisWidget = new ComponentName(context, OhmCalcWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		//manager.updateAppWidget(thisWidget, remoteViews);
		manager.updateAppWidget(widgetID, remoteViews);

		//super.onReceive(context, intent);
	}
	
	//TODO Reevaluate the if statements below
	private Integer getNextBandColor(Context context, BandType band, int currentBandValue) {
		int nextBandValue = currentBandValue + 1;
		
		switch (band) {
		case FIRST:
			if (nextBandValue > 9 || nextBandValue <= 0) {
				nextBandValue = 1;
			}
			break;
		case SECOND:
			if (nextBandValue > 9 || nextBandValue <= -1) {
				nextBandValue = 0;
			}
			break;
		case MULTIPLIER:
			if (nextBandValue > 7 || nextBandValue <= -1) {
				nextBandValue = 0;
			}
			break;
		case TOLERANCE:
			if (nextBandValue > 1 || nextBandValue <= -1) {
				nextBandValue = 0;
			}
			break;
		}
		
		return nextBandValue;
	}
	
	private void updateBands(Context context, int widgetId, int firstBand, int secondBand, int multiplier, int tolerance) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		// Update the First Value Band
		if (firstBand == 1) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.brown);
		}
		else if (firstBand == 2) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.red);
		}
		else if (firstBand == 3) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.orange);
		}
		else if (firstBand == 4) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.yellow);
		}
		else if (firstBand == 5) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.green);
		}
		else if (firstBand == 6) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.blue);
		}
		else if (firstBand == 7) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.violet);
		}
		else if (firstBand == 8) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.grey);
		}
		else if (firstBand == 9) {
			remoteViews.setImageViewResource(R.id.first_band, R.drawable.white);
		}
		
		// Update the Second Value Band
		if (secondBand == 0) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.black);
		}
		else if (secondBand == 1) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.brown);
		}
		else if (secondBand == 2) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.red);
		}
		else if (secondBand == 3) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.orange);
		}
		else if (secondBand == 4) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.yellow);
		}
		else if (secondBand == 5) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.green);
		}
		else if (secondBand == 6) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.blue);
		}
		else if (secondBand == 7) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.violet);
		}
		else if (secondBand == 8) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.grey);
		}
		else if (secondBand == 9) {
			remoteViews.setImageViewResource(R.id.second_band, R.drawable.white);
		}
		
		// Update the Multiplier Band
		if (multiplier == 0) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.black);
		}
		else if (multiplier == 1) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.brown);
		}
		else if (multiplier == 2) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.red);
		}
		else if (multiplier == 3) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.orange);
		}
		else if (multiplier == 4) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.yellow);
		}
		else if (multiplier == 5) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.green);
		}
		else if (multiplier == 6) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.blue);
		}
		else if (multiplier == 7) {
			remoteViews.setImageViewResource(R.id.multiplier, R.drawable.violet);
		}
		
		// Update the Tolerance Band
		if (tolerance == 0) {
			remoteViews.setImageViewResource(R.id.tolerance, R.drawable.silver);
		}
		else if (tolerance == 1) {
			remoteViews.setImageViewResource(R.id.tolerance, R.drawable.gold);
		}

		//TODO remove if testing goes as planned
		//ComponentName thiswidget = new ComponentName(context, OhmCalcWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        //manager.updateAppWidget(thiswidget, remoteViews);
        manager.updateAppWidget(widgetId, remoteViews);
	}
	
	private void multiplyBaseValue(int multiplierValue) {
		if (multiplierValue == 0) {
			// Multiply baseValue by 1, or do nothing to baseValue
		}
		else if (multiplierValue == 1) {
			baseValue = baseValue * 10;
		}
		else if (multiplierValue == 2) {
			baseValue = baseValue * 100;
		}
		else if (multiplierValue == 3) {
			baseValue = baseValue * 1000;
		}
		else if (multiplierValue == 4) {
			baseValue = baseValue * 10000;
		}
		else if (multiplierValue == 5) {
			baseValue = baseValue * 100000;
		}
		else if (multiplierValue == 6) {
			baseValue = baseValue * 1000000;
		}
		else if (multiplierValue == 7) {
			baseValue = baseValue * 10000000;
		}
	}

	private String getUnitsAndAdjustBaseValue() {
		String units = "";
		
		if (baseValue >= 1000000) {
			units = "M\u03A9";
			baseValue = baseValue / 1000000;
		}
		else if (baseValue >= 1000) {
			units = "k\u03A9";
			baseValue = baseValue / 1000;
		}
		else if (baseValue < 1000) {
			units = "\u03A9";
		}
		
		return units;
	}
	
	private String getTolerance(int toleranceValue) {
		String tolerance = "";
		
		if (toleranceValue == 0) {
			tolerance = " \u00B1" + " 10%";
		}
		else if (toleranceValue == 1) {
			tolerance = " \u00B1" + " 5%";
		}
		
		return tolerance;
	}
}
