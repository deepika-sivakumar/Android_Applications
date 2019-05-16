package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.seclass.crypto6300.adapters.ChooseCryptogramListArrayAdapter;
import edu.gatech.seclass.crypto6300.adapters.EditCryptogramListArrayAdaper;
import edu.gatech.seclass.crypto6300.adapters.PlayerStatsListArrayAdapter;

import edu.gatech.seclass.crypto6300.dbUtilities.Cryptogram;
import edu.gatech.seclass.crypto6300.dbUtilities.Player;
import edu.gatech.seclass.crypto6300.dbUtilities.PlayerGames;
import edu.gatech.seclass.crypto6300.global.Global;


public class ListActivity extends AppCompatActivity {

    ArrayList<Cryptogram> editCryptogramArrayList = null;
    ArrayList<PlayerGames> playerCryptogramsArrayList = null;
    ArrayList<Player> playerStatsArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        // Get data from the previous activity
        Bundle extras = getIntent().getExtras();
        String listName = null;
        if (extras != null) {
            listName = extras.getString("listName");
            //The key argument here must match that used in the other activity
        }
        // Load the list depending on which type is requested
        if(listName.equals(getString(R.string.choose_list_name))) { // Load the Choose Cryptogram list
            chooseCryptogramList();
        }
        else if(listName.equals(getString(R.string.edit_list_name))) { // Load the Edit Cryptogram list
            editCryptogramList();
        }
        else if(listName.equals(getString(R.string.player_stats_list_name))) { // Load the Player Stats list
            playerStatsList();
        }
    }

    // Show Choose Cryptogram List
    protected void chooseCryptogramList() {
        // Set the Choose Cryptogram List title
        TextView listTitleTextView = findViewById(R.id.listTitleTextView);
        listTitleTextView.setText(R.string.choose_cryptogram_list_title);

        //Get the list of choose cryptograms from the database
        getChooseCryptoNameDatabase();

        //Show error message on screen if no cryptograms present in database
        if(this.playerCryptogramsArrayList.size() == 0) {
            listTitleTextView.setText("No Cryptograms available!");
        }
        else {
            // Create List items for the Cryptogram list
            ChooseCryptogramListArrayAdapter chooseCryptogramListArrayAdapter = new ChooseCryptogramListArrayAdapter(getApplicationContext(), 0, this.playerCryptogramsArrayList);

            // Set it to the List View
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(chooseCryptogramListArrayAdapter);

            // When a list item is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Get the chosen cryptogram name
                    String cryptogramName = playerCryptogramsArrayList.get(position).getCryptogramName();

                    // Show the Solve Cryptogram screen for the player
                    Intent intent = new Intent(getBaseContext(), SolveCryptogramActivity.class);
                    //Send the chosen cryptogram name to the next activity
                    intent.putExtra("cryptogramName", cryptogramName);
                    startActivity(intent);
                    //Finish the current Choose Cryptogram List Activity to avoid repetitive screens
                    finish();
                }
            });
        }
    }

    protected void getChooseCryptoNameDatabase() {
        this.playerCryptogramsArrayList = new ArrayList<PlayerGames>(PlayerGames.find(PlayerGames.class, "username = ?", Global.getUsername()));
    }

    // Show Edit Cryptogram List
    protected void editCryptogramList() {
        // Set the Edit Cryptogram List title
        TextView listTitleTextView = findViewById(R.id.listTitleTextView);
        listTitleTextView.setText(R.string.edit_cryptogram_list_title);

        //Get the list of edit cryptograms from the database
        getEditCryptoNameDatabase();

        //Show error message on screen if no cryptograms present in database
        if(this.editCryptogramArrayList.size() == 0) {
            listTitleTextView.setText("No Cryptograms available!");
        }
        else {
            ListView listView = (ListView) findViewById(R.id.listView);
            // Create List items for the Edit Cryptogram list
            EditCryptogramListArrayAdaper editCryptogramListArrayAdaper = new EditCryptogramListArrayAdaper(getApplicationContext(), 0, this.editCryptogramArrayList);
            // Set it to the List View
            listView.setAdapter(editCryptogramListArrayAdaper);

            // When a list item is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Get the cryptogram name of the list item
                    String cryptogramName = editCryptogramArrayList.get(position).getCryptogramName();
                    // Load the Edit Cryptogram form
                    Intent intent = new Intent(getBaseContext(), CryptogramActivity.class);
                    intent.putExtra("formName", getString(R.string.edit_form_name));
                    intent.putExtra("cryptogramName", cryptogramName);
                    startActivity(intent);
                }
            });
        }
    }

    protected void getEditCryptoNameDatabase() {
        this.editCryptogramArrayList = new ArrayList<Cryptogram>(Cryptogram.listAll(Cryptogram.class));
        //TODO: Show error message on screen if no cryptograms present in database

    }

    // Show Player Statistics List
    protected void playerStatsList() {
        // Set the Player Statistics List title
        TextView listTitleTextView = findViewById(R.id.listTitleTextView);
        listTitleTextView.setText(R.string.player_stats_list_title);

        //Get the list of edit cryptograms from the database
        getPlayerStatsDatabase();

        //Show error message on screen if no players present in database
        if(this.playerStatsArrayList.size() == 0) {
            listTitleTextView.setText("No Players available!");
        }
        else {
            //The view for admin & player is handled in the PlayerStatsListArrayAdapter
            ListView listView = (ListView) findViewById(R.id.listView);
            // Create List items for the Edit Cryptogram list
            PlayerStatsListArrayAdapter playerStatsListArrayAdapter = new PlayerStatsListArrayAdapter(getApplicationContext(), 0, this.playerStatsArrayList);
            // Set it to the List View
            listView.setAdapter(playerStatsListArrayAdapter);
        }
    }

    // Get the list of players from the "Player" table
    protected void getPlayerStatsDatabase() {
//        this.playerStatsArrayList = new ArrayList<Player>(Player.listAll(Player.class));
        //Query
        this.playerStatsArrayList = new ArrayList<Player>(Player.findWithQuery(Player.class, "SELECT * FROM Player ORDER BY wins DESC", (String[])null));
    }

    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
