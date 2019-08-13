package com.github.wahyuadepratama.whatsmovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.wahyuadepratama.whatsmovie.database.AppDatabase;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FavoriteMovieProvider extends ContentProvider {

    public static final String AUTHORITY = "com.github.wahyuadepratama.whatsmovie.provider";
    private AppDatabase database;

    private static final int CODE_DIR = 1;
    private static final int CODE_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "favorite_movie", CODE_DIR);
        MATCHER.addURI(AUTHORITY, "favorite_movie" + "/*", CODE_ITEM);
    }

    @Override
    public boolean onCreate() {
        database = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_DIR || code == CODE_ITEM){
            final Context context = getContext();

            if (context == null) {
                return null;
            }

            database = AppDatabase.getInstance(context);
            Cursor cursor = null;

            if (code == CODE_DIR) {
                cursor = database.favoriteMovieDao().getAllFavoriteMovieWithCursor();
            }
            Objects.requireNonNull(cursor).setNotificationUri(context.getContentResolver(), uri);

            return cursor;
        }else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "Movie";
            case CODE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "Movie";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
