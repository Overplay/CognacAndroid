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

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.activity_in, R.anim.activity_out,
                            R.anim.activity_back_in, R.anim.activity_back_out)
                    .replace(R.id.hosted_fragment, fragment, fragment.getClass().getName())
                    .addToBackStack(null)
                    .commit();

        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.hosted_fragment, fragment)
                    .commit();
        }
    }

    public Fragment getFragment() {
        return fragment;
    }
}
