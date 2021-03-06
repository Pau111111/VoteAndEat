package com.voteandeat.voteandeat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.voteandeat.voteandeat.Model.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword,editTextName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        editTextName = findViewById(R.id.fullnameRegister);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnAddUser).setOnClickListener(this);
        findViewById(R.id.backLogin).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAddUser:
                // CALLING THE FUNCTION REGISTER WHEN WE CLICK ADD BUTTON
                registerUser();
                break;
            case R.id.backLogin:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
        }
    }

    private void registerUser() {
        //GET THE INPUT TEXT
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String photourl = "https://firebasestorage.googleapis.com/v0/b/voteandeat.appspot.com/o/default.jpg?alt=media&token=2c69c28c-3cae-47a0-ac05-e335e4abc29d";


        // RESTRICTIONS
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        //CREATING THE USER
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Registered Successfull", Toast.LENGTH_SHORT).show();
                    User user = new User(
                        name,email
                    );
                    user.setPhotourl(photourl);
                    FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user);
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
