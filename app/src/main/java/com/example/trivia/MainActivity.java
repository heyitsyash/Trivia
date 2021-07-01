package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controler.AppControler;
import com.example.trivia.data.AnswerListSync;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;
    List<Question> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBinding.Util.setContentView(this, R.layout.activity_main);

        score = new Score();

        binding.scoreText.setText(MessageFormat.format("CurrentScore :{0}", String.valueOf(score.getScore())));

        prefs = new Prefs(this);

        //retrieve the last index
       // currentQuestionIndex = prefs.getState();

        questionsList = new Repository().getQuestion((ArrayList<Question> questionArrayList) -> {

            binding.highestScoreText.setText(MessageFormat.format("Highest :{0}", String.valueOf(prefs.getHighestScore()))); //Replace with Mess format

                    binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

                    updateCounter(questionArrayList);
                    // this part is "Question :" but it is formatted to string in string resources

                }

        );

        binding.buttonNext.setOnClickListener(v -> {
            getNextQuestion();
        });

        binding.buttonTrue.setOnClickListener(v -> {
            checkAnswer(true);
            updateQuestion();
        });

        binding.buttonFalse.setOnClickListener(v -> {
            checkAnswer(false);
            updateQuestion();
        });


    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex+1) % questionsList.size();
        updateQuestion();
    }

    private void checkAnswer(boolean userChooseCorrect) {

        boolean answer = questionsList.get(currentQuestionIndex).isAnswerIsTrue();

        int snackMessageId = 0;
        if(userChooseCorrect == answer){
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            addSubmission();
        }else{
            snackMessageId = R.string.incorrect;
            shakeAnimation();
            deleteSubmission();
        }

        Snackbar.make(binding.cardView, snackMessageId , Snackbar.LENGTH_SHORT).show();
                 //here you can pass any view(ex cardView) it doesn't matter
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.noOfQuestion.setText(String.format(getString(R.string.text_formatted),
                currentQuestionIndex, questionArrayList.size()));
    }

    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f , 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void shakeAnimation (){

        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateQuestion() {
        String question = questionsList.get(currentQuestionIndex).getAnswer();
        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionsList);
    }

    private void addSubmission(){
        scoreCounter += 100;
        score.setScore(scoreCounter);
      //  binding.scoreText.setText(String.valueOf(score.getScore()));
        binding.scoreText.setText(MessageFormat.format("CurrentScore :{0}", String.valueOf(score.getScore())));
    }

    private void deleteSubmission(){

        if(scoreCounter > 0){
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            binding.scoreText.setText(MessageFormat.format("CurrentScore :{0}", String.valueOf(score.getScore())));
        }else {
            scoreCounter =0;
            score.setScore(scoreCounter);
        }
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
       // prefs.setState(currentQuestionIndex);
        super.onPause();
    }


}