package com.github.luucasdornelas.rxlocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Lucas on 21/11/2017.
 */

public class RxLocation implements IRxLocation{
    //region FIELDS
    private Context context;
    private LocationManager locationManager;
    //endregion

    //region CONSTRUCTORS
    public RxLocation(Context context){
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
    //endregion

    //region METHODS

    //region OVERRIDE METHODS
    @SuppressLint("MissingPermission")
    @Override
    public Observable<Location> requestLocationUpdates(String provider, long minTime, float minDistance){
        return new LocationObservable(locationManager, provider, minTime, minDistance);
    }

    @Override
    public Single<Location> requestLocationSingle(String provider, long minTime, float minDistance) {
        return requestLocationUpdates(provider, minTime, minDistance)
                .timeout(0, TimeUnit.MILLISECONDS)
                .first(new Location(LocationManager.PASSIVE_PROVIDER));
    }
    //endregion

    //endregion
}
