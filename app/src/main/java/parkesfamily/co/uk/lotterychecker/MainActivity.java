package parkesfamily.co.uk.lotterychecker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private LotteryNumbers _myNumbers;

    TextView _lblMyNumbers, _lblLatestNumbers;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //getLatestNumbers();
        getDayOfWeek();
    }

    private void getLatestNumbers()
    {
        new LotteryReader().execute("");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 1)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlayersFragment.newInstance("1", "2"))
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section_main);
                break;
            case 2:
                mTitle = getString(R.string.title_section_players);
                break;
            case 3:
                mTitle = getString(R.string.title_section_draws);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private void getDayOfWeek()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 0, 1);
        int iDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        Toast.makeText(this, "Day of Week: " + iDayOfWeek, Toast.LENGTH_LONG).show();
    }

    private class LotteryReader extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute()
        {
            progressDialog.setMessage("Downloading latest results...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    LotteryReader.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params)
        {
            // Latest Draw
            final String url = "http://api.bentasker.co.uk/lottopredict/?action=LatestResults&" +
                                "key=751fbf6ddfb7c3857d898c21bfdc2b22&game=1&draws=Any";

            // For month
            //final String url = "http://api.bentasker.co.uk/lottopredict/?action=retrieve&" +
            //                    "month=2015-01&key= 751fbf6ddfb7c3857d898c21bfdc2b22&game=1";
            
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try
            {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read
                inputStream = httpEntity.getContent();
            }
            catch (UnsupportedEncodingException e1)
            {
                Log.e("UnsupprtedEncodingException", e1.toString());
                e1.printStackTrace();
            }
            catch (ClientProtocolException e2)
            {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            }
            catch (IllegalStateException e3)
            {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            }
            catch (IOException e4)
            {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }


            // Try to convert the response using string builder
            try
            {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream,
                                                                                  "iso-8859-1"), 1);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null)
                {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            }
            catch (Exception e)
            {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }

            return null;
        }

        protected void onPostExecute(Void v)
        {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

            try
            {
                JSONObject jo = new JSONObject(result);

                LotteryNumbers latestNumbers = new LotteryNumbers();
                latestNumbers.set_No1(jo.getInt("Ball1"));
                latestNumbers.set_No2(jo.getInt("Ball2"));
                latestNumbers.set_No3(jo.getInt("Ball3"));
                latestNumbers.set_No4(jo.getInt("Ball4"));
                latestNumbers.set_No5(jo.getInt("Ball5"));
                latestNumbers.set_No6(jo.getInt("Ball6"));

                _lblMyNumbers = (TextView) MainActivity.this.findViewById(R.id.lblMyNumbersData);
                _lblLatestNumbers = (TextView) MainActivity.this.findViewById(
                        R.id.lblLatestNumbersData);

                _myNumbers = new LotteryNumbers(7, 14, 25, 30, 33, 44);
                _lblMyNumbers.setText(_myNumbers.toString());
                _lblLatestNumbers.setText(latestNumbers.toString());
            }
            catch (JSONException ex)
            {
                Log.e("JSONException", ex.getMessage());
                ex.printStackTrace();
            }

            this.progressDialog.dismiss();
        }

    }

    private class LotteryNumbers
    {
        private int _No1, _No2, _No3, _No4, _No5, _No6;

        private LotteryNumbers()
        {}

        private LotteryNumbers(int _No1, int _No2, int _No3, int _No4, int _No5, int _No6)
        {
            this._No1 = _No1;
            this._No2 = _No2;
            this._No3 = _No3;
            this._No4 = _No4;
            this._No5 = _No5;
            this._No6 = _No6;
        }

        public int get_No1()
        {
            return _No1;
        }

        public void set_No1(int _No1)
        {
            this._No1 = _No1;
        }

        public int get_No2()
        {
            return _No2;
        }

        public void set_No2(int _No2)
        {
            this._No2 = _No2;
        }

        public int get_No3()
        {
            return _No3;
        }

        public void set_No3(int _No3)
        {
            this._No3 = _No3;
        }

        public int get_No4()
        {
            return _No4;
        }

        public void set_No4(int _No4)
        {
            this._No4 = _No4;
        }

        public int get_No5()
        {
            return _No5;
        }

        public void set_No5(int _No5)
        {
            this._No5 = _No5;
        }

        public int get_No6()
        {
            return _No6;
        }

        public void set_No6(int _No6)
        {
            this._No6 = _No6;
        }

        @Override
        public String toString()
        {
            return _No1 + "; " + _No2 + "; " + _No3 + "; " + _No4 + "; " + _No5 + "; " + _No6;
        }
    }
}
