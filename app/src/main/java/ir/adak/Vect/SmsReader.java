package ir.adak.Vect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReader extends BroadcastReceiver {
    private static final String TAG =
            SmsReader.class.getSimpleName();
    public static final String pdu_type = "pdus";
    String Ms="";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle=intent.getExtras();
            if (bundle!=null)
            {
                Object[] mypdu=(Object[])bundle.get("pdus");
                SmsMessage[] message=new SmsMessage[mypdu.length];
                for (int i=0;i<mypdu.length;i++)
                {
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    {
                    String format=bundle.getString("format");
                    message[i]=SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    }else {
                    message[i]=SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    Ms=message[i].getMessageBody();
                    String Phone=message[i].getOriginatingAddress();
                    Log.i("svmasdkvsc",Ms);
                    Log.i("svmasdkvsc",Phone);


                    if (Phone.contains("981000")||Phone.contains("982000")||Phone.contains("983000"))
                    {
                        String  s=Ms.substring(20,24);
                        String s1= String.valueOf(s.charAt(0));
                        String s2=String.valueOf(s.charAt(1));
                        String s3=String.valueOf(s.charAt(2));
                        String s4=String.valueOf(s.charAt(3));
                        Intent intent1=new Intent("SmsJBase");
                        intent1.putExtra("Data1",s1);
                        intent1.putExtra("Data2",s2);
                        intent1.putExtra("Data3",s3);
                        intent1.putExtra("Data4",s4);
                        intent1.putExtra("Data5",Ms.substring(20,24));
                        context.sendBroadcast(intent1);
                    }


                }
            }
        }
    }



}
