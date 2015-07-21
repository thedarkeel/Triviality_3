package charis.Triviality_3.diss;

import java.util.Random;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

public class DataBaseHandler
{
    SQLiteDatabase DB;
    int Kateg;
    int NoQs;
    String DbName;
    QData[] Data;

    public DataBaseHandler (int K, int NQ, String Fn)
    {
        int[] Sel;
        DbName = Fn;
        Kateg = K;
        NoQs = NQ;
        Data = new QData [NoQs];
        try
        {
            DB = SQLiteDatabase.openDatabase (Fn, null, SQLiteDatabase.OPEN_READONLY);
            Sel = GetIDs ();
            LoadData (Sel);
            SuffleData ();

        }
        catch (SQLiteException e)
        {
            Toast.makeText (null, "Error in Database: " + e.getMessage (), Toast.LENGTH_LONG).show ();
            System.exit (1);
        }
    }

    int[] GetIDs ()
    {
        String Que1;
        String[] Que2;
        Cursor rs;
        int rss;
        int i, r;
        int[] TotalIds;
        int[] SelIds;
        Random Ra;
        if (Kateg == 0)
        {
            Que1 = "SELECT Id FROM Data WHERE Kateg > ?";
            Que2 = new String [] {"0"};
        }
        else
        {
            Que1 = "SELECT * FROM Data WHERE Kateg = ?";
            Que2 = new String [] {String.valueOf (Kateg)};
        }
        rs = DB.rawQuery (Que1, Que2);
        rss = rs.getCount ();
        TotalIds = new int [rss];
        for (i = 0; i < rss; i++)
        {
            rs.moveToPosition (i);
            TotalIds[i] = rs.getInt (0);
        }
        SelIds = new int [NoQs];
        Ra = new Random ();
        for (i = 0; i < NoQs; i++)
        {
            do
                r = Ra.nextInt (rss);
            while (TotalIds[r] == -1);
            SelIds[i] = TotalIds[r];
            TotalIds[r] = -1;
        }
        rs.close ();
        return SelIds;
    }

    void LoadData (int[] Sel)
    {
        String Que1;
        String[] Que2;
        Cursor rs;
        int i;
        Que1 = "SELECT * From Data WHERE Id = ?";
        for (i = 0; i < NoQs; i++)
        {
            System.out.println ("***" + Sel[i]);
            Que2 = new String [] {String.valueOf (Sel[i])};
            rs = DB.rawQuery (Que1, Que2);
            rs.moveToFirst ();
            Data[i] = new QData ();
            Data[i].Quest = rs.getString (1);
            Data[i].Ans[0] = rs.getString (2);
            Data[i].Ans[1] = rs.getString (3);
            Data[i].Ans[2] = rs.getString (4);
            Data[i].Ans[3] = rs.getString (5);
            Data[i].Corr = rs.getInt (6) - 1;
            Data[i].Kateg = rs.getInt (7);
            rs.close ();
        }
    }
    
    void SuffleData ()
    {
        int i, j;
        int s1, s2;
        String tmp;
        Random Ra = new Random ();
        for (i = 0; i < NoQs; i++)
        {
            for (j = 0; j < 8; j++)
            {
                s1 = Ra.nextInt (4);
                s2 = Ra.nextInt (4);
                tmp = Data[i].Ans[s1];
                Data[i].Ans[s1] = Data[i].Ans[s2];
                Data[i].Ans[s2] = tmp;
                if (Data[i].Corr == s2)
                    Data[i].Corr = s1;
                else 
                    if (Data[i].Corr == s1)
                        Data[i].Corr = s2;   
            }
        }
    }

    QData[] GetData ()
    {
        DB.close ();
        return Data;
        
    }
}

class QData
{
    String Quest;
    String[] Ans;
    int Kateg;
    int Corr;
    int Guess;
    int Time;

    QData ()
    {
        Quest = null;
        Ans = new String [4];
        Ans[3] = Ans[2] = Ans[1] = Ans[0] = null;
        Guess = -1;
        Time = 0;
    }
}