package com.example.online_aram_lejelents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String LOG_TAG = HistoryActivity.class.getName();

    private FirebaseAuth mAuth;

    public String date;

    private ListView mListView;
    private ArrayList<Meres>records = new ArrayList<>();

    private static final String FILE_NAME = "oraállások.txt";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth= FirebaseAuth.getInstance();
        mListView = findViewById(R.id.lisView);

        Log.d(LOG_TAG,"history onCreate done!");

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





    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    private void QueryShow() {
        FirebaseFirestore.getInstance().collection("allasok").whereEqualTo("uId",mAuth.getCurrentUser().getUid()).whereEqualTo("date",date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        records.add(new Meres(doc.get("serial").toString(),doc.get("allas").toString(),doc.get("date").toString()));
                        Log.d(LOG_TAG, doc.getData().toString());
                    }
                }
            }
        });

        RecordsListAdapter adpter = new RecordsListAdapter(this,R.layout.adapter_view_layout, records);
        mListView.setAdapter(adpter);

    }

    public void showall(View view) {
        FirebaseFirestore.getInstance().collection("allasok").whereEqualTo("uId",mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Log.d(LOG_TAG, doc.getData().toString());
                        records.add(new Meres(doc.get("serial").toString(),doc.get("allas").toString(),doc.get("date").toString()));
                    }
                }
            }
        });
        RecordsListAdapter adpter = new RecordsListAdapter(this,R.layout.adapter_view_layout, records);
        mListView.setAdapter(adpter);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (i1 < 10 && i2 < 10) {
            date=i+"-0"+i1+"-0"+i2;
        }else if (i1 < 10) {
            date=i+"-0"+i1+"-"+i2;
        }else if (i2 < 10) {
            date=i+"-"+i1+"-0"+i2;
        }else {
            date=i+"-"+i1+"-"+i2;
        }

        QueryShow();
    }

    public void save(View view) {
        if (records.size()==0) {
            Toast.makeText(this, "Elöbb listázz!", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = "";
        for (int i = 0; i < records.size(); i++) {
            text += records.get(i).toString();
        }
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());


            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public String keres(){
       ArrayList<String> s2 = new ArrayList<>();
        String s = findViewById(R.id.textView3).toString();
        FirebaseFirestore.getInstance().collection("allasok").whereEqualTo("uId",mAuth.getCurrentUser().getUid()).whereEqualTo("allas",s)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        s2.add(doc.getId());
                    }
                }
            }
        });
        return s2.get(0);
    }

    public void torol(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference noteRef = db.collection("allasok")
                .document(keres());
        noteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG,"sikeres");
                }
            }
        });
    }
}