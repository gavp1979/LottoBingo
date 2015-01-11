package parkesfamily.co.uk.lotterychecker;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Gav on 09/01/2015.
 */
public class Table_Players
{
    // Database table
    public static final String TABLE_PLAYERS = "players";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER_1 = "number1";
    public static final String COLUMN_NUMBER_2 = "number2";
    public static final String COLUMN_NUMBER_3 = "number3";
    public static final String COLUMN_NUMBER_4 = "number4";
    public static final String COLUMN_NUMBER_5 = "number5";
    public static final String COLUMN_NUMBER_6 = "number6";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLAYERS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_NUMBER_1 + " integer not null,"
            + COLUMN_NUMBER_2 + " integer not null,"
            + COLUMN_NUMBER_3 + " integer not null,"
            + COLUMN_NUMBER_4 + " integer not null,"
            + COLUMN_NUMBER_5 + " integer not null,"
            + COLUMN_NUMBER_6 + " integer not null,"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Table_Players.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(database);
    }
}
