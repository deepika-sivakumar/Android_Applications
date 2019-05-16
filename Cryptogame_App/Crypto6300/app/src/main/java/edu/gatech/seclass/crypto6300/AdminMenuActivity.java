package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.gatech.seclass.crypto6300.global.Global;

public class AdminMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        // Update admin's username in the welcome message
        TextView adminTextView = findViewById(R.id.adminTextView);
        adminTextView.setText("Hello " + Global.getUsername() + "!");

        //Removing the Edit Cryptogram Functionality
        /*Button editCryptogramButton = findViewById(R.id.editCryptogramButton);
        editCryptogramButton.setVisibility(View.GONE);*/
    }

    // Create Cryptogram button click
    public void onCreateCryptogramButtonClick(View view){
        // Load the Create Cryptogram form
        Intent intent = new Intent(this, CryptogramActivity.class);
        intent.putExtra("formName", getString(R.string.create_form_name));
        intent.putExtra("cryptogramName", "");
        startActivity(intent);
    };

    // Load the Edit Cryptograms list
    public void onEditCryptogramButtonClick(View view){
        // Load the Cryptogram list
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName", getString(R.string.edit_list_name));
        startActivity(intent);
    };

    // Create Player Button click
    public void onCreatePlayerButtonClick(View view){
        Intent intent = new Intent(this, CreatePlayerActivity.class);
        startActivity(intent);
    };

    // Load the Player Statistics list for the admin
    public void onViewStatsButtonClick(View view){
        // Load the Player Statistics list
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName", getString(R.string.player_stats_list_name));
        startActivity(intent);
    };

    //Logout the user
    public void onLogOutButtonClick(View view){
        Global.setUsername(null);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    };

    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
