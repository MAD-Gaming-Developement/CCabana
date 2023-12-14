package dev.kai.mgdg008;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CabanaConfig extends Application {

    public static final String appCode = "BRL";
    public static String success="";
    public static String apiURL = "https://backend.madgamingdev.com/api/gameid";
    public static String gameURL="";
    public static String jsInterface = "jsBridge";
    public static boolean BooleanStatus = false;

    private MCrypt mCrypt;


    @Override
    public void onCreate() {
        super.onCreate();
        setupURL();
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        Pusher pusher = new Pusher("fdcb398aff6445bc7bd6", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe(getPackageName());

        channel.bind("my-event", event -> {
            try {
                JSONObject notifyMsg = new JSONObject(event.getData());

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (!notificationManager.areNotificationsEnabled()) {
                    // Notifications are disabled, guide the user to enable them
                    // You can also open the app settings page for notifications
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    showNotification("Announcement", notifyMsg.getString("message"), notifyMsg.getString("url"));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });
    }
    private void showNotification(String title, String message, String link) {

        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification);
        remoteViews.setTextViewText(R.id.notificationTitle, title);
        remoteViews.setTextViewText(R.id.notificationMessage, message);

        // Create an intent to open the link when the button is clicked
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, openLinkIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        remoteViews.setOnClickPendingIntent(R.id.openLinkButton, pendingIntent);

        NotificationChannel channel = new NotificationChannel("my-channel", "Announcements", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my-channel")
                .setSmallIcon(R.drawable.baseline_doorbell)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setAutoCancel(true);

        NotificationManagerCompat notificationMg = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationMg.notify(1, builder.build());

    }
    public static class MCrypt {
        static char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        private static SecretKeySpec secretkey;
        private static Cipher convert;
        private static final String key = "21913618CE86B5D53C7B84A75B3774CD";
        private static final String transformation = "AES/CBC/NoPadding";

        public MCrypt() {
            secretkey = new SecretKeySpec("21913618CE86B5D53C7B84A75B3774CD".getBytes(), "AES");
            try {
                convert = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchPaddingException | NoSuchAlgorithmException var2) {
                var2.printStackTrace();
            }
        }
        public static String decrypt(String encryptedData, String secretKey) throws Exception {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[16];
            System.arraycopy(encryptedBytes, 0, iv, 0, 16);
            byte[] ciphertext = new byte[encryptedBytes.length - 16];
            System.arraycopy(encryptedBytes, 16, ciphertext, 0, ciphertext.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, secretKeySpec, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return (new String(decryptedBytes, StandardCharsets.UTF_8)).trim();
        }
    }
    public void setupURL() {
        this.mCrypt = new MCrypt();
        dev.kai.mgdg008.Volley.init(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("appid", appCode);
            requestBody.put("package", this.getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String endPoint = apiURL + "?appid=" + appCode + "&package=" + this.getPackageName();
        Log.d("urlResult", endPoint);

        JsonObjectRequest myReq = new JsonObjectRequest(0, endPoint, requestBody, (response) -> {
            Log.e("urlResponse", "JSON:Response - " + response.toString());
            try {
                MCrypt var10000 = this.mCrypt;
                String decryptedText = MCrypt.decrypt(response.getString("data"), "21913618CE86B5D53C7B84A75B3774CD");
                JSONObject jsonData = new JSONObject(decryptedText);
                CabanaConfig.gameURL = jsonData.getString("gameURL");
                CabanaConfig.BooleanStatus = Boolean.parseBoolean(response.getString("gameKey"));
                CabanaConfig.success = jsonData.getString("status");
                Log.e("decrypURL", "Decrypted: " + decryptedText);
            } catch (Exception var6) {
                var6.printStackTrace();
            }

        }, (error) -> {
            Log.e("VolleyError", "Error: " + error.getMessage());
        });
        requestQueue.add(myReq);
    }
}
