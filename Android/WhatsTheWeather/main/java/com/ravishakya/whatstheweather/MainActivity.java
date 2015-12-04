package com.ravishakya.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    Button btnGo;
    TextView weather;

    public void displayWeather(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {
            DownloadTask task = new DownloadTask();
            String city = cityName.getText().toString();
            city = URLEncoder.encode(city, "UTF-8");
            task.execute("http://api.openweathermap.org/data/2.5/weather?APPID=38d673f38ab2668d926985f4970dd404&q=" + city);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            String result = "";
            try {
                url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Count not find weather", Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject js = new JSONObject(result);
                String weatherInfo = js.getString("weather");
                Log.i("WEATHER: ", weatherInfo);
                String message = "";

                JSONArray jsonarr = new JSONArray(weatherInfo);
                for(int i = 0 ; i < jsonarr.length(); i++ ){
                    JSONObject part = jsonarr.getJSONObject(i);
                    String main = "";
                    String desc = "";

                    Log.i("main ", part.getString("main"));
                    Log.i("description ", part.getString("description"));

                    main = part.getString("main");
                    desc = part.getString("description");

                    if(main != "" && desc != ""){
                        message += main + ": " + desc + "\r\ns";
                    }

                    //weather.setText(part.getString("main") + " : " + part.getString("description"));
                }
                if (message != "") {
                    weather.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(), "Count not find weather", Toast.LENGTH_LONG);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Count not find weather", Toast.LENGTH_LONG);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.editText);
        btnGo = (Button) findViewById(R.id.button);
        weather = (TextView) findViewById(R.id.textView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
