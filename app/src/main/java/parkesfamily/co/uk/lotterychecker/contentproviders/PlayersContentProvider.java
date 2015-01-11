package parkesfamily.co.uk.lotterychecker.contentproviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import parkesfamily.co.uk.lotterychecker.Table_Players;
import parkesfamily.co.uk.lotterychecker.database.DatabaseHelper;

/**
 * Created by Gav on 09/01/2015.
 */
public class PlayersContentProvider extends ContentProvider
{
    // database
    private DatabaseHelper database;

    // used for the UriMacher
    private static final int PLAYERS = 10;
    private static final int PLAYERS_ID = 20;

    private static final String AUTHORITY = "uk.co.parkesfamily.android.players.contentprovider";

    private static final String BASE_PATH = "players";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                                                            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/todos";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/todo";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PLAYERS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLAYERS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(Table_Players.TABLE_PLAYERS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PLAYERS:
                break;
            case PLAYERS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(Table_Players.COLUMN_ID + "="
                                                 + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                                           selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case PLAYERS:
                id = sqlDB.insert(Table_Players.TABLE_PLAYERS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PLAYERS:
                rowsDeleted = sqlDB.delete(Table_Players.TABLE_PLAYERS, selection,
                                           selectionArgs);
                break;
            case PLAYERS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Table_Players.TABLE_PLAYERS,
                                               Table_Players.COLUMN_ID + "=" + id,
                                               null);
                } else {
                    rowsDeleted = sqlDB.delete(Table_Players.TABLE_PLAYERS,
                                               Table_Players.COLUMN_ID + "=" + id
                                                       + " and " + selection,
                                               selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PLAYERS:
                rowsUpdated = sqlDB.update(Table_Players.TABLE_PLAYERS,
                                           values,
                                           selection,
                                           selectionArgs);
                break;
            case PLAYERS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(Table_Players.TABLE_PLAYERS,
                                               values,
                                               Table_Players.COLUMN_ID + "=" + id,
                                               null);
                } else {
                    rowsUpdated = sqlDB.update(Table_Players.TABLE_PLAYERS,
                                               values,
                                               Table_Players.COLUMN_ID + "=" + id
                                                       + " and "
                                                       + selection,
                                               selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { Table_Players.COLUMN_ID,
                Table_Players.COLUMN_NAME, Table_Players.COLUMN_NUMBER_1,
                Table_Players.COLUMN_NUMBER_2, Table_Players.COLUMN_NUMBER_3,
                Table_Players.COLUMN_NUMBER_4, Table_Players.COLUMN_NUMBER_5,
                Table_Players.COLUMN_NUMBER_6};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
