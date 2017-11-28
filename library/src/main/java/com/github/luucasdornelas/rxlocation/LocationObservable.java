package com.github.luucasdornelas.rxlocation;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lucas on 21/11/2017.
 */

public class LocationObservable extends Observable<Location> {
    //region FIELDS
    private LocationManager locationManager;
    private String provider;
    private long minTime;
    private float minDistance;
    //endregion

    //region CONSTRUCTORS
    public LocationObservable(LocationManager locationManager, String provider, long minTime, float minDistance) {
        this.locationManager = locationManager;
        this.provider = provider;
        this.minTime = minTime;
        this.minDistance = minDistance;
    }
    //endregion

    //region METHODS

    //region OVERRIDE METHODS
    @SuppressLint("MissingPermission")
    @Override
    protected void subscribeActual(Observer<? super Location> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);
        if(locationManager.isProviderEnabled(provider)){
            locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
        }else{
            observer.onError(new ProviderDisabledException(provider));
        }
    }
    //endregion

    //endregion

    //region LISTENERS
    private class Listener implements LocationListener, Disposable {
        //region FIELDS
        private final Observer<? super Location> observer;

        private final AtomicBoolean unsubscribed = new AtomicBoolean();
        //endregion

        //region CONSTRUCTORS
        public Listener(Observer<? super Location> observer){
            this.observer = observer;
        }
        //endregion

        //region METHODS

        //region OVERRIDE METHODS
        @Override
        public void onLocationChanged(Location location) {
            if (!isDisposed()) {
                observer.onNext(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("onStatusChanged", s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("onProviderEnabled", s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("onProviderDisabled", s);
            if(!isDisposed() && provider.equals(s)){
                observer.onError(new ProviderDisabledException(provider));
            }
        }

        @Override
        public void dispose() {
            if (unsubscribed.compareAndSet(false, true)) {
                onDispose();
            }
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
        //endregion

        //region PRIVATE METHODS
        private void onDispose(){
            locationManager.removeUpdates(this);
        }
        //endregion

        //endregion
    }
    //endregion
}