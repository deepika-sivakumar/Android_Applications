package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import edu.gatech.seclass.crypto6300.dbUtilities.User;
import edu.gatech.seclass.crypto6300.global.Global;

public class LoginActivity extends AppCompatActivity {

    String userName;
    Button loginButton;
    EditText loginEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Create the admin record in database only when the application is launched for the first time
        if(User.find(User.class, "username = ?", "admin").isEmpty()) {
            User adminUser = new User("admin", true);
            adminUser.save();
        }

        //Initialize the SharedPreferences
        Global.init(getApplicationContext());

        //Get the instances of the widgets
        loginButton = findViewById(R.id.loginButton);
        loginEditText = findViewById(R.id.loginEditText);

        //Display the username if the user closed the application without logging out
        if(Global.getUsername() != null) {
            loginEditText.setText(Global.getUsername());
        }

    }

    // Login Button Click
    public void onLoginBtnClick(View view) {
        userName = loginEditText.getText().toString();

        if(userName.isEmpty()) {
            loginEditText.setError("Enter Username!");
        }
        else {
            // Store the username in the shared preference
            Global.setUsername(userName);

            // Find the user in the database
            List<User> user = User.find(User.class, "username = ?", userName);
            // If the username is not found show error message
            if(user.isEmpty()) {
                loginEditText.setError("Incorrect Username!");
            }
            else {
                User userItem = user.get(0);
                // If the user is an admin, load AdminMenuActivity
                if(userItem.getAdmin()) {
                    Intent intent = new Intent(this, AdminMenuActivity.class);
                    startActivity(intent);
                }
                // If the user is a player, load the PlayerMenuActivity
                else {
                    Intent intent = new Intent(this, PlayerMenuActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    // Method to provide back functionality
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}
