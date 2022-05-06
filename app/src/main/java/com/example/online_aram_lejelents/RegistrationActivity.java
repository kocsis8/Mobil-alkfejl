package com.example.online_aram_lejelents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;


    EditText fullNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);

        if (secret_key != 99){
            finish();
        }

        fullNameEditText = findViewById(R.id.fullNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditText);


        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        String emailAdress = preferences.getString("emailAdress","");
        String password = preferences.getString("password","");

        userEmailEditText.setText(emailAdress);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG,"onCreate");
    }

    public void register(View view) {
        String fullName = fullNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "Nem azonos a jelszó és a megerősítése");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + fullName + " ,e-mail: " + email);


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    usersave();
                    startdashboard();
                }else {
                    Log.d(LOG_TAG,"User wasn't created successfully");
                    Toast.makeText(RegistrationActivity.this,"User wasn't created successfully: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void usersave() {

      FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> dataToSave = new HashMap<>();
        dataToSave.put("FullName",fullNameEditText.getText().toString());
        dataToSave.put("email",userEmailEditText.getText().toString());
        dataToSave.put("uId",mAuth.getCurrentUser().getUid());


        db.collection("users").add(dataToSave).addOnCompleteListener(new OnCompleteListener<DocumentReference>(){
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task){
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG,"Sikeres!");
                }else {
                    Log.d(LOG_TAG,"Sikertelen!");
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    private void startdashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }

}