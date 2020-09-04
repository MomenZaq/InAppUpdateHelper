
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

import yallashoot.shoot.yalla.com.yallashoot.newapp.R;
import yallashoot.shoot.yalla.com.yallashoot.newapp.core.repositoryHelper.SharedPreferencesHelper;
import yallashoot.shoot.yalla.com.yallashoot.newapp.utility.Methods;

@Singleton
public class InAppUpdateHelper {
    //    public static final Integer DAYS_FOR_FLEXIBLE_UPDATE = 30;
    public static final int MY_REQUEST_CODE = 20;
    Context context;
    public AppUpdateManager appUpdateManager;
    public InstallStateUpdatedListener listener;


    public InAppUpdateHelper(Context context) {
        this.context = context;
    }

    public void checkUpdate(Activity activity, View v) {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(context);
// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks whether the platform allows the specified type of update,
// and current version staleness.

        System.out.println("CheckForAppUpdate");
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            System.out.println("CheckForAppUpdate: " + appUpdateInfo.updateAvailability());
            if (
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.FLEXIBLE,
                            // The current activity making the update request.
                            activity,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

                requestUpdate(activity, v);

            } else      // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate(v);
                }
        });
    }

    private void requestUpdate(Activity activity, View v) {
        System.out.println("CheckForAppUpdate: requestUpdate");

//                Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        listener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState state) {
                System.out.println("CheckForAppUpdate: " + state.installStatus());
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdate(v);
                } else if (state.installStatus() == InstallStatus.DOWNLOADING) {
                    // (Optional) Provide a download progress bar.
                    long bytesDownloaded = state.bytesDownloaded();
                    long totalBytesToDownload = state.totalBytesToDownload();
                    // Implement progress bar.
                } else {
                    // When status updates are no longer needed, unregister the listener.
                    appUpdateManager.unregisterListener(this);

                }
                // Log state or install the update.
            }
        };
        appUpdateManager.registerListener(listener);


    }


    /* Displays the snackbar notification and call to action. */
    public void popupSnackbarForCompleteUpdate(View v) {
        System.out.println("CheckForAppUpdate: popupSnackbarForCompleteUpdate");
        Snackbar snackbar =
                Snackbar.make(
                        v,
                        context.getString(R.string.an_update_has_just_been_downloaded),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                context.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
