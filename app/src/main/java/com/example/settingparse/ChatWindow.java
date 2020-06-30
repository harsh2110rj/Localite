package com.example.settingparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatWindow extends AppCompatActivity {

        String activeUser="";
        ArrayList<String> chats = new ArrayList<>();
        ArrayAdapter arrayAdapter;
        boolean isRunning=true;
        boolean toCall=true;
        static Handler handler;
        private Date lastMsgDate;
    private static final String TAG = "ChatWindow";

        public void loadConversation(){
            chats.clear();
            ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Messages");;
            ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Messages");
//            if(lastMsgDate==null) {
//            query1 = new ParseQuery<ParseObject>("Messages");
                query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
                query1.whereEqualTo("recipient", activeUser);

//            query2 = new ParseQuery<ParseObject>("Messages");
                query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
                query2.whereEqualTo("sender", activeUser);
//            }
//            else{
//                Log.i("last date",lastMsgDate.toString());
//                query1.whereGreaterThan("createdAt",lastMsgDate);
//                query2.whereGreaterThan("createdAt",lastMsgDate);
//            }
            List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
            queries.add(query1 );
            queries.add(query2);

            ParseQuery<ParseObject> query = ParseQuery.or(queries);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    try {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject message : objects) {
                                    String chatting = message.getString("message");
                                    if (message.getString("recipient").equals(ParseUser.getCurrentUser().getUsername())) {
                                        chats.add("Message >> " + chatting);
                                    } else {

                                        chats.add("you >> "+ chatting);
                                    }
                                    lastMsgDate = message.getDate("createdAt");

                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
//                if(toCall) {
//                    toCall=false;
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (isRunning)
                                loadConversation();
                        }
                    }, 2000);
//                }
                }
            });
            Log.i("loading","finished");

        }

        @Override
        protected void onResume() {
            super.onResume();
            isRunning=true;
            loadConversation();
        }

        @Override
        protected void onPause() {
            super.onPause();
            isRunning=false;
        }


        public void sendChat(View view){
            final EditText chatText = (EditText) findViewById(R.id.chatText);
            ParseObject messages = new ParseObject("Messages");
            messages.put("message",chatText.getText().toString());
            messages.put("sender",ParseUser.getCurrentUser().getUsername());
            messages.put("recipient",activeUser);
            messages.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
//                    Log.i("saving","successful");
//                    if(toCall){
//                        toCall=false;
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (isRunning)
//                                    loadConversation();
//                            }
//                        }, 1000);
//                    }
//                    chats.add(chatText.getText().toString());
//                    arrayAdapter.notifyDataSetChanged();
//                    loadConversation();

                    }
                    else{
//                    Log.i("saving","unsuccessful");
                    }
                }
            });
//        if(toCall) {
//            toCall=false;
//            previousChat();
//
//        }

            chatText.setText(null);
        }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        setTitle("Chat Activity");
        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        Log.d(TAG, "onCreate: activeuser name is: "+activeUser);
        if(activeUser==null){activeUser="Anonymous service provider";}
        ListView listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chats);
        listView.setAdapter(arrayAdapter);
//        Log.i("user",activeUser);
        handler = new Handler();

    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//
//        Intent intent=new Intent(ChatWindow.this,ServiceProviders.class);
//        startActivity(intent);
//
//
//
//    }
}