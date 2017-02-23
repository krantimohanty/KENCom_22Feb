package com.swash.kencommerce.GCM;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.swash.kencommerce.LoginActivity;

/**
 * Created by Kranti on 12/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Log.e(TAG, "remoteMessage: " + remoteMessage.toString());

            try {
                /*JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //String test = "{\"type\":1, \"badge\":1, \"message\":\"This is for test\", \"senderid\":19484110841}";
                //JSONObject json = new JSONObject(test);
                handleDataMessage(json);*/

                String type = remoteMessage.getData().get("type");
                String message = remoteMessage.getData().get("message");
                //boolean isBackground = data.getBoolean("is_background");
                //String imageUrl = data.getString("image");
                String badge = remoteMessage.getData().get("badge");
                String senderid = remoteMessage.getData().get("senderid");

                handleDataMessage(type,message,badge,senderid);


            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }



    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }


    /*private void handleDataMessage(String type, String message, String badge, String senderid) {
    }
*/
    //private void handleDataMessage(JSONObject json) {
    //Log.e(TAG, "push json: " + json.toString());
    private void handleDataMessage(String type, String message, String badge, String senderid){
       /* try {*/
            /*JSONObject data = new JSONObject(json);*/

           /* String type = json.getString("type");
            String message = json.getString("message");
            //boolean isBackground = data.getBoolean("is_background");
            //String imageUrl = data.getString("image");
            String badge = json.getString("badge");
            String senderid = json.getString("senderid");
            //JSONObject payload = data.getJSONObject("payload");*/

            Log.e(TAG, "type: " + type);
            Log.e(TAG, "message: " + message);
            //Log.e(TAG, "isBackground: " + isBackground);
           // Log.e(TAG, "payload: " + payload.toString());
            //Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "senderid: " + senderid);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), type, message, badge, resultIntent);

                // check for image attachment
                /*if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }*/
            }
        }
        /*catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }*/
    //}

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage("KenCommerceApp", message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
