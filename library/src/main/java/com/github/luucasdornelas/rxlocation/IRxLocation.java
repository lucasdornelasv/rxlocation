package com.github.luucasdornelas.rxlocation;

import android.location.Location;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Lucas on 21/11/2017.
 */

public interface IRxLocation {
    Observable<Location> requestLocationUpdates(String provider, long minTime, float minDistance);
    Single<Location> requestLocationSingle(String provider, long minTime, float minDistance);
}
