package tv.ourglass.alyssa.absinthe_android.Scenes.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Registration.WelcomeActivity;

/**
 * Created by alyssa on 11/6/16.
 */

public class SettingsListAdapter extends ArrayAdapter<SettingsListOption> {

    private Context context;

    ProgressDialog progress;

    public SettingsListAdapter(Context context, ArrayList<SettingsListOption> options) {
        super(context, 0, options);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SettingsListOption option = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.settings_option, parent, false);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        if (option != null) {
            icon.setImageResource(option.iconId);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (option != null) {
            name.setText(option.name);
        }

        Typeface font = Typeface.createFromAsset(context.getAssets(), OGConstants.regularFont);
        name.setTypeface(font);

        view.setTag(option);

        // Add actions to the options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsListOption option = (SettingsListOption) view.getTag();

                switch (option.name) {
                    case "Invite Friends":
                        Log.d("SettingsListAdapter", "invite friends");
                        break;

                    case "Edit Account":
                        Log.d("SettingsListAdapter", "edit account");
                        break;

                    case "Add New Ourglass Device":
                        Log.d("SettingsListAdapter", "add new device");
                        break;

                    case "Add/Manage Venues":
                        Log.d("SettingsListAdapter", "add/manage venues");
                        break;

                    case "Log Out":
                        progress = ProgressDialog.show(context, "Logging out...", "", true);
                        goTo(WelcomeActivity.class);
                        break;

                    default:
                        Log.d("SettingsListAdapter", "unknown option");
                        break;
                }
            }
        });

        return view;

    }

    private void goTo(Class dest) {
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
    }
}
