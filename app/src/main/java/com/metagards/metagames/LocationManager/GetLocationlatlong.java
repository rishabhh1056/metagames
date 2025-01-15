package com.metagards.metagames.LocationManager;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class GetLocationlatlong {

    Context context;
    NewGpsTracker newGpsTracker;
    GPSTracker gpsTracker;
    public GetLocationlatlong(Context context) {
        this.context = context;

         newGpsTracker = new NewGpsTracker(context);
         newGpsTracker.fetchLocation();
        gpsTracker = new GPSTracker(context);

    }

    public LatLng getLatlong() {

        Double latitude, longitude;
        try {
            LatLng loc = newGpsTracker.fetchLocation();

            if(loc != null)
            {
                latitude = loc.latitude;
                longitude = loc.longitude;
            }
            else {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

        } catch (Exception e) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }

        if(latitude <= 0 && longitude <= 0)
        {
            newGpsTracker = new NewGpsTracker(context);
            gpsTracker = new GPSTracker(context);

            try {
                LatLng loc = newGpsTracker.fetchLocation();

                if(loc != null)
                {
                    latitude = loc.latitude;
                    longitude = loc.longitude;
                }
                else {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                }

            } catch (Exception e) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }
        }

        LatLng latLng = new LatLng(latitude,longitude);

        return latLng;
    }


}
