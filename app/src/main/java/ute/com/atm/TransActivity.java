package ute.com.atm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {

    private static final String TAG = TransActivity.class.getSimpleName();
    private RecyclerView recycler;
    List<Transation> transations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


//        new TransTask().execute("http://atm201605.appspot.com/h");
        //使用OKHttp 連線抓資料 來源 https://square.github.io/okhttp/
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("http://atm201605.appspot.com/h").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);//讀取網頁內容(只能取一次)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        parseJSON(json);
                        parseGSON(json);//使用GSON
                    }
                });

            }
        });


    }

    private void parseGSON(String json) {
        Gson gson = new Gson();
        transations = gson.fromJson(json,
                new TypeToken<ArrayList<Transation>>() {
                }.getType());//使用GSON轉換到list
        TransAdapter adapter = new TransAdapter();
        recycler.setAdapter(adapter);
    }

    private void parseJSON(String json) {
        transations = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                transations.add(new Transation(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TransAdapter adapter = new TransAdapter();
        recycler.setAdapter(adapter);
    }

    public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView dateText, amountText, typeText;


            public ViewHolder(View v) {
                super(v);
                dateText = v.findViewById(R.id.item_date);
                amountText = v.findViewById(R.id.item_amount);
                typeText = v.findViewById(R.id.item_type);
            }

            public void bindTo(Transation tran) {
                dateText.setText(tran.getDate());
                amountText.setText(String.valueOf(tran.getAmount()));
                typeText.setText(String.valueOf(tran.getType()));
            }
        }

        public TransAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.trans_transation, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Transation tran = transations.get(position);
            holder.bindTo(tran);

        }

        @Override
        public int getItemCount() {
            return transations.size();
        }
    }

    public class TransTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = null;
            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                sb = new StringBuilder();
                String line = in.readLine();
                while (line != null) {
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute: " + s);
        }
    }
}
