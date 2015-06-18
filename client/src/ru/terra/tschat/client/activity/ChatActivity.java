package ru.terra.tschat.client.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import ru.terra.tschat.client.R;
import ru.terra.tschat.client.chat.ChatHandler;
import ru.terra.tschat.client.chat.ChatNotifier;
import ru.terra.tschat.client.service.ChatService;
import ru.terra.tschat.client.storage.entity.ContactEntity;
import ru.terra.tschat.shared.packet.server.chat.ChatMessagePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 18.03.15
 * Time: 17:39
 */
public class ChatActivity extends RoboActivity {
    private ViewPager viewPager;
    private List<View> pages = new ArrayList<View>();
    private ListView lvChats, lvContacts, lvChatMessages;
    private EditText edt_chat_msg;
    private TextView tvChatTo;

    private class ChatPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Object instantiateItem(View collection, int position) {
            View v = pages.get(position);
            ((ViewPager) collection).addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (View) arg1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        viewPager = (ViewPager) findViewById(R.id.pager);

        View contacts, chats, chat;
        contacts = inflater.inflate(R.layout.f_contacts, null);
        chats = inflater.inflate(R.layout.f_chats, null);
        chat = inflater.inflate(R.layout.f_chat, null);
        pages.add(contacts);
        pages.add(chats);
        pages.add(chat);

        viewPager.setAdapter(new ChatPagesAdapter());

        lvContacts = (ListView) contacts.findViewById(R.id.lv_chat_users);
        lvChats = (ListView) chats.findViewById(R.id.lv_chats);
        lvChatMessages = (ListView) chat.findViewById(R.id.lv_chat_messages);
        tvChatTo = (TextView) chat.findViewById(R.id.tvChatTo);

        edt_chat_msg = (EditText) chat.findViewById(R.id.edt_chat_msg);

        final List<String> messages = new ArrayList<>();
        final ArrayAdapter<String> chatMessagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        lvChatMessages.setAdapter(chatMessagesAdapter);

        Cursor c = getContentResolver().query(ContactEntity.CONTENT_URI, null, null, null, null);
        lvContacts.setAdapter(new SimpleCursorAdapter(this, R.layout.i_contact_info, c, new String[]{ContactEntity.NAME, ContactEntity.UID}, new int[]{R.id.tvName, R.id.tvUid}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));

        ChatHandler.getInstance().addNotifier(new ChatNotifier() {
            @Override
            public void onChatEvent(final ChatMessagePacket chatMessagePacket) {
                lvChatMessages.post(new Runnable() {
                    @Override
                    public void run() {
                        messages.add(chatMessagePacket.getMsg());
                        chatMessagesAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void sendMessage(View v) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(new Intent(ChatService.CHAT_SERVICE_RECEIVER).putExtra(ChatService.PARAM_DO, ChatService.DO_SAY).putExtra("msg", edt_chat_msg.getText().toString()));
    }
}