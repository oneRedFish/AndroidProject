package com.example.minyiyang.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.VISIBLE;

public class WeatherForecast extends Activity {
    ProgressBar pb;
    TextView TextView_curtem;
    TextView TextView_mintem;
    TextView TextView_maxtem;
    TextView TextView_speed;
    ImageView ImageView_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        pb = (ProgressBar) findViewById(R.id.ProgressBar);
        TextView_curtem = (TextView) findViewById(R.id.TextView_curtem);
        TextView_mintem = (TextView) findViewById(R.id.TextView_mintem);
        TextView_maxtem = (TextView) findViewById(R.id.TextView_maxtem);
        TextView_speed = (TextView) findViewById(R.id.TextView_speed);
        ImageView_pic = (ImageView) findViewById(R.id.ImageView_pic);
        pb.setVisibility(VISIBLE);
        //run thread
        new ForecastQuery().execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String wind_speed;
        private String min;
        private String max;
        private String current_temp;
        private Bitmap bm;
        private String iconName;
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                for (String siteUrl : args) {
                    URL url = new URL(siteUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream iStream = urlConnection.getInputStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(iStream, "UTF-8");

                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (xpp.getEventType() == XmlPullParser.START_TAG) {
                            if (xpp.getName().equals("temperature")) {
                                current_temp = xpp.getAttributeValue(null, "value");
                                Log.i("XML Message:", current_temp);
                                Thread.sleep(500);
                                publishProgress(20);
                                min = xpp.getAttributeValue(null, "min");
                                Log.i("XML Message:", min);
                                Thread.sleep(500);
                                publishProgress(40);
                                max = xpp.getAttributeValue(null, "max");
                                Log.i("XML Message:", max);
                                Thread.sleep(500);
                                publishProgress(60);
                            } else if (xpp.getName().equals("speed")) {
                                wind_speed = xpp.getAttributeValue(null, "value");
                                Log.i("XML Message:", wind_speed);
                                Thread.sleep(500);
                                publishProgress(80);
                            } else if (xpp.getName().equals("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");
                                Log.i("XML Message:", iconName);
                                Thread.sleep(500);
                                publishProgress(90);
                            }

                        }
                        eventType = xpp.next();
                    }
                    //download image from url
                    class HTTPUtils {
                        public Bitmap getImage(URL url) {
                            HttpURLConnection connection = null;
                            try {
                                connection = (HttpURLConnection) url.openConnection();
                                connection.connect();
                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    return BitmapFactory.decodeStream(connection.getInputStream());
                                } else
                                    return null;
                            } catch (Exception e) {
                                return null;
                            } finally {
                                if (connection != null) {
                                    connection.disconnect();
                                }
                            }
                        }

                        public Bitmap getImage(String urlString) {
                            try {
                                URL url = new URL(urlString);
                                return getImage(url);
                            } catch (MalformedURLException e) {
                                return null;
                            }
                        }

                    }
                    //store image
                    Bitmap image = new HTTPUtils().getImage("http://openweathermap.org/img/w/" + iconName + ".png");
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Thread.sleep(500);
                    publishProgress(100);
                    //read the image
                    if (fileExistance(iconName + ".png")) {
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(iconName + ".png");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bm = BitmapFactory.decodeStream(fis);
                        Log.i("fileExistance name is:", iconName);
                    } else {
                        Log.i("Notice Message:", "file is not Existed need to be downloaded");
                    }
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (XmlPullParserException e3) {
                e3.printStackTrace();
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }

            return "Finished";

        }

        //check image
        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pb.setVisibility(VISIBLE);
            pb.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            pb.setVisibility(VISIBLE);
            TextView_curtem.setText(" Current temperature is: " + current_temp);
            TextView_maxtem.setText(" Max temperature is: " + max);
            TextView_mintem.setText(" Min temperature is: " + min);
            TextView_speed.setText(" Min wind speed is: " + wind_speed + " m/s");
            //ImageView_pic;
            ImageView_pic.setImageBitmap(bm);
        }

    }
}
