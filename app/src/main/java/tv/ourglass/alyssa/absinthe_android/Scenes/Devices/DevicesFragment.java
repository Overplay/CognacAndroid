package tv.ourglass.alyssa.absinthe_android.Scenes.Devices;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.R;

public class DevicesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        TextView text = (TextView)view.findViewById(R.id.networkLabel);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Poppins-Regular.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView)view.findViewById(R.id.networkName);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Poppins-SemiBold.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        return view;
    }
}