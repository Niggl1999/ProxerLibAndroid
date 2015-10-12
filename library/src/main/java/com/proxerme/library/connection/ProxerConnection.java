package com.proxerme.library.connection;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.afollestad.bridge.ResponseValidator;
import com.proxerme.library.entity.LoginData;
import com.proxerme.library.entity.LoginUser;
import com.proxerme.library.entity.News;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static com.proxerme.library.connection.ErrorHandler.ErrorCodes.PROXER;
import static com.proxerme.library.connection.ErrorHandler.ErrorCodes.UNKNOWN;
import static com.proxerme.library.connection.ErrorHandler.ErrorCodes.UNPARSEABLE;

/**
 * A helper class, which starts all request and manages the {@link Bridge}.
 *
 * @author Ruben Gees
 */
public class ProxerConnection {

    public static final int TAG_NEWS = 0;
    public static final int TAG_NEWS_SYNC = 1;
    public static final int TAG_LOGIN = 2;
    public static final int TAG_LOGIN_SYNC = 3;
    public static final int TAG_LOGOUT = 4;
    public static final int TAG_LOGOUT_SYNC = 5;
    private static final String FORM_USERNAME = "username";
    private static final String FORM_PASSWORD = "password";
    private static final String RESPONSE_ERROR = "error";
    private static final String RESPONSE_ERROR_MESSAGE = "msg";
    private static final String VALIDATOR_ID = "default-validator";

    public static void loadNews(@IntRange(from = 1) int page,
                                @NonNull final ResultCallback<List<News>> callback) {
        Bridge.client().get(UrlHolder.getNewsUrl(page)).tag(TAG_NEWS).request(new Callback() {
            @Override
            public void response(Request request, Response response, BridgeException exception) {
                if (exception == null) {
                    try {
                        callback.onResult(ProxerParser.parseNewsJSON(response.asJsonObject()));
                    } catch (JSONException e) {
                        callback.onError(new ProxerException(UNPARSEABLE));
                    } catch (BridgeException e) {
                        callback.onError(ErrorHandler.handleException(e));
                    }
                } else {
                    if (exception.reason() != BridgeException.REASON_REQUEST_CANCELLED) {
                        callback.onError(ErrorHandler.handleException(exception));
                    }
                }
            }
        });
    }

    public static List<News> loadNewsSync(@IntRange(from = 1) int page) throws ProxerException {
        try {
            JSONObject result = Bridge.client().get(UrlHolder.getNewsUrl(page)).tag(TAG_NEWS_SYNC)
                    .asJsonObject();

            return ProxerParser.parseNewsJSON(result);
        } catch (BridgeException e) {
            throw ErrorHandler.handleException(e);
        } catch (JSONException e) {
            throw ErrorHandler.handleException(e);
        }
    }

    public static void login(@NonNull final LoginUser user,
                             @NonNull final ResultCallback<LoginUser> callback) {
        Form loginCredentials = new Form().add(FORM_USERNAME, user.getUsername())
                .add(FORM_PASSWORD, user.getPassword());

        Bridge.client().post(UrlHolder.getLoginUrl()).body(loginCredentials).tag(TAG_LOGIN)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException exception) {
                        if (exception == null) {
                            try {
                                LoginData data = ProxerParser
                                        .parseLoginJSON(response.asJsonObject());

                                callback.onResult(new LoginUser(user.getUsername(),
                                        user.getPassword(), data.getId(),
                                        data.getImageLink()));
                            } catch (JSONException e) {
                                callback.onError(new ProxerException(UNPARSEABLE));
                            } catch (BridgeException e) {
                                callback.onError(ErrorHandler.handleException(e));
                            }
                        } else {
                            if (exception.reason() != BridgeException.REASON_REQUEST_CANCELLED) {
                                callback.onError(ErrorHandler.handleException(exception));
                            }
                        }
                    }
                });
    }

    public static LoginUser loginSync(@NonNull final LoginUser user) throws ProxerException {
        Form loginCredentials = new Form().add(FORM_USERNAME, user.getUsername())
                .add(FORM_PASSWORD, user.getPassword());
        try {
            JSONObject result = Bridge.client().post(UrlHolder.getLoginUrl()).tag(TAG_LOGIN_SYNC)
                    .body(loginCredentials).asJsonObject();
            LoginData data = ProxerParser.parseLoginJSON(result);

            return new LoginUser(user.getUsername(), user.getPassword(), data.getId(),
                    data.getImageLink());
        } catch (JSONException e) {
            throw ErrorHandler.handleException(e);
        } catch (BridgeException e) {
            throw ErrorHandler.handleException(e);
        }
    }

    public static void logout(@NonNull final ResultCallback<Void> callback) {
        Bridge.client().get(UrlHolder.getLogoutUrl()).tag(TAG_LOGOUT).request(new Callback() {
            @Override
            public void response(Request request, Response response, BridgeException exception) {
                if (exception != null) {
                    if (exception.reason() != BridgeException.REASON_REQUEST_CANCELLED) {
                        callback.onError(ErrorHandler.handleException(exception));
                    }
                }
            }
        });
    }

    public static void logoutSync() throws ProxerException {
        try {
            Bridge.client().get(UrlHolder.getLogoutUrl()).tag(TAG_LOGOUT_SYNC).request();
        } catch (BridgeException e) {
            throw ErrorHandler.handleException(e);
        }
    }

    public static void cancel(@ConnectionTag int tag, boolean force) {
        Bridge.client().cancelAll(tag, force);
    }

    public static void cancelSync(@ConnectionTag int tag, boolean force) {
        Bridge.client().cancelAllSync(tag, force);
    }

    public static void init() {
        Bridge.client().config().validators(new ResponseValidator() {
            @Override
            public boolean validate(@NonNull Response response) throws Exception {
                JSONObject json = response.asJsonObject();

                if (json.has(RESPONSE_ERROR)) {
                    if (json.getInt(RESPONSE_ERROR) == 0) {
                        return true;
                    } else {
                        if (json.has(RESPONSE_ERROR_MESSAGE)) {
                            throw new ProxerException(PROXER,
                                    json.getString(RESPONSE_ERROR_MESSAGE));
                        } else {
                            throw new ProxerException(UNKNOWN);
                        }
                    }
                } else {
                    return false;
                }
            }

            @NonNull
            @Override
            public String id() {
                return VALIDATOR_ID;
            }
        });
    }

    public static void cleanup() {
        Bridge.cleanup();
    }

    public interface ResultCallback<T> {
        void onResult(@NonNull T result);

        void onError(@NonNull ProxerException exception);
    }

    @IntDef({TAG_LOGIN, TAG_LOGIN_SYNC, TAG_NEWS, TAG_NEWS_SYNC, TAG_LOGOUT, TAG_LOGOUT_SYNC})
    @Retention(value = RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    public @interface ConnectionTag {
    }
}
