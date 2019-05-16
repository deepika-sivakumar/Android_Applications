package edu.gatech.seclass.crypto6300.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.seclass.crypto6300.R;
import edu.gatech.seclass.crypto6300.dbUtilities.PlayerGames;

public class ChooseCryptogramListArrayAdapter extends ArrayAdapter {

    Context context;
    ArrayList<PlayerGames> cryptogramArrayList;
    LayoutInflater inflater;

    public ChooseCryptogramListArrayAdapter(Context context, int resource, ArrayList<PlayerGames> cryptogramArrayList) {
        super(context, resource, cryptogramArrayList);
        this.context = context;
        this.cryptogramArrayList = (ArrayList<PlayerGames>) cryptogramArrayList;
        inflater = (LayoutInflater.from(context));
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cryptogramArrayList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Get the Cryptogram List Item layout
        view = inflater.inflate(R.layout.cryptogram_list_item, null);

        // Get instances of items in the list item
        TextView cryptoNameListItem = (TextView) view.findViewById(R.id.cryptoNameListItem);
        TextView cryptoStatusListItem = (TextView) view.findViewById(R.id.cryptoStatusListItem);

        PlayerGames cryptogramItem = (PlayerGames) getItem(i);
        // Set values for the items in the list item
        cryptoNameListItem.setText("Name: " + cryptogramItem.getCryptogramName());
        cryptoStatusListItem.setText("Status: " + cryptogramItem.getStatus());

        return view;
    }
}
