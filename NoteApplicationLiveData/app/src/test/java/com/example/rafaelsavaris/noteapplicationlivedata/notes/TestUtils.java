package com.example.rafaelsavaris.noteapplicationlivedata.notes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;

public class TestUtils {

    public static final LifecycleOwner TEST_OBSERVER = new LifecycleOwner() {

        private LifecycleRegistry mLifecycleRegistry = init();

        private LifecycleRegistry init(){

            LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);

            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

            return lifecycleRegistry;

        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }
    };

}
