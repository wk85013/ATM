package ute.com.atm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FinanceActivity extends AppCompatActivity {

    private ExpenseAdapter adapter;
    private RecyclerView recycler;
    private ExpenseHelper helper;

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = helper.getReadableDatabase().query("expense",
                null, null, null, null, null, null);
        adapter = new ExpenseAdapter(cursor);
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FinanceActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        helper = new ExpenseHelper(this);
        Cursor cursor = helper.getReadableDatabase().query("expense",
                null, null, null, null, null, null);
        adapter = new ExpenseAdapter(cursor);
        recycler.setAdapter(adapter);
    }

    public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
        Cursor cursor;

        ExpenseAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_date, item_info, item_amount;


            ViewHolder(View v) {
                super(v);
                item_date = v.findViewById(R.id.item_date);
                item_info = v.findViewById(R.id.item_info);
                item_amount = v.findViewById(R.id.item_amount);
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.expense_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            cursor.moveToPosition(position);
            String date = cursor.getString(cursor.getColumnIndex("cdate"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            holder.item_date.setText(date);
            holder.item_info.setText(info);
            holder.item_amount.setText(String.valueOf(amount));

        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }
    }

}
