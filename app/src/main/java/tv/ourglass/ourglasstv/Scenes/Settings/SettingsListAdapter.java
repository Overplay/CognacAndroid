package tv.ourglass.ourglasstv.Scenes.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.ourglasstv.Networking.OGCloud;
import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Settings.EditAccount.EditAccountFragment;
import tv.ourglass.ourglasstv.Scenes.Settings.MyVenues.MyVenuesFragment;
import tv.ourglass.ourglasstv.Scenes.Settings.SetupDevice.PickVenueFragment;
import tv.ourglass.ourglasstv.Scenes.Tabs.MainTabsActivity;

/**
 * Created by alyssa on 11/6/16.
 */

public class SettingsListAdapter extends ArrayAdapter<SettingsListOption> {

    private String TAG = "SettingsListAdapter";

    private Context context;

    public SettingsListAdapter(Context context, ArrayList<SettingsListOption> options) {
        super(context, 0, options);
        this.context = context;
    }

    @Override
    @NonNull public View getView(int position, View view, @NonNull ViewGroup parent) {
        SettingsListOption option = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.settings_option, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (option != null) {
            name.setText(option.name);
        }

        ImageView img = (ImageView) view.findViewById(R.id.rightArrow);
        if (option != null) {
            if (option.name.equals("Log Out")) {
                img.setVisibility(View.INVISIBLE);
            }
        }

        view.setTag(option);

        // Add actions to the options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsListOption option = (SettingsListOption) view.getTag();

                switch (option.name) {
                    case "Invite Friends":
                        ((MainTabsActivity) context).openNewFragment(new InviteFriendFragment());
                        break;

                    case "Edit Account":
                        ((MainTabsActivity) context).openNewFragment(new EditAccountFragment());
                        break;

                    case "Setup OG Device":
                        ((MainTabsActivity) context).openNewFragment(new PickVenueFragment());
                        break;

                    case "My Venues":
                        ((MainTabsActivity) context).openNewFragment(new MyVenuesFragment());
                        break;

                    case "Log Out":

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder
                                .setTitle("Log out")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ProgressDialog.show(context, "Logging out...", "", true);
                                        OGCloud.getInstance().logout(context);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        break;

                    default:
                        Log.d("SettingsListAdapter", "unknown option");
                        break;
                }
            }
        });

        return view;

    }
}
