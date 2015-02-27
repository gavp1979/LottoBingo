package parkesfamily.co.uk.lotterychecker;

import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.base.BaseDateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

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

    private LocalDate _cal;
    //private LocalDate _date;

    public DrawDates(int iDay, int iMonth, int iYear)
    {
        /*_cal = Calendar.getInstance();
        _cal.set(iYear, iMonth, iDay);*/

        _cal = new LocalDate(iYear, iMonth, iDay);

        //_date = new LocalDate(iYear, iMonth, iDay);
    }

    public ArrayList<DrawDate> getDrawDates()
    {
        ArrayList<DrawDate> lst = new ArrayList<DrawDate>();

        LocalDate calNow = LocalDate.now();
        String str = calNow.toString();

        DrawDate first = new DrawDate(_cal.getDayOfMonth(), _cal.getMonthOfYear(),
                                      _cal.getYear());

        lst.add(first);
        LocalDate calNext = getNextDrawDate(_cal);

        // Iterate through all draw dates from _cal to now and add them to the list

        while (calNext.compareTo(calNow) < 0)
        {
            lst.add(new DrawDate(calNext.getYear(),
                                 calNext.getMonthOfYear(),
                                 calNext.getDayOfMonth()
                                )
                    );

            calNext = getNextDrawDate(calNext);
        }

        return lst;
    }

    private LocalDate getNextDrawDate(LocalDate calCurrent)
    {
        final int iDay = calCurrent.getDayOfWeek();
        LocalDate calNext = new LocalDate(calCurrent);

        if (iDay == DateTimeConstants.SATURDAY)
        {
            // Add 4 days (To get Wednesday)
            calNext = calCurrent.plusDays(4);// add(Calendar.DAY_OF_MONTH, 4);
        }
        else if (iDay == DateTimeConstants.WEDNESDAY)
        {
            // Add 3 days (To get Saturday)
            calNext = calCurrent.plusDays(3);
        }
        /* else
            throw new Exception("Invalid Draw Day");*/

        return calNext;
    }
}
