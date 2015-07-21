package charis.Triviality_3.diss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import charis.Triviality_3.diss.SettingsAct;

public class SplashAct extends Activity implements OnClickListener 
{
    LinearLayout LL;
    boolean DbFlag = false;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.splashlayout);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LL = (LinearLayout) findViewById (R.id.LinLay);
        LL.setOnClickListener (this);
        CheckDB ();
    }

    @Override
    public void onClick (View v)
    {
        if (DbFlag)
        {
            Intent NA = new Intent (this, SettingsAct.class);
            startActivity (NA);
            finish ();
        }
    }
    
    private void CheckDB ()
    {
        InputStream Src;
        OutputStream Dest;
        File DF = getApplicationContext ().getDatabasePath ("Database.db");
        byte[] Buff;
        int r;
        try
        {
            if (!DF.exists ())
            {
                getApplicationContext ().getDatabasePath ("Database.db").getParentFile ().mkdir ();
                Src = getAssets ().open ("Database.db");
                Dest = new FileOutputStream (DF);
                Buff = new byte[1024];
                while ((r = Src.read (Buff)) > 0)
                    Dest.write (Buff, 0,  r);
                Src.close ();
                Dest.close ();
                Toast.makeText (getApplicationContext (), "Copy OK!", Toast.LENGTH_LONG).show ();
            }
            DbFlag = true;
        }
        catch (Exception e)
        {
            Toast.makeText (getApplicationContext (), e.getMessage (), Toast.LENGTH_LONG).show ();
        }
    }

    public void onBackPressed() 
    {
        
    }
}
