package ru.terra.tschat.client.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import ru.terra.tschat.client.R;
import ru.terra.tschat.client.activity.ChatActivity;
import ru.terra.tschat.client.framework.*;
import ru.terra.tschat.client.network.NetworkHelper;
import ru.terra.tschat.client.storage.entity.ContactEntity;
import ru.terra.tschat.client.util.AndroidClassSearcher;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Date: 18.03.15
 * Time: 17:40
 */
public class ChatService extends IntentService {

    public static final String CHAT_SERVICE_RECEIVER = " ru.terra.tschat.client.service.chat_service_receiver";
    public static final String PARAM_DO = "do";
    public static final int DO_CONNECT = 1;
    public static final int DO_LOGIN = 2;
    public static final int DO_REG = 3;
    public static final int DO_SAY = 4;


    private ChatServiceReceiver chatReceiver;
    private SharedPreferences prefs;
    private Notification notification;
    private Notification.Builder builder;

    public ChatService() {
        super("Chat background service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter statusFilter = new IntentFilter(CHAT_SERVICE_RECEIVER);
        statusFilter.addCategory(Intent.CATEGORY_DEFAULT);
        chatReceiver = new ChatServiceReceiver();
        Intent intent = new Intent(this, ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, statusFilter);
        builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("Terra chat");
        builder.setContentText("Terra chat status");
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setLargeIcon(bm);
        builder.setPriority(0);
        builder.setOngoing(true);
        NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(01, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(01);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedContext.getInstance().setClassSearcher(new AndroidClassSearcher<AbstractPacket>(getApplicationContext()));

        ClientStateHolder.getInstance().setNotifier(new MyClientStateChangeNotifier());
        ClientManager.getInstance().setNotifier(new ClientManagerNotifier() {
            @Override
            public void onEvent(ClientManagerEvent event) {
                switch (event) {
                    case CONTACT_INFO: {
                        List<UserInfo> info = new ArrayList<>(ClientManager.getInstance().getContacts());
                        for (UserInfo ui : info)
                            if (!isContactExists(ui.getUID())) {
                                ContentValues cv = new ContentValues();
                                cv.put(ContactEntity.NAME, ui.getName());
                                cv.put(ContactEntity.UID, ui.getUID());
                                getContentResolver().insert(ContactEntity.CONTENT_URI, cv);
                            }
                    }
                    break;
                }
            }
        });

        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.INIT);
        NetworkManager.getInstance().start(ClientWorker.class, getString(R.string.ip), 12345);

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ChatServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(PARAM_DO, 0)) {
                case DO_LOGIN:
                    NetworkHelper.sendLogin(prefs.getString(getString(R.string.login), UUID.randomUUID().toString()), prefs.getString(getString(R.string.pass), UUID.randomUUID().toString()));
                    break;
                case DO_REG: {
                }
                break;
                case DO_SAY:
                    NetworkHelper.sendMessage(new Date() + " " + intent.getStringExtra("msg"), 0L);
                    break;
            }
        }
    }

    private class MyClientStateChangeNotifier implements ClientStateChangeNotifier {
        @Override
        public void onClientStateChange(ClientStateHolder.ClientState oldgs, ClientStateHolder.ClientState newgs) {
//            Toast.makeText(ChatService.this, "Changing state from " + oldgs.name() + " to " + newgs.name(), Toast.LENGTH_SHORT).show();
            builder.setContentText("Current state: " + newgs.name());
            NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManger.notify(01, builder.build());
            if (newgs.equals(ClientStateHolder.ClientState.LOGGED_IN)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.loggedIn), true);
                editor.commit();
            }
            if (newgs.equals(ClientStateHolder.ClientState.USER_BOOT)) {
                NetworkHelper.sendContactsInfoRequest();
            }
        }
    }

    private boolean isContactExists(Long uid) {
        Cursor c = null;
        try {
            c = getContentResolver().query(ContactEntity.CONTENT_URI, null, ContactEntity.UID + " = ?", new String[]{uid.toString()}, null);
            if (c == null)
                return false;
            if (c.getCount() > 0)
                return true;
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }
        return false;
    }
}
