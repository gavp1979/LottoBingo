package parkesfamily.co.uk.lotterychecker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gav on 03/02/2015.
 */
public class GameDates
{
    public class GameDate
    {
        final int Day;
        final int Month;
        final int Year;

        public GameDate(int day, int month, int year)
        {
            this.Day = day;
            this.Month = month;
            this.Year = year;
        }

        public int getDay()
        {
            return Day;
        }

        public int getMonth()
        {
            return Month;
        }

        public int getYear()
        {
            return Year;
        }
    }

    private Calendar _cal;

    public GameDates(int iDay, int iMonth, int iYear)
    {
        _cal = Calendar.getInstance();
        _cal.set(iYear, iMonth, iYear);
    }

    public ArrayList<GameDate> getGameDates()
    {
        ArrayList<GameDate> lst = new ArrayList<GameDate>();

        Calendar now = Calendar.getInstance();

        Iterate through all draw dates from _cal to now and add them to the list

        return lst;
    }

    private GameDate getNextGameDate(GameDate current)
    {
        Calendar calCurrent = Calendar.getInstance();
        calCurrent.set(current.getYear(), current.getMonth(), current.getDay());

        if Day = Saturday
            Add 4 days (To get Wednesday)
        else if Day = Wednesday
            Add 3 days (To get Saturday)
        else
            throw Invalid Day Exception

        return the new GameDate
    }
}
