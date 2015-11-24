package com.example.kyle.whatsupwiththat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.ParseException;

public class NewItem extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
    }

    EditText editTitle, editBody;
    String editTitleString, editBodyString;


    //When the submit button is clicked, get text from user and return it to the main activity
    public void SubmitInfo(View v)
    {
        editTitle = (EditText) findViewById(R.id.typeNewTitle);
        editBody = (EditText) findViewById(R.id.typenewBody);
        editTitleString = editTitle.getText().toString();
        editBodyString = editBody.getText().toString();

        if(editTitleString.isEmpty() && editBodyString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a title and body", Toast.LENGTH_LONG).show();
        }
        else if(editTitleString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_LONG).show();
        }
        else if(editBodyString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a body", Toast.LENGTH_LONG).show();
        }

        else
        {
            ParseObject newPost = new ParseObject("Post");
            newPost.put("postTitle", editTitleString);
            newPost.put("postBody", editBodyString);
            newPost.saveInBackground();
            setResult(RESULT_OK);
            finish();
        }
    }

}