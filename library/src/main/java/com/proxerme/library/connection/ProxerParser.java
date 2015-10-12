package com.proxerme.library.connection;

import android.support.annotation.NonNull;

import com.proxerme.library.entity.LoginData;
import com.proxerme.library.entity.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing all different answers, received by the {@link ProxerConnection}.
 *
 * @author Ruben Gees
 */
public class ProxerParser {

    private static final String NEWS_ARRAY = "notifications";
    private static final String NEWS_ID = "nid";
    private static final String NEWS_TIME = "time";
    private static final String NEWS_DESCRIPTION = "description";
    private static final String NEWS_IMAGE_ID = "image_id";
    private static final String NEWS_SUBJECT = "subject";
    private static final String NEWS_HITS = "hits";
    private static final String NEWS_THREAD = "thread";
    private static final String NEWS_USER_ID = "uid";
    private static final String NEWS_USER_NAME = "uname";
    private static final String NEWS_POSTS = "posts";
    private static final String NEWS_CATEGORY_ID = "catid";
    private static final String NEWS_CATEGORY = "catname";

    @NonNull
    public static List<News> parseNewsJSON(@NonNull JSONObject object) throws JSONException {
        JSONArray newsArray = object.getJSONArray(NEWS_ARRAY);
        List<News> result = new ArrayList<>(newsArray.length());

        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject newsObject = newsArray.getJSONObject(i);

            result.add(new News(newsObject.getString(NEWS_ID), newsObject.getLong(NEWS_TIME),
                    newsObject.getString(NEWS_DESCRIPTION), newsObject.getString(NEWS_IMAGE_ID),
                    newsObject.getString(NEWS_SUBJECT), newsObject.getInt(NEWS_HITS),
                    newsObject.getString(NEWS_THREAD), newsObject.getString(NEWS_USER_ID),
                    newsObject.getString(NEWS_USER_NAME), newsObject.getInt(NEWS_POSTS),
                    newsObject.getString(NEWS_CATEGORY_ID), newsObject.getString(NEWS_CATEGORY)));
        }

        return result;
    }

    public static LoginData parseLoginJSON(@NonNull JSONObject object) throws JSONException {
        return new LoginData(object.getString("uid"), object.getString("avatar"));
    }
}
