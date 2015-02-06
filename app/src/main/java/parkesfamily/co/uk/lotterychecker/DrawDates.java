package parkesfamily.co.uk.lotterychecker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gav on 03/02/2015.
 */
public class DrawDates
{
    public class DrawDate
    {
        final int Day;
        final int Month;
        final int Year;

        public DrawDate(int day, int month, int year)
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

    public DrawDates(int iDay, int iMonth, int iYear)
    {
        _cal = Calendar.getInstance();
        _cal.set(iYear, iMonth, iDay);
    }

    public ArrayList<DrawDate> getDrawDates()
    {
        ArrayList<DrawDate> lst = new ArrayList<DrawDate>();

        Calendar calNow = Calendar.getInstance();
        String str = calNow.toString();

        DrawDate first = new DrawDate(_cal.get(Calendar.DAY_OF_MONTH), _cal.get(Calendar.MONTH),
                                      _cal.get(Calendar.YEAR));

        lst.add(first);
        Calendar calNext = getNextDrawDate(_cal);
        // Iterate through all draw dates from _cal to now and add them to the list
        while (calNext.getTimeInMillis() < calNow.getTimeInMillis())
        {
            lst.add(new DrawDate(calNext.get(Calendar.YEAR),
                                 calNext.get(Calendar.MONTH),
                                 calNext.get(Calendar.DAY_OF_MONTH)
                                )
                    );

            calNext = getNextDrawDate(calNext);
        }

        return lst;
    }

    private Calendar getNextDrawDate(Calendar calCurrent)
    {
        final int iDay = calCurrent.get(Calendar.DAY_OF_WEEK);
        Calendar calNext = Calendar.getInstance();
        calNext.set(calCurrent.get(Calendar.YEAR),
                    calCurrent.get(Calendar.MONTH),
                    calCurrent.get(Calendar.DAY_OF_MONTH));

        if (iDay == Calendar.SATURDAY)
        {
            // Add 4 days (To get Wednesday)
            calNext.add(Calendar.DAY_OF_MONTH, 4);
        }
        else if (iDay == Calendar.WEDNESDAY)
        {
            // Add 3 days (To get Saturday)
            calNext.add(Calendar.DAY_OF_MONTH, 3);
        }
        /* else
            throw new Exception("Invalid Draw Day");*/

        return calNext;
    }
}
