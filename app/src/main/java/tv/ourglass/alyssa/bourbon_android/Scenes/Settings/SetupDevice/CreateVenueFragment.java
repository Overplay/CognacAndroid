package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SetupDevice;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.Input.InputType;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextFocusChangeListener;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextValidator;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Model.Yelp.YelpVenue;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 5/17/17.
 */

public class CreateVenueFragment extends Fragment {

    String TAG = "CreateVenueFragment";

    EditText mYelpSearchTerm, mYelpLocation;

    EditText mVenueName, mAddress1, mAddress2, mCity, mState, mZip;

    Button createButton;

    YelpVenue mYelpVenue;

    private boolean mUsingCurrentLoc = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_venue, container, false);

        // set up toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Add venue");
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // set up Yelp search on button click
        Button btn = (Button) rootView.findViewById(R.id.yelpSearch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doYelpSearch();
            }
        });

        // set up creating venue on button click
        createButton = (Button) rootView.findViewById(R.id.createVenue);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVenue();
            }
        });

        // set up use of current location in Yelp search
        mYelpSearchTerm = (EditText) rootView.findViewById(R.id.yelpSearchTerm);
        mYelpLocation = (EditText) rootView.findViewById(R.id.yelpSearchLocation);
        mYelpLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mYelpLocation.getRight() -
                            mYelpLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        mYelpLocation.setTextColor(ContextCompat.getColor(getActivity(), R.color.actionBlue));
                        mYelpLocation.setText(getResources().getString(R.string.currentLocation));
                        mUsingCurrentLoc = true;
                        return true;
                    }
                }
                return false;
            }
        });

        mYelpLocation.addTextChangedListener(new TextValidator(mYelpLocation) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.equals(getResources().getString(R.string.currentLocation))) {
                    mUsingCurrentLoc = false;
                    mYelpLocation.setTextColor(ContextCompat.getColor(getActivity(), R.color.OGWhite));
                }
            }
        });

        // listeners to validate the inputs
        mVenueName = (EditText) rootView.findViewById(R.id.venueName);
        mVenueName.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(mVenueName, InputType.NONEMPTY));

        mAddress1 = (EditText) rootView.findViewById(R.id.address);
        mAddress1.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(mAddress1, InputType.NONEMPTY));

        mAddress2 = (EditText) rootView.findViewById(R.id.address2);

        mCity = (EditText) rootView.findViewById(R.id.city);
        mCity.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(mCity, InputType.NONEMPTY));

        mState = (EditText) rootView.findViewById(R.id.state);
        mState.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(mState, InputType.NONEMPTY));

        mZip = (EditText) rootView.findViewById(R.id.zip);
        mZip.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(mZip, InputType.NONEMPTY));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // set search location to proper format if using current location
        EditText yelpLocation = (EditText) getView().findViewById(R.id.yelpSearchLocation);
        if (mUsingCurrentLoc) {
            yelpLocation.setTextColor(ContextCompat.getColor(getActivity(), R.color.actionBlue));
            yelpLocation.setText(getResources().getString(R.string.currentLocation));
            yelpLocation.clearFocus();
        }

        // check if a yelp venue has been selected
        mYelpVenue = ((MainTabsActivity) getActivity()).getSelectedYelpVenue();
        if (mYelpVenue != null) {
            mVenueName.setText(mYelpVenue.ogVenue.name);
            mVenueName.setError(null);
            mAddress1.setText(mYelpVenue.ogVenue.address1);
            mAddress1.setError(null);
            mAddress2.setText(mYelpVenue.ogVenue.address2);
            mAddress2.setError(null);
            mCity.setText(mYelpVenue.ogVenue.city);
            mCity.setError(null);
            mState.setText(mYelpVenue.ogVenue.state);
            mState.setError(null);
            mZip.setText(mYelpVenue.ogVenue.zip);
            mZip.setError(null);
        }
    }

    /**
     * Performs a Yelp search using the provided input.
     */
    private void doYelpSearch() {
        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) { // keyboard not open
        }

        // go to Yelp search results page
        if (mUsingCurrentLoc) {
            ((MainTabsActivity) getActivity()).openNewFragment(
                    FindYelpVenueFragment.newInstance(mYelpSearchTerm.getText().toString()));

        } else {
            ((MainTabsActivity) getActivity()).openNewFragment(
                    FindYelpVenueFragment.newInstance(mYelpSearchTerm.getText().toString(),
                            mYelpLocation.getText().toString()));
        }
    }

    /**
     * Creates a new venue using the provided input.
     */
    private void createVenue() {
        // disable button
        createButton.setEnabled(false);

        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) { // keyboard not open
        }

        // remove focus from the EditText's
        getView().findViewById(R.id.layout).requestFocus();

        // check inputs for error
        ArrayList<EditText> inputs = new ArrayList<>(Arrays.asList(
                mVenueName, mAddress1, mCity, mState, mZip));

        boolean inputValid = true;

        for (EditText input : inputs) {
            if (input.getError() != null || input.getText().toString().trim().isEmpty()) {
                inputValid = false;
                input.setError(getString(R.string.req_field));
            }
        }

        if (inputValid) {
            String name = mVenueName.getText().toString();
            String addr1 = mAddress1.getText().toString();
            String addr2 = mAddress2.getText().toString();
            String city = mCity.getText().toString();
            String state = mState.getText().toString();
            String zip = mZip.getText().toString();

            OGVenue toAdd = new OGVenue(name, addr1, addr2, city, state, zip, 0.0, 0.0, "");

            if (mYelpVenue != null) { // we have a yelp venue and the associated data
                toAdd.yelpId = mYelpVenue.ogVenue.yelpId;
                toAdd.latitude = mYelpVenue.ogVenue.latitude;
                toAdd.longitude = mYelpVenue.ogVenue.longitude;

            } else { // we need to get latitude and longitude
                Geocoder geocoder = new Geocoder(getActivity());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(toAdd.getAddress(), 1);
                    toAdd.latitude = addresses.get(0).getLatitude();
                    toAdd.longitude = addresses.get(0).getLongitude();

                } catch (IOException | IndexOutOfBoundsException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    showAlert("Uh oh!", "It looks like the address provided isn't valid.");
                    return;
                }
            }
            Applejack.getInstance().addVenue(getActivity(), toAdd, addVenueCb);

        } else {
            createButton.setEnabled(true);
        }
    }

    Applejack.HttpCallback addVenueCb = new Applejack.HttpCallback() {
        @Override
        public void onSuccess(Response response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), getString(R.string.venue_created), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            });
            response.body().close();
        }

        @Override
        public void onFailure(Call call, IOException e, Applejack.ApplejackError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAlert("Uh oh!", "It looks like something went wrong creating your venue.");
                }
            });
        }
    };

    private void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }
}
