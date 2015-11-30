package com.example.kyle.whatsupwiththat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kpoll on 11/26/2015.
 */
public class YourPosts extends AppCompatActivity {

    ListView listview;
    TextView txtTitle;
    TextView txtBody;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private ListView DrawerlistView;
    private ArrayList<String> drawerItems;
    private ArrayAdapter<String> adapter;
    private MainActivity.DrawerItemClickListener listener;

    //Initializes adapter and lists
    CustomListAdapter customAdapter;
    ArrayList<String> listItemArrayListTitle = new ArrayList<>();
    ArrayList<String> listItemArrayListBody = new ArrayList<>();
    MainActivity.DrawerItemClickListener drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_posts_page);
        setTitle("Your Posts");

        listview = (ListView) findViewById(R.id.yourpostlist);
        txtTitle = (TextView) findViewById(R.id.textViewTitle);
        txtBody = (TextView) findViewById(R.id.textViewBody);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerlistView = (ListView) findViewById(R.id.drawer_list_view);
        customAdapter = new CustomListAdapter(getApplicationContext(), listItemArrayListTitle, listItemArrayListBody);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        downloadYourPostsList();
        refreshDrawer();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ItemFullInfo.class);
                i.putExtra("TitleClick", listItemArrayListTitle.get(position));
                i.putExtra("BodyClick", listItemArrayListBody.get(position));
                startActivity(i);
            }
        });

    }

    public void downloadYourPostsList(){
        final ParseQuery<ParseObject> postQuery = ParseQuery.getQuery("Post");
        postQuery.orderByDescending("createdAt");
        postQuery.whereEqualTo("postedBy", ParseUser.getCurrentUser().getObjectId());

        postQuery.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    customAdapter.getTitleData().clear();
                    customAdapter.getBodyData().clear();

                    for (int i = 0; i < list.size(); i++) {
                        listItemArrayListTitle.add(list.get(i).getString("postTitle"));
                        listItemArrayListBody.add(list.get(i).getString("postBody"));
                    }
                    Log.d("YourPosts", "Retrieved " + list.size() + " posts");
                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
                    Log.d("YourPosts", "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Could not refresh feed", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                customAdapter.notifyDataSetChanged();
                listview.invalidateViews();
                listview.refreshDrawableState();
                listview.setAdapter(customAdapter);
            }
        });
        refreshDrawer();
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


        DrawerlistView.setOnItemClickListener(listener);
    }


}
