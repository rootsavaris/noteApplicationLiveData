package com.example.rafaelsavaris.noteapplicationlivedata;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rafael.savaris on 28/03/2018.
 */

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<T> observer) {

        super.observe(owner, new Observer<T>() {

            @Override
            public void onChanged(@Nullable T t) {

                if (mPending.compareAndSet(true, false)){
                    observer.onChanged(t);
                }

            }
        });

    }

    @MainThread
    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    @MainThread
    public void call(){
        setValue(null);
    }

}
