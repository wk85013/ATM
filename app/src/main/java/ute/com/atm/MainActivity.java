package ute.com.atm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 100;
    boolean logon = false;
    private RecyclerView recycler_functions;
    String[] functionNames;
    List<Function> functions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setupFunction();

        recycler_functions = findViewById(R.id.recycler_functions);
        recycler_functions.setLayoutManager(new GridLayoutManager(this, 3));
        FunctionAdapter functionAdapter = new FunctionAdapter(functions, this);
        recycler_functions.setAdapter(functionAdapter);

        setSupportActionBar(toolbar);
        if (!logon) {
            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
            startActivityForResult(intent, REQUEST_LOGIN);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupFunction() {
        functionNames = getResources().getStringArray(R.array.function_array);
        functions.add(new Function(functionNames[0], R.drawable.func_transation));
        functions.add(new Function(functionNames[1], R.drawable.func_balance));
        functions.add(new Function(functionNames[2], R.drawable.func_finance));
        functions.add(new Function(functionNames[3], R.drawable.func_contacts));
        functions.add(new Function(functionNames[4], R.drawable.func_exit));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
        private List<Function> functions;
        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txName;
            ImageView image_icon;


            public ViewHolder(View v) {
                super(v);
                txName = v.findViewById(R.id.tx_name);
                image_icon = v.findViewById(R.id.image_icon);
            }
        }

        public FunctionAdapter(List<Function> data, Context context) {
            functions = data;
            this.context = context;
        }

        @Override
        public FunctionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            View v = mLayoutInflater.inflate(R.layout.item_icon, parent, false);
            return new ViewHolder(v);
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onBindViewHolder(FunctionAdapter.ViewHolder holder, final int position) {
            final Function function = functions.get(position);
            holder.txName.setText(function.getName());
            holder.image_icon.setImageResource(function.getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(function);
                }
            });

        }

        @Override
        public int getItemCount() {
            return functions.size();
        }
    }

    private void itemClicked(Function function) {
        int icon = function.getIcon();
        Intent intent = new Intent();
        switch (icon) {
            case R.drawable.func_transation:
                intent.setClass(MainActivity.this, TransActivity.class);
                startActivity(intent);
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:

                intent.setClass(MainActivity.this, FinanceActivity.class);
                startActivity(intent);
                break;
            case R.drawable.func_contacts:
                intent.setClass(MainActivity.this, ContactActivity.class);
                startActivity(intent);
                break;
            case R.drawable.func_exit:
                finish();
                break;


        }
    }
}
