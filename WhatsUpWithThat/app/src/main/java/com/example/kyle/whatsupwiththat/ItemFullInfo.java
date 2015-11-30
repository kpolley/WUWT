package com.example.kyle.whatsupwiththat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ItemFullInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_full_info);
        setTitle("View Post");
        Intent i = getIntent();
        String Title = i.getStringExtra("TitleClick");
        String Body = i.getStringExtra("BodyClick");

        TextView thisTitle, thisBody;
        thisTitle = (TextView)findViewById(R.id.TitleFullView);
        thisBody = (TextView)findViewById(R.id.BodyFullView);

        thisTitle.setText(Title);
        thisBody.setText(Body);

    }




}
