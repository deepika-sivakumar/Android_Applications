package edu.gatech.seclass.crypto6300;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.dbUtilities.Cryptogram;
import edu.gatech.seclass.crypto6300.dbUtilities.Player;
import edu.gatech.seclass.crypto6300.dbUtilities.PlayerGames;

public class CryptogramActivity extends AppCompatActivity {

    String formName = null;
    Boolean result = false;
    String cryptogramName = null;

    EditText cryptoNameEditText;
    EditText solutionEditText;
    EditText easyAttemptsEditText;
    EditText normalAttemptsEditText;
    EditText hardAttemptsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram);

        // Get the instances of the form fields
        cryptoNameEditText = findViewById(R.id.cryptoFormCryptoName);
        solutionEditText = findViewById(R.id.cryptoFormSolution);
        easyAttemptsEditText = findViewById(R.id.cryptoFormEasy);
        normalAttemptsEditText = findViewById(R.id.cryptoFormNormal);
        hardAttemptsEditText = findViewById(R.id.cryptoFormHard);

        //Set Solution field as scrollable
        solutionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.cryptoFormSolution) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        // Get data from the previous activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            this.formName = extras.getString("formName");
            this.cryptogramName = extras.getString("cryptogramName");
            //The key argument here must match that used in the other activity
        }

        TextView cryptogramFormTitleTextView = findViewById(R.id.cryptogramFormTitleTextView);
        Button cryptogramFormButton = findViewById(R.id.cryptogramFormButton);

        //Load the form which is requested
        if (this.formName.equals(getString(R.string.create_form_name))) {
            // Set the Create Cryptogram Form title
            cryptogramFormTitleTextView.setText(R.string.create_form_title);
            // Set the Button name as "Add Cryptogram"
            cryptogramFormButton.setText(R.string.add_button);

        }
        else if(this.formName.equals(getString(R.string.edit_form_name))) {
            // Set the Edit Cryptogram Form title
            cryptogramFormTitleTextView.setText(R.string.edit_form_title);
            // Set the Button name as "Save"
            cryptogramFormButton.setText(R.string.save_button);
            //Disable the cryptogram Name & solution fields for the edit form
            cryptoNameEditText.setFocusable(false);
            solutionEditText.setFocusable(false);
            //cryptoNameEditText.setEnabled(false);
            //Get the cryptogram data from the database set to the form fields
            getEditCryptogramData();
        }
    }

    // Create Cryptogram
    public void onCryptogramFormBtnClick(View view) {
        if(formName.equals(getString(R.string.create_form_name))) {
            //Validate the form fields
            if(!validateFields()) {
                // Add the cryptogram to the database
                addCryptogram();
            }
        }
        else if(formName.equals(getString(R.string.edit_form_name))) {
            //Validate the form fields
            if(!validateFields()) {
                // Save the edited cryptogram to the database
                editCryptogram();
            }
        }
    }

     // Method to add the cryptogram to the database
    protected void addCryptogram() {
        String message;
        //Get the cryptogram name entered in the form
        String cryptogramName = cryptoNameEditText.getText().toString();
        // Check if the cryptogram name is already present in the database
        if (Cryptogram.find(Cryptogram.class, "cryptogramname = ?", cryptogramName).size() > 0) {
            message = "Sorry, Cryptogram name is not unique! Could not add Cryptogram!!";
            this.result = false;
            displayResult(message);
            cryptoNameEditText.setError("Enter a unique Cryptogram name!");
        }
        else {
            //Add the cryptogram to "Cryptogram" table
            //Get the Form values
            String solution = solutionEditText.getText().toString();
            int easyAttempts = Integer.parseInt(easyAttemptsEditText.getText().toString());
            int normalAttempts = Integer.parseInt(normalAttemptsEditText.getText().toString());
            int hardAttempts = Integer.parseInt(hardAttemptsEditText.getText().toString());

            //Add to database
            Cryptogram cryptogram = new Cryptogram(cryptogramName, solution, easyAttempts, normalAttempts, hardAttempts);
            cryptogram.save();

            //Add the cryptogram to all players in the "Player Games" table
            ArrayList<Player> playerArrayList = new ArrayList<Player>(Player.listAll(Player.class));
            //Add only if there are already players present in the database
            if(playerArrayList.size() > 0) {
                int remainingAttempts = 0;
                PlayerGames playerGames;
                for (Player player : playerArrayList) {
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
            message = "Cryptogram name is unique! Cryptogram was successfully added!!";
            this.result = true;
            displayResult(message);
        }
    }

    //Method to save the edited cryptogram to the database
    protected void editCryptogram() {
        //Query if the cryptogram username is unique and return error message if not -> CryptogramName field is disabled
        //Get the Form values
        String cryptogramName = cryptoNameEditText.getText().toString();
        String solution = solutionEditText.getText().toString();
        int easyAttempts = Integer.parseInt(easyAttemptsEditText.getText().toString());
        int normalAttempts = Integer.parseInt(normalAttemptsEditText.getText().toString());
        int hardAttempts = Integer.parseInt(hardAttemptsEditText.getText().toString());

        //Save to the database
        Cryptogram cryptogram = new Cryptogram(cryptogramName, solution, easyAttempts, normalAttempts, hardAttempts);
        cryptogram.save();

        //Update the "PlayerGames" table
        ArrayList<PlayerGames> playerGamesArrayList = new ArrayList<PlayerGames>(
                PlayerGames.find(PlayerGames.class, "cryptogramname = ?", this.cryptogramName));
        //If the cryptogram is present in the "PlayerGames" table
        if(playerGamesArrayList.size() > 0) {
            for(PlayerGames playerGames: playerGamesArrayList) {
                List<Player> playerList = Player.find(Player.class, "username = ?", playerGames.getUsername());
                Player player = playerList.get(0);
                PlayerGames newPlayerGames;
                int remainingAttempts = 0;
                if(player.getCategory().equals(getString(R.string.easy))) {
                    remainingAttempts = easyAttempts - playerGames.getIncorrectAttempts();

                } else if(player.getCategory().equals(getString(R.string.normal))) {
                    remainingAttempts = normalAttempts - playerGames.getIncorrectAttempts();

                } else if(player.getCategory().equals(getString(R.string.hard))) {
                    remainingAttempts = hardAttempts - playerGames.getIncorrectAttempts();

                }
                newPlayerGames = new PlayerGames(playerGames.getUsername(), playerGames.getCryptogramName(), playerGames.getStatus(),
                        playerGames.getEncryptedPhrase(), remainingAttempts);
                newPlayerGames.save();
            }
        }

        //Display the result dialog after saving to database
        String message = "Cryptogram saved successfully!!";
        this.result = true;
        displayResult(message);
    }

    // Method to query the database for edit cryptogram form data
    protected void getEditCryptogramData() {
        //Get the cryptogram from the database
        List<Cryptogram> cryptogramList = Cryptogram.find(Cryptogram.class, "cryptogramname = ?", this.cryptogramName);
        Cryptogram cryptogram = cryptogramList.get(0);

        //Set the values in the edit form fields
        cryptoNameEditText.setText(cryptogram.getCryptogramName());
        solutionEditText.setText(cryptogram.getSolution());
        easyAttemptsEditText.setText(cryptogram.getEasyAttempts());
        normalAttemptsEditText.setText(cryptogram.getNormalAttempts());
        hardAttemptsEditText.setText(cryptogram.getHardAttempts());
    }

    //Method to check if the fields are not empty & the attempts are greater than 1
    protected Boolean validateFields () {
        Boolean isEmpty = false;
        if(cryptoNameEditText.getText().toString().isEmpty()) {
            cryptoNameEditText.setError("Enter Cryptogram Name!");
            isEmpty = true;
        }
        if(solutionEditText.getText().toString().isEmpty()) {
            solutionEditText.setError("Enter Solution!");
            isEmpty = true;
        } else {
            String solution = solutionEditText.getText().toString();
            //Check if the solution contains atleast one alphabet
            if(!solution.matches(".*[a-zA-Z]+.*")) {
                solutionEditText.setError("Solution must contain alphabets!");
                isEmpty = true;
            }
        }
        if(easyAttemptsEditText.getText().toString().isEmpty()) {
            easyAttemptsEditText.setError("Enter Easy Attempts!");
            isEmpty = true;
        } else {
            int easy = Integer.parseInt(easyAttemptsEditText.getText().toString());
            if(easy < 1) {
                easyAttemptsEditText.setError("Attempts should be greater than 1!");
                isEmpty = true;
            }
        }
        if(normalAttemptsEditText.getText().toString().isEmpty()) {
            normalAttemptsEditText.setError("Enter Normal Attempts!");
            isEmpty = true;
        } else {
            int normal = Integer.parseInt(normalAttemptsEditText.getText().toString());
            if(normal < 1) {
                normalAttemptsEditText.setError("Attempts should be greater than 1!");
                isEmpty = true;
            }
        }
        if(hardAttemptsEditText.getText().toString().isEmpty()) {
            hardAttemptsEditText.setError("Enter Hard Attempts!");
            isEmpty = true;
        } else {
            int hard = Integer.parseInt(hardAttemptsEditText.getText().toString());
            if(hard < 1) {
                hardAttemptsEditText.setError("Attempts should be greater than 1!");
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    // Method to display the result dialog after form submission
    protected void displayResult(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(CryptogramActivity.this).create();
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
    public void onCryptogramFormResetClick(View view) {
        cryptoNameEditText.setText("");
        solutionEditText.setText("");
        easyAttemptsEditText.setText("");
        normalAttemptsEditText.setText("");
        hardAttemptsEditText.setText("");
        cryptoNameEditText.setError(null);
        solutionEditText.setError(null);
        easyAttemptsEditText.setError(null);
        normalAttemptsEditText.setError(null);
        hardAttemptsEditText.setError(null);

    }

    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
