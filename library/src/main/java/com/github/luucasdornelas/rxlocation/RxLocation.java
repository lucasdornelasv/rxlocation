package com.github.luucasdornelas.rxlocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Lucas on 21/11/2017.
 */

public class RxLocation implements IRxLocation {
    //region FIELDS
    private Context context;
    private LocationManager locationManager;
    private String defaultProvider;
    private boolean defaultEnableOnly;
    //endregion

    //region CONSTRUCTORS
    public RxLocation(Context context) {
        this(context, null);
    }

    public RxLocation(Context context, String defaultProvider) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.defaultProvider = defaultProvider == null || defaultProvider.isEmpty()
                ? LocationManager.NETWORK_PROVIDER : defaultProvider;
    }
    //endregion

    //region METHODS

    //region OVERRIDE METHODS
    @Override
    public Observable<Location> location(Criteria criteria, boolean enableOnly) {
        return toSingleObservable(
                getBestProvider(criteria, enableOnly)
                        .flatMap(this::location));
    }

    @Override
    public Observable<Location> location(boolean enableOnly) {
        return location(generateCriteria(), enableOnly);
    }

    @Override
    public Observable<Location> location(Criteria criteria) {
        return location(criteria, defaultEnableOnly);
    }

    @Override
    public Observable<Location> location(String provider) {
        return toSingleObservable(listenLocation(provider));
    }

    @Override
    public Observable<Location> location() {
        return location(generateCriteria());
    }

    @Override
    public Observable<Location> listenLocation(Criteria criteria, boolean enableOnly) {
        return getBestProvider(criteria, enableOnly)
                .flatMap(this::listenLocation);
    }

    @Override
    public Observable<Location> listenLocation(boolean enableOnly) {
        return listenLocation(generateCriteria(), enableOnly);
    }

    @Override
    public Observable<Location> listenLocation(Criteria criteria) {
        return listenLocation(criteria, defaultEnableOnly);
    }

    @Override
    public Observable<Location> listenLocation(String provider) {
        return requestLocationUpdates(provider);
    }

    @Override
    public Observable<Location> listenLocation() {
        return listenLocation(generateCriteria());
    }

    @Override
    public Observable<String> getBestProvider(Criteria criteria, boolean enableOnly) {
        return getBestProviderOrDefault(criteria, enableOnly, "");
    }

    @Override
    public Observable<String> getBestProviderOrDefault(Criteria criteria, boolean enableOnly, String defaultProvider) {
        return Observable.fromCallable(() -> {
            String provider = locationManager.getBestProvider(criteria, enableOnly);
            if (provider == null || provider.isEmpty()) provider = defaultProvider;
            return provider;
        });
    }

    @Override
    public Observable<String> getBestProviderOrDefault(Criteria criteria, boolean enableOnly) {
        return getBestProviderOrDefault(criteria, enableOnly, defaultProvider);
    }
    //endregion

    //region PRIVATE METHODS
    private Observable<Location> requestLocationUpdates(String provider, long minTime, float minDistance) {
        return new LocationObservable(locationManager, provider, minTime, minDistance);
    }

    private Observable<Location> requestLocationUpdates(String provider) {
        return requestLocationUpdates(provider, 10000, 10);
    }

    private Criteria generateCriteria() {
        return generateCriteria(defaultProvider);
    }

    private Criteria generateCriteria(String provider) {
        Criteria criteria = new Criteria();
        switch (provider) {
            case LocationManager.NETWORK_PROVIDER:
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                break;
            case LocationManager.GPS_PROVIDER:
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                break;
        }
        return criteria;
    }

    private <T> Observable<T> toSingleObservable(Observable<T> observable) {
        return observable
                .singleOrError()
                .toObservable();
    }
    //endregion

    //endregion
}
