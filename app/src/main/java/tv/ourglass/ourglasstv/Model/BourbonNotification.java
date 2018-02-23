package tv.ourglass.ourglasstv.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by atorres on 5/13/17.
 */

public enum BourbonNotification {
    allVenuesUpdated,
    myVenuesUpdated,
    addedVenue,
    updatedDevice,
    networkChanged;

    public void issue(Context context) {
        Intent intent = new Intent(this.name());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
