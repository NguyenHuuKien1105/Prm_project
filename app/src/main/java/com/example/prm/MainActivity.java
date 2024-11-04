package com.example.prm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabMain;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        fabMain = findViewById(R.id.fab_main);
        fabMain.setOnClickListener(v -> showDialog());

        loadData();

        setToolbar();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new ClassAdapter(this, classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));

    }

    private void loadData() {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getClassTable();

            if (cursor != null && cursor.moveToFirst()) {
                classItems.clear();
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.C_ID));
                    String className = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CLASS_NAME_KEY));
                    String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT_NAME_KEY));

                    classItems.add(new ClassItem(id, className, subjectName));
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

        title.setText("Attendance App");
        subTitle.setVisibility(RecyclerView.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);

        intent.putExtra("className", classItems.get(position).getClassName());
        intent.putExtra("subjectName", classItems.get(position).getSubject());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    // show dialog to add new class
    private void showDialog() {
        MyDialog myDialog = new MyDialog();
        myDialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        myDialog.setListener((className, subjectName) -> addClass(className, subjectName));
    }

    private void addClass(String className, String subjectName) {
        long cid = dbHelper.addClass(className, subjectName);
        ClassItem classItem = new ClassItem(cid, className, subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }
}