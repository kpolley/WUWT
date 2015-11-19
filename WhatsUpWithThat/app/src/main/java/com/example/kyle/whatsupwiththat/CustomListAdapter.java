package com.example.kyle.whatsupwiththat;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<String> titles;
    private ArrayList<String> bodies;

    public CustomListAdapter(Context context, ArrayList<String> titles, ArrayList<String> bodies){
        mInflater = LayoutInflater.from(context);
        this.titles = titles;
        this.bodies = bodies;
    }

    public List<String> getTitleData(){
        return titles;
    }

    public List<String> getBodyData(){
        return bodies;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ItemViewHolder itemViewHolder;

        if(convertView == null){
            view = mInflater.inflate(R.layout.listdatastore, parent, false);
            itemViewHolder = new ItemViewHolder();
            itemViewHolder.title = (TextView) view.findViewById(R.id.textViewTitle);
            itemViewHolder.body = (TextView) view.findViewById(R.id.textViewBody);
            view.setTag(itemViewHolder);
        }
        else {
            view = convertView;
            itemViewHolder = (ItemViewHolder) view.getTag();
        }

        itemViewHolder.title.setText(titles.get(position));
        itemViewHolder.body.setText(bodies.get(position));

        return view;
    }

    public class ItemViewHolder {
        private TextView title;
        private TextView body;
    }

}