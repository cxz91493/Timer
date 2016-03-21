package com.example.ykk.hw2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean count_start = false;
    private boolean already_start = false;
    private int minute;
    private int second;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.timer);


        //宣告Timer
        Timer timer =new Timer();
        //設定Timer(task為執行內容，0代表立刻開始,間格1秒執行一次)
        timer.schedule(task, 0, 1000);

        initViews();
        setListeners();
    }

    private Button btnStart;
    private Button btnPause;
    private Button btnReset;
    private TextView texMin;
    private TextView texSec;
    private Spinner spnMin;
    private Spinner spnSec;

    private void initViews() {

        btnStart = (Button) findViewById(R.id.btn_start);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnReset = (Button) findViewById(R.id.btn_reset);
        texMin = (TextView) findViewById(R.id.show_min);
        texSec = (TextView) findViewById(R.id.show_sec);

        spnMin = (Spinner) findViewById(R.id.spinner_min);
        spnSec = (Spinner) findViewById(R.id.spinner_sec);

        ArrayAdapter<CharSequence> listMin = ArrayAdapter.createFromResource(this, R.array.list_time, R.layout.myspinner);
        ArrayAdapter<CharSequence> listSec = ArrayAdapter.createFromResource(this, R.array.list_time, R.layout.myspinner);

        listMin.setDropDownViewResource(R.layout.myspinner);
        listSec.setDropDownViewResource(R.layout.myspinner);

        spnMin.setAdapter(listMin);
        spnSec.setAdapter(listSec);


    }

    private void setListeners(){
        btnStart.setOnClickListener(btn_listener);
        btnPause.setOnClickListener(btn_listener);
        btnReset.setOnClickListener(btn_listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case R.id.action_pastTime:

                //preference
                String pref_min = Pref.getMin(this);
                String pref_sec = Pref.getSec(this);
                if(! "".equals(pref_min) && ! "".equals(pref_sec)){
                    spnMin.setSelection(Integer.parseInt(pref_min));
                    spnSec.setSelection(Integer.parseInt(pref_sec));
                }
                break;

            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler(){
        public  void  handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    second = second % 60;
                    String smin = "";
                    String ssec = "";
                    if(minute < 10){
                        smin="0"+minute;
                    }else{
                        smin=""+minute;
                    }
                    if(second < 10){
                        ssec="0"+second;
                    }else{
                        ssec=""+second;
                    }
                    texMin.setText(smin);
                    texSec.setText(ssec);
                    break;
            }
        }
    };

    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(minute == 0 && second == 0){
                if(count_start) {
                    showNotification();
                }
                count_start = false;
                already_start = false;
            }else if(minute != 0 && second == 0){
                minute--;
                second = 60 ;
            }
            if (count_start){

                //sec-1 if count_start==true
                second--;
                Message message = new Message();

                //傳送訊息1
                message.what =1;
                handler.sendMessage(message);
            }
        }
    };


    private View.OnClickListener btn_listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_start:

                    //preference
                    Pref.setPast(MainActivity.this, spnMin.getSelectedItem().toString(), spnSec.getSelectedItem().toString());

                    if(!already_start){
                        minute = Integer.parseInt(spnMin.getSelectedItem().toString());
                        second = Integer.parseInt(spnSec.getSelectedItem().toString());
                        texMin.setText(spnMin.getSelectedItem().toString());
                        texSec.setText(spnSec.getSelectedItem().toString());
                    }
                    count_start = true;
                    already_start = true ;
                    break;
                case R.id.btn_pause:
                    already_start = true;
                    count_start = false;
                    break;
                case R.id.btn_reset:
                    already_start = false;
                    count_start = false;
                    minute = 0;
                    second = 0;
                    texMin.setText("00");
                    texSec.setText("00");
                    spnMin.setSelection(0);
                    spnSec.setSelection(0);
                    break;
                /*case R.id.end:
                    finish();
                    break;*/
            }
        }
    };

    protected void showNotification() {

        int Ntf_ID = 0;
        NotificationManager ntfMgr;
        ntfMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setTicker("!!!!!!!")//出不來ＱＱＱ
                .setContentTitle("Timer")
                .setContentText("Time's up")
                .setContentInfo("notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();
        ntfMgr.notify(Ntf_ID, notification);

        //ntfMgr.cancel(Ntf_ID);

    }
}

