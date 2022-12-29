package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myChange extends AppCompatActivity {

    TextView picChange, whatName, whatBirth, whatGender, deptId, nickName, whenEmpty,
            whatInterest, writeIntroduce, nickNameChage, introduceChange, emptyTimeChange, interstChange, signoutUser, deleteUser;
    ImageView profileImg;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    private String pathUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychage);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        whatName = findViewById(R.id.whatName);
        whatBirth = findViewById(R.id.whatBrith);
        whatGender = findViewById(R.id.whatGender);
        deptId = findViewById(R.id.deptId);
        nickName = findViewById(R.id.nickName);
        whenEmpty = findViewById(R.id.whenEmpty);
        whatInterest = findViewById(R.id.whatInterest);
        writeIntroduce = findViewById(R.id.writeIntroduce);
        nickNameChage = findViewById(R.id.nickNameChange);
        introduceChange = findViewById(R.id.introduceChange);
        emptyTimeChange = findViewById(R.id.emptyTimeChange);
        interstChange = findViewById(R.id.interestChange);

        signoutUser = findViewById(R.id.signoutUser);
        deleteUser = findViewById(R.id.deleteUser);
        picChange = findViewById(R.id.picChange);
        profileImg = findViewById(R.id.myPicture);

        storage = FirebaseStorage.getInstance(); //스토리지 인스턴스
        storageRef = storage.getReference(); //스토리지 참조

        //파이어베이스에서 데이터 가져와서 표시
        mDatabaseRef.child("userInfo").child(mUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        userProfile profile = snapshot.getValue(userProfile.class);
                        String name = profile.getName();
                        String birth = profile.getBirth();
                        String gender = profile.getGender();
                        String dept = profile.getDept();
                        String studentId = profile.getStudentId();
                        String whatnickName = profile.getNickName();
                        String emptyString = profile.getEmptyTime();
                        String interestString = profile.getInterest();
                        String introduce = profile.getIntroduce();

                        whatName.setText(name);
                        whatBirth.setText(birth);
                        whatGender.setText(gender);
                        deptId.setText(dept + "/" + studentId);
                        nickName.setText(whatnickName);
                        whenEmpty.setText(emptyString);
                        whatInterest.setText(interestString);
                        writeIntroduce.setText(introduce);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException());
                    }
                }
        );

        //프로필 사진 띄우기
        StorageReference pathRef = storageRef.child("UserProfile").child(mUser.getUid()+"_img");
        if(pathRef!=null) { //profile.getProfileImageUrl() != null

            pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(myChange.this)
                            .load(uri)
                            .into(profileImg);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(myChange.this).load(R.drawable.no_profile_image).into(profileImg);
                }
            });

        }

        //프로필사진 변경
        picChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                launcher.launch(intent);

            }
        });

        //닉네임 변경
        nickNameChage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myChange.this,nickNameChange.class);
                intent.putExtra("nickname",nickName.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        //공강시간 추가
        emptyTimeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myChange.this,emptyTime.class);
                startActivity(intent);
                finish();
            }
        });

        //관심사 추가
        interstChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myChange.this,interest.class);
                startActivity(intent);
                finish();
            }
        });


        //한줄소개 변경
        introduceChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myChange.this,introduce.class);
                if(!writeIntroduce.getText().toString().equals("한줄소개를 작성해주세요.")) {
                    intent.putExtra("oneline",writeIntroduce.getText().toString());
                }
                startActivity(intent);
                finish();
            }
        });


        //로그아웃
        signoutUser.setPaintFlags(signoutUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(myChange.this,login.class));
            }
        });

        //회원탈퇴
        deleteUser.setPaintFlags(deleteUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    } //oncreate 끝


    //사진 선택 후 로드하기
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Log.e(TAG, "result : " + result);
                        Intent intent = result.getData();
                        imageUri = intent.getData();
                        pathUri = getPath(intent.getData());
                        Log.e(TAG, "uri : " + imageUri);
                        //imageview.setImageURI(imageUri); -> glide 안쓰면 얘 쓰기
                        Glide.with(myChange.this)
                                .load(imageUri)
                                .into(profileImg);

                        uploadToFirebase(imageUri);
                    }

                }
            });
    //uri의 path를 가져오는...???
    public String getPath(Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

    //프사 바꿀 때 올렸던 사진 있으면 삭제하고 업로드 -> 프사용 사진은 한 계정당 항상 한개만 저장됨
    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = storageRef.child("UserProfile").child(mUser.getUid()+"_img");
        UploadTask uploadTask = fileRef.putFile(uri);

        StorageReference desertRef = storageRef.child("UserProfile").child(mUser.getUid()+"_img");
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG,exception.toString());
                Toast.makeText(myChange.this,"프로필 사진을 변경하지 못했어요.",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userProfile userProfile = new userProfile();
                        userProfile.setProfileImageUrl(uri.toString());
                        mDatabaseRef.child("userInfo").child(mUser.getUid()).child("profileImageUrl").removeValue();
                        mDatabaseRef.child("userInfo").child(mUser.getUid()).child("profileImageUrl").setValue(uri.toString());
                        Toast.makeText(myChange.this,"프로필 사진이 변경되었어요.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}