package com.fa15be0013.umair.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.fa15be0013.umair.registration.Adapter.OrderAdapter;
import com.fa15be0013.umair.registration.Model.FetchOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDashBoard extends Base
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference reference;
    private ProgressDialog mProgressDialog;
    ListView recyclerView;
    FetchOrder fetchorder;
    List<FetchOrder> fetchOrderList = new ArrayList<>();

    String OrderDate, OrderId, UserId, resId;
    TextView name , email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (ListView)findViewById(R.id.recycler_view);

//        Intent i = getIntent();
//
//        email = (TextView)findViewById(R.id.txtRestEmail);
//
//        email.setText(i.getStringExtra("email"));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        resId = getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("/CartDetails/"+resId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String ui = dataSnapshot.getKey();
//                Toast.makeText(RestaurantDashBoard.this, ""+ui, Toast.LENGTH_SHORT).show();
                for (DataSnapshot resid:dataSnapshot.getChildren()) {
                    UserId = resid.getKey();
                    Toast.makeText(RestaurantDashBoard.this, ""+UserId, Toast.LENGTH_SHORT).show();
                    for (DataSnapshot date:resid.getChildren()) {
                        OrderDate = date.getKey();
                        Toast.makeText(RestaurantDashBoard.this, ""+OrderDate, Toast.LENGTH_SHORT).show();
                        for (DataSnapshot orderid:date.getChildren()) {
                            OrderId = orderid.getKey();
                            fetchorder = new FetchOrder();
                            fetchorder.setAddress(orderid.child("address").getValue().toString());
                            fetchorder.setPhoneNumber(orderid.child("phoneNumber").getValue().toString());
                            fetchorder.setDate(OrderDate);
                            fetchorder.setOrderID(OrderId);
                            Toast.makeText(RestaurantDashBoard.this, ""+OrderId, Toast.LENGTH_SHORT).show();
                            Toast.makeText(RestaurantDashBoard.this, ""+orderid.child("address").getValue().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(RestaurantDashBoard.this, ""+orderid.child("phoneNumber").getValue().toString(), Toast.LENGTH_SHORT).show();
                            fetchOrderList.add(fetchorder);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        OrderAdapter adapter = new OrderAdapter(RestaurantDashBoard.this,fetchOrderList);
        recyclerView.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.restaurant_dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(RestaurantDashBoard.this, SetMenu.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Toast.makeText(this, "No Sucessfull orders yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "No Sucessfull Invoices yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "No Satisfied Customers yet", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Update work is in progress", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(RestaurantDashBoard.this, LoginScreen.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
