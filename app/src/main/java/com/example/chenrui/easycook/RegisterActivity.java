package com.example.chenrui.easycook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordEditText2;
    private EditText mEmailEditText;
    private Button mRegisterButton;
    private TextView txt_login;

    // database setting
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //welcomeScreen.forceShow();

        setContentView(R.layout.activity_register);

        // connect to the firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // get text from the user input
        mUsernameEditText = (EditText) findViewById(R.id.editTextNickname);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPassword);
        mPasswordEditText2 = (EditText) findViewById(R.id.editTextPassword2);
        mRegisterButton = (Button) findViewById(R.id.register);
        mEmailEditText = (EditText) findViewById(R.id.editTextEmail);

        txt_login = (TextView)findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);

        // save user - password into database
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String email = mEmailEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();
                final String password2 = mPasswordEditText2.getText().toString();
                final User user = new User(username,email, password, System.currentTimeMillis());
                boolean isEmailValid = ValidateUserInfo.isEmailValid(getApplicationContext(),email);
                boolean isNameValid = ValidateUserInfo.isNameValid(getApplicationContext(),username);
                boolean isPasswordValid = ValidateUserInfo.isPasswordValid(getApplicationContext(),password,password2);
                if (!isEmailValid || !isNameValid || !isPasswordValid ) return;
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username)&& !username.equals("")) {
                            Toast.makeText(getBaseContext(),"username is already registered, please change one", Toast.LENGTH_SHORT).show();
                        } else {
                            // put username as key to set value
                            mDatabase.child("users").child(user.getUsername()).setValue(user);
                            Toast.makeText(getBaseContext(),"Successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
        }
    }


}

