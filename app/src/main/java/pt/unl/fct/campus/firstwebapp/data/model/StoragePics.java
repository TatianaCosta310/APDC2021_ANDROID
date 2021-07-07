package pt.unl.fct.campus.firstwebapp.data.model;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public interface StoragePics {

      String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

      int REQUEST_EXTERNAL_STORAGE = 1;

     int PICK_IMAGE_REQUEST = 9544;

     void verifyPermission(Activity activity);

}
