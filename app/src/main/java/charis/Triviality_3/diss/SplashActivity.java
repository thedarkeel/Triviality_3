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

public class SplashActivity extends Activity implements OnClickListener
{
    LinearLayout LinearLayout;
    boolean DbFlag = false;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.splashlayout);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout = (LinearLayout) findViewById (R.id.LinearSplash);
        LinearLayout.setOnClickListener(this);
        CheckDB ();
    }

    @Override
    public void onClick (View v)
    {
        if (DbFlag)
        {
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity (intent);
            finish ();
        }
    }
    
    private void CheckDB ()
    {
        InputStream Input;
        OutputStream Output;
        File DF = getApplicationContext ().getDatabasePath ("Database.db");
        byte[] Buff;
        int r;
        try
        {
            if (!DF.exists ())
            {
                getApplicationContext ().getDatabasePath ("Database.db").getParentFile ().mkdir ();
                Input = getAssets ().open ("Database.db");
                Output = new FileOutputStream (DF);
                Buff = new byte[1024];
                while ((r = Input.read (Buff)) > 0)
                    Output.write (Buff, 0,  r);
                Input.close();
                Output.close();
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
