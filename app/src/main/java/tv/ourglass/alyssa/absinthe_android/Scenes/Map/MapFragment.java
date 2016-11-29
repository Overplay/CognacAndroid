package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.absinthe_android.R;

public class MapFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Context c = getContext();

        /*TextView firstName = (TextView)view.findViewById(R.id.firstName);
        firstName.setText(SharedPrefsManager.getUserFirstName(c));

        TextView lastName = (TextView)view.findViewById(R.id.lastName);
        lastName.setText(SharedPrefsManager.getUserLastName(c));

        TextView email = (TextView)view.findViewById(R.id.email);
        email.setText(SharedPrefsManager.getUserEmail(c));

        TextView jwt = (TextView)view.findViewById(R.id.jwt);
        jwt.setText(String.valueOf(SharedPrefsManager.getUserApplejackJwtExpiry(c)));*/

        return view;
    }
}
