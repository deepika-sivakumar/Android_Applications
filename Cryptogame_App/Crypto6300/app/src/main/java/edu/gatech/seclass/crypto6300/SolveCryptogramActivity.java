package edu.gatech.seclass.crypto6300;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;

import android.util.Log;

import android.text.method.ScrollingMovementMethod;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.seclass.crypto6300.Helper.EncryptionHelper;
import edu.gatech.seclass.crypto6300.dbUtilities.Cryptogram;
import edu.gatech.seclass.crypto6300.dbUtilities.Player;
import edu.gatech.seclass.crypto6300.dbUtilities.PlayerGames;
import edu.gatech.seclass.crypto6300.global.Global;

public class SolveCryptogramActivity extends AppCompatActivity {


    String cryptogram = "Mellow Yellow Fellow";
    double[] frequency = {1,1,1,3,3,3,6};

    Cryptogram cryptogramObj;
    PlayerGames playerGames;

    char[] encryptedLettersArray;

    String encryptedPhrase;
    String potentialSolution;
    Boolean result;

    TextView cryptogramNameTextView;
    TextView attemptsTextView;
    TextView encryptedPhraseTextView;
    TextView potentialLabelTextView;
    TextView potentialSolutionTextView;
    LinearLayout solveLayout1;
    LinearLayout solveLayout2;
    Button solveFormButtonSubmit;

    //Used to create dynamic Edit Texts
    EditText replacementLetterEditText;

    //Store encrypted letter & edit text pair
    HashMap<Character, EditText> encryptedReplacementMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_cryptogram);

        //Get the instances of the widgets in the layout
        this.cryptogramNameTextView = findViewById(R.id.cryptogramNameTextView);
        this.attemptsTextView = findViewById(R.id.attemptsTextView);
        this.encryptedPhraseTextView = findViewById(R.id.encryptedPhraseTextView);
        this.potentialLabelTextView = findViewById(R.id.potentialLabel);
        this.potentialSolutionTextView = findViewById(R.id.potentialSolutionTextView);
        this.solveLayout1 = findViewById(R.id.solveLayout1);
        this.solveLayout2 = findViewById(R.id.solveLayout2);
        this.solveFormButtonSubmit = findViewById(R.id.solveFormButtonSubmit);

        //Set scrollbar
        this.potentialSolutionTextView.setMovementMethod(new ScrollingMovementMethod());
        this.encryptedPhraseTextView.setMovementMethod(new ScrollingMovementMethod());
//        this.solveLayout1.setMovementMethod(new ScrollingMovementMethod());

        //Intialize the hashmap
        this.encryptedReplacementMap = new HashMap<Character, EditText>();

        // Get the cryptogram name from the previous activity
        Bundle extras = getIntent().getExtras();
        String cryptogramName = null;
        if (extras != null) {
            cryptogramName = extras.getString("cryptogramName");
            //The key argument here must match that used in the other activity
        }
        //Query the database and get the Cryptogram object
        List<Cryptogram> cryptogramList = Cryptogram.find(Cryptogram.class, "cryptogramname = ?", cryptogramName);
        this.cryptogramObj = cryptogramList.get(0);

        //Query the database and get the PlayerGames record
        List<PlayerGames> playerGamesList = PlayerGames.find(PlayerGames.class, "username = ?", Global.getUsername());
        if(playerGamesList.size() > 0) {
            for (PlayerGames playerGames : playerGamesList) {
                if(playerGames.getCryptogramName().equals(cryptogramName)) {
                    this.playerGames = playerGames;
                }
            }
        }

        //Generate encrypted Phrase and save to the database for the first time the user chooses the cryptogram
        if(playerGames.getStatus().equals("unstarted")) {
            encryptedPhrase();
        }

        //Set the Cryptogram fields in the layout
        this.cryptogramNameTextView.setText(cryptogramName);
        this.attemptsTextView.setText(Integer.toString(this.playerGames.getRemainingAttempts()));
        this.encryptedPhraseTextView.setText(this.playerGames.getEncryptedPhrase());

        //If the game was inprogress display the previous state
        if(playerGames.getStatus().equals("inprogress")) {
            this.potentialLabelTextView.setText("Previous State:");
            this.potentialSolutionTextView.setText(this.playerGames.getPreviousState());
            this.solveFormButtonSubmit.setEnabled(true);
        }

        //Create Edit Text dynamically in the layout for the encrypted letters & replacement letters
        createSolveLayout();

        //Update the status of the record to inprogress in the database
        this.playerGames.setStatus("inprogress");
        this.playerGames.save();
    }

    protected void createSolveLayout() {
        this.encryptedPhrase = this.playerGames.getEncryptedPhrase();
        this.encryptedLettersArray = this.encryptedPhrase.toCharArray();
        //Remove special characters & repeated letters
        String refinedEncryptedLetters = EncryptionHelper.removeDuplicate(EncryptionHelper.removeSpecialCharacter(this.encryptedPhrase));
        char[] refinedEncryptedLettersArray = refinedEncryptedLetters.toCharArray();

        LinearLayout linearLayout;
        TextView encryptedLetterTextView;

        //Iterate through the array and create textboxes for each letter
        for(int i=0; i < refinedEncryptedLettersArray.length; i++) {
            char encryptedLetter = refinedEncryptedLettersArray[i];
            //A Vertical LinearLayout to include the encrypted & replacement letter pair
            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            encryptedLetterTextView = new TextView(this);
            encryptedLetterTextView.setText(Character.toString(encryptedLetter));
            //Set padding to align it with the edit text below
            encryptedLetterTextView.setPadding(7,0,0,0);

            replacementLetterEditText = new EditText(this);
            replacementLetterEditText.setId(encryptedLetter);
            //Set Height & Width to wrap content
            replacementLetterEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //Set Input type
            replacementLetterEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            replacementLetterEditText.setFilters(new InputFilter[]{
                    //Add Input Filter to edit text to accept only alphabets
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                            if (src.equals("")) {
                                return src;
                            }
                            if (src.toString().matches("[a-zA-Z ]+")) {
                                return src;
                            }
                            return "";
                        }
                    },
                    //Add Length filter to edit text Restricting to 1 character only
                    new InputFilter.LengthFilter(1)
            });

            //Adding text change listener to the Edit Text
            replacementLetterEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) { }
                public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Disable the button if user changes any edit text
                    solveFormButtonSubmit.setEnabled(false);
                }
            });

            //Add the (EncryptedLetter, EditText) pair to the Hash map
            this.encryptedReplacementMap.put(encryptedLetter, replacementLetterEditText);
            //Add the (EncryptedLetter, EditText) to the vertical linear layout
            linearLayout.addView(encryptedLetterTextView, 0);
            linearLayout.addView(replacementLetterEditText, 1);
            //Add the first 13 letters to the solveLayout1 and remaining on the solveLayout2
            if(i < 13) {
                this.solveLayout1.addView(linearLayout);
                this.solveLayout2.setVisibility(View.GONE);
            } else {
                this.solveLayout2.setVisibility(View.VISIBLE);
                this.solveLayout2.addView(linearLayout);
            }
        }
    }

    // Method to generate encrypted phrase
    public void encryptedPhrase() {
        //Get the Cryptogram Solution from the database
        String solution = this.cryptogramObj.getSolution();
        char[] solutionLettersArray = solution.toCharArray();
        String plainLettersStr = EncryptionHelper.removeDuplicate(EncryptionHelper.removeSpecialCharacter(solution));
        char[] plainLetters = plainLettersStr.toCharArray();

        char[] encryptedLettersArray = new char[solutionLettersArray.length];
        char cipher;

        HashMap<Character,Character> encryptionMap = new HashMap<Character, Character>();

        //Iterate through
        for(char letter: plainLetters) {
            //Generate a cipher by shifting each letter randomly
            cipher = EncryptionHelper.shiftAlphabet(letter, EncryptionHelper.getRandomNumber());
            while(letter == cipher || encryptionMap.containsValue(cipher)) {
                cipher = EncryptionHelper.shiftAlphabet(letter, EncryptionHelper.getRandomNumber());
            }
            encryptionMap.put(letter, cipher);
        }

        int j = 0;

        //Now form the encryptedPhrase
        for(char solutionLetter: solutionLettersArray) {
            //Do all this only if the character is an alphabet, else just add the encrypted letter i++ and move to the next position
            if( (solutionLetter >= 'a' && solutionLetter <= 'z') || (solutionLetter >= 'A' && solutionLetter <= 'Z')) {
                //Get the cipher value mapped to the letter
                char cipherLetter = encryptionMap.get(Character.toUpperCase(solutionLetter));
                //Replace the solution letter with the cipher
                //Preserve Capitalization
                if (Character.isUpperCase(solutionLetter)) {
                    cipherLetter = Character.toUpperCase(cipherLetter);
                } else if (Character.isLowerCase(solutionLetter)) {
                    cipherLetter = Character.toLowerCase(cipherLetter);
                }
                //Form the encrypted phrase array with the replaced letters
                encryptedLettersArray[j] = cipherLetter;
            }
            else {
                //If not an alphabet just add the character to the array
                encryptedLettersArray[j] = solutionLetter;
            }
            j++;
        }

        //Save the char array as a string
        String encryptedPhrase = String.valueOf(encryptedLettersArray);
        //Save the encrypted phrase in the "Player Games" table in database
        this.playerGames.setEncryptedPhrase(encryptedPhrase);
        this.playerGames.save();
    }

    //Reset Button Click
    public void onResetButtonClick(View view) {
        //Reset all the edit text fields
        for (EditText editText : this.encryptedReplacementMap.values()) {
            editText.setText("");
        }
    }

    // View Potential Solution button click
    public void onViewSolutionButtonClick(View view) {
        //Check if user has entered values for all the edit text fields
        if(!validateReplacementFields()) {
            //Form the Potential Solution
            char[] potentialSolutionArray = new char[this.encryptedLettersArray.length];
            int i = 0;
            for(char encryptedLetter: this.encryptedLettersArray) {
                //TODO: Do all this only if the character is an alphabet, else just add the encrypted letter i++ and move to the next position
                if( (encryptedLetter >= 'a' && encryptedLetter <= 'z') || (encryptedLetter >= 'A' && encryptedLetter <= 'Z')) {
                    //Get the instance of the mapped EditText and get the text the user has entered
                    char replaceLetter = this.encryptedReplacementMap.get(Character.toUpperCase(encryptedLetter)).getText().toString().charAt(0);
                    //Replace the encrypted letter with the user provided replacement letter and store it as the potential solution
                    //Preserve Capitalization
                    if (Character.isUpperCase(encryptedLetter)) {
                        replaceLetter = Character.toUpperCase(replaceLetter);
                    } else if (Character.isLowerCase(encryptedLetter)) {
                        replaceLetter = Character.toLowerCase(replaceLetter);
                    }
                    //Form the potential solution array with the replaced letters
                    potentialSolutionArray[i] = replaceLetter;
                }
                else {
                    //If not an alphabet just add the character to the array
                    potentialSolutionArray[i] = encryptedLetter;
                }
                i++;
            }

            //Save the char array as a string
            this.potentialSolution = String.valueOf(potentialSolutionArray);

            //Set the resulting potential solution to the text view
            this.potentialLabelTextView.setText("Potential Solution:");
            this.potentialSolutionTextView.setText(this.potentialSolution);
            //Enable the Submit Answer button
            this.solveFormButtonSubmit.setEnabled(true);

            //Update the previous state in the database with the potential solution
            this.playerGames.setPreviousState(this.potentialSolution);
            this.playerGames.save();

            //Enable the Submit button
            this.solveFormButtonSubmit.setEnabled(true);
        }
    }

    // Validate the Edit Text fields
    public Boolean validateReplacementFields() {
        Boolean isEmpty = false;
        for (EditText editText : this.encryptedReplacementMap.values()) {
            if(editText.getText().toString().isEmpty()) {
                editText.setError("Enter a letter!");
                isEmpty = true;
            }
        }
        //Check if the all the letters entered by the player are unique
        /*if(!isEmpty) {
            HashMap<Character, EditText> encryptedReplacementMap2 = new HashMap<Character, EditText>();
            encryptedReplacementMap2.putAll(this.encryptedReplacementMap);
            for (EditText editText : encryptedReplacementMap.values()) {
                String userInput = editText.getText().toString();
                if (!userInput.isEmpty() && Character.toUpperCase(s.charAt(0)) == Character.toUpperCase(userInput.charAt(0))) {
                    replacementLetterEditText.setText("");
                    replacementLetterEditText.setError("Enter a unique letter!");
                }
            }
        }*/
        return isEmpty;
    }

    // Submit Answer button click
    public void onSubmitAnswerButtonClick(View view) {
        //Get the player's solution from the potential solution
        this.potentialSolution = this.potentialSolutionTextView.getText().toString();
        if(this.potentialSolution.isEmpty()) {
            this.result = false;
            displayResult("View your solution before submitting!");
        }
        else {
            // Submit the answer, display result & update the database
            submitAnswer();
        }
    }

    // Method to Submit answer and update database
    protected void submitAnswer() {
        //Get the player record from the database
        List<Player> playerList = Player.find(Player.class, "username = ?", Global.getUsername());
        Player player = playerList.get(0);

        //Query the database if the answer is correct
        if(this.potentialSolution.equals(this.cryptogramObj.getSolution())) {
            //If the answer is correct, Remove the playergames record from the database to make it unavailable for the player
            this.playerGames.delete();
            //Update the "wins" in the "Player" table
            player.setNoOfWins(player.getNoOfWins() + 1);
            player.save();
            //Display the result
            this.result = true;
            displayResult("Congrats! You won the game!");
        } else {
            //If the answer is wrong and this was the last attempt
            if(this.playerGames.getRemainingAttempts() <= 1) {
                //Remove the playergames record from the database to make it unavailable for the player
                this.playerGames.delete();
                //Update the "loss" in the "Player" table
                player.setNoOfLoss(player.getNoOfLoss() + 1);
                player.save();
                //Display the result
                this.result = true;
                displayResult("Sorry! Wrong answer! No more attempts remaining! You lost the game!");
            } else {
                //If the answer is wrong, Decrement the remaining attempts in the database
                this.playerGames.setRemainingAttempts(this.playerGames.getRemainingAttempts() - 1);
                this.playerGames.save();
                //Update the view
                this.attemptsTextView.setText(Integer.toString(this.playerGames.getRemainingAttempts()));
                //Display the result
                this.result = false;
                // If only one more attempt remaining
                if(this.playerGames.getRemainingAttempts() == 1) {
                    displayResult("Sorry! Wrong answer! Try Again! One more attempt remaining!");
                } else {
                    displayResult("Sorry! Wrong answer! Try Again!");
                }
            }
        }
    }

    // Method to display the result dialog after form submission
    protected void displayResult(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SolveCryptogramActivity.this).create();
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


    // Reset button click
    public void onResetAnswerButtonClick(View view) {

        for (EditText editText : this.encryptedReplacementMap.values()) {
            editText.setText("");
            editText.setError(null);
        }
    }


    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Load the Choose Cryptogram List again for the status to get updated
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName",getString(R.string.choose_list_name));
        startActivity(intent);
    }
}
