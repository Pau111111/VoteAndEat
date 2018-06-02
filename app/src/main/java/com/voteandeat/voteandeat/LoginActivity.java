package com.voteandeat.voteandeat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.Model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailLogin, passwordLogin;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    //SignInButton signInButton;
    Button btnRegister, btnLogin;
    DatabaseReference mDatabase;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);
        if(session.loggedin()){
            Intent homeIntent =  new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.googleAuth).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passLogin);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            session.setLoggedin(true);
                            final String email = mAuth.getCurrentUser().getEmail();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
                                    new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(i);
                                                finish();
                                                // User Exists
                                                // Do your stuff here if user already exists
                                            } else {

                                                // User Not Yet Exists
                                                // Do your stuff here if user not yet exists
                                                String user = mAuth.getCurrentUser().getDisplayName();
                                                String url = mAuth.getCurrentUser().getPhotoUrl().toString();
                                                User userAux = new User(
                                                        user,email
                                                );

                                                userAux.setPhotourl(url);
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(userAux);
                                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(i);
                                                finish();
                                                Toast.makeText(getApplicationContext(), "User logged succesfully", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }

                            );
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Could not log in user", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
                break;
            case R.id.googleAuth:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                break;
            case R.id.btnLogin:
                final String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if (email.isEmpty()) {
                    emailLogin.setError("Email is required");
                    emailLogin.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLogin.setError("Please enter a valid email");
                    emailLogin.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordLogin.setError("Password is required");
                    passwordLogin.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    passwordLogin.setError("Minimum lenght of password should be 6");
                    passwordLogin.requestFocus();
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            session.setLoggedin(true);
                            //Log.d("Mail","s");
                            if (email.equals("admin@admin.com")) {
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                //NO POTS TIRAR ENRRERE
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                //progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                //NO POTS TIRAR ENRRERE
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                //progressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
}
