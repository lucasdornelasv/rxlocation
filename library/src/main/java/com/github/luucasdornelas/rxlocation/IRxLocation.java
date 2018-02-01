package com.github.luucasdornelas.rxlocation;

import android.location.Criteria;
import android.location.Location;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Lucas on 21/11/2017.
 */

public interface IRxLocation {

    Observable<Location> location(Criteria criteria, boolean enableOnly);
    Observable<Location> location(boolean enableOnly);
    Observable<Location> location(Criteria criteria);
    Observable<Location> location(String provider);
    Observable<Location> location();

    Observable<Location> listenLocation(Criteria criteria, boolean enableOnly);
    Observable<Location> listenLocation(boolean enableOnly);
    Observable<Location> listenLocation(Criteria criteria);
    Observable<Location> listenLocation(String provider);
    Observable<Location> listenLocation();

    Observable<String> getBestProvider(Criteria criteria, boolean enableOnly);

    Observable<String> getBestProviderOrDefault(Criteria criteria, boolean enableOnly, String defaultProvider);
    Observable<String> getBestProviderOrDefault(Criteria criteria, boolean enableOnly);

}
