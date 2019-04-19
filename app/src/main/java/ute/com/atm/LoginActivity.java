package ute.com.atm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_password;
    private EditText ed_id;
    private CheckBox chk_remember;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        settingsTest();
        findViews();
        new TestTask().execute("http://tw.yahoo.com");
    }

    public class TestTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute: ");
            Toast.makeText(LoginActivity.this, "TEsttttToast", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i(TAG, "onPostExecute: ");
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int data = 0;
            try {
                URL url = new URL(strings[0]);
                data = url.openStream().read();
                Log.d(TAG, "onCreate: " + data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    private void findViews() {
        ed_id = findViewById(R.id.ed_id);
        ed_password = findViewById(R.id.ed_password);
        chk_remember = findViewById(R.id.chk_remember);
        chk_remember.setChecked(getSharedPreferences("atm", MODE_PRIVATE)
                .getBoolean("REMAMBER_USERID", false));
        chk_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm", MODE_PRIVATE)//"atm"為檔名
                        .edit()//編輯
                        .putBoolean("REMAMBER_USERID", isChecked)//定義參數
                        .apply();
            }
        });


        //讀取 SharedPreferences
        String userid = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("USERID", "");
        ed_id.setText(userid);
    }

    private void settingsTest() {
        //寫入SharedPreferences
        getSharedPreferences("atm", MODE_PRIVATE)//"atm"為檔名
                .edit()//編輯
                .putInt("LEVEL", 3)//定義參數
                .putString("Name", "James")//定義參數
                .commit();
        //讀取 SharedPreferences
        int level = getSharedPreferences("atm", MODE_PRIVATE)//"atm"為檔名
                .getInt("LEVEL", 0);
        Log.i("see level", "onCreate: " + level);
    }

    public void login(View view) {
        final String userid = ed_id.getText().toString();
        final String password = ed_password.getText().toString();
        //使用Firebase RealtimeDatabase 驗證帳號密碼
        FirebaseApp.initializeApp(this);//先做initializeApp
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if (pw.equals(password)) {
                            boolean remember = getSharedPreferences("atm", MODE_PRIVATE)
                                    .getBoolean("REMAMBER_USERID", false);
                            if (remember) {
                                getSharedPreferences("atm", MODE_PRIVATE)//寫入"atm".xml
                                        .edit()//編輯
                                        .putString("USERID", userid)//定義參數
                                        .apply();
                            }


                            setResult(RESULT_OK);
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("LoginResult")
                                    .setMessage("LoginFail")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void quit(View view) {


    }

    public void map(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }
}
