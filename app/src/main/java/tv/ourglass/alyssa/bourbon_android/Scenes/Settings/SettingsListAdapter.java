package tv.ourglass.alyssa.bourbon_android.Scenes.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.WelcomeActivity;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

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
    public View getView(int position, View view, ViewGroup parent) {
        SettingsListOption option = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.settings_option, parent, false);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        if (option != null) {
            icon.setImageResource(option.iconId);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (option != null) {
            name.setText(option.name);
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

                    case "Add New Ourglass Device":
                        Log.d("SettingsListAdapter", "add new device");
                        break;

                    case "Add/Manage Venues":
                        Log.d("SettingsListAdapter", "add/manage venues");
                        break;

                    case "Log Out":

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder
                                .setTitle("Logging out")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ProgressDialog.show(context, "Logging out...", "", true);
                                        Applejack.getInstance().logout(context);
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
