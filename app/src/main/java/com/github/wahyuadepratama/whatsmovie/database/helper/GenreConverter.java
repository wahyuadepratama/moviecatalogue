package com.github.wahyuadepratama.whatsmovie.database.helper;

import androidx.room.TypeConverter;

import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class GenreConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Genre> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Genre>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Genre> someObjects) {
        return gson.toJson(someObjects);
    }
}
