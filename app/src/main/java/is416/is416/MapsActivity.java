package is416.is416;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        View mDecorView = getWindow().getDecorView();
//        // Hide both the navigation bar and the status bar.
//        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//        // a general rule, you should design your app to hide the status bar whenever you
//        // hide the navigation bar.
//        mDecorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

//    private void getDeviceLocation() {
//    /*
//     * Before getting the device location, you must check location
//     * permission, as described earlier in the tutorial. Then:
//     * Get the best and most recent location of the device, which may be
//     * null in rare cases when a location is not available.
//     */
//        if (mLocationPermissionGranted) {
//            mLastKnownLocation = LocationServices.FusedLocationApi
//                    .getLastLocation(mGoogleApiClient);
//        }
//
//        // Set the map's camera position to the current location of the device.
//        if (mCameraPosition != null) {
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
//        } else if (mLastKnownLocation != null) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(mLastKnownLocation.getLatitude(),
//                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//        } else {
//            Log.d(TAG, "Current location is null. Using defaults.");
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        }
//    }


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
        MarkerOptions smuMarker = new MarkerOptions()
                .position(singapore)
                .title("Marker in SMU")
                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false)));

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,16.0f));
        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.addMarker(smuMarker);

//        List<Polygon> list = new ArrayList<>();

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
                        mMap.addMarker(new MarkerOptions()
                                .position(polyBounds.getCenter())
                                .title("Polygon"+(polygonMasterList.size()-1))
                        );
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
        }

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(final Polygon polygon) {
                Toast.makeText(getApplicationContext(),polygon.getId(),Toast.LENGTH_LONG).show();
                DialogFragment dlFrag = new RewardsWindowDialog();
                FragmentManager fm = getFragmentManager();
                dlFrag.show(fm,"");
            }

        });

//        mMap.setInfoWindowAdapter();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
//                System.out.println("Test"+PolyUtil.containsLocation(marker.getPosition(),polygonMasterList,false));
                DialogFragment dlFrag = new RewardsWindowDialog();
                FragmentManager fm = getFragmentManager();
                dlFrag.show(fm,"");
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
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
