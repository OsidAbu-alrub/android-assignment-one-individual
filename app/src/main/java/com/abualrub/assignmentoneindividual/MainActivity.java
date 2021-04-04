package com.abualrub.assignmentoneindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    private Spinner spinner;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText customSkill;
    private RadioGroup genderRadioGrp;
    private CheckBox newsletter;
    private LinearLayoutCompat customSkillLayout;
    private TextView chooseImageTxtView;
    private Uri imageUri;
    private static final String[] DYNAMIC_VALUES = {"Mathematics", "Physics", "Programming", "Cooking", "Other..."};
    private static final String[] NICE_QUOTES =
            {
                    "Beautiful Photo!",
                    "Looking Good!",
                    "Consider Modeling?",
                    "Nice Photo Mate",
            };
    private static final int IMAGE_PICK_CODE = 1;
    private static final String IMAGE = "IMAGE";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String GENDER = "GENDER";
    private static final String EMAIL = "EMAIL";
    private static final String SKILL = "SKILL";
    private static final String CUSTOM_SKILL = "CUSTOM_SKILL";
    private static final String IS_SUBSCRIBED = "IS_SUBSCRIBED";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customSkillLayout = findViewById(R.id.customSkillLayout);
        chooseImageTxtView = findViewById(R.id.chooseImageTxtView);
        firstName = findViewById(R.id.inputFieldFirstName);
        lastName = findViewById(R.id.inputFieldLastName);
        email = findViewById(R.id.inputFieldEmail);
        customSkill = findViewById(R.id.inputFieldCustom);
        genderRadioGrp = findViewById(R.id.genderRadioGrp);
        newsletter = findViewById(R.id.newsSettlerCheckBox);

        initToolBar();
        initImageView();
        initSpinner();
        checkInstance(savedInstanceState);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // user image
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initImageView() {
        imgView = findViewById(R.id.imgView);
        imgView.setClipToOutline(true);
    }

    // spinner for skills
    private void initSpinner() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adp = new ArrayAdapter<String>
                (MainActivity.this, android.R.layout.simple_list_item_1, DYNAMIC_VALUES);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp);
        initSpinnerHandler();
    }

    // handle selecting an item event
    private void initSpinnerHandler() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (DYNAMIC_VALUES.length - 1 == position) {
                    // if chosen option is "Other..." then show custom skill field
                    customSkillLayout.setVisibility(View.VISIBLE);
                    return;
                }
                customSkillLayout.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    // handle create account button click
    public void onClickCreate(View view) {
        String firstName = this.firstName.getText().toString().trim();
        String lastName = this.lastName.getText().toString().trim();
        RadioButton gender = findViewById(genderRadioGrp.getCheckedRadioButtonId());
        String email = this.email.getText().toString().trim();
        boolean newsletter = this.newsletter.isChecked();

        // this skill value can be custom, so we need
        // to determine if user chose custom value or
        // chose one from the list
        String skill;
        if(this.spinner.getSelectedItemPosition() == DYNAMIC_VALUES.length - 1)
            skill = this.customSkill.getText().toString().trim();
        else
           skill =  this.spinner.getSelectedItem().toString().trim();

        if(validateInput(firstName,lastName,email,skill,gender)){
            hideKeyboard(this);
            Intent i = new Intent(this, InformationActivity.class);

            i.putExtra(IMAGE,imageUri);
            i.putExtra(FIRST_NAME,firstName);
            i.putExtra(LAST_NAME,lastName);
            i.putExtra(GENDER,gender.getText()+"");
            i.putExtra(EMAIL,email);
            i.putExtra(SKILL,skill);
            i.putExtra(IS_SUBSCRIBED,newsletter);
            startActivity(i);
        }
    }

    // validate that necessary input exists
    private boolean validateInput(String firstName,String lastName,String email,String otherSkill,RadioButton gender) {
        try {
            // HANDLE MISSING VALUES
            if (firstName.isEmpty()) throw new IllegalArgumentException("Must Enter First Name");
            if (lastName.isEmpty()) throw new IllegalArgumentException("Must Enter Last Name");
            if(gender == null) throw new IllegalArgumentException("Must Choose Gender");
            if (email.isEmpty()) throw new IllegalArgumentException("Must Enter Email");
            if (spinner.getSelectedItemPosition() == DYNAMIC_VALUES.length - 1 &&
                    otherSkill.isEmpty())
                throw new IllegalArgumentException("Must Enter Custom Name");
            return true;
        } catch (IllegalArgumentException e) {
            showToast(e.getMessage());
        }
        return false;
    }

    // when user clicks to choose an image
    public void onClickBrowse(View view) {
        pickImage();
    }

    // method to open chooser and allow user to pick image
    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_CODE);
    }

    // shows a random quote when the user chooses a picture
    private void showRandomQuote() {
        try {
            Random r = new Random();
            String quote = NICE_QUOTES[r.nextInt(NICE_QUOTES.length)];
            showToast(quote);
        } catch (IndexOutOfBoundsException e) {
            showToast("Sharp Eyes You Got There");
        }
    }

    // method to create a toast with certain message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // method to hide keyboard when user clicks "create" button
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // wait for user to choose an image
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            resolveImage(data.getData());
            showRandomQuote();
        }
    }

    private void resolveImage(Uri data){

        // save imageUri to pass to information activity
        imageUri = data;

        // set image view with chosen picture
        imgView.setImageURI(data);
        imgView.setVisibility(View.VISIBLE);
        chooseImageTxtView.setVisibility(View.GONE);
    }

    // save data if orientation changes
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE,imageUri);
        outState.putString(FIRST_NAME,firstName.getText()+"");
        outState.putString(LAST_NAME,lastName.getText()+"");
        outState.putInt(GENDER,genderRadioGrp.getCheckedRadioButtonId());
        outState.putString(EMAIL,email.getText()+"");
        outState.putInt(SKILL,spinner.getSelectedItemPosition());
        outState.putString(CUSTOM_SKILL, customSkill.getText()+"");
        outState.putBoolean(IS_SUBSCRIBED,newsletter.isChecked());
    }

    // set data from changed orientation
    private void checkInstance(Bundle savedInstanceState) {
        if(savedInstanceState == null) return;
        firstName.setText(savedInstanceState.getString(FIRST_NAME));
        lastName.setText(savedInstanceState.getString(LAST_NAME));
        email.setText(savedInstanceState.getString(EMAIL));
        customSkill.setText(savedInstanceState.getString(CUSTOM_SKILL));
        newsletter.setChecked(savedInstanceState.getBoolean(IS_SUBSCRIBED));
        spinner.setSelection(savedInstanceState.getInt(SKILL));

        int genderId = savedInstanceState.getInt(GENDER);
        Log.d("GENDER",genderId+"");
        if(genderId != -1){
            ((RadioButton)findViewById(genderId)).setChecked(true);
        }

        Uri data = savedInstanceState.getParcelable(IMAGE);
        if(data != null)
        {
            resolveImage(data);
        }
    }
}