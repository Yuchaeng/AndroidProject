package com.example.graduproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFunction extends AppCompatActivity {
    private String chatRoomUid; //채팅방 id
    private String myUid; //내 id
    private String destUid; // 상대방 id

    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;

    private FirebaseDatabase firebaseDatabase;

    private userProfile destUser;

    private  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_function);



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

        init();
        sendMsg();


    }

    private void init()
    {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        destUid = intent.getStringExtra("destUid");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        button = (Button) findViewById(R.id.btnSend);
        editText = (EditText) findViewById(R.id.etText);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if (editText.getText().toString() == null) button.setEnabled(false);
        else button.setEnabled(true);

        checkChatRoom();
    }

    private void sendMsg()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myUid, true);
                chatModel.users.put(destUid, true);

                if (chatRoomUid == null) {
                    Toast.makeText(ChatFunction.this, "채팅방 생성", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });
                } else {
                    sendMsgToDataBase();
                }
            }
        });
    }

    private void sendMsgToDataBase()
    {
        if (!editText.getText().toString().equals(""))
        {
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myUid;
            comment.message = editText.getText().toString();
            comment.timestamp = ServerValue.TIMESTAMP;
            firebaseDatabase.getReference().child("chatrooms").child(chatRoomUid).child("comments").push()
                    .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            editText.setText("");
                        }
                    });
        }
    }

    private void checkChatRoom()
    {
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/"+myUid)
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                            if (chatModel.users.containsKey(destUid)){
                                chatRoomUid = dataSnapshot.getKey();
                                button.setEnabled(true);

                                recyclerView.setLayoutManager(new LinearLayoutManager(ChatFunction.this));
                                recyclerView.setAdapter(new RecyclerViewAdapter());

                                sendMsgToDataBase();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            getDestUid();
        }

        private void getDestUid()
        {
            firebaseDatabase.getReference().child("users").child(destUid).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            destUser = snapshot.getValue(userProfile.class);

                            getMessageList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
        }

        private void getMessageList()
        {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid)
                    .child("comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            comments.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                comments.add(dataSnapshot.getValue(ChatModel.Comment.class));
                            }
                            notifyDataSetChanged();

                            recyclerView.scrollToPosition(comments.size()-1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            ViewHolder viewHolder = ((ViewHolder) holder);

            if (comments.get(position).uid.equals(myUid))
            {
                viewHolder.textViewMsg.setText(comments.get(position).message);
                //viewHolder.textViewMsg.setBackgroundResource(R.drawable.chat_bubble);
                viewHolder.linearLayoutDest.setVisibility(View.INVISIBLE);        //상대방 레이아웃
                viewHolder.linearLayoutRoot.setGravity(Gravity.RIGHT);
                viewHolder.linearLayoutTime.setGravity(Gravity.RIGHT);
            }else{
                //상대방 말풍선 왼쪽
                FirebaseDatabase.getInstance().getReference("userInfo").child(comments.get(position).uid).child("profileImageUrl").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profileimg = snapshot.getValue(String.class);
                        Glide.with(holder.itemView.getContext())
                                .load(profileimg)
                                //.apply(new RequestOptions().circleCrop())
                                .into(holder.imageViewProfile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /*if (destUser.getProfileImageUrl() == null){
                    Glide.with(holder.itemView.getContext())
                            .load(R.drawable.no_profile_image)
                            .into(holder.imageViewProfile);

                } else{
                    Glide.with(holder.itemView.getContext())
                            .load(destUser.getProfileImageUrl())
                            //.apply(new RequestOptions().circleCrop())
                            .into(holder.imageViewProfile);
                }*/

                FirebaseDatabase.getInstance().getReference("userInfo").child(comments.get(position).uid).child("nickName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nick = snapshot.getValue(String.class);
                        viewHolder.textViewName.setText(nick);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                viewHolder.linearLayoutDest.setVisibility(View.VISIBLE);
                //viewHolder.textViewMsg.setBackgroundResource(R.drawable.chat_bubble);
                viewHolder.textViewMsg.setText(comments.get(position).message);
                viewHolder.linearLayoutRoot.setGravity(Gravity.LEFT);
                viewHolder.linearLayoutTime.setGravity(Gravity.LEFT);
            }
            viewHolder.textViewTimeStamp.setText(getDateTime(position));


        }

        public String getDateTime(int position)
        {
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            String time = simpleDateFormat.format(date);
            return time;
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewMsg;
            public TextView textViewName;
            public TextView textViewTimeStamp;
            public CircleImageView imageViewProfile;
            public LinearLayout linearLayoutDest;
            public LinearLayout linearLayoutRoot;
            public LinearLayout linearLayoutTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewMsg = (TextView) itemView.findViewById(R.id.item_messagebox_textview_msg);
                textViewName = (TextView) itemView.findViewById(R.id.item_messagebox_TextView_name);
                textViewTimeStamp = (TextView)itemView.findViewById(R.id.item_messagebox_textview_timestamp);
                imageViewProfile = (CircleImageView) itemView.findViewById(R.id.item_messagebox_ImageView_profile);
                linearLayoutDest = (LinearLayout)itemView.findViewById(R.id.item_messagebox_LinearLayout);
                linearLayoutRoot = (LinearLayout)itemView.findViewById(R.id.item_messagebox_root);
                linearLayoutTime = (LinearLayout)itemView.findViewById(R.id.item_messagebox_layout_timestamp);
            }
        }
    }


}
/*
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
        String destUid = intent.getStringExtra("destUid");

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
                //mAdapter = new MyAdapter(chatArrayList, nick, nick2);
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

               */
/*if (chat.getId().equals(nick2)){
                    chatArrayList.add(chat);
                } else if(chat.getId().equals(mUser.getUid())){
                    chatArrayList.add(chat);
                }*//*

                //String nick2 = getArguments().getString("nick2");
                */
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
                }*//*

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
                */
/*Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();*//*

            }
        };
        DatabaseReference databaseReference = database.getReference("message");
        databaseReference.addChildEventListener(childEventListener);
    }
}*/
