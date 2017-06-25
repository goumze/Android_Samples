package com.example.goumze.geoquizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeoQuizActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)};

    private int mCurrentIndex = 0;
    private boolean mIsCheater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, mCurrentIndex);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GeoQuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GeoQuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex == 0) ? (mQuestionBank.length - 1) : (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /**
                 * Start CheatActivity. When not found the below method is going to throw "ActivityNotFoundException"
                 The below is an example of an explicit intent.
                 When you create an Intent with a Context and a Class object, you are creating an explicit intent.
                 You use explicit intents to start activities within your application.

                 When an activity in your application wants to start an activity in another application, you create an
                 implicit intent. You will use implicit intents in Chapter 15.
                 **/

                Log.i("GeoQuiz", "Starting GeoCheatActivity");
                //Intent i = new Intent(GeoQuizActivity.this, GeoCheatActivity.class);

                //In the below intent routine we are sending required data from parent activity.
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = GeoCheatActivity.newIntent(GeoQuizActivity.this, answerIsTrue);
                //This method will only start with nothing in return to the parent activity.
                // startActivity(i);

                //This method will start with an expectation of a return data from a child activity.
                startActivityForResult(i, REQUEST_CODE_CHEAT);

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i("GeoQuiz", "Called the event 'onSaveInstanceState'");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("", "onActivityResult called");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = GeoCheatActivity.wasAnswerShown(data);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateQuestion();

        Log.i("GeoQuizActivity", "On Resume has been called");
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
            Log.i("mIsCheater: " + mIsCheater + "===" + "messageResId: " + messageResId, "");
            Toast.makeText(GeoQuizActivity.this, messageResId, Toast.LENGTH_SHORT);
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
    }

    private void updateQuestion() {
        Log.i("GeoQuiz", "Current Index:" + mCurrentIndex);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    public TextView getmQuestionTextView() {
        return mQuestionTextView;
    }

    public void setmQuestionTextView(TextView mQuestionTextView) {
        this.mQuestionTextView = mQuestionTextView;
    }

}