package ute.com.atm;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private EditText edit_amount;
    private EditText edit_info;
    private EditText edit_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edit_date = findViewById(R.id.edit_Date);
        edit_info = findViewById(R.id.edit_info);
        edit_amount = findViewById(R.id.edit_Amount);
    }

    public void add(View view) {
        String date = edit_date.getText().toString();
        String info = edit_info.getText().toString();
        int amount = Integer.valueOf(edit_amount.getText().toString());
        ExpenseHelper helper = new ExpenseHelper(this);
        ContentValues values = new ContentValues();
        values.put("cdate", date);
        values.put("info", info);
        values.put("amount", amount);
        long id = helper.getWritableDatabase().insert("expense", null, values);
        Log.i("see id", "add: " + String.valueOf(id));
        if (id > -1) {
            Toast.makeText(this, "Add Success", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Add Failed", Toast.LENGTH_SHORT).show();
    }
}
