package com.example.prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, rePassword;
    Button signupBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Spinner
        Spinner rollSpinner = findViewById(R.id.roll);

        //Set up ArrayAdapter for Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rolls, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rollSpinner.setAdapter(adapter);

        //DB setting
        dbHelper = new DBHelper(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);

        signupBtn = findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String rePasswordStr = rePassword.getText().toString();

                String rollStr = rollSpinner.getSelectedItem().toString();

                //Check if the fields are empty
                if (usernameStr.isEmpty() || passwordStr.isEmpty() || rePasswordStr.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter information", Toast.LENGTH_SHORT).show();
                }else{
                    // Check if the username already exists
                    if (dbHelper.checkUserExists(usernameStr)) {
                        Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }else {
                        // Check if the password matches
                        if (passwordStr.equals(rePasswordStr)) {
                            Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                            dbHelper.addUser(usernameStr, passwordStr, rollStr);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("username", usernameStr);
                            intent.putExtra("password", passwordStr);
                            intent.putExtra("roll", rollStr);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}