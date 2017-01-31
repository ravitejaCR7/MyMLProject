package com.example.talarir.mymlproject;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener
{

    TextView textView;
    PermissionCheckInterface permissionCheckInterface;
    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    public Location mLastLocation;
    public GoogleMap map;
    public double lat =0, lng=0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();



    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    public static final int ALT_HEATMAP_RADIUS = 10;
    public static final double ALT_HEATMAP_OPACITY = 0.4;
    public static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };
    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };
    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);
    public HeatmapTileProvider mProvider;
    public TileOverlay mOverlay;
    public boolean mDefaultGradient = true;
    public boolean mDefaultRadius = true;
    public boolean mDefaultOpacity = true;
    public HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        buildGoogleApiClient();

        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        permissionCheckInterface = new PermissionCheckInterfaceImp(this);
        setUpMap();
        //mGoogleApiClient.connect();

    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
        /*if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }*/
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d("something", "Location update resumed .....................");
        }
    }
    private void setUpMap() {
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        this.map=map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            permissionCheckInterface.locationPermissionRequest();
            return;
        }
        map.setMyLocationEnabled(true);
        //map.setTrafficEnabled(true);
        //map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        buildGoogleApiClient();
        startDemo();

        //LatLng sydney = new LatLng(-33.867, 151.206);
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

        Log.d("something", "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            permissionCheckInterface.locationPermissionRequest();
            return;
        }
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            LatLng loc = new LatLng(lat, lng);
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
            //map.addMarker(new MarkerOptions().position(loc).title("New Marker"));
            Toast.makeText(this,lat+" : "+lng,Toast.LENGTH_SHORT).show();
            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
            Log.e("location ra",lat+"   "+lng);
        }*/

    }
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            permissionCheckInterface.locationPermissionRequest();
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d("something", "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.i("ento emo", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

            stopLocationUpdates();

    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d("something", "Location update stopped .......................");
    }




    public GoogleMap getMap() {
        return map;
    }








    public void startDemo()
    {
        //getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-25, 143), 4));

        // Set up the spinner/dropdown list
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.heatmaps_datasets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerActivity());

        try {
            mLists.put(getString(R.string.police_stations), new DataSet(readItems(R.raw.police),
                    getString(R.string.police_stations_url)));
            mLists.put(getString(R.string.medicare), new DataSet(readItems(R.raw.medicare),
                    getString(R.string.medicare_url)));
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    public void changeRadius(View view) {
        if (mDefaultRadius) {
            mProvider.setRadius(ALT_HEATMAP_RADIUS);
        } else {
            mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);
        }
        mOverlay.clearTileCache();
        mDefaultRadius = !mDefaultRadius;
    }

    public void changeGradient(View view) {
        if (mDefaultGradient) {
            mProvider.setGradient(ALT_HEATMAP_GRADIENT);
        } else {
            mProvider.setGradient(HeatmapTileProvider.DEFAULT_GRADIENT);
        }
        mOverlay.clearTileCache();
        mDefaultGradient = !mDefaultGradient;
    }

    public void changeOpacity(View view) {
        if (mDefaultOpacity) {
            mProvider.setOpacity(ALT_HEATMAP_OPACITY);
        } else {
            mProvider.setOpacity(HeatmapTileProvider.DEFAULT_OPACITY);
        }
        mOverlay.clearTileCache();
        mDefaultOpacity = !mDefaultOpacity;
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    @Override
    public void onLocationChanged(Location location) {

        DatabaseReference myRef = database.getReference("value");
        myRef.setValue(lat+":"+lng);
        textView.setText(String.valueOf(location.getLatitude())+" : "+String.valueOf(location.getLongitude()));
    }


    public class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }

    }

    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            String dataset = parent.getItemAtPosition(pos).toString();


            // Check if need to instantiate (avoid setData etc twice)
            if (mProvider == null) {
                mProvider = new HeatmapTileProvider.Builder().data(
                        mLists.get(getString(R.string.police_stations)).getData()).build();
                mOverlay = getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            } else {
                mProvider.setData(mLists.get(dataset).getData());
                mOverlay.clearTileCache();
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

}
