package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.gatech.seclass.crypto6300.global.Global;

public class PlayerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_menu);

        //Update player username in the welcome message
        TextView playerTextView = findViewById(R.id.playerTextView);
        playerTextView.setText("Hello " + Global.getUsername() + "!");
    }

    // Choose Cryptogram Button click
    public void onChooseCryptogramBtnClick(View view){
        // Load the Cryptogram list
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName",getString(R.string.choose_list_name));
        startActivity(intent);
    }

    // View Player Statistics button click
    public void onViewStatisticsBtnClick(View view){
        // Load the Player Statistics list for the player
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName",getString(R.string.player_stats_list_name));
        startActivity(intent);
    };

    //Logout the user
    public void onLogOutBtnClick(View view){
        // Redirect to the Login page
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
