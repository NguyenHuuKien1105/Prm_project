package com.example.prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, registerBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        String usernameStr = intent.getStringExtra("username");
        String passwordStr = intent.getStringExtra("password");
        String rolStr = intent.getStringExtra("roll");

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        username.setText(usernameStr);
        password.setText(passwordStr);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                //Check if the fields are empty
                if (usernameStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter information", Toast.LENGTH_SHORT).show();
                }else {
                    // Check if the user exists in the database
                    if (dbHelper.checkUser(usernameStr, passwordStr)) {
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        // Check the roll of the user
                        switch (dbHelper.getRoll(usernameStr)) {
                            case "Admin":
                                Intent adminIntent = new Intent(LoginActivity.this, ManagerActivity.class);
                                startActivity(adminIntent);
                                finish();
                                break;
                            case "Teacher":
                                Intent teacherIntent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("username", usernameStr);
                                intent.putExtra("password", passwordStr);
                                startActivity(teacherIntent);
                                finish();
                                break;
                            default:
                                break;
                        }

                    }else {
                        Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerBtn = findViewById(R.id.signupBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}