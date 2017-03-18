package tv.ourglass.alyssa.bourbon_android.Scenes.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tv.ourglass.alyssa.bourbon_android.R;

/**
 * Created by atorres on 3/17/17.
 */

public class HostFragment extends BackStackFragment {

    private Fragment fragment;

    public static HostFragment newInstance(Fragment fragment) {
        HostFragment hostFragment = new HostFragment();
        hostFragment.fragment = fragment;
        return hostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.host_fragment, container, false);

        if (fragment != null) {
            replaceFragment(fragment, false);
        }

        return view;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackstack) {
        if (addToBackstack) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.hosted_fragment, fragment)
                    .addToBackStack(null)
                    .commit();

        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.hosted_fragment, fragment)
                    .commit();
        }
    }
}
