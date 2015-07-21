package charis.Triviality_3.diss;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SettingsActivity extends Activity implements OnClickListener
{

    Button BtStart;
    RadioButton RbTeach, RbExam;
    Spinner SpLevel;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.settingslay);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BtStart = (Button) findViewById (R.id.BtStart);
        RbTeach = (RadioButton) findViewById (R.id.RbTeach);
        RbExam = (RadioButton) findViewById (R.id.RbExamine);
        SpLevel = (Spinner) findViewById (R.id.SpLevel);
        BtStart.setOnClickListener (this);
        RbTeach.setOnClickListener (this);
        RbExam.setOnClickListener (this);
        RbExam.setChecked (true);
    }

    @Override
    public void onClick (View v)
    {
        int Level;
        boolean Teach;
        if (v == RbTeach)
            if (RbTeach.isChecked ())
                RbExam.setChecked (false);
            else
                RbExam.setChecked (true);
        if (v == RbExam)
            if (RbExam.isChecked ())
                RbTeach.setChecked (false);
            else
                RbTeach.setChecked (true);
        if (v == BtStart)
        {
            Intent ns = new Intent (this, MainActivity.class);
            Bundle Bu = new Bundle ();
            Teach = RbTeach.isChecked ();
            Level = SpLevel.getSelectedItemPosition ();
            if (++Level == 3)
                Level = 0;
            Bu.putInt ("Kategory", Level);
            Bu.putBoolean ("IsTeaching", Teach);
            ns.putExtras (Bu);
            startActivity (ns);
            finish ();
        }
    }
    
    public void onBackPressed() 
    {
        
    }

}
