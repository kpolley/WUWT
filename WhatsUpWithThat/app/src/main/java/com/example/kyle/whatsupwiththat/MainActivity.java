package com.example.kyle.whatsupwiththat;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initializes objects
    ListView listview;
    TextView txtTitle;
    TextView txtBody;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton mAddButton;
    /*DrawerLayout mDrawerLayout;
    ListView drawerList;
    String drawerItems[] = {"Home", "Your Posts", "Your Comments", "Sign Out"};*/

    //Initializes adapter and lists
    CustomListAdapter customAdapter;
    ArrayList<String> listItemArrayListTitle = new ArrayList<>();
    ArrayList<String> listItemArrayListBody = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // DON'T NEED?? MAGIC?
        // Enable Local Datastore and connects to my parse server
        /*Parse.enableLocalDatastore(this);
        Parse.initialize(this, "K2Ff07sRqj6VPQYT12VQLQv7LtxIOe7eWfGRwWXz", "YyQLX9jwUpT2dxgsKeR5GEHTaweZuB6JN9V2eHMZ");*/


        setContentView(R.layout.activity_main);

        //assigns initialized objects to ones I created in xml files.

        //Main Page
        listview = (ListView) findViewById(R.id.list);
        txtTitle = (TextView) findViewById(R.id.textViewTitle);
        txtBody = (TextView) findViewById(R.id.textViewBody);
        //Navigation
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_container);
        mAddButton = (FloatingActionButton) findViewById(R.id.AddNewButton);

        //TODO: GET DRAWER FINISHED BELOW
        /*mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerList = (ListView) findViewById(R.id.drawerList);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerItems));*/

        customAdapter = new CustomListAdapter(getApplicationContext(), listItemArrayListTitle, listItemArrayListBody);

        //Changes colors and formats of XML files
        mAddButton.setColorFilter(getResources().getColor(R.color.niceLightGrey));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        //Downloads the list from parse at startup
        downloadList();

        //If user swipes down, refresh list
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                downloadList();
            }
        });

        //If user clicks on an item, open new activity to show full info and pass Title and Body info
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ItemFullInfo.class);
                i.putExtra("TitleClick", listItemArrayListTitle.get(position));
                i.putExtra("BodyClick", listItemArrayListBody.get(position));
                startActivity(i);

            }
        });
    }


    //Opens edit page when click add button
    public void openEditPage(View v) {
        Intent i = new Intent(this, NewItem.class);
        startActivityForResult(i, 1);
    }


    //Gets data from input from NewItem activity and uploads it to Parse. If no new data then no action.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 & resultCode == Activity.RESULT_OK) {
            ParseObject newPost = new ParseObject("Post");
            newPost.put("postTitle", data.getStringExtra("titleResult"));
            newPost.put("postBody", data.getStringExtra("bodyResult"));
            newPost.saveInBackground();
            downloadList();
        }
    }
    //Downloads and displays list from Parse.
    public void downloadList() {

        final ParseQuery<ParseObject> postQuery = ParseQuery.getQuery("Post");
        postQuery.orderByDescending("createdAt");

        postQuery.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    customAdapter.getTitleData().clear();
                    customAdapter.getBodyData().clear();

                    for(int i = 0; i < list.size(); i++){
                        listItemArrayListTitle.add(list.get(i).getString("postTitle"));
                        listItemArrayListBody.add(list.get(i).getString("postBody"));
                    }
                    Log.d("score", "Retrieved " + list.size() + " posts");
                    mSwipeRefreshLayout.setRefreshing(false);

                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Could not refresh feed", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                customAdapter.notifyDataSetChanged();
                listview.invalidateViews();
                listview.refreshDrawableState();
                listview.setAdapter(customAdapter);
            }
        });
    }
}

