package com.github.wahyuadepratama.whatsmovie.database.helper;

import androidx.room.TypeConverter;

import com.github.wahyuadepratama.whatsmovie.model.video.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class VideoConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Video> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Video>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Video> someObjects) {
        return gson.toJson(someObjects);
    }
}
