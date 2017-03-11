package custom.list;

import com.example.search.car.pools.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class database_method {
	private SQLiteDatabase database;

	public String get_data(Context con, String query) {
		String data = "null";
		DataBaseHelper helper = new DataBaseHelper(con);
		database = helper.getWritableDatabase();
		Cursor c = database.rawQuery(query, null);
		try {

			while (c.moveToNext()) {
				data = c.getString(0);

			}
			database.close();
		} catch (Exception e) {

		}
		return data;
	}

	public String[] send_sms(Context con, String query) {
		String[] data;
		DataBaseHelper helper = new DataBaseHelper(con);
		database = helper.getWritableDatabase();
		Cursor c = database.rawQuery(query, null);
		data = new String[c.getColumnCount()];
		try {

			while (c.moveToNext()) {

				for (int i = 0; i < c.getColumnCount(); i++) {
					data[i] = c.getString(i);
				}

			}
			database.close();
		} catch (Exception e) {

		}

		return data;

	}
}
