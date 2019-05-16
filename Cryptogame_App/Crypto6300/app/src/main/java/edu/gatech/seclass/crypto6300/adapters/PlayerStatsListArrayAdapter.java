package edu.gatech.seclass.crypto6300.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.global.Global;
import edu.gatech.seclass.crypto6300.R;

import edu.gatech.seclass.crypto6300.dbUtilities.Player;
import edu.gatech.seclass.crypto6300.dbUtilities.User;

public class PlayerStatsListArrayAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Player> playerArrayList;
    LayoutInflater inflater;

    public PlayerStatsListArrayAdapter(Context context, int resource, ArrayList<Player> playerArrayList) {
        super(context, resource, playerArrayList);
        this.context = context;
        this.playerArrayList = (ArrayList<Player>) playerArrayList;
        inflater = (LayoutInflater.from(context));
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return playerArrayList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Get the Cryptogram List Item layout
        view = inflater.inflate(R.layout.player_stats_list_item, null);

        // Get instances of items in the list item
        // Get instances of items in the list item
        TextView playerNameListItem = (TextView) view.findViewById(R.id.playerNameListItem);
        TextView playerUsernameListItem = (TextView) view.findViewById(R.id.playerUsernameListItem);
        TextView playerLevelListItem = (TextView) view.findViewById(R.id.playerLevelListItem);
        TextView playerWonListItem = (TextView) view.findViewById(R.id.playerWonListItem);
        TextView playerLostListItem = (TextView) view.findViewById(R.id.playerLostListItem);

        Player playerItem = (Player) getItem(i);
        // Set values for the items in the list item
        playerNameListItem.setText("First Name: " + playerItem.getFirstName());
        playerWonListItem.setText("Won: " + playerItem.getNoOfWins());
        playerLostListItem.setText("Lost: " + playerItem.getNoOfLoss());

        List<User> user = User.find(User.class, "username = ?", Global.getUsername());
        User userItem = user.get(0);
        //Display the username, level only if the user is admin
        if(userItem.getAdmin()) {
            playerUsernameListItem.setText("Username: " + playerItem.getUsername());
            playerLevelListItem.setText("Level: " + playerItem.getCategory());
        } else {
            playerUsernameListItem.setVisibility(View.GONE);
            playerLevelListItem.setVisibility(View.GONE);
        }

        return view;
    }
}
