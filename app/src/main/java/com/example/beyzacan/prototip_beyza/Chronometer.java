package com.example.beyzacan.prototip_beyza;

/**
 * Created by fince on 22.04.2018.
 */
import android.content.Context;

/**
 * Created by gen on 20.04.2018.
 */

public class Chronometer implements Runnable {
    public static final long MILLIS_TO_MINUTES=60000;
    public static final long MILLSI_TO_HOURS=3600000;
    private Context mContext;
    private long mStartTime;
    private boolean isTravelPause=false;

    public void setTravelPause(boolean travelPause) {
        isTravelPause = travelPause;
    }

    private boolean mIsRunning;
    public Chronometer(Context context){
        mContext=context;

    }
    public void start(){
        mStartTime= System.currentTimeMillis();
        mIsRunning=true;
    }
    public void stop(){
        mIsRunning=false;
        mStartTime= System.currentTimeMillis();
    }



    @Override
    public void run() {
        while(mIsRunning){
            if(!isTravelPause)
            {
                long since = System.currentTimeMillis()-mStartTime;

                int seconds = (int) ((since/1000)%60);
                int minutes= (int) ((since/MILLIS_TO_MINUTES)%60);
                int hours=(int)((since/MILLSI_TO_HOURS)%24);


                ((WayActivity)mContext).updateTimerText(String.format("%02d:%02d:%02d",hours,minutes,seconds));
            }

        }

    }
}