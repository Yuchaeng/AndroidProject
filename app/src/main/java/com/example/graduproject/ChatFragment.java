package com.example.graduproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatFragment extends Fragment {
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

    private static final String TAG = "ChatFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        //firebase 선언
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSend = v.findViewById(R.id.btnSend);
        etText = v.findViewById(R.id.etText);
        chatArrayList = new ArrayList<>();

        //recyclerview
        recyclerView = v.findViewById(R.id.my_recycler_view);
        //recyclerview 높이가 가변적이지 않게
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //finish button 클릭 시 메인으로 돌아감
        Button btnFinish = v.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
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

                        Hashtable<String,String> numbers
                                = new Hashtable<String,String>();
                        numbers.put("nickname", nick);
                        numbers.put("msg", stText);

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
                mAdapter = new MyAdapter(chatArrayList, nick);
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
                Log.d(TAG, "nick"+nick);
                Log.d(TAG, "msg"+msg);
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
                Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference databaseReference = database.getReference("message");
        databaseReference.addChildEventListener(childEventListener);


        return v;
    }

}

/*

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Button btnSend;
    TextView nickname;

    FirebaseDatabase database;
    ArrayList<Chat> chatArrayList;
    private DatabaseReference mDatabaseRef;
    String nick;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        database = FirebaseDatabase.getInstance();

        Button btnFinish = v.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        btnSend = v.findViewById(R.id.btnSend);
        etText = v.findViewById(R.id.etText);
        nickname = v.findViewById(R.id.friend_name);


        //recyclerview 세팅
        recyclerView = v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"test1", "test2", "test3", "test4"};

        chatArrayList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("userInfo").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile info = snapshot.getValue(userProfile.class);
                String a = info.getNickName();
                nick = a;
                nickname.setText(a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mAdapter = new MyAdapter(chatArrayList, nick);
        recyclerView.setAdapter(mAdapter);



        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();
                String nick = chat.getNickname();
                String msg = chat.getMsg();
                Log.d(TAG, "nick: "+nick);
                Log.d(TAG, "msg: "+msg);

                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };



        return v;
    }
}*/
