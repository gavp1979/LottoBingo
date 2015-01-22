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

import parkesfamily.co.uk.lotterychecker.database.DatabaseHelper;
import parkesfamily.co.uk.lotterychecker.database.Table_Draws;

/**
 * Created by Gav on 22/01/2015.
 */
public class DrawsContentProvider extends ContentProvider
{
    // database
    private DatabaseHelper database;

    // used for the UriMacher
    private static final int DRAWS = 10;
    private static final int DRAWS_ID = 20;

    private static final String AUTHORITY = "uk.co.parkesfamily.android.draws.contentprovider";

    private static final String BASE_PATH = "draws";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                                                            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/draws";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/draw";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, DRAWS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DRAWS_ID);
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
        queryBuilder.setTables(Table_Draws.TABLE_DRAWS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case DRAWS:
                break;
            case DRAWS_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(Table_Draws.COLUMN_ID + "="
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
            case DRAWS:
                id = sqlDB.insert(Table_Draws.TABLE_DRAWS, null, values);
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
            case DRAWS:
                rowsDeleted = sqlDB.delete(Table_Draws.TABLE_DRAWS, selection,
                                           selectionArgs);
                break;
            case DRAWS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Table_Draws.TABLE_DRAWS,
                                               Table_Draws.COLUMN_ID + "=" + id,
                                               null);
                } else {
                    rowsDeleted = sqlDB.delete(Table_Draws.TABLE_DRAWS,
                                               Table_Draws.COLUMN_ID + "=" + id
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
            case DRAWS:
                rowsUpdated = sqlDB.update(Table_Draws.TABLE_DRAWS,
                                           values,
                                           selection,
                                           selectionArgs);
                break;
            case DRAWS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(Table_Draws.TABLE_DRAWS,
                                               values,
                                               Table_Draws.COLUMN_ID + "=" + id,
                                               null);
                } else {
                    rowsUpdated = sqlDB.update(Table_Draws.TABLE_DRAWS,
                                               values,
                                               Table_Draws.COLUMN_ID + "=" + id
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
        String[] available = { Table_Draws.COLUMN_ID,
                Table_Draws.COLUMN_INTEGER_ID, Table_Draws.COLUMN_DATE, Table_Draws.COLUMN_DRAW_ID,
                Table_Draws.COLUMN_DAY, Table_Draws.COLUMN_GAME, Table_Draws.COLUMN_BALL_1,
                Table_Draws.COLUMN_BALL_2, Table_Draws.COLUMN_BALL_3,
                Table_Draws.COLUMN_BALL_4, Table_Draws.COLUMN_BALL_5,
                Table_Draws.COLUMN_BALL_6};
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
