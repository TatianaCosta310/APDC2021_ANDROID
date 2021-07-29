package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Constantes;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.ProfileResponse;


public class UpdateUser extends AppCompatActivity implements Constantes {

    private LoginViewModel loginViewModel;
    String token,profilePic;
    Bundle bundleExtra;
    ProfileResponse atribs;
    ImageView image;

    private TextView quote;
    private TextView about;
    private TextView facebook;
    private TextView twitter;

    private TextView username,ranking,instagram;

    private String name;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);


        quote = findViewById(R.id.QUOTE);
        about = findViewById(R.id.About);
        instagram = findViewById(R.id.Social_networks_Instagram);
        facebook = findViewById(R.id.Social_networks_Facebook);
        twitter = findViewById(R.id.Social_networks_Twitter);

        username = findViewById(R.id.UpdTextName);
        ranking = findViewById(R.id.Ranking);

        image = findViewById(R.id.person2);


        final Button update=findViewById(R.id.update);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);


        Intent oldIntent = getIntent();

        token = null;

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            token = bundleExtra.getString("token");
            profilePic = bundleExtra.getString("profile_pic");
            name = bundleExtra.getString("name");
        }


        if(profilePic!= null){
            String[] split = profilePic.split("/");

            if(split.length == 5) {
                String imageName = split[4];

                Storage storage = StorageOptions.newBuilder()
                        .setProjectId(BLOB_PIC_ID_PROJECT)
                        // .setCredentials(GoogleCredentials.fromStream(new FileInputStream(f)))
                        .build()
                        .getService();

                Thread thread = new Thread(new Runnable() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {

                        File f = new File(UpdateUser.this.getCacheDir(), imageName + ".png");
                        try {
                            f.createNewFile();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }



                        try {
                            Blob blob = storage.get(BlobId.of(BLOB_PIC_ID_PROJECT, imageName));
                            blob.downloadTo(Paths.get(f.toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        byte[] decodedString = new byte[0];
                        try {
                            decodedString = Files.readAllBytes(f.toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                        image.post(new Runnable() {
                            public void run() {
                                image.setImageBitmap(decodedByte);
                            }
                        });

                    }
                });

                thread.start();



            }

        }

        loginViewModel.getInfos(token,token);

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    showGetInfosFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {

                    LoggedInUserView model = loginResult.getSuccess();

                    atribs =  (ProfileResponse) model.getObject();

                    putInfos(atribs);
                }


            }
        });


        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){


                bundleExtra.putString("quote",quote.getText().toString());
                bundleExtra.putString("about",about.getText().toString());
                bundleExtra.putString("facebook",facebook.getText().toString());
                bundleExtra.putString("instagram",instagram.getText().toString());
                bundleExtra.putString("twitter",twitter.getText().toString());

                openNextPage(bundleExtra);

            }
        });

    }

    private void putInfos(ProfileResponse atribs) {


        username.setText(name);
        ranking.setText("PARTICIPATION SCORE: " + atribs.getParticipationScore());

        if(!atribs.getQuote().equals("")){
            quote.setText(atribs.getQuote());
        }

        if(!atribs.getBio().equals("")){
            about.setText(atribs.getBio());

        }

        if(!atribs.getInstagram().equals("")){
            instagram.setText(atribs.getInstagram());
            instagram.setPaintFlags(instagram.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }

        if(!atribs.getFacebook().equals("")){
            facebook.setText(atribs.getFacebook());
            facebook.setPaintFlags(facebook.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        }

        if(!atribs.getTwitter().equals("")){
            twitter.setText(atribs.getTwitter());
            twitter.setPaintFlags(twitter.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }


    }

    private void showGetInfosFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }


    public void openNextPage(Bundle bundleExtra){

        Intent intent = new Intent(this, UpdateInfos.class);


        intent.putExtras(bundleExtra);
        startActivity(intent);
    }
}