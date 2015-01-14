package parkesfamily.co.uk.lotterychecker.database;

import android.content.ContentValues;
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
            + COLUMN_NUMBER_6 + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        addDefaults(database);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Table_Players.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(database);
    }

    private static void addDefaults(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, "Gav Parkes");
        values.put(COLUMN_NUMBER_1, 7);
        values.put(COLUMN_NUMBER_2, 14);
        values.put(COLUMN_NUMBER_3, 35);
        values.put(COLUMN_NUMBER_4, 30);
        values.put(COLUMN_NUMBER_5, 33);
        values.put(COLUMN_NUMBER_6, 44);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Broady");
        values.put(COLUMN_NUMBER_1, 5);
        values.put(COLUMN_NUMBER_2, 7);
        values.put(COLUMN_NUMBER_3, 10);
        values.put(COLUMN_NUMBER_4, 20);
        values.put(COLUMN_NUMBER_5, 38);
        values.put(COLUMN_NUMBER_6, 47);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Lee Rawson");
        values.put(COLUMN_NUMBER_1, 1);
        values.put(COLUMN_NUMBER_2, 5);
        values.put(COLUMN_NUMBER_3, 11);
        values.put(COLUMN_NUMBER_4, 14);
        values.put(COLUMN_NUMBER_5, 22);
        values.put(COLUMN_NUMBER_6, 23);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Gazzo");
        values.put(COLUMN_NUMBER_1, 9);
        values.put(COLUMN_NUMBER_2, 18);
        values.put(COLUMN_NUMBER_3, 24);
        values.put(COLUMN_NUMBER_4, 34);
        values.put(COLUMN_NUMBER_5, 40);
        values.put(COLUMN_NUMBER_6, 48);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Adam Rawson");
        values.put(COLUMN_NUMBER_1, 9);
        values.put(COLUMN_NUMBER_2, 10);
        values.put(COLUMN_NUMBER_3, 17);
        values.put(COLUMN_NUMBER_4, 18);
        values.put(COLUMN_NUMBER_5, 32);
        values.put(COLUMN_NUMBER_6, 34);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Streety");
        values.put(COLUMN_NUMBER_1, 2);
        values.put(COLUMN_NUMBER_2, 15);
        values.put(COLUMN_NUMBER_3, 22);
        values.put(COLUMN_NUMBER_4, 36);
        values.put(COLUMN_NUMBER_5, 40);
        values.put(COLUMN_NUMBER_6, 44);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "John W");
        values.put(COLUMN_NUMBER_1, 1);
        values.put(COLUMN_NUMBER_2, 6);
        values.put(COLUMN_NUMBER_3, 8);
        values.put(COLUMN_NUMBER_4, 14);
        values.put(COLUMN_NUMBER_5, 22);
        values.put(COLUMN_NUMBER_6, 24);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Grimmy");
        values.put(COLUMN_NUMBER_1, 8);
        values.put(COLUMN_NUMBER_2, 14);
        values.put(COLUMN_NUMBER_3, 15);
        values.put(COLUMN_NUMBER_4, 23);
        values.put(COLUMN_NUMBER_5, 32);
        values.put(COLUMN_NUMBER_6, 48);
        db.insert(TABLE_PLAYERS, null, values);

        values.put(COLUMN_NAME, "Martin");
        values.put(COLUMN_NUMBER_1, 4);
        values.put(COLUMN_NUMBER_2, 7);
        values.put(COLUMN_NUMBER_3, 12);
        values.put(COLUMN_NUMBER_4, 17);
        values.put(COLUMN_NUMBER_5, 20);
        values.put(COLUMN_NUMBER_6, 26);
        db.insert(TABLE_PLAYERS, null, values);
    }
}
