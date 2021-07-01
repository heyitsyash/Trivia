package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controler.AppControler;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    ArrayList<Question> questionArrayList = new ArrayList<>();

    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListSync callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i <response.length() ; i++) {
                    try {
                        Question question = new Question(response.getJSONArray(i).get(0).toString(),
                                response.getJSONArray(i).getBoolean(1));

                        questionArrayList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(null != callBack) callBack.processFinished(questionArrayList);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppControler.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }

}
