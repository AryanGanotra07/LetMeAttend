package com.attendance.letmeattend.geofencing

import android.location.Location
import com.attendance.letmeattend.application.AppApplication
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.core.Constants

class GeofenceClient() {

    private var geofencingClient: GeofencingClient
    private final val REQUEST_ID = "college"

    init {
        geofencingClient = LocationServices.getGeofencingClient(AppApplication?.context!!)
    }

    fun addGeofence(location : LatLng, radius : Float): GeofencingRequest? {
        val geofencelist = ArrayList<Geofence>()
        geofencelist.add(Geofence
            .Builder()
            .setRequestId(REQUEST_ID)
            .setCircularRegion(location.latitude, location.longitude, radius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
        )

        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_EXIT)
            addGeofences(geofencelist)
        }.build()
    }

    fun getGeofencingClient() : GeofencingClient {
        return geofencingClient
    }


}