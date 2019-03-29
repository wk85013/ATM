package ute.com.atm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class ExpenseHelper extends SQLiteOpenHelper {
    public ExpenseHelper(Context context) {
        super(context, "atm.db", null, 1);

    }

    private ExpenseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expense (_id INTEGER PRIMARY KEY NOT NULL," +
                "cdate VARCHAR NOT NULL," +
                "info VARCHAR ," +
                "amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
