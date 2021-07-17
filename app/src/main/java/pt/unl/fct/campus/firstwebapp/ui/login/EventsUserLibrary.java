package pt.unl.fct.campus.firstwebapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import pt.unl.fct.campus.firstwebapp.LoginApp;
import pt.unl.fct.campus.firstwebapp.R;
import pt.unl.fct.campus.firstwebapp.data.Events.MyEvents;
import pt.unl.fct.campus.firstwebapp.data.Events.SeeParticipatingEvents;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;

public class EventsUserLibrary extends AppCompatActivity {

    TextView participateNum,creatednum,pointsNum;
    Button seeParticipatingEventsButton,seeCreatedEventsButton;

    private LoginViewModel loginViewModel;
    AdditionalAttributes atribs;
    Bundle bundleExtra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_library);


        String token = null;

        Intent oldIntent = getIntent();
         bundleExtra = new Bundle();

        if(oldIntent != null) {
            bundleExtra = oldIntent.getExtras();

            if(bundleExtra != null)
            token = bundleExtra.getString("token");
        }




        participateNum = findViewById(R.id.textViewParticipatingNumber);
        creatednum = findViewById(R.id.textViewCreatedNumber);
        pointsNum = findViewById(R.id. textViewPointsNumber);

        seeParticipatingEventsButton = findViewById(R.id.ButtonSeeParticipatingEvents);
        seeCreatedEventsButton = findViewById(R.id.ButtonSeeCreatedEvents);


        float radius = getResources().getDimension(R.dimen.corner_radius);

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,radius)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        ViewCompat.setBackground(participateNum,shapeDrawable);
        ViewCompat.setBackground(creatednum,shapeDrawable);
        ViewCompat.setBackground(pointsNum,shapeDrawable);

        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this,R.color.quantum_tealA700));
        shapeDrawable.setStroke(2.0f, ContextCompat.getColor(this,R.color.quantum_tealA700));

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(((LoginApp) getApplication()).getExecutorService()))
                .get(LoginViewModel.class);

        //loginViewModel.getInfos(token,userid); have to get userId as long value????

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

                    atribs =  (AdditionalAttributes) model.getObject();

                    participateNum.setText(String.valueOf(atribs.getInterestedEvents()));
                    creatednum.setText(String.valueOf(atribs.getEvents()));

                }


            }
        });


        seeParticipatingEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(bundleExtra, SeeParticipatingEvents.class);
            }
        });

        seeCreatedEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(bundleExtra, MyEvents.class);
            }
        });

    }


    private void openPage(Bundle params, Class c){

        Intent intent = new Intent(this, c);

        Intent oldIntent = getIntent();

        if(oldIntent != null) {
            params = oldIntent.getExtras();
            intent.putExtras(params);
        }

        startActivity(intent);

    }

    private void showGetInfosFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
}
