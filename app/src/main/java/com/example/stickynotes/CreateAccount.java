package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    EditText email, password, confirm;
    Button create_account;
    ProgressBar progressBar;
    TextView login_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account); // Inflate the layout

        // Initialize UI elements
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm_Password);
        create_account = findViewById(R.id.create_btn);
        progressBar = findViewById(R.id.progress_bar);
        login_text = findViewById(R.id.login_text);

        create_account.setOnClickListener(v -> CreateAccount());
        login_text.setOnClickListener(v -> finish());
    }


    void CreateAccount()
    {
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        String confirm1 = confirm.getText().toString();

        boolean is_validated = validate_data(email1,password1,confirm1);

        if(!is_validated)
        {
            return;
        }

        CreateAccount_inFirebase(email1,password1);
    }

    void CreateAccount_inFirebase(String email1,String password1)
    {
        changeIn_progress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeIn_progress(false);
                if(task.isSuccessful())
                {
                    Utility.showToast(CreateAccount.this,"Successfully create account,Check email to verify");
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    Utility.showToast(CreateAccount.this,"Successfully create account,Check email to verify");
                }

            }
        });

    }

    void changeIn_progress(boolean in_progress)
    {
        if(in_progress)
        {
            progressBar.setVisibility(View.VISIBLE);
            create_account.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            create_account.setVisibility(View.VISIBLE);
        }
    }

    boolean validate_data(String email1,String password1,String confirm1)
    {
        if(Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Email is invalid");
        }
        if(password1.length() < 6){
            password.setError("Password length is invalid");
            return false;
        }
        if(!password1.equals(confirm1)){
            confirm.setError("Password not matched");
            return false;
        }
        return true;
    }
}

