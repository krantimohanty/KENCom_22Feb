package com.swash.kencommerce.retrofit_call;

/**
 * Created by Kranti on 12/12/2016.
 */

public interface RestCallback1<T> {
    public abstract void success(T o);
    public abstract void invalid(T o);
    public abstract void failure();
}