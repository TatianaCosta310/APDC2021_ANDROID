package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.firebase.storage.StorageReference;

public interface FileImageDownload {

    StorageReference getFileImageDownload(String url);
}
