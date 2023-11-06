package ir.adak.Vect.Utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Random;

import ir.adak.Vect.App;
import ir.adak.Vect.R;
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class FirebaseService extends FirebaseMessagingService {


    String title = "", content = "", id = "";
    int mode = 0;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    NotificationManager notificationManager;
    NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManagerCompat = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = getSystemService(NotificationManager.class);
            createNotificationChannel1();
        }


    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i("MyTagg", "onNewToken: New Firebase Token = " + token);
        Log.i("MyTagg", "Token Saved " + saveTokenToPrefs(token));
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

//        Call<DefaultResponse> req = App.getApi().saveFirebaseToken("Bearer " + PreferenceManager.getDefaultSharedPreferences(App.app).getString(Constants.USER_TOKEN, ""), tokenBody);
//        req.enqueue(new Callback<DefaultResponse>() {
//            @Override
//            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//                Log.i("MyTagg", "onNewToken : onFailure: " + t.getMessage());
//
//            }
//        });


    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // CheckActivity if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());

            JSONObject message = new JSONObject(remoteMessage.getData());

            try {
                Log.i("MyTagg", "onMessageReceived: " + message);
                title = message.getString("title");
                content = message.getString("content");
                mode = Integer.valueOf(message.getString("mode"));
                id = message.getString("Id");
                createNotif(title, content);


            } catch (Exception e) {
                Log.i("MyTagg", "onMessageReceived: " + e.getMessage());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "خطایی رخ داده است", Toast.LENGTH_LONG).show();
                    }
                });
            }
            //
        }

        // CheckActivity if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
//                        Toast.makeText(FirebaseService.this, ""+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();

                }
            };

            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Kasb-v3", importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setDescription("Kasb-v3");
//            channel.setGroup(channelGroup_elan.getId());
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void createNotif(String title, String content) {
        Intent notificationIntent = null;

        Bitmap bitmap = null;
        Log.i("MyTagg", "createNotif: " + mode);

        notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("fromNotif", true);


        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(100),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bitmap == null ?
                                BitmapFactory.decodeResource(getResources(), R.drawable.logo)
                                : bitmap)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);
//             notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setSummaryText("");
        Spannable sb = new SpannableString(content);
        inboxStyle.addLine(sb);
        mBuilder.setStyle(inboxStyle);

        Notification newMessageNotification = mBuilder.build();
        notificationManagerCompat.notify(new Random().nextInt(100), newMessageNotification);

    }

    public static String getToken() {
        return App.Companion.getSharedPreferences().getString(Constants.FIREBASE_TOKEN, "empty");
    }


    public static boolean saveTokenToPrefs(String _token) {
        return App.Companion.getSharedPreferences().edit().putString(Constants.FIREBASE_TOKEN , _token).commit();
    }
}
