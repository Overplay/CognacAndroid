package tv.ourglass.alyssa.absinthe_android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by alyssa on 11/6/16.
 */

public class SettingsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] options;
    private final Integer[] iconIds;

    public SettingsListAdapter(Activity context, String[] options, Integer[] iconIds) {
        super(context, 0, options);
        this.context = context;
        this.options = options;
        this.iconIds = iconIds;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.settings_option, null);

        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        icon.setImageResource(iconIds[position]);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(options[position]);

        return rowView;

    }
}
