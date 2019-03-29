package ute.com.atm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS = 80;
    private static final String TAG = "see";
    private RecyclerView recycler_contact;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        recycler_contact = findViewById(R.id.recycler_contact);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            readContects();
        } else {
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.READ_CONTACTS}
                    , REQUEST_CONTACTS);
        }

    }

    private void readContects() {//聯絡人資料
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts._ID));//聯絡人id
            String name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));//聯絡人欄位
            Contact contact = new Contact(id, name);
            int hasPhoneNumber = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));//聯絡人有無電話號碼 0:無 1:有
            Log.i("see name", name);
            if (hasPhoneNumber == 1) {
                //取得電話號號碼
                Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)}, null);
                while (cursor2.moveToNext()) {
                    String phone = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    Log.d("see phone", phone);
                    contact.getPhones().add(phone);//加入電話號碼
                }
            }
            contacts.add(contact);
        }

        ContactAdapter contactAdapter = new ContactAdapter(contacts, this);
        recycler_contact.setLayoutManager(new GridLayoutManager(this, 1));
        recycler_contact.setAdapter(contactAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_upload:
                Log.i(TAG, "onOptionsItemSelected: ");
                String userid = getSharedPreferences("atm",MODE_PRIVATE)
                        .getString("USERID",null);
                if (userid != null) {
                    FirebaseDatabase.getInstance().getReference("users")//上傳Contact資料到Firebase
                            .child(userid)
                            .child("contacts")
                            .setValue(contacts);
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContects();
            }
        }
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        private List<Contact> contacts;
        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txName, txPhone;


            public ViewHolder(View v) {
                super(v);
                txName = v.findViewById(android.R.id.text1);
                txPhone = v.findViewById(android.R.id.text2);
            }
        }

        public ContactAdapter(List<Contact> data, Context context) {
            contacts = data;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            View v = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Contact contact = contacts.get(position);
            holder.txName.setText(contact.getName());
            StringBuilder sb = new StringBuilder();
            for (String phone : contact.getPhones()) {
                sb.append(phone);
            }

            holder.txPhone.setText(sb);

        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }
}
