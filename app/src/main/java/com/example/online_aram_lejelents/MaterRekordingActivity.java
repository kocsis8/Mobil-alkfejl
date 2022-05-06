package com.example.online_aram_lejelents;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MaterRekordingActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String SERIAL_KEY = "serial";
    public static final String ALLAS_KEY = "allas";


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mater_rekording);
        mAuth = FirebaseAuth.getInstance();

        Log.d(LOG_TAG,"materRecording onCreate done!");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                logout();
                return true;
            case R.id.nav_record:
                startmaterRecording();
                return true;
            case R.id.nav_account:
                startProfil();
                return true;
            case R.id.nav_history:
                startHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void startHistory() {
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
        finish();

    }


    private void startProfil() {
        Intent intent = new Intent(this,ProfilActivity.class);
        startActivity(intent);
        finish();

    }

    private void startmaterRecording() {
        Intent intent = new Intent(this,MaterRekordingActivity.class);
        startActivity(intent);
        finish();

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void kuld(View view) {
        EditText serial =  findViewById(R.id.serialNumeditTextl);
        EditText allas =  findViewById(R.id.resoulteditText);

        String serialText = serial.getText().toString();
        String allasText = allas.getText().toString();

        if (allasText.isEmpty() || serialText.isEmpty()) {
            Toast.makeText(this, "Mind kettőt ki kell tölteni!", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> dataToSave = new HashMap<>();
        dataToSave.put(SERIAL_KEY,serialText);
        dataToSave.put(ALLAS_KEY,allasText);
        dataToSave.put("uId",mAuth.getCurrentUser().getUid());
        LocalDate date = LocalDate.now();
        dataToSave.put("date", date.toString());

        db.collection("allasok").add(dataToSave).addOnCompleteListener(new OnCompleteListener<DocumentReference>(){
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task){
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG,"Sikeres!");
                    serial.setText("");
                    allas.setText("");
                    Toast.makeText(MaterRekordingActivity.this, "Sikeres en rögzítetted az óraállást", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(LOG_TAG,"Sikertelen!");
                }
            }
        });


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
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }
}