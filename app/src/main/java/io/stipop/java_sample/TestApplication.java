package io.stipop.java_sample;

import androidx.multidex.MultiDexApplication;

import io.stipop.Stipop;

public class TestApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stipop.Companion.configure(this, null);
    }
}
