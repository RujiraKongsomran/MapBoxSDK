package com.rujirakongsomran.mapboxsdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.net.URL;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;

    private Handler handler;
    private Runnable runnable;

    // Constant
    private final String ID = "wanderdrone";
    private final String URL_GET_DATA = "https://wanderdrone.appspot.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1IjoiYW5kcm9pZHNwcmVhZCIsImEiOiJja2I4MmJ1Y3cwMDNjMnhxdGd2dmFicjlkIn0.Iq2woFinJLy2jnBIpM0Ufg");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapBoxMap) {
                map = mapBoxMap;

                try {
                    map.addSource(new GeoJsonSource(ID, new URL(URL_GET_DATA)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Add layer
                SymbolLayer layer = new SymbolLayer(ID, ID);
                layer.setProperties(iconImage("rocket-15"));
                mapBoxMap.addLayer(layer);

                // Refresh Data
                handler = new Handler();
                runnable = new RefreshData(map, handler);
                handler.postDelayed(runnable, 2000);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private class RefreshData implements Runnable {
        private MapboxMap map;
        private Handler handler;

        public RefreshData(MapboxMap map, Handler handler) {
            this.map = map;
            this.handler = handler;
        }

        @Override
        public void run() {
            ((GeoJsonSource) map.getSource(ID)).setUrl(URL_GET_DATA);
            handler.postDelayed(this, 2000);
        }
    }
}