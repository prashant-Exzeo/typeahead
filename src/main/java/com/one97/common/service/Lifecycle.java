package com.one97.common.service;

import org.elasticsearch.ElasticsearchIllegalStateException;

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
 * Lifecycle state. Allows the following transitions:
 * <ul>
 * <li>INITIALIZED -> STARTED, STOPPED, CLOSED</li>
 * <li>STARTED     -> STOPPED</li>
 * <li>STOPPED     -> STARTED, CLOSED</li>
 * <li>CLOSED      -> </li>
 * </ul>
 * <p/>
 * <p>Also allows to stay in the same state. For example, when calling stop on a component, the
 * following logic can be applied:
 * <p/>
 * <pre>
 * public void stop() {
 *  if (!lifeccycleState.moveToStopped()) {
 *      return;
 *  }
 * // continue with stop logic
 * }
 * </pre>
 * <p/>
 * <p>Note, closed is only allowed to be called when stopped, so make sure to stop the component first.
 * Here is how the logic can be applied:
 * <p/>
 * <pre>
 * public void close() {
 *  if (lifecycleState.started()) {
 *      stop();
 *  }
 *  if (!lifecycleState.moveToClosed()) {
 *      return;
 *  }
 *  // perofrm close logic here
 * }
 * </pre>
 */
public class Lifecycle {

    public static enum State {
        INITIALIZED,
        STOPPED,
        STARTED,
        CLOSED
    }

    private volatile State state = State.INITIALIZED;

    public State state() {
        return this.state;
    }

    /**
     * Returns <tt>true</tt> if the state is initialized.
     */
    public boolean initialized() {
        return state == State.INITIALIZED;
    }

    /**
     * Returns <tt>true</tt> if the state is started.
     */
    public boolean started() {
        return state == State.STARTED;
    }

    /**
     * Returns <tt>true</tt> if the state is stopped.
     */
    public boolean stopped() {
        return state == State.STOPPED;
    }

    /**
     * Returns <tt>true</tt> if the state is closed.
     */
    public boolean closed() {
        return state == State.CLOSED;
    }

    public boolean stoppedOrClosed() {
        Lifecycle.State state = this.state;
        return state == State.STOPPED || state == State.CLOSED;
    }

    public boolean canMoveToStarted() throws ElasticsearchIllegalStateException {
        State localState = this.state;
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            return true;
        }
        if (localState == State.STARTED) {
            return false;
        }
        if (localState == State.CLOSED) {
            throw new ElasticsearchIllegalStateException("Can't move to started state when closed");
        }
        throw new ElasticsearchIllegalStateException("Can't move to started with unknown state");
    }


    public boolean moveToStarted() throws ElasticsearchIllegalStateException {
        State localState = this.state;
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            state = State.STARTED;
            return true;
        }
        if (localState == State.STARTED) {
            return false;
        }
        if (localState == State.CLOSED) {
            throw new ElasticsearchIllegalStateException("Can't move to started state when closed");
        }
        throw new ElasticsearchIllegalStateException("Can't move to started with unknown state");
    }

    public boolean canMoveToStopped() throws ElasticsearchIllegalStateException {
        State localState = state;
        if (localState == State.STARTED) {
            return true;
        }
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            return false;
        }
        if (localState == State.CLOSED) {
            throw new ElasticsearchIllegalStateException("Can't move to started state when closed");
        }
        throw new ElasticsearchIllegalStateException("Can't move to started with unknown state");
    }

    public boolean moveToStopped() throws ElasticsearchIllegalStateException {
        State localState = state;
        if (localState == State.STARTED) {
            state = State.STOPPED;
            return true;
        }
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            return false;
        }
        if (localState == State.CLOSED) {
            throw new ElasticsearchIllegalStateException("Can't move to started state when closed");
        }
        throw new ElasticsearchIllegalStateException("Can't move to started with unknown state");
    }

    public boolean canMoveToClosed() throws ElasticsearchIllegalStateException {
        State localState = state;
        if (localState == State.CLOSED) {
            return false;
        }
        if (localState == State.STARTED) {
            throw new ElasticsearchIllegalStateException("Can't move to closed before moving to stopped mode");
        }
        return true;
    }


    public boolean moveToClosed() throws ElasticsearchIllegalStateException {
        State localState = state;
        if (localState == State.CLOSED) {
            return false;
        }
        if (localState == State.STARTED) {
            throw new ElasticsearchIllegalStateException("Can't move to closed before moving to stopped mode");
        }
        state = State.CLOSED;
        return true;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
