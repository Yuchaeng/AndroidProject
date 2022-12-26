package com.example.graduproject;

import static java.sql.DriverManager.println;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class emptyTime extends AppCompatActivity {
    TextView emptyComplete;
    Spinner selectDay, selectAmPm;
    TextInputEditText addTime;
    TextInputLayout addTimeLayout;
    CheckBox checkAllday;
    Button addBtn;
    ListView resultList;
    emptyListAdapter adapter;
    private String[] dayItem = {"월","화","수","목","금"};
    private String[] ampmItem = {"오전","오후"};
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(emptyTime.this, myChange.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_time);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        emptyComplete = findViewById(R.id.emptyComplete);
        selectDay = findViewById(R.id.selectDay);
        selectAmPm = findViewById(R.id.selectAmPm);
        addTime = findViewById(R.id.addTime);
        addTimeLayout = findViewById(R.id.addTimeLayout);
        checkAllday = findViewById(R.id.checkAllday);
        addBtn = findViewById(R.id.addBtn);
        resultList = findViewById(R.id.resultList);

        adapter = new emptyListAdapter();
        resultList.setAdapter(adapter);


        ArrayList<String> itemArray = new ArrayList<String>();


        //요일 어댑터
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,dayItem);
        dayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        selectDay.setAdapter(dayAdapter);
        //오전오후 어댑터
        ArrayAdapter<String> ampmAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,ampmItem);
        ampmAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        selectAmPm.setAdapter(ampmAdapter);

        //종일 체크
        checkAllday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox)view).isChecked();
                if(checkAllday.isChecked()) {
                    addTime.getText().clear();
                    selectAmPm.setSelection(0);
                    addTimeLayout.setVisibility(View.GONE);
                    selectAmPm.setVisibility(View.GONE);
                }
                else {
                    addTimeLayout.setVisibility(View.VISIBLE);
                    selectAmPm.setVisibility(View.VISIBLE);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = selectDay.getSelectedItem().toString();
                String when = selectAmPm.getSelectedItem().toString();
                String time = addTime.getText().toString();
                if(checkAllday.isChecked()) {
                    adapter.addItem(day+"공강");
                    itemArray.add(day+"공강");
                    checkAllday.setChecked(false);
                    addTimeLayout.setVisibility(View.VISIBLE);
                    selectAmPm.setVisibility(View.VISIBLE);
                }
                else {
                    adapter.addItem(day+" "+when+" "+time);
                    itemArray.add(day+" "+when+" "+time);
                }
                adapter.notifyDataSetChanged();

                addTime.getText().clear();
                selectAmPm.setSelection(0);
                selectDay.setSelection(0);

            }
        });

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(emptyTime.this,position,Toast.LENGTH_SHORT).show();
//                adapter.deleteItem(position);
//                itemArray.remove(position);
            }
        });

        //배열에 저장해뒀다가 스트링에 넣고 완료 누르면 스트링으로 저장

        emptyComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemArraytoString = String.join(",",itemArray);
                userProfile user = new userProfile();
                user.setEmptyTime(itemArraytoString);
                mDatabaseRef.child("userInfo").child(mUser.getUid()).child("emptyTime").setValue(itemArraytoString);

                Intent intent = new Intent(emptyTime.this,myChange.class);
                startActivity(intent);
                finish();

            }
        });
    }

}