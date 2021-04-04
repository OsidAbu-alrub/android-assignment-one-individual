package com.abualrub.assignmentoneindividual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class InformationActivity extends AppCompatActivity {

    private ImageView imgView;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private TextView email;
    private TextView skill;
    private TextView isSubscribed;
    private static final String IMAGE = "IMAGE";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String GENDER = "GENDER";
    private static final String EMAIL = "EMAIL";
    private static final String SKILL = "SKILL";
    private static final String IS_SUBSCRIBED = "IS_SUBSCRIBED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        imgView = findViewById(R.id.imgView);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        skill = findViewById(R.id.skill);
        isSubscribed = findViewById(R.id.isSubscribed);
        initToolbar();
        setValues();
    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void setValues(){
        Intent i = getIntent();
        firstName.setText(i.getStringExtra(FIRST_NAME));
        lastName.setText(i.getStringExtra(LAST_NAME));
        gender.setText(i.getStringExtra(GENDER));
        skill.setText(i.getStringExtra(SKILL));
        email.setText(i.getStringExtra(EMAIL));

        boolean isSubbed = i.getBooleanExtra(IS_SUBSCRIBED,false);
        String ans = isSubbed ? "Yes" : "No";
        isSubscribed.setText(ans);

        Uri imgUri = (Uri)i.getParcelableExtra(IMAGE);
        if(imgUri != null) imgView.setImageURI(imgUri);
    }
}