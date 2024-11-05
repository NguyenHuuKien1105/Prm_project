package com.example.prm;

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

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position, cid;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_student), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position", -1);
        cid = intent.getIntExtra("cid", -1);

        setToolbar();
        loadData();
        recyclerView = findViewById(R.id.student_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));

            studentItems.add(new StudentItem(sid, roll, name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText(className);
        subTitle.setText(subjectName);

        back.setOnClickListener(v -> onBackPressed());

        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        }
        return true;
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_ADD_DIALOG);
        //nhan du lieu roi chuyen du lieu xuong function addStudent ben duoi
        dialog.setListener(this::addStudent);
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid, roll, name);
        studentItems.add(new StudentItem(sid, roll, name));
        StudentItem studentItem = new StudentItem(sid, roll, name);
        // In log để kiểm tra dữ liệu
        Log.d("StudentActivity", "Added student: Roll = " + roll + ", Name = " + name);

        // In log để xem kích thước và nội dung của ArrayList
        Log.d("StudentActivity", "Total students in list: " + studentItems.size());
        for (StudentItem student : studentItems) {
            Log.d("StudentActivity", "Student - Roll: " + student.getRoll() + ", Name: " + student.getName());
        }
        //
        adapter.notifyDataSetChanged();
//        adapter.notifyItemChanged(studentItems.size() - 1);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }

        return super.onContextItemSelected(item);

    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(), studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string, name) -> updateStudent(position, name));
    }

    private void updateStudent(int position, String name) {
        //update trong database
        dbHelper.updateStudent(studentItems.get(position).getSid(), name);
        //update trong arrayList
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        //xoa khoi database
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        //xoa khoi arrayList
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }


}