package com.attendance.letmeattend.activities

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.geofencing.GeofenceBroadcastReceiver
import com.attendance.letmeattend.geofencing.GeofenceClient
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.map_fragment.*
import java.util.*

// these are listeners, they recieve a callback whenever specific event happens
class MapFragment : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    override fun onClick(v: View?)
    {
        when(v){
            button_next-> {
                viewModel.setCollegeLocation(CollegeLocation(circle.radius,circle.center.latitude,circle.center.longitude))
                val geofenceClient = GeofenceClient()
                geofenceClient.getGeofencingClient().addGeofences(geofenceClient.addGeofence(circle.center,circle.radius.toFloat()),geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        AppApplication?.context?.toast("Geofence Added")
                    }
                    addOnFailureListener {
                        AppApplication?.context?.toast("Geofence failed")
                    }
                }
                startActivity(Intent(this@MapFragment, EnterDetailsActivity::class.java))
            }
            search_btn->   launchPlacesIntent()
        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {

    }

    override fun onMarkerDragStart(p0: Marker?) {


    }

    private lateinit var viewModel : EnterDetailsViewModel

    override fun onMarkerDrag(p0: Marker?) {
        //finding markers with tag then setting new locations and radius
        if (p0!!.tag == "center") {
            centerMarker.position = p0.position
            circle.center = p0.position
            circle.radius = getRadius(centerMarker.position, edgeMarker.position)


        } else if (p0!!.tag == "edge") {
            edgeMarker.position = p0.position
            circle.radius = getRadius(centerMarker.position, edgeMarker.position)

        }

    }


    private lateinit var mMap: GoogleMap //lateint is used for late initialization(outside constructor) to prevent null exceptions
    private lateinit var mfusedLocationClient: FusedLocationProviderClient  //used to get latest location
    private var mlocationPermissionGranted: Boolean = false
    private lateinit var mLastKnownLocation: Location
    private val mdefaultLocation = LatLng(28.5693052, 77.2520095)
    private lateinit var centerMarker: Marker
    private lateinit var edgeMarker: Marker
    private lateinit var circle: Circle
    private var locationCenter: Location = Location("Center")
    private var locationEdge: Location = Location("Edge")


    private fun addCircle(latLng: LatLng) {
        //latlng is center marker. position of place searched by the user.
        //adds cirlce


        //edgelatlng at some distance far
        val edgeLatLng: LatLng = LatLng(latLng.latitude + 0.0007, latLng.longitude + 0.0007)

        centerMarker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(true)

        )

        centerMarker.tag = "center"


        edgeMarker = mMap.addMarker(
            MarkerOptions()
                .position(edgeLatLng)
                .draggable(true)

        )

        edgeMarker.tag = "edge"

        circle =
            mMap.addCircle(CircleOptions().center(latLng).radius(getRadius(centerMarker.position, edgeMarker.position)))
        circle.strokeColor = resources.getColor(R.color.mapStroke)


    }


    private fun getRadius(centerLatLng: LatLng, edgeLatLng: LatLng): Double {


        locationCenter.longitude = centerLatLng.longitude
        locationCenter.latitude = centerLatLng.latitude


        locationEdge.longitude = edgeLatLng.longitude
        locationEdge.latitude = edgeLatLng.latitude

        return locationCenter.distanceTo(locationEdge).toDouble()

    }


    //onMapReadyCallback
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        mMap.setOnMarkerDragListener(this@MapFragment)




        try {
            var success: Boolean = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@MapFragment,
                    R.raw.style_json
                )
            )// styling map to night mode

            if (!success) {

                Log.e(TAG, "Style parsing failed.")
            }


        } catch (e: Exception) {
            Log.e(TAG, "Map styling exception", e)
        }
//if permission granted this will add current location button
        updateLocationUI()

        //this will fetch current location data
        getLocationData()

    }


    private fun launchPlacesIntent() {
        val fields: List<Place.Field>
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).build(this)
        startActivityForResult(intent,
            AUTOCOMPLETE_REQUEST_CODE
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place? = data?.let { it -> Autocomplete.getPlaceFromIntent(it) }
                Log.e(TAG, place!!.name)
                place!!.name?.let { this@MapFragment.toast(it) }
                mMap.clear()
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                  place.latLng?.let { addCircle(it) }
                   button_next.visibility=View.VISIBLE


            }
            else {
                this@MapFragment.toast("Failed Request: ")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_fragment)//MapFragment lies here

        //Inititating places
        Places.initialize(applicationContext, resources.getString(R.string.google_places_key))
        button_next.visibility = View.VISIBLE

        viewModel = ViewModelProviders.of(this).get(EnterDetailsViewModel::class.java)
        var placeClient: PlacesClient = Places.createClient(this)

        getLocationPermission() //asking permission for user's location (also check manifest for declaring permission)

        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager  //setting map to the fragment
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)







    }


    companion object {    //companion objects are tied to the class rather than class objects.like static methods
        private val TAG = MapFragment::class.java.simpleName
        private const val LOCATION_PERMISSION = 12
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permission not granted

            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )

            //we will now check for permission granted in onRequestPermissionResult whether the user has granted permission or not
        } else {
            //permission is granted
            mlocationPermissionGranted = true


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                mlocationPermissionGranted = true;


                //we will update the ui
                //check if map not null
                if (mMap != null) {
                    updateLocationUI()
                    getLocationData()
                }

            } else {

                //permission denied. I don't know to regularly ask for permission for now

                //    ActivityCompat.requestPermissions(this@MapFragment, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                //      LOCATION_PERMISSION)

            }

        }
    }


    private fun updateLocationUI() {
        //adds a current location button to map
        if (mMap == null) {
            return
        }
        try {
            addCircle(LatLng(28.564416,77.256673))
            if (mlocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun getLocationData() {
        /*
      Get the best and most recent location of the device, which may be null in rare
      cases when a location is not available.
     */

        try {
            if (mlocationPermissionGranted) {

                var mLocationResult: Task<Location>

                mLocationResult = mfusedLocationClient.lastLocation

                mLocationResult.addOnCompleteListener {

                    if (it.isSuccessful) {
                        mLastKnownLocation = it.result!!
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    mLastKnownLocation.latitude,
                                    mLastKnownLocation.longitude
                                )
                            )
                        )

                    } else {

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mdefaultLocation))
                        Log.e(TAG, "Location data failed")
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }


            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mdefaultLocation))
            }

        } catch (e: SecurityException) {

            Log.e(TAG, "Error fetching current location", e)

        }


    }

}