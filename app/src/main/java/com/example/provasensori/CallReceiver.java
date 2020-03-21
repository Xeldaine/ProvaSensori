package com.example.provasensori;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    public final static String TAG = CallReceiver.class.getSimpleName();

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        Log.i(TAG, "Incoming call");
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        Log.i(TAG, "Answered incoming call");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        Log.i(TAG, "Ended incoming call");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        Log.i(TAG, "Started outgoing call");
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        Log.i(TAG, "Ended outgoing call");
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        Log.i(TAG, "Missed call");
    }

}
