package pt.unl.fct.campus.firstwebapp.data.model;

import android.Manifest;
import android.app.Activity;

public interface StoragePics {

     static final String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
     static final int REQUEST_EXTERNAL_STORAGE = 1;

     static final int PICK_IMAGE_REQUEST = 9544;

     void verifyPermission(Activity activity);
}
