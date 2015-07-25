package charis.Triviality_3.diss;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    final int ComGreen = 1;
    final int ComNext = 2;
    final int ComTimer = 3;
    ProgressBar PbTime;
    TextView TvTitle;
    TextView TvQuest;
    TextView[] TvAns;
    Button OKbutton;
    QuestionData[] Data;
    int Kateg;
    boolean Teach;
    int NoQ;
    int MaxErrors;
    int TotalTime;
    int CurrentQuestion;
    int DoneQuest;
    int MissQuest;
    int ElapsedTime;
    int Selected;
    boolean Next;
    boolean CanPress;
    Timer MainTimer;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        Intent intent;
        super.onCreate(savedInstanceState);
        setContentView (R.layout.mainlayout);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        NoQ = 5;
        MaxErrors = 3;
        TotalTime = 90;
        intent = getIntent ();
        Kateg = intent.getIntExtra ("Kategory", 0);
        Teach = intent.getBooleanExtra ("IsTeaching", false);
        //Toast.makeText (getApplicationContext(), "*" + Kateg + "*" + Teach + "*", Toast.LENGTH_LONG).show ();
        SetViews ();
        CreateTest ();
        InitTest ();
    }

    void SetViews ()
    {
        PbTime = (ProgressBar) findViewById (R.id.PbTime);
        TvTitle = (TextView) findViewById (R.id.TvTitle);
        TvQuest = (TextView) findViewById (R.id.TvQuest);
        TvAns = new TextView [4];
        TvAns[0] = (TextView) findViewById (R.id.TvAns1);
        TvAns[1] = (TextView) findViewById (R.id.TvAns2);
        TvAns[2] = (TextView) findViewById (R.id.TvAns3);
        TvAns[3] = (TextView) findViewById (R.id.TvAns4);
        OKbutton = (Button) findViewById (R.id.BtOK);
        TvAns[0].setOnClickListener (this);
        TvAns[1].setOnClickListener (this);
        TvAns[2].setOnClickListener (this);
        TvAns[3].setOnClickListener (this);
        OKbutton.setOnClickListener(this);
        if (Teach)
        {
            MainTimer = null;
            PbTime.setVisibility (ProgressBar.GONE);
        }
        else
        {
            PbTime.setVisibility (ProgressBar.VISIBLE);
            PbTime.setMax (TotalTime);
            PbTime.setProgress (ElapsedTime);
            MainTimer = new Timer ();
            MainTimer.schedule (new PbTask (), 1000, 1000);
        }
    }

    void CreateTest ()
    {
        String Fn = getApplicationContext ().getDatabasePath ("Database.db").getAbsolutePath ();
        DataBaseHandler H = new DataBaseHandler (Kateg, NoQ, Fn);
        Data = H.GetData ();
    }

    void InitTest ()
    {
        DoneQuest = 0;
        CurrentQuestion = 0;
        MissQuest = 0;
        ElapsedTime = 0;
        ShowQuest (CurrentQuestion);
    }

    void ShowQuest (int N)
    {
        int i;
        Selected = -1;
        TvTitle.setText ("Ερώτηση " + (N + 1));
        TvQuest.setText (Data[N].Question);
        for (i = 0; i < 4; i++)
        {
            TvAns[i].setTextColor (Color.rgb (0, 0, 0));
            TvAns[i].setText (Data[N].Answer[i]);
        }
        OKbutton.setEnabled(false);
        CanPress = true;

    }

    @Override
    public void onClick (View v)
    {
        int i;
        if (v == OKbutton)
        {
            OKPressed ();
            return;
        }
        for (i = 0; i < 4; i++)
            if (v == TvAns[i])
                AnsPressed (i);

    }

    void OKPressed ()
    {
        OKbutton.setEnabled(false);
        CanPress = false;
        DoneQuest++; 
        if (Selected != Data[CurrentQuestion].Correct)
            MissQuest++;
        if (Teach)
        {
            SendMessage (ComGreen);
            Timer MyTimer = new Timer ();
            MyTimer.schedule (new MyTask (), 3000); 
        }
        else
        {
            NextQuest ();
        }
    }
     
    void NextQuest ()
    {
        CurrentQuestion++;
        if (CurrentQuestion == NoQ)
        {
            ToResults ();
            finish ();
        }   
        else
        {
            ShowQuest (CurrentQuestion);
        }
    }
    
    void AnsPressed (int A)
    {
        int i;
        if (!CanPress)
            return;
        Selected = A;
        for (i = 0; i < 4; i++)
            if (i == Selected)
                TvAns[i].setTextColor (Color.rgb (0, 132, 202));
            else
                TvAns[i].setTextColor (Color.rgb (0, 0, 0));
        OKbutton.setEnabled(true);
    }
    
    void SendMessage (int Ms)
    {
        Message msg = new Message ();
        Bundle bu = new Bundle ();
        bu.putInt ("Command", Ms);
        msg.setData (bu);
        MyHandler.sendMessage (msg);
    }
    
    void ToResults ()
    {   
        Bundle Bu;
        Intent intent;
        Toast.makeText (getApplicationContext(), "ΤΕΛΟΣ", Toast.LENGTH_LONG).show ();
        if (MainTimer != null)
        {
            MainTimer.cancel ();
            MainTimer = null;
        }
        Bu = new Bundle ();
        Bu.putInt ("Kategory", Kateg);
        Bu.putInt ("NumberOfQuestrions", NoQ);
        Bu.putInt ("Answered", DoneQuest);
        Bu.putInt ("Correct", DoneQuest - MissQuest);
        Bu.putInt ("Wrong", MissQuest);
        Bu.putInt ("UsedTime", ElapsedTime);
        Bu.putBoolean ("IsTeaching", Teach);
        intent = new Intent (this, ResultsActivity.class);
        intent.putExtras(Bu);
        startActivity (intent);
        finish ();
    }
    
    void UpdTimer ()
    {
        PbTime.setProgress (ElapsedTime);
        if (ElapsedTime == TotalTime)
            ToResults ();   
    }
    
    final Handler MyHandler = new Handler ()
    {
        public void handleMessage (Message msg)
        {
            Bundle bu = msg.getData ();
            int Comm = bu.getInt ("Command");
            switch (Comm)
            {
                case ComGreen:
                    TvAns[Data[CurrentQuestion].Correct].setTextColor (Color.rgb (0, 255, 0));
                    OKbutton.setEnabled(false);
                    break;
                case ComNext:
                    NextQuest ();
                    break;
                case ComTimer:
                    UpdTimer ();
                    break;
            }
        }
    };
    
    public void onBackPressed() 
    {
        
    }
    
    class MyTask extends TimerTask
    {

        @Override
        public void run ()
        {
            SendMessage (ComNext);
            cancel ();
        }
    }
    
    class PbTask extends TimerTask 
    {
        public void run ()
        {
            ElapsedTime++;
            SendMessage (ComTimer);
            if (ElapsedTime == TotalTime)
                cancel ();
        }
    }
    

}
