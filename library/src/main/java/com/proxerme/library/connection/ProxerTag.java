package com.proxerme.library.connection;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.proxerme.library.connection.ProxerConnection.ConferencesRequest;
import static com.proxerme.library.connection.ProxerConnection.LoginRequest;
import static com.proxerme.library.connection.ProxerConnection.LogoutRequest;
import static com.proxerme.library.connection.ProxerConnection.NewsRequest;
import static com.proxerme.library.connection.ProxerConnection.ProxerRequest;

/**
 * Class which holds all the different tags for request.
 * @see ProxerRequest
 *
 * @author Ruben Gees
 */
public class ProxerTag {

    /**
     * Tag for the asynchronous {@link NewsRequest}.
     */
    public static final int NEWS = 0;

    /**
     * Tag for the synchronous {@link NewsRequest}.
     */
    public static final int NEWS_SYNC = 1;

    /**
     * Tag for the asynchronous {@link LoginRequest}.
     */
    public static final int LOGIN = 2;

    /**
     * Tag for the synchronous {@link LoginRequest}.
     */
    public static final int LOGIN_SYNC = 3;

    /**
     * Tag for the asynchronous {@link LogoutRequest}.
     */
    public static final int LOGOUT = 4;

    /**
     * Tag for the synchronous {@link LogoutRequest}.
     */
    public static final int LOGOUT_SYNC = 5;

    /**
     * Tag for the asynchronous {@link ConferencesRequest}.
     */
    public static final int CONFERENCES = 6;

    /**
     * Tag for the synchronous {@link ConferencesRequest}.
     */
    public static final int CONFERENCES_SYNC = 7;

    /**
     * An annotation, specifying the different tags of a request.
     */
    @IntDef({LOGIN, LOGIN_SYNC, NEWS, NEWS_SYNC, LOGOUT, LOGOUT_SYNC, CONFERENCES,
            CONFERENCES_SYNC})
    @Retention(value = RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    public @interface ConnectionTag {
    }
}