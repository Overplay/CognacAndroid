package tv.ourglass.ourglasstv.Scenes.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Tabs.MainTabsActivity;


public class SettingsFragment extends Fragment {

    String TAG = "SettingsFragment";

    private ArrayList<SettingsListOption> options = new ArrayList<SettingsListOption>() {{
        add(new SettingsListOption("My Venues", R.drawable.default_dot));
        add(new SettingsListOption("Setup OG Device", R.drawable.ic_queue_play_next_black_36dp));
        add(new SettingsListOption("Edit Account", R.drawable.ic_person_outline_black_36dp));
        //add(new SettingsListOption("Invite Friends", R.drawable.ic_card_giftcard_black_36dp));
        add(new SettingsListOption("Log Out", R.drawable.ic_chevron_left_black_24dp));
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // set up top toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.tb_settings));
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // Create the adapter to convert the array to views
        SettingsListAdapter adapter = new SettingsListAdapter(getActivity(), options);

        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.optionsList);
        listView.setAdapter(adapter);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_add).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
