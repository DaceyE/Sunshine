package com.example.android.sunshine.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherDataService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SEND_WEATHER_DATA = "com.example.android.sunshine.app.wearable.action.ACTION_SEND_WEATHER_DATA";

    public static final String EXTRA_DATE_1 = "com.example.android.sunshine.app.wearable.extra.DATE1";
    public static final String EXTRA_MAX_1 = "com.example.android.sunshine.app.wearable.extra.MAX1";
    public static final String EXTRA_MIN_1 = "com.example.android.sunshine.app.wearable.extra.MIN1";
    public static final String EXTRA_WEATHER_1 = "com.example.android.sunshine.app.wearable.extra.WEATHER1";

    public static final String EXTRA_DATE_2 = "com.example.android.sunshine.app.wearable.extra.DATE2";
    public static final String EXTRA_MAX_2 = "com.example.android.sunshine.app.wearable.extra.MAX2";
    public static final String EXTRA_MIN_2 = "com.example.android.sunshine.app.wearable.extra.MIN2";
    public static final String EXTRA_WEATHER_2 = "com.example.android.sunshine.app.wearable.extra.WEATHER2";

    private GoogleApiClient mGoogleApiClient;

    public WeatherDataService() {
        super("WeatherDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSendWeatherData(Context context,
                                      int date1, String max1, String min1, int weather1,
                                      int date2, String max2, String min2, int weather2) {

        Intent intent = new Intent(context, WeatherDataService.class);
        intent.setAction(ACTION_SEND_WEATHER_DATA);

        intent.putExtra(EXTRA_DATE_1, date1);
        intent.putExtra(EXTRA_MAX_1, max1);
        intent.putExtra(EXTRA_MIN_1, min1);
        intent.putExtra(EXTRA_WEATHER_1, weather1);

        intent.putExtra(EXTRA_DATE_2, date2);
        intent.putExtra(EXTRA_MAX_2, max2);
        intent.putExtra(EXTRA_MIN_2, min2);
        intent.putExtra(EXTRA_WEATHER_2, weather2);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_WEATHER_DATA.equals(action)) {
                final PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/SUNSHINE/WEATHER");
                final DataMap map = putDataMapRequest.getDataMap();
                map.putInt("date1", intent.getIntExtra(EXTRA_DATE_1, Integer.MIN_VALUE));
                map.putString("max1", intent.getStringExtra(EXTRA_MAX_1));
                map.putString("min1", intent.getStringExtra(EXTRA_MIN_1));
                map.putInt("weather_id1", intent.getIntExtra(EXTRA_WEATHER_1, Integer.MIN_VALUE));

                map.putInt("date2", intent.getIntExtra(EXTRA_DATE_2, Integer.MIN_VALUE));
                map.putString("max2", intent.getStringExtra(EXTRA_MAX_2));
                map.putString("min2", intent.getStringExtra(EXTRA_MIN_2));
                map.putInt("weather_id2", intent.getIntExtra(EXTRA_WEATHER_2, Integer.MIN_VALUE));

                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest().setUrgent()).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()){
                            Log.e("mywatch", "Failed to send weather data item to com.example.android.app");
                        } else {
                            Log.d("mywatch", "Successfully sent weather data item to com.example.android.app");
                        }

                    }
                });;
            } else {
                //do nothing
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i("mywatch","onConnected(...)");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("mywatch","onConnectionSuspended(...)");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("mywatch","onConnectionFailed(...)");
    }

}
