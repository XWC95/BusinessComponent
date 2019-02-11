package com.from.business.http.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.uber.autodispose.android.internal.MainThreadDisposable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.BehaviorSubject;

import static android.arch.lifecycle.Lifecycle.Event.ON_CREATE;
import static android.arch.lifecycle.Lifecycle.Event.ON_DESTROY;
import static android.arch.lifecycle.Lifecycle.Event.ON_RESUME;
import static android.arch.lifecycle.Lifecycle.Event.ON_START;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static com.uber.autodispose.android.internal.AutoDisposeAndroidUtil.isMainThread;

@RestrictTo(LIBRARY)
class LifecycleEventsObservable extends Observable<Lifecycle.Event> {
    private final Lifecycle lifecycle;
    private final BehaviorSubject<Lifecycle.Event> eventsObservable = BehaviorSubject.create();

    @SuppressWarnings("CheckReturnValue")
    LifecycleEventsObservable(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    Lifecycle.Event getValue() {
        return eventsObservable.getValue();
    }

    /**
     * Backfill if already created for boundary checking. We do a trick here for corresponding events
     * where we pretend something is created upon initialized state so that it assumes the
     * corresponding event is DESTROY.
     */
    void backfillEvents() {
        @Nullable Lifecycle.Event correspondingEvent;
        switch (lifecycle.getCurrentState()) {
            case INITIALIZED:
                correspondingEvent = ON_CREATE;
                break;
            case CREATED:
                correspondingEvent = ON_START;
                break;
            case STARTED:
            case RESUMED:
                correspondingEvent = ON_RESUME;
                break;
            case DESTROYED:
            default:
                correspondingEvent = ON_DESTROY;
                break;
        }
        eventsObservable.onNext(correspondingEvent);
    }

    @Override
    protected void subscribeActual(Observer<? super Lifecycle.Event> observer) {
        ArchLifecycleObserver archObserver = new ArchLifecycleObserver(lifecycle, observer, eventsObservable);
        observer.onSubscribe(archObserver);
        if (!isMainThread()) {
            observer.onError(new IllegalStateException("Lifecycles can only be bound to on the main thread!"));
            return;
        }
        lifecycle.addObserver(archObserver);
        if (archObserver.isDisposed()) {
            lifecycle.removeObserver(archObserver);
        }
    }

    static final class ArchLifecycleObserver extends MainThreadDisposable implements LifecycleObserver {
        private final Lifecycle lifecycle;
        private final Observer<? super Lifecycle.Event> observer;
        private final BehaviorSubject<Lifecycle.Event> eventsObservable;

        ArchLifecycleObserver(Lifecycle lifecycle,
            Observer<? super Event> observer,
            BehaviorSubject<Event> eventsObservable) {
            this.lifecycle = lifecycle;
            this.observer = observer;
            this.eventsObservable = eventsObservable;
        }

        @Override
        protected void onDispose() {
            lifecycle.removeObserver(this);
        }

        @OnLifecycleEvent(Event.ON_ANY)
        void onStateChange(@SuppressWarnings("unused") LifecycleOwner owner, Event event) {
            if (!isDisposed()) {
                if (!(event == ON_CREATE && eventsObservable.getValue() == event)) {
                    // Due to the INITIALIZED->ON_CREATE mapping trick we do in backfill(),
                    // we fire this conditionally to avoid duplicate CREATE events.
                    eventsObservable.onNext(event);
                }
                observer.onNext(event);
            }
        }
    }
}
