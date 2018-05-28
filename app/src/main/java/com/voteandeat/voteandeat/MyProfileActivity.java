package com.voteandeat.voteandeat;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.Model.User;

public class MyProfileActivity extends android.support.v4.app.Fragment {
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button btnUpdate;
    TextView nameUser,showUpdateName,showUpdateMail;
    ImageView imageUserUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_myprofile,container,false);
       btnUpdate = view.findViewById(R.id.btnUpdate);
       nameUser = view.findViewById(R.id.textUsername);
        showUpdateName = view.findViewById(R.id.showNameUpdate);
        showUpdateMail = view.findViewById(R.id.showMailUpdate);
        imageUserUpdate = view.findViewById(R.id.imageUpdateUser);
        mAuth = FirebaseAuth.getInstance();
        //GET MAIL FROM FIREBASE
        final String id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView nameUser = view.findViewById(R.id.textUsername);
                String username = dataSnapshot.child(id).child("name").getValue(String.class);
                nameUser.setText(username);

                showUpdateName.setText(username);
                String mail = dataSnapshot.child(id).child("email").getValue(String.class);
                showUpdateMail.setText(mail);

                String photourl = dataSnapshot.child(id).child("photourl").getValue(String.class);
                Picasso.get().load(photourl).into(imageUserUpdate);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameUser.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    nameUser.setError("Name required");
                    return;
                }
                updateArtist(id,name);
            }
        });
        return view;

    }
    private boolean updateArtist(String id,String name){
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(id);
        mDatabase.child("name").setValue(name);
        Toast.makeText(getView().getContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();
        return true;
    }
}
