package charis.Triviality_3.diss;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener
{
    LinearLayout LlR;
    TextView TvR2;
    TextView TvR3;
    TextView TvR4;
    TextView TvR5;
    TextView TvR6;
    TextView TvR7;
    TextView TvR8;
    Button BtRestart;
    int Kateg;
    int NoQ;
    int Answered;
    int Correct;
    int Wrong;
    int Elapsed;
    boolean Teach;
      
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.resultslayout);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        GetData ();
        LlR = (LinearLayout) findViewById (R.id.ResultsMain);
        TvR2 = (TextView) findViewById (R.id.RF2);
        TvR3 = (TextView) findViewById (R.id.RF3);
        TvR4 = (TextView) findViewById (R.id.RF4);
        TvR5 = (TextView) findViewById (R.id.RF5);
        TvR6 = (TextView) findViewById (R.id.RF6);
        TvR7 = (TextView) findViewById (R.id.RF7);
        TvR8 = (TextView) findViewById (R.id.RF8);
        BtRestart = (Button) findViewById (R.id.BtRestart);
        BtRestart.setOnClickListener (this);
        LlR.setBackgroundResource (R.drawable.maskres1);
        ShowData ();            
    }
    
    void GetData ()
    {
        Intent Imp = getIntent ();
        Bundle Bu = Imp.getExtras ();
        Kateg = Bu.getInt ("Kategory");
        NoQ = Bu.getInt ("NumberOfQuestrions");
        Answered = Bu.getInt ("Answered");
        Correct = Bu.getInt ("Correct");
        Wrong = Bu.getInt ("Wrong");
        Elapsed = Bu.getInt ("UsedTime");
        Teach = Bu.getBoolean ("IsTeaching");
    }
    
    void ShowData ()
    {
        int Score;
        double Synt;
        switch (Kateg)
        {
            case 1: TvR2.setText ("Εύκολο"); Synt = 1.0; break;
            case 2: TvR2.setText ("Δύσκολο"); Synt = 1.8; break;
            default: TvR2.setText ("Μεικτό"); Synt = 1.5; break;
        }
        TvR3.setText (String.valueOf (NoQ));
        TvR4.setText (String.valueOf (Answered));
        TvR5.setText (String.valueOf (Correct));
        TvR6.setText (String.valueOf (Wrong));
        
        if (Teach)
        {
            TvR7.setText ("-");
            TvR8.setText ("-");
        }
        else
        {
            TvR7.setText (String.valueOf (Elapsed) + "'");
            Score = (int) (Correct * 100 * Synt);
            if (Correct > 8) 
                Score += (int) ((90 - Elapsed) * 10 * Synt); 
            TvR8.setText (String.valueOf (Score));
        }
    }  
    
    public void onBackPressed() 
    {
        finish ();
    }

    @Override
    public void onClick (View v)
    {
        Intent ri = new Intent (this, SettingsActivity.class);
        startActivity (ri);    
    }
}
