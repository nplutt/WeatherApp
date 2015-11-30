package com.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrentConditionsFragment extends AbstractVolleyRequest{
    private RequestQueue mQueue;
    private ViewGroup view;
    private int count = 0;
    private String msg = "";
    String day1Name = "";
    String day2Name = "";
    String day3Name = "";
    String day1DayDetails = "";
    String day1NightDetails = "";
    String day2DayDetails = "";
    String day2NightDetails = "";
    String day3DayDetails = "";
    String day3NightDetails = "";

    public CurrentConditionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        view = rootView;
        setDetails(view);
        return rootView;
    }

    void setMessage(String msg){
        this.msg = msg;
        if(count == 0){
            mQueue = volleyRequest.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
            String url = "http://api.wunderground.com/api/dc31d0a67b942c44/conditions/q/"+ msg +"/format.json";
            start(url,mQueue);
        }
        else if(count == 1){
            String url = "http://api.wunderground.com/api/dc31d0a67b942c44/forecast/q/"+ msg +"/format.json";
            start(url,mQueue);
        }
    }

    void setGpsLocation(String latitude, String longitude){
        mQueue = volleyRequest.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
        String url = "http://api.wunderground.com/api/dc31d0a67b942c44/conditions/q/"+ latitude + ","+ longitude +".json";
        start(url,mQueue);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        try {
            if(count == 0) {
                String city = ((JSONObject) response).getJSONObject("current_observation").getJSONObject("display_location").getString("full");
                String temp_val = ((JSONObject) response).getJSONObject("current_observation").getString("temp_f");
                String humid_val = ((JSONObject) response).getJSONObject("current_observation").getString("relative_humidity");
                String wind_speed = ((JSONObject) response).getJSONObject("current_observation").getString("wind_mph");
                String wind_dir = ((JSONObject) response).getJSONObject("current_observation").getString("wind_dir");
                String uv_val = ((JSONObject) response).getJSONObject("current_observation").getString("UV");
                //Sets the current conditions text
                displayText(view, city, temp_val, humid_val, wind_speed, wind_dir, uv_val);
                //Sends the second api call
                count ++;
                setMessage(msg);
            }
            else if(count == 1){
                //Gets the day's names
                day1Name = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("date").getString("weekday");
                day2Name = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("date").getString("weekday");
                day3Name = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getJSONObject("date").getString("weekday");

                //Sets the names in the three day forecast
                TextView day1a = (TextView) view.findViewById(R.id.day1Name);
                day1a.setText(day1Name);
                TextView day2a = (TextView) view.findViewById(R.id.day2Name);
                day2a.setText(day2Name);
                TextView day3a = (TextView) view.findViewById(R.id.day3Name);
                day3a.setText(day3Name);
                TextView day1d = (TextView) view.findViewById(R.id.Selected_Day);
                day1d.setText(day1Name);

                //Sets the day's images
                ImageView day1b = (ImageView) view.findViewById(R.id.day1Pic);
                ImageView day2b = (ImageView) view.findViewById(R.id.day2Pic);
                ImageView day3b = (ImageView) view.findViewById(R.id.day3Pic);

                //Sets the correct gifs based on the urls that were received (All gifs are stored locally)
                getCorrectGif(((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getString("icon_url"), day1b);
                getCorrectGif(((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getString("icon_url"), day2b);
                getCorrectGif(((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getString("icon_url"), day3b);

                //Sets the high and low temps
                String day1Hi = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("high").getString("fahrenheit");
                String day1Lo = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("low").getString("fahrenheit");
                String day1AvgWind = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("avewind").getString("mph");
                String day1AvgHumid = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0).getString("avehumidity");
                String day2Hi = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("high").getString("fahrenheit");
                String day2Lo = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("low").getString("fahrenheit");
                String day2AvgWind = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("avewind").getString("mph");
                String day2AvgHumid = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(1).getString("avehumidity");
                String day3Hi = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getJSONObject("high").getString("fahrenheit");
                String day3Lo = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getJSONObject("low").getString("fahrenheit");
                String day3AvgWind = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getJSONObject("avewind").getString("mph");
                String day3AvgHumid = ((JSONObject) response).getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(2).getString("avehumidity");

                //Sets the Hi/Lo for the 3 day forecast
                TextView day1c = (TextView) view.findViewById(R.id.day1HL);
                day1c.setText(day1Hi + "/" + day1Lo + " F");
                TextView day2c = (TextView) view.findViewById(R.id.day2HL);
                day2c.setText(day2Hi + "/" + day2Lo + " F");
                TextView day3c = (TextView) view.findViewById(R.id.day3HL);
                day3c.setText(day3Hi + "/" + day3Lo + " F");

                int point = 0;

                //Gets and stores the details for the days
                day1DayDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");
                point = point +1;
                day1NightDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");
                point ++;
                day2DayDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");
                point ++;
                day2NightDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");
                point ++;
                day3DayDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");
                point ++;
                day3NightDetails = ((JSONObject) response).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(point).getString("fcttext");

                //Sets the 1st days details so that it is displayed w/o being clicked on
                TextView details = (TextView) view.findViewById(R.id.info);
                details.setText("Day: " + day1DayDetails + " " + "Night: " + day1NightDetails);

                //Sets the count back to 0
                count --;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Sets the displayed text for the current conditions
    public void displayText(View view, String city, String temp_val, String humid_val, String wind_speed,
                            String wind_dir, String uv_val){

        TextView location = (TextView) view.findViewById(R.id.current_conditions_title);
        location.setText("Current Conditions " + city);

        TextView temp = (TextView) view.findViewById(R.id.temp_value);
        temp.setText(temp_val + "F");

        TextView humidity = (TextView) view.findViewById(R.id.humidity_value);
        humidity.setText(humid_val);

        TextView wind = (TextView) view.findViewById(R.id.wind_value);
        wind.setText(wind_speed + " " + wind_dir);

        TextView uv = (TextView) view.findViewById(R.id.uv_value);
        uv.setText( uv_val + "UV");
    }

    //Sets the gifs for the three day forecast based on the received urls
    public void getCorrectGif(String url, ImageView day){
        if(url.contains("snow") || url.contains("flurries")){
            day.setImageResource(R.drawable.snow);
        }
        else if(url.contains("rain")){
            day.setImageResource(R.drawable.rain);
        }
        else if(url.contains("sleet")){
            day.setImageResource(R.drawable.sleet);
        }
        else if((url.contains("nt_") == false && url.contains("mostly")) || ((url.contains("nt_") == false && url.contains("partly")))) {
            day.setImageResource(R.drawable.mostlycloudy);
        }
        else if((url.contains("nt_") == true && url.contains("mostly")) || ((url.contains("nt_") == true && url.contains("partly")))){
            day.setImageResource(R.drawable.nt_mostlycloudy);
        }
        else if(url.equals("http://icons.wxug.com/i/c/k/clear.gif")|| url.equals("http://icons.wxug.com/i/c/k/sunny.gif")){
            day.setImageResource(R.drawable.clear);
        }
        else if(url.equals("http://icons.wxug.com/i/c/k/nt_clear.gif")|| url.equals("http://icons.wxug.com/i/c/k/nt_sunny.gif")){
            day.setImageResource(R.drawable.nt_clear);
        }
        else if(url.contains("storms")){
            day.setImageResource(R.drawable.storms);
        }
        else{
            day.setImageResource(R.drawable.fog);
        }
    }

    //Sets the details for the selected day
    public void setDetails(ViewGroup root){

        //If day 1 is selected, sets day 1 details
        ImageView b1 = (ImageView) root.findViewById((R.id.day1Pic));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the day
                TextView day = (TextView) view.findViewById(R.id.Selected_Day);
                day.setText(day1Name);

                //Sets the text for details
                TextView details = (TextView) view.findViewById(R.id.info);
                details.setText("Day: " + day1DayDetails + " " + "Night: " + day1NightDetails);
            }
        });

        //If day 2 is selected, sets day 2 details
        ImageView b2 = (ImageView) root.findViewById((R.id.day2Pic));
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the day
                TextView day = (TextView) view.findViewById(R.id.Selected_Day);
                day.setText(day2Name);

                //Sets the text for details
                TextView details = (TextView) view.findViewById(R.id.info);
                details.setText("Day: " + day2DayDetails + " " + "Night: " + day2NightDetails);
            }
        });

        //If day 3 is selected, sets day 3 details
        ImageView b3 = (ImageView) root.findViewById((R.id.day3Pic));
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the day
                TextView day = (TextView) view.findViewById(R.id.Selected_Day);
                day.setText(day3Name);

                //Sets the text for details
                TextView details = (TextView) view.findViewById(R.id.info);
                details.setText("Day: " + day3DayDetails + " " + "Night: " + day3NightDetails);
            }
        });
    }
}
