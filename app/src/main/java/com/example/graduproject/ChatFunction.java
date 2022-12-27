package com.example.graduproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatFunction extends AppCompatActivity {
    //recyclerview
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    EditText etText;
    Button btnSend;

    ArrayList<Chat> chatArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_function);

        //firebase 선언
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSend = findViewById(R.id.btnSend);
        etText = findViewById(R.id.etText);
        chatArrayList = new ArrayList<>();

        //recyclerview
        recyclerView = findViewById(R.id.my_recycler_view);
        //recyclerview 높이가 가변적이지 않게
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String nick2 = intent.getStringExtra("nick2");

        //finish button 클릭 시 메인으로 돌아감
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatFunction.this, FriendTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        //send button 클릭 시
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String datetime = dateFormat.format(c.getTime());

                DatabaseReference myRef = database.getReference("message").child(datetime);

                mDatabase.child("userInfo").child(mUser.getUid()).child("nickName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nick = snapshot.getValue(String.class);
                        String id = mUser.getUid();

                        Hashtable<String,String> numbers
                                = new Hashtable<String,String>();
                        numbers.put("nickname", nick);
                        numbers.put("msg", stText);
                        numbers.put("id", id);

                        //firebase에 데이터 생성
                        myRef.setValue(numbers);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                etText.setText("");
            }
        });

        mDatabase.child("userInfo").child(mUser.getUid()).child("nickName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nick = snapshot.getValue(String.class);

                //recyclerview adapter
                mAdapter = new MyAdapter(chatArrayList, nick, nick2);
                recyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //firebase에서 data 읽어오기
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                String nick = chat.getNickname();
                String msg = chat.getMsg();
                String id = chat.getId();
                Log.d(TAG, "nick"+nick);
                Log.d(TAG, "msg"+msg);

               /*if (chat.getId().equals(nick2)){
                    chatArrayList.add(chat);
                } else if(chat.getId().equals(mUser.getUid())){
                    chatArrayList.add(chat);
                }*/
                //String nick2 = getArguments().getString("nick2");
                /*if(nick.equals(nick2)) {
                    if (msg.isEmpty()){
                        msg = "대화를 시작하세요";

                    }
                    chatArrayList.add(chat);
                } else if(id.equals(mUser.getUid())){
                    if (msg == null) {
                        msg = "";
                    }
                    chatArrayList.add(chat);
                }*/
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                /*Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();*/
            }
        };
        DatabaseReference databaseReference = database.getReference("message");
        databaseReference.addChildEventListener(childEventListener);
    }
}