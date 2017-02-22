package com.wgbb.vlc.ui;
/**
 * Created by GianniYan on 2016/2/20.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wgbb.vlc.R;
import android.widget.ArrayAdapter;
import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.vlc.util.VLCInstance;
import android.widget.EditText;
import android.widget.BaseExpandableListAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VlcVideoActivity extends Activity implements SurfaceHolder.Callback, IVideoPlayer, OnClickListener {


    private final static String TAG = "[VlcVideoActivity]";

    public static String[] playListStr = new String[]{
            "T",
            /*"http://www.sample-videos.com/audio/mp3/india-national-anthem.mp3",
            "http://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"*/
    };

    private static String url = "mp3Link.txt";

    private final static String TAG_OS = "MP3";
    private final static String J_NAME = "name";
    private final static String J_BREIF = "brief";
    private final static String J_URL = "url";
    private final static String PLAY_LIST_HOST = "http://www.chineseradionetwork.com/FilesUpload/Radio/";
    public static boolean isFromOK = false;
    public static boolean isPlayStream = true;

    private SurfaceView mSurfaceView;
    private LibVLC mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;

    private View mLoadingView;

    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;
    private View rlLoading;
    private TextView tvBuffer;
    private ImageButton ibPlay;
    private ImageButton ibStop;
    //private ImageButton ibLock;
    private Intent intent;
    private Intent intentMenu;
    private Button pStreamButton;
    private ImageButton ibMenu;

    private ImageButton ibBack;
    private ImageButton ibOk;
    private ImageButton iAdd;
    private ImageButton iDel;
    private RelativeLayout mainView;
    private RelativeLayout menuView;
    //private ArrayAdapter<String> adapter;
    //private ListViewAdapter adapter;
    private MyExpandableListViewAdapter adapter;
    private CheckBox radioButton;
    private ExpandableListView lv;

    private RelativeLayout rlLink;

    private boolean isLocked = true;
    // private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ArrayList<Programs> pList = new ArrayList<Programs>();



    private Builder vars;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private ChildItemdata[][] mChilds = null;
    private String[][] mGroups = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_vlc);

        vars = new Builder(this);

        lv = (ExpandableListView ) findViewById(R.id.listView);

        getListData();
        //adapter=new ArrayAdapter<String>(this,R.layout.item,R.id.title, getListData() );
        //adapter = new ListViewAdapter(list, this);
        adapter = new MyExpandableListViewAdapter(pList, this);
        lv.setAdapter(adapter);

        iAdd = (ImageButton) findViewById(R.id.imageButtonAdd);


        mSurfaceView = (SurfaceView) findViewById(R.id.video);
        try {
            mMediaPlayer = VLCInstance.getLibVlcInstance();
        } catch (LibVlcException e) {
            e.printStackTrace();
        }

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(this);


        //rlLoading = findViewById(R.id.rl_loading);
        //tvBuffer = (TextView) findViewById(R.id.tv_buffer);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        menuView = (RelativeLayout) findViewById(R.id.lstMenu);

        ibPlay = (ImageButton) findViewById(R.id.startMusic);
        ibPlay.setOnClickListener(this);
        ibStop = (ImageButton) findViewById(R.id.stopMusic);
        ibStop.setOnClickListener(this);
        ibMenu = (ImageButton) findViewById(R.id.listMenu);
        ibMenu.setOnClickListener(this);

        ibOk = (ImageButton) findViewById(R.id.imageButtonOk);
        ibOk.setOnClickListener(this);

        ibBack = (ImageButton) findViewById(R.id.imageButtonBack);
        ibBack.setOnClickListener(this);


        pStreamButton = (Button) findViewById(R.id.playStreamButton);
        pStreamButton.setOnClickListener(this);


        rlLink = (RelativeLayout)findViewById(R.id.radioLinkView);
        rlLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb= new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.chineseradionetwork.com/"));
                startActivity(intentWeb);
            }
        });

        //iAdd = (ImageButton) findViewById(R.id.imageButtonAdd);
        /*iAdd.setOnClickListener(this);     //Mar262016 del by Johnny
        iAdd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                vars.setTitle("Please Entered the mp3 link");
                vars.setMessage("Entered mp3 link: ");
                final EditText input = new EditText(VlcVideoActivity.this);
                input.findViewById(R.id.eTb);
                vars.setView(input);
                vars.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String convtext = input.getText().toString();
                        //list.add(convtext);
                        //adapter.notifyDataSetChanged();
                        adapter.notify();
                        return;
                    }
                });
                vars.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                vars.show();
            }
        });
        //adapter.notifyDataSetChanged();
        adapter.notify();*/

        iDel = (ImageButton) findViewById(R.id.imageButtonDel);
       /* iDel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });*/
        //iDel.setOnClickListener(this);

        radioButton = (CheckBox) findViewById((R.id.radioButton));
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //radioButton.setChecked(false);
                    isPlayStream = true;
                } else if (!isChecked) {
                    //radioButton.setChecked(true);
                    isPlayStream = false;
                }
            }

        });


        mMediaPlayer.eventVideoPlayerActivityCreated(true);

        //EventHandler em = EventHandler.getInstance();
        //em.addHandler(mVlcHandler);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSurfaceView.setKeepScreenOn(true);
        intent = new Intent(VlcVideoActivity.this, VlcService.class);
        //bindService(intent,conn, Context.BIND_AUTO_CREATE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public void getListData() {

        BufferedReader reader = null;
        StringBuilder json = null;//Apr14 d
        String tmpStr = "123";
        //InputStream is = null;


        JSONParser jParser = new JSONParser();//Apr14 d

         //JSONObject json = ;  //Apr14 a
        //jParser.execute(tmpStr, json);




        try {
            json = new JSONParser().execute(tmpStr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        SystemClock.sleep(800);






        //JSONObject obj = new JSONObject( tmpsdag );
        //String json = new JSONParser().execute(tmpStr).get();


        try {
            /*
            JSONObject jObj = new JSONObject(json.toString());
            jSONArray = jObj.getJSONArray(TAG_OS);
            JSONArray jSONArray = jObj.getJSONArray("");
            */

            //String tmpsdag = loadJSONFromAsset();
            String tmpsdag = json.toString();

            tmpsdag = tmpsdag.replace("\\\"", "\"");

            int strEnd = tmpsdag.length() - 1;

            tmpsdag = tmpsdag.substring(1, strEnd);
            tmpsdag = tmpsdag.replace("\\n", "\n");
            tmpsdag = tmpsdag.replace("\\/\\/\\/", "");
            // JSONArray jSONArray = new JSONArray(tmpStrJson); // add Apr 11
            JSONArray jSONArray = new JSONArray(tmpsdag);

           // JSONArray jSONArray = new JSONArray(json.toString());
            mGroups = new String[jSONArray.length()][3];
            mChilds =  new ChildItemdata[jSONArray.length()][];

            for (int i = 0; i < jSONArray.length(); i++) {

                JSONObject jsonObject = jSONArray.getJSONObject(i);
                String progName = jsonObject.getString("ShowTitle");
                //String progCaster = jsonObject.getString("caster");
                String progBrief = jsonObject.getString("ShowBrief");
                progBrief = progBrief.replace("\\\\n"," \\\\n");
                progBrief = progBrief + " ";

                mGroups[i][0] = progName;

                //mGroups[i][1] =  progCaster;
                mGroups[i][1] =  "";
                mGroups[i][2] = progBrief;
                JSONArray childArr = jsonObject.getJSONArray("programList");
                mChilds[i] =  new ChildItemdata[childArr.length()];
                ChildItemdata childItemdata = null;
                for (int j = 0; j < childArr.length(); j++) {
                    JSONObject childObject = childArr.getJSONObject(j);
                    String pTitle = childObject.getString("Path");
                    int start = pTitle.indexOf("/");
                    int end = pTitle.indexOf(".");
                    pTitle = pTitle.substring(start+1, end);
                    String pURL = childObject.getString("Path");
                    pURL =  PLAY_LIST_HOST + pURL;

                    childItemdata = new ChildItemdata(pTitle, pURL );
                    mChilds[i][j] = childItemdata;
                }
                Programs tmpProgram = new Programs(  mGroups[i][0],mGroups[i][1], mGroups[i][2], mChilds[i], childArr.length()  );
                pList.add(tmpProgram);
            }



            /*for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jc = jSONArray.getJSONObject(i);
                String tName = jc.getString(J_NAME);
                String tBreif = jc.getString(J_BREIF);
                String tURL = jc.getString(J_URL);
                HashMap<String, String> tMap = new HashMap<String, String>();
                tMap.put(J_NAME, tName);
                tMap.put(J_BREIF, tBreif);
                tMap.put(J_URL, tURL);
                list.add(tMap);
             }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View view) {
        //Intent intent = new Intent(VlcVideoActivity.this,VlcService.class);
        //bindService(intent, conn, Context.BIND_AUTO_CREATE);
        boolean isFromView;
        //ImageButton bStart = ( ImageButton )findViewById(R.id.startMusic);
        //ImageButton bStop = ( ImageButton )findViewById(R.id.stopMusic);
        switch (view.getId()) {
            case R.id.startMusic:
                //if (mMediaPlayer.isPlaying()) {
                //mMediaPlayer.playMRL("http://216.18.21.102:8080");
                //findViewById(R.id.startMusic).setBackgroundResource(R.drawable.ic_pause);
                //startService( new Intent(VlcVideoActivity.this, VlcService.class) );

                if (isFromOK) {
                    if( !isPlayStream ) {
                        //intent.putExtra("PlayListString", playListStr);
                    } else {
                        playListStr = new String[1];
                        //playListStr[0] = "http://216.18.21.102:8080";
                        playListStr[0] = "http://209.17.186.202:8275/1240";
                    }
                    isFromOK = false;
                } else {
                    if( !isPlayStream ) {
                       // intent.putExtra("PlayListString", playListStr);
                    } else {
                        //playListStr[0] = "http://216.18.21.102:8080";
                        playListStr[0] = "http://209.17.186.202:8275/1240";
                    }
                }
                intent.putExtra("PlayListString", playListStr);

                ibPlay.setVisibility(View.GONE);
                //bStart.setEnabled(false);
                ibStop.setVisibility(View.VISIBLE);
                startService(intent);
                //bStop.setEnabled(true);
                //mMediaPlayer.play();
                break;
            case R.id.stopMusic:
                //findViewById(R.id.startMusic).setBackgroundResource(R.drawable.ic_play);
                //stopService( new Intent(VlcVideoActivity.this, VlcService.class) );
                stopService(intent);
                ibPlay.setVisibility(View.VISIBLE);
                //bStart.setEnabled(true);
                ibStop.setVisibility(View.GONE);
                //bStop.setEnabled(false);
                break;
            /*case R.id.bindMusic:
				if(isLocked) {
					isLocked = false;
					findViewById(R.id.bindMusic).setBackgroundResource(R.drawable.ic_lock);
					unbindService(conn);
				} else {
					isLocked = true;
					findViewById(R.id.bindMusic).setBackgroundResource(R.drawable.ic_locked);
					bindService(intent,conn, Context.BIND_AUTO_CREATE);
				}
				break;*/
            case R.id.listMenu:
                mainView.setVisibility(View.GONE);
                menuView.setVisibility(View.VISIBLE);
                break;

            case R.id.imageButtonOk:
                isFromView = adapter.isFromView;

                if( isFromView ) {
                    //int pos = adapter.urlID;
                    String urlTmpStr = adapter.urlStr;
                    isFromOK = false;
                    playListStr = new String[1];
                   // playListStr[0] = list.get(pos).get(J_URL);
                    playListStr[0] = urlTmpStr;
                    intent.putExtra("PlayListString", playListStr);

                    adapter.isFromView = false;
                    isPlayStream = false;
                    radioButton.setChecked(false);
                    ibPlay.performClick();
                } else {
                    isFromOK = true;
                    int count = list.size() + 1;
                    playListStr = new String[count];
                    if (isPlayStream)
                        playListStr[0] = "T";
                    else {
                        playListStr[0] = "F";
                    }

                    for (int i = 1; i < list.size() + 1; i++) {
                        playListStr[i] = list.get(i - 1).get(J_URL);
                        //playListStr[i] = list.get(i-1);
                    }
                    intent.putExtra("PlayListString", playListStr);
                }
                mainView.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.GONE);

                //startActivity(intent);
                break;
            case R.id.imageButtonBack:
                isFromOK = false;
                mainView.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.GONE);
                break;
            case R.id.playStreamButton:
                radioButton.setChecked(true);
                ibPlay.performClick();
                mainView.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.GONE);
                break;
            /*case  R.id.radioButton:
                if( radioButton.isChecked() ) {

                    radioButton.setChecked(false);
                    isPlayStream = false;
                } else {
                    radioButton.setChecked(true);
                    isPlayStream = true;
                }



                break;*/
            /*case R.id.imageButtonAdd:
                iAdd.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        vars.setTitle("Please Entered the mp3 link");
                        vars.setMessage("Entered mp3 link: ");
                        final EditText input = new EditText(VlcVideoActivity.this);
                        input.findViewById(R.id.eTb);
                        vars.setView(input);
                        vars.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String convtext = input.getText().toString();
                                list.add(convtext);
                                adapter.notifyDataSetChanged();
                                return;
                            }
                        });
                        vars.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        vars.show();
                    }
                });
                adapter.notifyDataSetChanged();
                break;*/
            default:
                break;
        }
    }

    public void getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = getLayoutInflater();

        row = inflater.inflate(R.layout.item, parent, false);

        if (position % 2 == 1) {
            row.setBackgroundResource(R.drawable.view_bg);
        } else {

        }


        ImageButton deleteButton = (ImageButton) row.findViewById(R.id.imageButtonDel);
        deleteButton.setTag(position);

        deleteButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer index = (Integer) v.getTag();
                        list.remove(index.intValue());
                        adapter.notifyDataSetChanged();
                    }
                }
        );

    }


    /*private Dialog createExampleDialog( ) {

        EditText text = (EditText)findViewById(R.id.eTb);
        AlertDialog.Builder builder = new AlertDialog.Builder(VlcVideoActivity.this);
        builder.setTitle("Please input the mp3 link");
        final EditText input = new EditText(VlcVideoActivity.this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        return builder.create();
    }*/
    final ServiceConnection conn = new ServiceConnection() {


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }
    };
	/*@Override
	public void onPause() {
		super.onPause();

		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			//mSurfaceView.setKeepScreenOn(false);
		}
	}

	@Override
	protected void onDestroy() {
		//if(!isLocked) {
			super.onDestroy();
			//EventHandler em = EventHandler.getInstance();
			//em.removeHandler(mVlcHandler);
			*//*if (mMediaPlayer != null) {
				mMediaPlayer.eventVideoPlayerActivityCreated(false);

				EventHandler em = EventHandler.getInstance();
				em.addHandler(mVlcHandler);
				em.removeHandler(mVlcHandler);

			}*//*
		//}
	}*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setSurfaceSize(mVideoWidth, mVideoHeight, mVideoVisibleWidth, mVideoVisibleHeight, mSarNum, mSarDen);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mSurfaceHolder = holder;
            mMediaPlayer.attachSurface(holder.getSurface(), this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
        if (mMediaPlayer != null) {
            mMediaPlayer.attachSurface(holder.getSurface(), this);//, width, height
        }
        if (width > 0) {
            mVideoHeight = height;
            mVideoWidth = width;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.detachSurface();
        }
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        //mHandler.removeMessages(HANDLER_SURFACE_SIZE);
        //mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
    }

    private static final int HANDLER_BUFFER_START = 1;
    private static final int HANDLER_BUFFER_END = 2;
    private static final int HANDLER_SURFACE_SIZE = 3;

    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_HORIZONTAL = 1;
    private static final int SURFACE_FIT_VERTICAL = 2;
    private static final int SURFACE_FILL = 3;
    private static final int SURFACE_16_9 = 4;
    private static final int SURFACE_4_3 = 5;
    private static final int SURFACE_ORIGINAL = 6;
    private int mCurrentSize = SURFACE_BEST_FIT;

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VlcVideo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.wgbb.vlc.ui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VlcVideo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.wgbb.vlc.ui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }

	/*private Handler mVlcHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.getData() == null)
				return;

			switch (msg.getData().getInt("event")) {
				case EventHandler.MediaPlayerTimeChanged:
					break;
				case EventHandler.MediaPlayerPositionChanged:
					break;
				case EventHandler.MediaPlayerPlaying:
					mHandler.removeMessages(HANDLER_BUFFER_END);
					mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
					break;
				case EventHandler.MediaPlayerBuffering:
					break;
				case EventHandler.MediaPlayerLengthChanged:
					break;
				case EventHandler.MediaPlayerEndReached:
					break;
			}

		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case HANDLER_BUFFER_START:
					showLoading();
					break;
				case HANDLER_BUFFER_END:
					hideLoading();
					break;
				case HANDLER_SURFACE_SIZE:
					//changeSurfaceSize();
					break;
			}
		}
	};


	private void showLoading() {
		mLoadingView.setVisibility(View.VISIBLE);
	}

	private void hideLoading() {
		mLoadingView.setVisibility(View.GONE);
	}*/
//	private void changeSurfaceSize() {
//		// get screen size
//		int dw = getWindowManager().getDefaultDisplay().getWidth();
//		int dh = getWindowManager().getDefaultDisplay().getHeight();
//
//		// calculate aspect ratio
//		double ar = (double) mVideoWidth / (double) mVideoHeight;
//		// calculate display aspect ratio
//		double dar = (double) dw / (double) dh;
//
//		switch (mCurrentSize) {
//		case SURFACE_BEST_FIT:
//			if (dar < ar)
//				dh = (int) (dw / ar);
//			else
//				dw = (int) (dh * ar);
//			break;
//		case SURFACE_FIT_HORIZONTAL:
//			dh = (int) (dw / ar);
//			break;
//		case SURFACE_FIT_VERTICAL:
//			dw = (int) (dh * ar);
//			break;
//		case SURFACE_FILL:
//			break;
//		case SURFACE_16_9:
//			ar = 16.0 / 9.0;
//			if (dar < ar)
//				dh = (int) (dw / ar);
//			else
//				dw = (int) (dh * ar);
//			break;
//		case SURFACE_4_3:
//			ar = 4.0 / 3.0;
//			if (dar < ar)
//				dh = (int) (dw / ar);
//			else
//				dw = (int) (dh * ar);
//			break;
//		case SURFACE_ORIGINAL:
//			dh = mVideoHeight;
//			dw = mVideoWidth;
//			break;
//		}
//
//		mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
//		ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//		lp.width = dw;
//		lp.height = dh;
//		mSurfaceView.setLayoutParams(lp);
//		mSurfaceView.invalidate();
    //}
}
