package pt.unl.fct.campus.firstwebapp.data.Events;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FileImageDownloadClass implements FileImageDownload{


    public FileImageDownloadClass( ){


    }

    @Override
    public StorageReference getFileImageDownload(String url) {


        FirebaseStorage storage = FirebaseStorage.getInstance();

       /*
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        // StorageReference pathReference = storageRef.child("images/stars.jpg");

        // Create a reference to a file from a Google Cloud Storage URI
        //StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        StorageReference httpsReference = storage.getReferenceFromUrl(url);//"https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
*/

        // Reference to an image file in Cloud Storage
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference();

// ImageView in your Activity
        // ImageView imageView = findViewById(R.id.imageView);

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

        StorageReference httpsReference = storage.getReferenceFromUrl(url);

        return httpsReference;
    }

}
