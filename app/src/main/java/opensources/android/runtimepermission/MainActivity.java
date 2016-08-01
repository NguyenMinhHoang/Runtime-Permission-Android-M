package opensources.android.runtimepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RationalDialog.OnDialogInteractionListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] requestedPermission = new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION};

        if (!checkToShowRationalDialog(requestedPermission, getResources().getStringArray(R.array.titles), getResources().getStringArray(R.array.messages))) {
            requestRuntimePermission(requestedPermission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                HashMap<String, Integer> checkedResult = checkPermissionsAfterRequest(permissions, grantResults);
                if (checkedResult.containsKey(Manifest.permission.READ_CONTACTS)) {
                    if (checkedResult.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Manifest.permission.READ_CONTACTS : OK");
                    } else {
                        Log.d(TAG, "Manifest.permission.READ_CONTACTS : FAIL");
                    }
                }
                if (checkedResult.containsKey(Manifest.permission.CAMERA)) {
                    if (checkedResult.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Manifest.permission.CAMERA : OK");
                    } else {
                        Log.d(TAG, "Manifest.permission.CAMERA : FAIL");
                    }
                }
                if (checkedResult.containsKey(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (checkedResult.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Manifest.permission.ACCESS_FINE_LOCATION : OK");
                    } else {
                        Log.d(TAG, "Manifest.permission.ACCESS_FINE_LOCATION : FAIL");
                    }
                }
                break;
        }
    }

    /**
     * Callback function is called when you click on ok or cancel button on RationalDialog
     * @param dialog
     * @param v
     * @param permissionName
     */
    @Override
    public void onDialogInteraction(RationalDialog dialog, View v, String[] permissionName) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dialog.dismiss();
                requestRuntimePermission(permissionName);
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                Toast.makeText(this, "You should allow permission to do something what app need", Toast.LENGTH_LONG).show();
                Log.d(TAG, "You should allow permission to do something what app need");
                break;
        }
    }

    /**
     * check see if do we need show rational dialog or not
     * @param permission
     * @param allTitle
     * @param allMessage
     */
    private boolean checkToShowRationalDialog(String[] permission, String[] allTitle, String[] allMessage) {
        ArrayList<String> filteredPermissions = new ArrayList<>();
        ArrayList<RationalItemModel> filteredItem = new ArrayList<>();
        //foreach to find see if should or should not show ration dialog
        for (int i = 0; i < permission.length; i ++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[i])) {
                filteredPermissions.add(permission[i]);
                RationalItemModel item = new RationalItemModel(allTitle[i], allMessage[i]);
                filteredItem.add(item);
            }
        }
        if (filteredItem.size() > 0) {
            showRationalDialog(filteredPermissions.toArray(new String[filteredPermissions.size()]), filteredItem);
            return true;
        }
        return false;
    }

    /**
     * show rational dialog to explain permission what do you need them for
     * @param permission
     * @param data
     */
    private void showRationalDialog(String[] permission, ArrayList<RationalItemModel> data) {
        RationalDialog dialog = RationalDialog.newInstance(permission, data);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    /**
     * After request you should check again see if user allow or not
     * @param permissions
     * @param grantResults
     * @return HashMap has key is permission name, value is true or false
     */
    private HashMap<String, Integer> checkPermissionsAfterRequest(String[] permissions, int[] grantResults) {
        HashMap<String, Integer> permissionsNotGranted = new HashMap<>();
        for (int i = 0; i < permissions.length; i ++) {
            permissionsNotGranted.put(permissions[i], grantResults[i]);
        }
        return permissionsNotGranted;
    }

    /**
     * request permission what you want in your app
     * @param requestPermissions
     */
    private void requestRuntimePermission(String[] requestPermissions) {
        ArrayList<String> permissionsNeedToRequest = checkPermission(requestPermissions);
        if (permissionsNeedToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsNeedToRequest.toArray(new String[permissionsNeedToRequest.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * check permission you provide see if it granted
     * @param permissionsNeedToCheck
     * @return true if granted otherwise false
     */
    private boolean checkPermission(String permissionsNeedToCheck) {
        return ContextCompat.checkSelfPermission(this, permissionsNeedToCheck) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * check permission you provide see if it granted
     * @param permissionsNeedToCheck
     * @return permissions do not grant; if list is empty, all permission you provide they are granted
     */
    private ArrayList checkPermission(String[] permissionsNeedToCheck) {
        ArrayList<String> needPermissions = new ArrayList<>();
        for (int i = 0; i < permissionsNeedToCheck.length; i ++) {
            if (ContextCompat.checkSelfPermission(this, permissionsNeedToCheck[i]) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permissionsNeedToCheck[i]);
            }
        }
        return needPermissions;
    }
}
