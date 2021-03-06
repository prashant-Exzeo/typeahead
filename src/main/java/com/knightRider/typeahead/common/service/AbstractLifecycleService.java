package com.knightRider.typeahead.common.service;

import com.knightRider.typeahead.TypeaheadException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

/**
 *
 */
public abstract class AbstractLifecycleService<T> extends AbstractService implements ServiceCycle<T> {

    protected final Lifecycle lifecycle = new Lifecycle();

    private final List<LifecycleListener> listeners = new CopyOnWriteArrayList<>();

    protected AbstractLifecycleService() {

    }

    @Override
    public Lifecycle.State lifecycleState() {
        return this.lifecycle.state();
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public T start() throws TypeaheadException {
        if (!lifecycle.canMoveToStarted()) {
            return (T) this;
        }
        for (LifecycleListener listener : listeners) {
            listener.beforeStart();
        }
        doStart();
        lifecycle.moveToStarted();
        for (LifecycleListener listener : listeners) {
            listener.afterStart();
        }
        return (T) this;
    }

    protected abstract void doStart() throws TypeaheadException;

    @SuppressWarnings({"unchecked"})
    @Override
    public T stop() throws TypeaheadException {
        if (!lifecycle.canMoveToStopped()) {
            return (T) this;
        }
        for (LifecycleListener listener : listeners) {
            listener.beforeStop();
        }
        lifecycle.moveToStopped();
        doStop();
        for (LifecycleListener listener : listeners) {
            listener.afterStop();
        }
        return (T) this;
    }

    protected abstract void doStop() throws TypeaheadException;

    @Override
    public void close() throws TypeaheadException {
        if (lifecycle.started()) {
            stop();
        }
        if (!lifecycle.canMoveToClosed()) {
            return;
        }
        for (LifecycleListener listener : listeners) {
            listener.beforeClose();
        }
        lifecycle.moveToClosed();
        doClose();
        for (LifecycleListener listener : listeners) {
            listener.afterClose();
        }
    }

    protected abstract void doClose() throws TypeaheadException;
}
