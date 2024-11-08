package com.example.prm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private String username;
    private String password;
    private int position;
    private int roll;
    private int uid;

    private RecyclerView.LayoutManager layoutManager;
    private DBHelper dbHelper;
    private ArrayList<UserItem> userItems = new ArrayList<>();
    private ManagerAdapter adapter;

    FloatingActionButton fabManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainManger), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabManager = findViewById(R.id.fabManager);
        fabManager.setOnClickListener(v -> showDialog());


        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", -1);
        username = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        roll = intent.getIntExtra("roll", -1);
        position = intent.getIntExtra("position", -1);


        setToolbar();

        loadData();

        recyclerView = findViewById(R.id.manager_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ManagerAdapter(userItems);

        recyclerView.setAdapter(adapter);
    }


    private void loadData() {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getUserTable();

            if (cursor != null && cursor.moveToFirst()) {
                userItems.clear();
                do {
                    int uid = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.USER_ID));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_NAME));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_PASSWORD));
                    int roll = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_ROLL)));
                    userItems.add(new UserItem(uid, username, password,roll));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng Cursor sau khi hoàn thành để tránh rò rỉ bộ nhớ
            }
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Manage Account");
        subTitle.setVisibility(RecyclerView.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }


    private void showDialog() {
        MyManagerDialog myManagerDialog = new MyManagerDialog();
        myManagerDialog.show(getSupportFragmentManager(), MyManagerDialog.USER_ADD_DIALOG);
        myManagerDialog.setListener((username, password, roll) -> addUser(username, password, String.valueOf(roll)));
    }


    private void addUser(String username, String password, String roll_string) {
        int roll = Integer.parseInt(roll_string);
        long uid = dbHelper.addUserRoll(username, password, roll);

        userItems.add(new UserItem(uid, username, password, roll));
        UserItem userItems = new UserItem(uid, username, password, roll);
        Log.d("UserItems", userItems.getUsername());
        Log.d("UserItems", userItems.getPassword());
        Log.d("UserItems", String.valueOf(userItems.getRoll()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                 deleteManager(item.getGroupId());
        }

        return super.onContextItemSelected(item);

    }


    private void showUpdateDialog(int position) {
        MyManagerDialog dialog = new MyManagerDialog(userItems.get(position).getUsername(), userItems.get(position).getPassword(), userItems.get(position).getRoll());
        dialog.show(getSupportFragmentManager(), MyManagerDialog.USER_UPDATE_DIALOG);
        dialog.setListener((username, password, roll) -> updateManager(position, roll));
    }

    private void updateManager(int position, int roll) {
        //update trong database
        dbHelper.updateUserRoll(userItems.get(position).getUid(), String.valueOf(roll));
        //update trong arrayList
        userItems.get(position).setRoll(roll);
        adapter.notifyItemChanged(position);
    }

    private void deleteManager(int position) {
        //xoa khoi database
        dbHelper.deleteUser(userItems.get(position).getUid());
        //xoa khoi arrayList
        userItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}