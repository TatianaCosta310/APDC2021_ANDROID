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


public class UpdateInfos extends AppCompatActivity implements Constantes {

    private LoginViewModel loginViewModel;
    String token,quoteA,aboutA,facebookA,instagramA,twitterA;
    Bundle bundleExtra;

    private EditText quote;
    private EditText about;
    private EditText facebook;
    private EditText twitter;

    private EditText instagram;

    private  AdditionalAttributes a = new AdditionalAttributes();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_infos);


        quote = findViewById(R.id.QUOTE);
        about = findViewById(R.id.About);
        instagram = findViewById(R.id.Social_networks_Instagram);
        facebook = findViewById(R.id.Social_networks_Facebook);
        twitter = findViewById(R.id.Social_networks_Twitter);

        final Button update=findViewById(R.id.update);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);


        Intent oldIntent = getIntent();

        token = null;

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();
            token = bundleExtra.getString("token");

            aboutA = bundleExtra.getString("about");
            quoteA = bundleExtra.getString("quote");
            facebookA = bundleExtra.getString("facebook");
            instagramA = bundleExtra.getString("instagram");
            twitterA = bundleExtra.getString("twitter");

            a.setBio(aboutA);
            a.setQuote(quoteA);
            a.setInstagram(instagramA);
            a.setFacebook(facebookA);
            a.setTwitter(twitterA);

        }

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

                   openNextPage(bundleExtra);
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){




                if(!quote.getText().toString().equals(""))
                a.setQuote(quote.getText().toString());

                if(!about.getText().toString().equals(""))
                a.setBio(about.getText().toString());

                if(!facebook.getText().toString().equals(""))
                a.setFacebook(facebook.getText().toString());

                if(!instagram.getText().toString().equals(""))
                a.setInstagram(instagram.getText().toString());

                if(!twitter.getText().toString().equals(""))
                a.setTwitter(twitter.getText().toString());

                loginViewModel.updateInfo(token,a);

            }
        });

    }



    private void showGetInfosFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }


    public void openNextPage(Bundle bundleExtra){

        Intent intent = new Intent(this, Main_Page.class);

        intent.putExtras(bundleExtra);
        startActivity(intent);
    }
}