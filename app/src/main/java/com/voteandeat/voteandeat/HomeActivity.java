package com.voteandeat.voteandeat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import com.voteandeat.voteandeat.Room.Chat.ChatActivity;
import com.voteandeat.voteandeat.GoogleAPI.MapsActivity;
import com.voteandeat.voteandeat.Room.Model.Chat;
import com.voteandeat.voteandeat.Room.Model.Member;
import com.voteandeat.voteandeat.Room.Model.Room;

import java.util.Map;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView nameUser,mailUser;
    final Context context = this;
    String username;
    String urlUser;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new Session(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_room,null);
                final EditText rName = mView.findViewById(R.id.etRoomName);
                final EditText rPassword1 = mView.findViewById(R.id.etRoomPassword1);
                final EditText rPassword2 = mView.findViewById(R.id.etRoomPassword2);
                Button btnCreateRoom = mView.findViewById(R.id.btnCreateRoom);
                alertDialogBuilder.setView(mView);
                final AlertDialog dialog = alertDialogBuilder.create();
                btnCreateRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rPassword1.getText().toString().equals(rPassword2.getText().toString())) {
                            Chat chatAux = new Chat();

                            mDatabase = FirebaseDatabase.getInstance().getReference("Rooms");
                            String room_key = mDatabase.push().getKey();
                            Room roomAux = new Room(room_key, rName.getText().toString(), rPassword1.getText().toString(), true, chatAux);
                            DatabaseReference mDatabaseRooms = mDatabase.child(room_key);
                            mDatabaseRooms.setValue(roomAux);
                            Toast.makeText(getApplicationContext(), "Room created successfully", Toast.LENGTH_SHORT).show();

                            Member memberAux = new Member(mAuth.getCurrentUser().getUid(), username, urlUser, "admin");
                            mDatabase = FirebaseDatabase.getInstance().getReference("Rooms").child(room_key).child("Members");
                            String memberInRoomKey = mDatabase.push().getKey();
                            mDatabase = FirebaseDatabase.getInstance().getReference("Rooms").child(room_key).child("Members").child(memberInRoomKey);
                            mDatabase.setValue(memberAux);
                            dialog.cancel();

                            Intent i = new Intent(HomeActivity.this, RoomActivity.class);
                            i.putExtra("idActualRoom",room_key);
                            startActivity(i);
                        } else{
                            Toast.makeText(getApplicationContext(), "Passwords must match!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // GET VIEW TO GET THE ID
        final View headerView = navigationView.getHeaderView(0);
        //ID TEXTVIEW EMAIL
        //--
        //GET MAIL FROM FIREBASE
        final String id = mAuth.getCurrentUser().getUid();
        //PRUEBA
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //MAIL
                /*TextView mailUser = headerView.findViewById(R.id.emailUser);
                String email = dataSnapshot.child(id).child("email").getValue(String.class);
                mailUser.setText(email);*/
                //NAME
                TextView nameUser = headerView.findViewById(R.id.fullNameUser);
                username = dataSnapshot.child(id).child("name").getValue(String.class);
                nameUser.setText(username);

                ImageView imageUser = headerView.findViewById(R.id.imageUser);
                urlUser = dataSnapshot.child(id).child("photourl").getValue(String.class);
                Picasso.get().load(urlUser).into(imageUser);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_aboutus) {
            return true;
        }
        if (id == R.id.action_maps) {
            startActivity(new Intent(HomeActivity.this,MapsActivity.class));
        }
        if (id == R.id.action_chat) {
            startActivity(new Intent(HomeActivity.this,ChatActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_votes:
                getSupportFragmentManager().beginTransaction().replace(R.id.test,
                        new VotesActivity()).commit();
                break;
            case R.id.nav_favs:
                getSupportFragmentManager().beginTransaction().replace(R.id.test,
                        new FavoritesActivity()).commit();
                break;
            case R.id.nav_restaurants:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.test,
                        new RestaurantsActivity()).commit();*/
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.test,
                        new SettingsActivity()).commit();
                break;
            case R.id.nav_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.test,
                        new MyProfileActivity()).commit();
                break;
            case R.id.nav_logout:
                session.setLoggedin(false);
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class)); //Go back to home page
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
