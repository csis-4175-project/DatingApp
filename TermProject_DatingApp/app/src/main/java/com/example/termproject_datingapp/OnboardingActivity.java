package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.termproject_datingapp.models.OnboardingContent;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private ImageView image;
    private TextView header;
    private TextView message;
    private Button btnNext;
    private Button btnGetStarted;
    private int position = 0;
    private int numberOfContents;
    private SharedPreferences sharedPreferences;

    private ArrayList<OnboardingContent> onboardingContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onbourding);

        image = findViewById(R.id.onboarding_image);
        header = findViewById(R.id.onboarding_header);
        message = findViewById(R.id.onboarding_message);
        btnNext = findViewById(R.id.onboarding_next_btn);
        btnGetStarted = findViewById(R.id.onboarding_get_started_btn);

        onboardingContents = initOnboardingContents();
        numberOfContents = setNumberOfContents();

        fillViewsWithContentsByPosition();
        sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        setOnboardingHasSeen();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementPosition();
                fillViewsWithContentsByPosition();
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnboardingActivity.this, EntryScreenActivity.class));
                finish();
            }
        });
    }

    private ArrayList<OnboardingContent> initOnboardingContents() {

        String[] headers = {"Chat", "Enjoy", "Together"};
        String[] messages = {"Let's get you started on your dating journey.", "Explore profiles and meet interesting people.", "Your perfect match is just a click away."};
        int[] imageIds = {R.drawable.chatting, R.drawable.hang_out, R.drawable.romantic_night};

        int numberOfContents = imageIds.length;

        ArrayList<OnboardingContent> onboardingContents = new ArrayList<>();
        for (int index = 0; index < numberOfContents; index++) {
            OnboardingContent content = new OnboardingContent();
            content.setHeader(headers[index]);
            content.setMessages(messages[index]);
            content.setImageId(imageIds[index]);
            onboardingContents.add(content);
        }

        return onboardingContents;
    }

    private int setNumberOfContents() {
        return onboardingContents.size();
    }

    private void incrementPosition() {
        position++;
    }

    private void fillViewsWithContentsByPosition() {
        OnboardingContent content = onboardingContents.get(position);
        image.setImageResource(content.getImageId());
        header.setText(content.getHeader());
        message.setText(content.getMessages());
        setButtonVisibilityByPosition();
    }

    private void setButtonVisibilityByPosition() {
        if(isLastContent()) {
            btnNext.setVisibility(View.GONE);
            btnGetStarted.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnGetStarted.setVisibility(View.GONE);
        }
    }

    private boolean isLastContent() {
        return position == numberOfContents-1;
    }

    private void setOnboardingHasSeen() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("onboarding", "seen");
        editor.apply();
    }
}