package com.example.quiztion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_INDEX = "index";
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private Button mPreviousButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_america, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_usha, true),
            new Question(R.string.question_ashvini, true),
            new Question(R.string.question_light, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgement_toast;
        }else{

            if (userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1)% mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1)% mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        mPreviousButton= (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                }else{
                    mCurrentIndex = (mCurrentIndex-1)% mQuestionBank.length;
                }
                mIsCheater = false;
                updateQuestion();
            }
        });
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                        Intent i = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                        startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        updateQuestion();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
}
