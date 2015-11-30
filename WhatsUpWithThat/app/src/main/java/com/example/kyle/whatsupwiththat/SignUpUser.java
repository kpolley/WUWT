package com.example.kyle.whatsupwiththat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        setTitle("Sign Up");
    }

    EditText userName, password, email;



    public void signUp(View V){

        userName = (EditText) findViewById(R.id.typeUsername);
        password = (EditText) findViewById(R.id.typePassword);
        email = (EditText) findViewById(R.id.typeEmail);

        ParseUser user = new ParseUser();
        user.setUsername(userName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "User account created", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error please try again", Toast.LENGTH_LONG).show();
                    Log.d("SignUp", "Error: " + e.getMessage());
                }

            }
        });
    }
}
