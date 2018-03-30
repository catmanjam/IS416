package is416.is416;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import is416.is416.Database.StepDatabase;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private static final long LOCATION_REFRESH_TIME = 100;
    private static final float LOCATION_REFRESH_DISTANCE = 1;
    private GoogleMap mMap;
    private StepDatabase stepDb;
    private TextView stepView;
    private TextView calendarView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mStepSensor;
    private List<Marker> markerList = new ArrayList<>();
    private MapMarkerBounce bounce;
    private Float globalStepCount;
    private Calendar cal;
    private boolean mLocationPermissionGranted;
    protected LocationManager mLocationManager;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    float stepVal;


    TimeZone SG = TimeZone.getTimeZone("Singapore");
    Random rand = new Random();
    Handler handler = new Handler();
    // Define the code block to be executed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        stepView = (TextView) findViewById(R.id.steps);

        calendarView = (TextView) findViewById(R.id.date);
//        calendarView.setText(calculateCalendar());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        bounce = new MapMarkerBounce();
        stepDb = StepDatabase.getInstance(this);
        cal = Calendar.getInstance();
        cal.setTimeZone(SG);

        globalStepCount = (float) stepDb.getStepsToday(cal);

        stepView.setText("Steps taken today: " + Integer.toString(stepDb.getStepsToday(cal)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }else{
            initMap();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final List<List<LatLng>> polygonMasterList = new ArrayList<>();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.presentvector);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng singapore = new LatLng(1.296568, 103.852119);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,16.0f));
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.attractions,getApplicationContext());
            LatLngBounds.Builder builder;

            for (GeoJsonFeature feature : layer.getFeatures()) {
                // do something to the feature
                if (feature!=null){

                    builder = LatLngBounds.builder();
                    GeoJsonPolygonStyle polygon = layer.getDefaultPolygonStyle();
                    if (polygon!=null){
                        Geometry geometry = feature.getGeometry();
                        GeoJsonPolygon pol = (GeoJsonPolygon) geometry;
                        List<? extends List<LatLng>> lists =
                                ((GeoJsonPolygon) geometry).getCoordinates();

                        // Feed the coordinates to the builder.

                        for (List<LatLng> list : lists) {
                            for (LatLng latLng : list) {
                                builder.include(latLng);
                            }
                            polygonMasterList.add(list);
                        }

                        LatLngBounds polyBounds = builder.build();
                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(polyBounds.getCenter())
                                .title("Reward"+(polygonMasterList.size()-1))
                                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false)))
                        );

                        markerList.add(m);

//                        System.out.print("Poly coords"+feature.getId());
                        polygon.setFillColor(Color.GREEN);

//                        feature.setPolygonStyle(polygon);
//                        mMap.addMarker(new MarkerOptions().position(polyBounds.getCenter()));
                    }

                }
            }

            layer.addLayerToMap();
//        getDeviceLocation();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            for (Marker m : markerList){
                setMarkerBounce(m);
            }
        }

//        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
//            @Override
//            public void onPolygonClick(final Polygon polygon) {
//                Toast.makeText(getApplicationContext(),polygon.getId(),Toast.LENGTH_LONG).show();
//                DialogFragment dlFrag = new RewardsWindowDialog();
//                FragmentManager fm = getFragmentManager();
//                dlFrag.show(fm,"");
//            }
//
//        });

//        mMap.setInfoWindowAdapter();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
//                System.out.println("Test"+PolyUtil.containsLocation(marker.getPosition(),polygonMasterList,false));
                DialogFragment dlFrag = new RewardsWindowDialog();
                FragmentManager fm = getFragmentManager();
                dlFrag.show(fm,"");
                Bundle args = new Bundle();
                args.putString("PolyId",marker.getTitle());
//                Toast.makeText(getApplicationContext(),marker.getSnippet(),Toast.LENGTH_LONG).show();
                dlFrag.setArguments(args);
                marker.hideInfoWindow();
                return false;
            }
        });




//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if (PolyUtil.containsLocation(latLng,polygon.getPoints(),true)){
//                    mMap.addMarker(new MarkerOptions().position(latLng));
//                    Toast.makeText(getApplicationContext(),"I clicked inside",Toast.LENGTH_LONG);
//                }
//            }
//        });


//        mMap.animateCamera(CameraUpdateFa/ctory.zoomTo(2.5f));

//        GroundOverlayOptions presentBox = new GroundOverlayOptions()
//            .image(fromResource(R.drawable.presentvector))
//            .position(singapore, 150f, 150f);
//        mMap.addGroundOverlay(presentBox);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepDb!=null && cal != null && globalStepCount!=null){
            stepDb.updateStepRecord(cal,Math.round(globalStepCount+stepVal));
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        mSensorManager.registerListener(mSensorEventListener,mSensor,mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mStepTakenSensorEventListener,mStepSensor,mSensorManager.SENSOR_DELAY_FASTEST);
        globalStepCount = (float) stepDb.getStepsToday(cal);


    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        private float mStepOffset;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (mStepOffset == 0){
                mStepOffset = sensorEvent.values[0];
            }
//            stepView.setText(Float.toString(sensorEvent.values[0]-mStepOffset));
            float stepVal = sensorEvent.values[0]-mStepOffset;
            stepView.setText("Steps take today: "+Math.round(globalStepCount+stepVal));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private SensorEventListener mStepTakenSensorEventListener = new SensorEventListener() {

        private float mStepOffset;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private String calculateCalendar(){
        stepDb = new StepDatabase(this);
        Calendar today = Calendar.getInstance();
        Integer day = today.get(today.DAY_OF_MONTH);
        Integer month =  today.get(today.MONTH);
        Integer year = today.get(today.YEAR);
        Log.d("SEE THIS", ""+day+"-"+month+"-"+year);
        today.set(year, month, 27, 0,0,0);
        Log.d("SEE THAT", ""+today.get(today.DAY_OF_MONTH)+"-"+today.get(today.MONTH)+"-"+today.get(Calendar.YEAR));
        Cursor c = stepDb.getAllSteps();

        // Cursor c = stepDb.getStepsToday(today);
        String str = "";
        c.moveToNext();
        while(c.getCount()-c.getPosition() > 0){

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(c.getLong(1));
            Log.d("SEE retrieved time", ""+cal.getTimeInMillis() );
            cal.set(Calendar.MILLISECOND,0);

            str += c.getInt(0)+ ",";
            str += cal.get(cal.DAY_OF_MONTH) + "-" + cal.get(cal.MONTH) + "-"+ cal.get(cal.YEAR)+ ",";
            str += c.getInt(2) + "\n";
            c.moveToNext();
        }

        return str;
    }

    private void setMarkerBounce(final Marker marker) {
        final Handler handler = new Handler();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
//                long test = (rand.nextInt(2) + 1) * 1000;
                long test = 2000;
                final long startTime = SystemClock.uptimeMillis();
                final long duration = test;
                final Interpolator interpolator = new BounceInterpolator();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long elapsed = SystemClock.uptimeMillis() - startTime;
                        float t = Math.max(1 - interpolator.getInterpolation((float) elapsed/duration), 0);
                        marker.setAnchor(0.5f, 1.0f +  t);

                        if (t > 0.0) {
                            handler.postDelayed(this, 28);
                        } else {
                            setMarkerBounce(marker);
                        }
                    }
                });
            }
        }, 4000);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Toast.makeText(getApplicationContext(),"Result: "+Double.toString(location.getLatitude()),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
