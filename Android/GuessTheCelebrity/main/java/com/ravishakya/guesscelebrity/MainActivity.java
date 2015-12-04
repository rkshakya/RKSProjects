package com.ravishakya.guesscelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int chosenCeleb = 0;
    int correctAnswerLoc = 0;
    ImageView imageView;
    String[] answerOptions = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public void celebChosen(View view){
        if(view.getTag().equals(Integer.toString(correctAnswerLoc))){
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Incorrect! Correct is " + answerOptions[correctAnswerLoc], Toast.LENGTH_LONG).show();
        }

        displayQuestion();
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                Bitmap mybitmap = BitmapFactory.decodeStream(in);
                return mybitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String result ="";
            HttpURLConnection connection;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                   char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        DownloadTask task = new DownloadTask();
        String result;
        try {
            result = task.execute("http://www.posh24.com/celebrities").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while(m.find()){
                //System.out.println(m.group(1));
                celebURLs.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);
            while(m.find()){
                //System.out.println(m.group(1));
                celebNames.add(m.group(1));
            }

            displayQuestion();
            //Log.i("RESult: ", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void displayQuestion(){
        //randomly select a celebrity
        Random rand = new Random();
        int chosenCeleb = rand.nextInt(celebNames.size());

        //display image for chosen
        ImageDownloader imageTask = new ImageDownloader();
        Bitmap celebImage = null;
        try {
            celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(celebImage);

        correctAnswerLoc = rand.nextInt(4);
        int incorrectAns;

        for(int i = 0; i < 4; i++){
            if(correctAnswerLoc == i){
                answerOptions[i] = celebNames.get(chosenCeleb);
            }else{
                incorrectAns = rand.nextInt(celebNames.size());

                while(incorrectAns == chosenCeleb){
                    incorrectAns = rand.nextInt(celebNames.size());
                }

                answerOptions[i] = celebNames.get(incorrectAns);
            }

        }

        //display answers on buttons
        button0.setText(answerOptions[0]);
        button1.setText(answerOptions[1]);
        button2.setText(answerOptions[2]);
        button3.setText(answerOptions[3]);
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
