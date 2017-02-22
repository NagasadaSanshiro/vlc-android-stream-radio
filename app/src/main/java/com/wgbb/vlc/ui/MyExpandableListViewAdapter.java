package com.wgbb.vlc.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wgbb.vlc.R;

import java.util.ArrayList;

/**
 * Created by GianniYan on 2016/3/26.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private ArrayList<Programs> list = new ArrayList<Programs>();
    private Context context;
    public int urlID = 9999;
    public boolean isFromView = false;
    private ExpandableListView expandableListView;
    private int lastExpandedGroupPosition = -1;
    public String urlStr = "";
    private ImageButton ibBack;

    public MyExpandableListViewAdapter( ArrayList<Programs> list, Context context  ) {
        /*public ListViewAdapter( ArrayList<HashMap<String, String>> list, Context context  ) {*/
        this.list = list;
        this.context = context;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_group, null);
        }
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.listView);

        expandableListView.setGroupIndicator(null);
        /*if(isExpanded){
            view.setBackgroundResource(R.drawable.arrow_down);
        }else{
            view.setBackgroundResource(R.drawable.arrow_up); }*/

        TextView programTitle = (TextView) view.findViewById(R.id.txt1);
        programTitle.setText(list.get(groupPosition).getProgramName());
       /* TextView programCaster = (TextView) view.findViewById(R.id.txt2);

                String strTmp = list.get(groupPosition).getProgramCaster();
                if( strTmp.equals( " ") ) {

                } else {
                    strTmp = "\t" + "主持人：" + strTmp;
                }

        programCaster.setText(strTmp);*/
        TextView programBrief = (TextView) view.findViewById(R.id.txt3);
        programBrief.setText(list.get(groupPosition).getProgramBrief());

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == 2) {
                    return false;
                } else
                    return false;
                }
        });


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < list.size(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }

        });

        return view;
    }


    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if  (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expendlist_item, null);
        }

        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        ibBack = (ImageButton) rootView.findViewById(R.id.imageButtonOk);


        TextView programName = (TextView)view.findViewById(R.id.title);
        programName.setText(list.get(groupPosition).getProgramList()[childPosition].getTitle());
       // String urlTMP = list.get(groupPosition).getProgramList()[childPosition].getUrl();


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                isFromView = true;
                urlStr = list.get(groupPosition).getProgramList()[childPosition].getUrl();
                ibBack.performClick();
                return false;
            }
        });

        if ( childPosition % 2 == 1) {
            view.setBackgroundResource(0);
        } else {
            view.setBackgroundResource(R.drawable.view_bg);

            /*android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = 20;
            layoutParams.height = 20;
            view.setLayoutParams(layoutParams);*/
        }


        return view;
    }



    @Override
    public int getGroupCount()
    {
        return list.size();
    }


    @Override
    public int getChildrenCount(int groupPosition)
    {
        return list.get(groupPosition).size();
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        return list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return list.get(groupPosition).getProgramList()[childPosition];
    }


    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }


    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
