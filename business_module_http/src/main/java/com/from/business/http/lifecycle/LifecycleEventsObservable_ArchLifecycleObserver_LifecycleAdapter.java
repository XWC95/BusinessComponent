package com.from.business.http.lifecycle;

import android.arch.lifecycle.GeneratedAdapter;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MethodCallsLogger;

public class LifecycleEventsObservable_ArchLifecycleObserver_LifecycleAdapter implements GeneratedAdapter {
    final LifecycleEventsObservable.ArchLifecycleObserver mReceiver;

    LifecycleEventsObservable_ArchLifecycleObserver_LifecycleAdapter(LifecycleEventsObservable.ArchLifecycleObserver receiver) {
        this.mReceiver = receiver;
    }

    @Override
    public void callMethods(LifecycleOwner owner, Lifecycle.Event event, boolean onAny, MethodCallsLogger logger) {
        boolean hasLogger = logger != null;
        if (onAny) {
            if (!hasLogger || logger.approveCall("onStateChange", 4)) {
                this.mReceiver.onStateChange(owner, event);
            }
        }
    }
}
