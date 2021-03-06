package com.moringaschool.mumapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.moringaschool.mumapp.Constant;
import com.moringaschool.mumapp.R;
import com.moringaschool.mumapp.models.AppUser;
import com.moringaschool.mumapp.ui.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignupTabFragment extends Fragment implements View.OnClickListener{

//    @BindView(R.id.Signup1)
//    Button mButton;
Button mButton;
    EditText username;
    EditText email;
    EditText phone ;

    EditText password;
    EditText confirmPassword;


    FirebaseAuth auth;
    private AppUser mAppUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phone = view.findViewById(R.id.phone);
        username= view.findViewById(R.id.username);
        email = view.findViewById(R.id.signemail);


        password= view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmpass);
        ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();
     mButton  = view.findViewById(R.id.signup1);
        mButton.setOnClickListener(this);




        };

    public void userDetails(){
        String username = this.username.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String phone = this.phone.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        Toast.makeText(getContext(),username, Toast.LENGTH_SHORT).show();

        String confirmPassword = this.confirmPassword.getText().toString().trim();
        signup(email,password,username, phone);


    }

    public void signup(String email, String password,String username, String phone){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    Log.d("name", username);
                    createFirebaseUserProfile(Objects.requireNonNull(task.getResult().getUser()),username);
                    Log.d("created user", "wertyuioptyuio");
                    DatabaseReference restaurantRef = FirebaseDatabase
                            .getInstance()
                            .getReference("User").child(uid);
                    DatabaseReference pushRef = restaurantRef.push();
                    String pushId = pushRef.getKey();
                    AppUser newUser = new AppUser(email,username,phone);
                    newUser.setPushId(pushId);
                    pushRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void onComplete(@NonNull Task task) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_LONG).show();

                }


            }
        });
    }
    private void createFirebaseUserProfile(final FirebaseUser user, String name) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("creating user", Objects.requireNonNull(user.getDisplayName()));
                        } else {
                            Log.d("ffffaaaaaiiilll", Objects.requireNonNull(user.getDisplayName()));

                        }
                    }

                });

    }

    //SAVING THE USER OBJECT

    @Override
    public void onClick(View v) {
        if(v == mButton){
            userDetails();
            DatabaseReference userRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constant.FIREBASE_CHILD_USER);
                    userRef.push().setValue(mAppUser);

        }
    }
}
