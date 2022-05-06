package com.example.online_aram_lejelents;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfilActivity.class.getName();

    private EditText fullNameszerk;
    private EditText emailszerk;
    private boolean checked;
    private CheckBox CheckBox;
    private KeyListener listener;
    private String document;
    private FirebaseAuth mAuth;
    private String newName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


         fullNameszerk = findViewById(R.id.ET1);
         emailszerk = findViewById(R.id.ET2);
        listener = fullNameszerk.getKeyListener();
         fullNameszerk.setKeyListener(null);
         emailszerk.setKeyListener(null);

        mAuth= FirebaseAuth.getInstance();

        show();

        NotificationChannel chanel = new NotificationChannel("Modosítás","Modosítás", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(chanel);

        Log.d(LOG_TAG,"Profil onCreate done!");

    }

    private void show() {
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("uId",mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        document = doc.getId();
                        Log.d(LOG_TAG, " Dokumentum olvasás sikeres");
                        fullNameszerk.setText(doc.getString("FullName"));
                        emailszerk.setText(doc.getString("email"));
                    }
                }
            }
        });


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

    public void onCheckboxClicked(View view) {
         checked = ((CheckBox) view).isChecked();
        fullNameszerk.setKeyListener(listener);
    }

    public void modosit(View view) {
        if (checked) {
            newName = fullNameszerk.getText().toString();
            DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(document);
            ref.update("FullName",newName);

            notification();
            startProfil();

        }else {
            Toast.makeText(this, "Ha módosítani szeretnél pipáld ki dobozt!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Modosítás");
        builder.setContentTitle("Sikeresn módosítottál!");
        builder.setContentText("Az új beállított név: "+newName);
        builder.setSmallIcon(R.drawable.ic_baseline_autorenew_24);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1,builder.build());
    }
}