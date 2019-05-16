package edu.gatech.seclass.crypto6300;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gatech.seclass.crypto6300.dbUtilities.Cryptogram;
import edu.gatech.seclass.crypto6300.dbUtilities.Player;
import edu.gatech.seclass.crypto6300.dbUtilities.PlayerGames;
import edu.gatech.seclass.crypto6300.dbUtilities.User;

public class CreatePlayerActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    RadioGroup categoryRadioGroup;
    RadioButton categoryEasyRadioButton;

    Boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);

        //Get the instances of the form fields
        firstNameEditText = findViewById(R.id.playerFormFirstName);
        lastNameEditText = findViewById(R.id.playerFormLastName);
        usernameEditText = findViewById(R.id.playerFormUserName);
        categoryRadioGroup = findViewById(R.id.radioGroup1);
        categoryEasyRadioButton = findViewById(R.id.categoryEasy);
    }

    // Add Player Button Click
    public void onPlayerFormBtnClick(View view) {
        //Validate the form fields
        if(!validateFields()) {
            // Add the player to the database
            addPlayer();
        }
    }

    //Method to add the player to the database
    protected void addPlayer() {

        //Query if the player username is unique and return error message if not
        String username = usernameEditText.getText().toString();
        String message;
        // Check if the username name is already present in the database (User table)
        if(User.find(User.class, "username = ?", username).size() > 0) {
            message = "Sorry, Player username is not unique! Could not add Player!!";
            this.result = false;
            displayResult(message);
            usernameEditText.setError("Enter a unique Username!");
        } else {
            //Get the form field values
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();

            // Get selected radio button from radioGroup
            int selectedId = categoryRadioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            String category = radioButton.getText().toString();

            //Add Player to "User" table
            User playerUser = new User(username, false);
            playerUser.save();

            //Add Player to "Player" table
            Player player = new Player(username, firstName, lastName, category);
            player.save();

            //Add all the cryptograms to the player in "PlayerGames" table with status unstarted
            ArrayList<Cryptogram> cryptogramArrayList = new ArrayList<Cryptogram>(Cryptogram.listAll(Cryptogram.class));
            //Add only if there are already cryptograms present in the database
            if(cryptogramArrayList.size() > 0) {
                int remainingAttempts = 0;
                PlayerGames playerGames;
                for (Cryptogram cryptogram : cryptogramArrayList) {
                    if (player.getCategory().equals(getString(R.string.easy))) {
                        remainingAttempts = cryptogram.getEasyAttempts();
                    } else if (player.getCategory().equals(getString(R.string.normal))) {
                        remainingAttempts = cryptogram.getNormalAttempts();
                    } else if (player.getCategory().equals(getString(R.string.hard))) {
                        remainingAttempts = cryptogram.getHardAttempts();
                    }
                    playerGames = new PlayerGames(player.getUsername(), cryptogram.getCryptogramName(),
                            "unstarted", null, remainingAttempts);
                    playerGames.save();
                }
            }

            //Display the result dialog after saving to database
            message = "Player name is unique! Player was successfully added!!";
            this.result = true;
            displayResult(message);
        }
    }

    //Validate fields
    protected Boolean validateFields () {
        Boolean isEmpty = false;
        if(firstNameEditText.getText().toString().isEmpty()) {
            firstNameEditText.setError("Enter Firstname!");
            isEmpty = true;
        }
        if(lastNameEditText.getText().toString().isEmpty()) {
            lastNameEditText.setError("Enter Lastname!");
            isEmpty = true;
        }
        if(usernameEditText.getText().toString().isEmpty()) {
            usernameEditText.setError("Enter Username!");
            isEmpty = true;
        }
        if(categoryRadioGroup.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
            isEmpty = true;
        }
        return isEmpty;
    }

    // Method to display the result dialog after form submission
    protected void displayResult(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(CreatePlayerActivity.this).create();
        alertDialog.setTitle("Result");
        //Set the message according to the result
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect to the menu if success
                        if (result) {
                            onBackPressed();
                        }
                        // Else remain on the same page
                        else {
                            dialog.dismiss();
                        }
                    }
                });
        alertDialog.show();
    }

    //On Reset button click
    public void onPlayerFormResetClick(View view) {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        usernameEditText.setText("");
        categoryEasyRadioButton.setChecked(true);
        firstNameEditText.setError(null);
        lastNameEditText.setError(null);
        usernameEditText.setError(null);

    }

    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
