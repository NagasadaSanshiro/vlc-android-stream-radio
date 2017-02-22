package com.wgbb.vlc.ui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GianniYan on 2016/3/25.
 */
public class Programs {
    private String programName = null;
    private String programCaster = null;
    private String programBrief = null;
    private ChildItemdata[] programlist = null;
    int childCount;
    public Programs(  String n, String c,String b, ChildItemdata[] l,int count  ) {
        programName = n;
        programCaster = c;
        programBrief = b;
        programlist = l;
        this.childCount = count;
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramCaster() {
        return programCaster;
    }
    public String getProgramBrief() {
        return programBrief;
    }

    public ChildItemdata[] getProgramList() {
        return programlist;
    }
    public int size() {
        return childCount;
    }
    public boolean isEmpty() {
        return  ( childCount == 0 ? true:false );
    }
}
