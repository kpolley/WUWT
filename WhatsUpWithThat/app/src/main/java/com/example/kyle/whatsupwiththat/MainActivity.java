package com.example.kyle.whatsupwiththat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Initializes Main Page Items
    ListView listview;
    TextView txtTitle;
    TextView txtBody;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton mAddButton;

    //Initializes drawer layout items
    private DrawerLayout drawerLayout;
    private ListView DrawerlistView;
    private ArrayList<String> drawerItems;
    private ArrayAdapter<String> adapter;
    private DrawerItemClickListener listener;

    //Initializes adapter and lists
    CustomListAdapter customAdapter;
    ArrayList<String> listItemArrayListTitle = new ArrayList<>();
    ArrayList<String> listItemArrayListBody = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Main Page
        listview = (ListView) findViewById(R.id.list);
        txtTitle = (TextView) findViewById(R.id.textViewTitle);
        txtBody = (TextView) findViewById(R.id.textViewBody);
        //Navigation
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_container);
        mAddButton = (FloatingActionButton) findViewById(R.id.AddNewButton);


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

        //EVERYTHING DRAWER
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        DrawerlistView = (ListView) findViewById(R.id.drawer_list_view);

        String[] temp = getResources().getStringArray(R.array.drawer_items);
        drawerItems = new ArrayList<>(Arrays.asList(temp));

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.drawer_list_item, R.id.listtext, drawerItems);
        DrawerlistView.setAdapter(adapter);

        listener = new DrawerItemClickListener();
        DrawerlistView.setOnItemClickListener(listener);


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

                    for (int i = 0; i < list.size(); i++) {
                        listItemArrayListTitle.add(list.get(i).getString("postTitle"));
                        listItemArrayListBody.add(list.get(i).getString("postBody"));
                    }
                    Log.d("score", "Retrieved " + list.size() + " posts");
                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
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

    public class DrawerItemClickListener implements AdapterView.OnItemClickListener {


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO Do Fun things with Fragments
            if(Objects.equals(drawerItems.get(position), "Home")){
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }

            if(Objects.equals(drawerItems.get(position), "Sign In")){
                Intent i = new Intent(MainActivity.this, SignInUser.class);
                startActivity(i);
            }

            if(Objects.equals(drawerItems.get(position), "Your Posts")){
                Toast.makeText(getApplicationContext(), "Clicked " + drawerItems.get(position), Toast.LENGTH_LONG).show();
            }

            if(Objects.equals(drawerItems.get(position), "Your Comments")){
                Toast.makeText(getApplicationContext(), "Clicked " + drawerItems.get(position), Toast.LENGTH_LONG).show();
            }
            drawerLayout.closeDrawer(DrawerlistView);
        }
    }
}

