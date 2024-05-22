package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MainQuestionsActivity extends AppCompatActivity {

    int selectedAnswerIndex = -1; //to keep track of the selected answer
    int correctAnswersCount = 0; //to keep a count of the questions answered correctly
    int currentQuestionIndex = 0; //the index of the question being displayed
    ArrayList<TriviaQuestion> questionsList = new ArrayList<TriviaQuestion>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_questions);

        //Reads questions from input file
        questionsList = getQuestions();

           displayQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private ArrayList<TriviaQuestion> getQuestions(){
        ArrayList<TriviaQuestion> questions = new ArrayList<>();
        Scanner File = new Scanner(getResources().openRawResource(R.raw.questions));
        while (File.hasNext()) {
            String question = File.nextLine();
            String option0 = File.nextLine();
            String option1 = File.nextLine();
            String option2 = File.nextLine();
            String option3 = File.nextLine();
            int answerChoice = Integer.parseInt(File.nextLine());
            TriviaQuestion group = new TriviaQuestion(question,option0, option1, option2, option3, answerChoice);
            questions.add(group);
        }
            Random random = new Random();
            int[] numbers = new int[10];
            numbers[0] = random.nextInt(questions.size());
            ArrayList<TriviaQuestion> questions10 = new ArrayList<>();
            for(int i = 0; i < 10; i+=1){
                if(i == 0){
                    questions10.add(questions.get(numbers[0]));
                    continue;
                }
                numbers[i] = (numbers[i-1] + 1) % questions.size();
                questions10.add(questions.get(numbers[i]));
            }
                return questions10;
        }

    public void submitAnswer(View v){
        if (selectedAnswerIndex != -1) {
            if (selectedAnswerIndex == questionsList.get(currentQuestionIndex).getCorrectAnswerIndex()) {
                correctAnswersCount++;
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questionsList.size()) {
                selectedAnswerIndex = -1; //Reset the selected answer index
                RadioGroup radio = findViewById(R.id.radioGroup);
                radio.clearCheck();
                displayQuestion();
            } else {
                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("correctAnswersCount", correctAnswersCount);
                intent.putExtra("totalQuestions", questionsList.size());
                startActivity(intent);
            }
        }
    }


    public void radioButtonClick(View view){
        if (view.getId() == R.id.radioButtonOption0) selectedAnswerIndex = 0;
        if (view.getId() == R.id.radioButtonOption1) selectedAnswerIndex = 1;
        if (view.getId() == R.id.radioButtonOption2) selectedAnswerIndex = 2;
        if (view.getId() == R.id.radioButtonOption3) selectedAnswerIndex = 3;
    }

    public void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            TriviaQuestion currentQuestion = questionsList.get(currentQuestionIndex);
            TextView tv = findViewById(R.id.textView7);
            TextView tv2 = findViewById(R.id.textView8);
            tv.setText(currentQuestion.getQuestion());
            String tracker = "Question " + (currentQuestionIndex + 1) + " of 10";
            tv2.setText(tracker);

            RadioButton rb0 = findViewById(R.id.radioButtonOption0);
            rb0.setText(currentQuestion.getOption(0));
            RadioButton rb1 = findViewById(R.id.radioButtonOption1);
            rb1.setText(currentQuestion.getOption(1));
            RadioButton rb2 = findViewById(R.id.radioButtonOption2);
            rb2.setText(currentQuestion.getOption(2));
            RadioButton rb3 = findViewById(R.id.radioButtonOption3);
            rb3.setText(currentQuestion.getOption(3));
        }
    }
}