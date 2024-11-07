package com.example.prm;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyManagerDialog extends DialogFragment {
    public static final String USER_ADD_DIALOG = "addUser";
//    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String USER_UPDATE_DIALOG = "updateUser";
//    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";


    private OnCLickListener listener;
    private String username;
    private String password;
    private int roll;

    public MyManagerDialog() {

    }

    public MyManagerDialog(String username, String password, int roll) {
        this.username = username;
        this.password = password;
        this.roll = roll;

    }

    public interface OnCLickListener {
        void onClick(String text1, String text2, int text3);
    }


    public void setListener(OnCLickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(USER_ADD_DIALOG)) dialog = getAddUserDialog();
        if (getTag().equals(USER_UPDATE_DIALOG)) dialog = getUpdateUserDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.manager_dialog, null);
        builder.setView(view);

        // Thiết lập tiêu đề cho dialog
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New User");

        // Thiết lập các trường input
        EditText usernameEdt = view.findViewById(R.id.edt101);
        EditText passwordEdt = view.findViewById(R.id.edt102);
        EditText rollEdt = view.findViewById(R.id.edt103); // Sử dụng edt03 cho Roll

        // Đặt gợi ý cho các trường
        usernameEdt.setHint("Username");
        passwordEdt.setHint("Password");
        rollEdt.setHint("Roll");

        // Xử lý nút Cancel
        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> dismiss());

        // Xử lý nút Add
        Button addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(v -> {
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();
            int roll = Integer.parseInt(rollEdt.getText().toString());

            // Truyền dữ liệu qua listener
            listener.onClick(username, password, roll);
        });

        return builder.create();
    }

    private Dialog getUpdateUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.manager_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Class");


        EditText usernameEdt = view.findViewById(R.id.edt101);
        EditText passwordEdt = view.findViewById(R.id.edt102);
        EditText rolledt = view.findViewById(R.id.edt103);

        usernameEdt.setText(username + "");
        usernameEdt.setEnabled(false);
        passwordEdt.setText(password + "");
        passwordEdt.setEnabled(false);

        rolledt.setHint("Roll");

        Button cancle = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancle.setOnClickListener(v -> dismiss());

        add.setOnClickListener(v -> {
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();
            int roll = Integer.parseInt(rolledt.getText().toString());
            listener.onClick(username, password, roll);
            dismiss();
        });
        return builder.create();
    }
}
