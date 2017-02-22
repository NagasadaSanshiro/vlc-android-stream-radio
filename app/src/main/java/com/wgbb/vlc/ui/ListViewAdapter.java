package com.wgbb.vlc.ui;


import android.app.Activity;
import android.content.Context;
//import android.support.v7.internal.view.menu.MenuView;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wgbb.vlc.R;


import java.util.*;

/**
 * Created by GianniYan on 2016/3/14.
 */
/*public class ListViewAdapter extends BaseExpandableListAdapter  {
    //private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ArrayList<Programs> list = new ArrayList<Programs>();
    private Context context;
    private final static String J_NAME = "name";
    private final static String J_BREIF = "brief";
    private final static String J_URL = "url";
    private ImageButton ibBack;
    public int urlID = 9999;
    public boolean isFromView = false;

    public ListViewAdapter( ArrayList<Programs> list, Context context  ) {
        //public ListViewAdapter( ArrayList<HashMap<String, String>> list, Context context  ) {
        this.list = list;
        this.context = context;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int pos) {
        return list.get(pos);
    }

    public long getItemId( int pos  ) {
        return  pos;
    }

    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ) {
        View view = convertView;
        if  (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);
        }


        TextView programTitle = (TextView)view.findViewById(R.id.txt1);
        programTitle.setText( list.get(groupPosition).getProgramName() );
        TextView programCaster = (TextView)view.findViewById(R.id.txt2);
        programTitle.setText(list.get(groupPosition).getProgramCaster() );
        TextView programBrief = (TextView)view.findViewById(R.id.txt3);
        programTitle.setText( list.get(groupPosition).getProgramBrief() );


       /* View view = convertView;
        isFromView = false;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);
        }
            View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
            ibBack = (ImageButton) rootView.findViewById(R.id.imageButtonOk);
            TextView listItemName = (TextView)view.findViewById(R.id.title);
            listItemName.setText(list.get(position).get(J_NAME));

            TextView listItemURL = (TextView)view.findViewById(R.id.url);
            listItemURL.setText("  " + list.get(position).get(J_URL));
        */
         /*   listItemURL.setOnClickListener(new View.OnClickListener() { //Mar152016 Yan

            public void onClick(View v) {
                //ibBack.setImageAlpha(position);
                urlID  = position;
                isFromView = true;
                ibBack.performClick();
            }
        });

            ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.imageButtonDel);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });

        if (position % 2 == 1) {
            view.setBackgroundResource(0);
        } else {
            view.setBackgroundResource(R.drawable.view_bg);
        }
        return view;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if  (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);
        }
        TextView programName = (TextView)view.findViewById(R.id.title);
        programName.setText( list.get(groupPosition).getProgramList()[childPosition].getTitle() );
        String urlTMP = list.get(groupPosition).getProgramList()[childPosition].getUrl();

        programName.setOnClickListener(new View.OnClickListener() { //Mar152016 Yan

            public void onClick(View v) {
                //ibBack.setImageAlpha(position);
                urlID = position;
                isFromView = true;
                ibBack.performClick();
            }
        });

        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.imageButtonDel);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        if (position % 2 == 1) {
            view.setBackgroundResource(0);
        } else {
            view.setBackgroundResource(R.drawable.view_bg);
        }


        return view;
    }



}*/
