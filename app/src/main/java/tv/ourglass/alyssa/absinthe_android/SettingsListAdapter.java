package tv.ourglass.alyssa.absinthe_android;

import android.content.Context;
import android.graphics.Typeface;
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
        icon.setImageResource(option.iconId);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(option.name);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
        name.setTypeface(font);

        return view;

    }
}
