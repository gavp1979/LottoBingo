package parkesfamily.co.uk.lotterychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Gav on 22/01/2015.
 */
public class Table_Draws
{
    // Database table
    public static final String TABLE_DRAWS = "draws";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INTEGER_ID = "integerid";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GAME = "game";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_DRAW_ID = "drawid";
    public static final String COLUMN_BALL_1 = "ball1";
    public static final String COLUMN_BALL_2 = "ball2";
    public static final String COLUMN_BALL_3 = "ball3";
    public static final String COLUMN_BALL_4 = "ball4";
    public static final String COLUMN_BALL_5 = "ball5";
    public static final String COLUMN_BALL_6 = "ball6";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_DRAWS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_INTEGER_ID + " integer not null, "
            + COLUMN_DATE + " integer not null, "
            + COLUMN_GAME + " integer not null, "
            + COLUMN_DAY + " varchar(3) not null, "
            + COLUMN_DRAW_ID + " integer not null, "
            + COLUMN_BALL_1 + " integer not null,"
            + COLUMN_BALL_2 + " integer not null,"
            + COLUMN_BALL_3 + " integer not null,"
            + COLUMN_BALL_4 + " integer not null,"
            + COLUMN_BALL_5 + " integer not null,"
            + COLUMN_BALL_6 + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Table_Players.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAWS);
        onCreate(database);
    }
}
