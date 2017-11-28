package com.github.luucasdornelas.rxlocation;


/**
 * Created by Lucas on 21/11/2017.
 */

public class ProviderDisabledException extends Throwable {
    //region FIELDS
    private final String provider;
    //endregion

    //region CONSTRUCTORS
    public ProviderDisabledException(String provider) {
        super("The " + provider + " provider is disabled");
        this.provider = provider;
    }
    //endregion

    //region METHODS

    //region GETTER AND SETTER METHODS
    public final String getProvider() {
        return this.provider;
    }
    //endregion

    //endregion
}