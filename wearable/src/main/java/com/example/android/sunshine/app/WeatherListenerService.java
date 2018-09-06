package com.example.android.sunshine.app;

import android.content.SharedPreferences;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class WeatherListenerService extends WearableListenerService {
    public static final String DATE1_KEY = "date1";
    public static final String MAX1_KEY = "max1";
    public static final String MIN1_KEY = "min1";
    public static final String WEATHER_ID1_KEY = "weather_id1";

    public static final String DATE2_KEY = "date2";
    public static final String MAX2_KEY = "max2";
    public static final String MIN2_KEY = "min2";
    public static final String WEATHER_ID2_KEY = "weather_id2";

    private static final String TOGGLE_KEY = "datatoggler";

    private static boolean toggle;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if ( dataEvent.getType() == DataEvent.TYPE_CHANGED ) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();

                if (path.equals("/SUNSHINE/WEATHER")) {

                    SharedPreferences sharedPreferences = this.getSharedPreferences(
                            this.getResources().getString(R.string.shared_preferences_weather_key),
                            this.MODE_PRIVATE);

                    boolean recent = sharedPreferences.getBoolean(TOGGLE_KEY, false);

                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putInt(DATE1_KEY, dataMap.getInt(DATE1_KEY));
                    edit.putString(MAX1_KEY, dataMap.getString(MAX1_KEY));
                    edit.putString(MIN1_KEY, dataMap.getString(MIN1_KEY));
                    edit.putInt(WEATHER_ID1_KEY, dataMap.getInt(WEATHER_ID1_KEY));

                    edit.putInt(DATE2_KEY, dataMap.getInt(DATE2_KEY));
                    edit.putString(MAX2_KEY, dataMap.getString(MAX2_KEY));
                    edit.putString(MIN2_KEY, dataMap.getString(MIN2_KEY));
                    edit.putInt(WEATHER_ID2_KEY, dataMap.getInt(WEATHER_ID2_KEY));

                    if (recent == toggle) {
                        toggle = !toggle;
                    }
                    edit.putBoolean(TOGGLE_KEY, toggle);

                    edit.commit(); //vs apply(), threading
                }
            }
        }
    }

    public static boolean getToggle() {
        return toggle;
    }
}
