package com.example.android.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.*;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private int scoreTeamA = 0;
    private int scoreTeamB = 0;

    private static final String KEY_STEPS = "STEPS";

    private TextView scoreViewTeamA;
    private TextView scoreViewTeamB;

    private Stack<Step> steps = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreViewTeamA = (TextView) findViewById(R.id.team_a_score);
        scoreViewTeamB = (TextView) findViewById(R.id.team_b_score);

        if (savedInstanceState != null) {
            Object o = savedInstanceState.getSerializable(KEY_STEPS);
            if (o instanceof Stack) {
                steps = (Stack<Step>) o;
            }
            Step lastStep = steps.pop();
            scoreTeamA = lastStep.getScoreTeamA();
            scoreTeamB = lastStep.getScoreTeamB();
        }

        displayScore();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_STEPS, steps);
    }


    public void doPressBottom(View view) {
        switch (view.getId()) {
            case R.id.a_1 : scoreTeamA += 1; break;
            case R.id.a_2 : scoreTeamA += 2; break;
            case R.id.a_3 : scoreTeamA += 3; break;

            case R.id.b_1 : scoreTeamB += 1; break;
            case R.id.b_2 : scoreTeamB += 2; break;
            case R.id.b_3 : scoreTeamB += 3; break;
        }
        displayScore();
    }


    public void reset(View view) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        steps.clear();
        displayScore();
    }

    public void undo(View view) {
        if (steps.size() > 1) {
            steps.pop();
            Step previousScores = steps.pop();
            scoreTeamA = previousScores.getScoreTeamA();
            scoreTeamB = previousScores.getScoreTeamB();
            displayScore();
        }

    }

    private void displayScore() {
        boolean needLog = true;
        if (scoreTeamA > 99 || scoreTeamB > 99) {
            if (scoreTeamA > 99) scoreTeamA = 99;
            if (scoreTeamB > 99) scoreTeamB = 99;
            needLog = false;
        }

        scoreViewTeamA.setText(String.valueOf(scoreTeamA));
        scoreViewTeamB.setText(String.valueOf(scoreTeamB));
        if (needLog) {
            steps.push(new Step(scoreTeamA, scoreTeamB));
            Log.i("The \"Steps\" size", String.valueOf(steps.size()));
        }
    }

    private class Step implements Serializable{
        private int scoreTeamA;
        private int ScoreTeamB;

        private Step(int scoreTeamA, int scoreTeamB) {
            this.scoreTeamA = scoreTeamA;
            this.ScoreTeamB = scoreTeamB;
        }

        private int getScoreTeamA() {
            return scoreTeamA;
        }

        private int getScoreTeamB() {
            return ScoreTeamB;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeInt(scoreTeamA);
            out.writeInt(scoreTeamB);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.readInt();
            in.readInt();
        }
    }


}
