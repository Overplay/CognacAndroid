package tv.ourglass.alyssa.absinthe_android.Scenes.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.R;


public class SettingsFragment extends Fragment {

    private String[] names = {
            "Invite Friends",
            "Edit Account",
            "Add New Ourglass Device",
            "Add/Manage Venues",
            "Log Out"
    };

    private Integer[] iconIds = {
            R.drawable.ic_card_giftcard_black_36dp,
            R.drawable.ic_person_outline_black_36dp,
            R.drawable.ic_queue_play_next_black_36dp,
            R.drawable.ic_add_location_black_36dp,
            R.drawable.ic_chevron_left_black_24dp
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Construct the data source
        ArrayList<SettingsListOption> options = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            options.add(new SettingsListOption(names[i], iconIds[i]));
        }

        // Create the adapter to convert the array to views
        SettingsListAdapter adapter = new SettingsListAdapter(getActivity(), options);

        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.optionsList);
        listView.setAdapter(adapter);

        return view;
    }
}
