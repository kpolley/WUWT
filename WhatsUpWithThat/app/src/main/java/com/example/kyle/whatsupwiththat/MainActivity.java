package com.example.kyle.whatsupwiththat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable2;
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
import com.parse.ParseUser;

import java.security.spec.ECField;
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
        setTitle("Home");


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
        refreshDrawer();

    }

    //Opens edit page when click add button
    public void openEditPage(View v) {
        Intent i = new Intent(this, NewItem.class);
        startActivityForResult(i, 1);

    }
    //If added post, download list again
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 & resultCode == Activity.RESULT_OK) {
            downloadList();
        }
        if (requestCode == 2 & resultCode == Activity.RESULT_OK) {
            refreshDrawer();
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
                    Log.d("downloadposts", "Retrieved " + list.size() + " posts");
                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
                    Log.d("downloadposts", "Error: " + e.getMessage());
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

            if(Objects.equals(drawerItems.get(position), "Sign In")){
                Intent i = new Intent(MainActivity.this, SignInUser.class);
                startActivityForResult(i, 2);
                }

            if(Objects.equals(drawerItems.get(position), "Sign Out")){
                ParseUser.logOut();
                ParseUser user = ParseUser.getCurrentUser();

                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Error Please Try Again", Toast.LENGTH_LONG).show();
                }
                refreshDrawer();
            }

            if(Objects.equals(drawerItems.get(position), "Your Posts")){
                Intent i = new Intent(MainActivity.this, YourPosts.class);
                startActivity(i);
            }

            if(Objects.equals(drawerItems.get(position), "Your Comments")){
                Toast.makeText(getApplicationContext(), "Clicked " + drawerItems.get(position), Toast.LENGTH_LONG).show();
            }
            drawerLayout.closeDrawer(DrawerlistView);
        }

    }

    public void refreshDrawer(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            String[] temp = getResources().getStringArray(R.array.drawer_items_with_user);
            drawerItems = new ArrayList<>(Arrays.asList(temp));
        } else {
            String[] temp = getResources().getStringArray(R.array.drawer_items_no_user);
            drawerItems = new ArrayList<>(Arrays.asList(temp));
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.drawer_list_item, R.id.listtext, drawerItems);
        DrawerlistView.setAdapter(adapter);

        listener = new DrawerItemClickListener();
        DrawerlistView.setOnItemClickListener(listener);
    }

}