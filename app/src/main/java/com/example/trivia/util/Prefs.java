package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    public static final String HIGHEST_SCORE = "highestScore";
    public static final String STATE = "Trivia_State";
    private SharedPreferences preferences;

    public Prefs(Activity Context) {
        this.preferences = Context.getPreferences(android.content.Context.MODE_PRIVATE);
    }

    public void saveHighestScore( int score){

        int lastScore = preferences.getInt(HIGHEST_SCORE, 0);
                                 // it has to be initiated up(static final) ^

        if(score > lastScore){
            // We have a new highest score

            preferences.edit().putInt(HIGHEST_SCORE, score).apply();
        }
    }

    public int getHighestScore(){

        return preferences.getInt(HIGHEST_SCORE,0);
    }


  /*  public void setState(int index){
        preferences.edit().putInt(STATE, index).apply();
    }                          // create constant variable

    public int getState(){
        return preferences.getInt(STATE , 0);
    } */


}
