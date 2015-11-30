package com.example.kyle.whatsupwiththat;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class SignInUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);
        setTitle("Sign In");

    }

    public void signIn(View V){
        EditText userName, password;

        userName = (EditText) findViewById(R.id.typeOldUsername);
        password = (EditText) findViewById(R.id.typeOldPassword);

        ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Error Please Try Again", Toast.LENGTH_LONG).show();
                    Log.d("SignIn", "Error: " + e.getMessage());
                }
            }
        });


    }

    public void openSignUpPage(View V){
        Intent i = new Intent(this, SignUpUser.class);
        finish();
        startActivity(i);

    }

}



