package tv.ourglass.alyssa.absinthe_android;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alyssa on 11/6/16.
 */

public class SettingsListAdapter extends ArrayAdapter<SettingsListOption> {

    Context context;

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

        Typeface font = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
        name.setTypeface(font);

        view.setTag(option);

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
                        Log.d("SettingsListAdapter", "Log out");
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
