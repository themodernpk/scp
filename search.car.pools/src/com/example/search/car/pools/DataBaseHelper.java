package com.example.search.car.pools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

    public class DataBaseHelper extends SQLiteOpenHelper{
     
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.search.car.pools/databases/";
     
    private static String DB_NAME ="searchca_carpools";
     
    private SQLiteDatabase myDataBase;
     
    private final Context myContext;
     
    /**
      * Constructor
      * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
      * @param context
      */
    public DataBaseHelper(Context context) {
     
    super(context, DB_NAME, null, 1);
    this.myContext = context;
    }	
     
    /**
      * Creates a empty database on the system and rewrites it with your own database.
      * */
    public void createDataBase() throws IOException{
     
    boolean dbExist = checkDataBase();
     
    if(dbExist){
    //do nothing - database already exist
    }else{
     
    //By calling this method and empty database will be created into the default system path
    //of your application so we are gonna be able to overwrite that database with our database.
    this.getReadableDatabase();
     
    try {
     
    copyDataBase();
     
    } catch (IOException e) {
     
    throw new Error("Error copying database");
     
    }
    }
     
    }
     
    /**
      * Check if the database already exist to avoid re-copying the file each time you open the application.
      * @return true if it exists, false if it doesn't
      */
    private boolean checkDataBase(){
     
    SQLiteDatabase checkDB = null;
     
    try{
    String myPath = DB_PATH + DB_NAME;
    checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
    }catch(SQLiteException e){
     
    //database does't exist yet.
     
    }
     
    if(checkDB != null){
     
    checkDB.close();
     
    }
     
    return checkDB != null ? true : false;
    }
     
    /**
      * Copies your database from your local assets-folder to the just created empty database in the
      * system folder, from where it can be accessed and handled.
      * This is done by transfering bytestream.
      * */
    private void copyDataBase() throws IOException{
     
    //Open your local db as the input stream
    InputStream myInput = myContext.getAssets().open(DB_NAME);
     
    // Path to the just created empty db
    String outFileName = DB_PATH + DB_NAME;
     
    //Open the empty db as the output stream
    OutputStream myOutput = new FileOutputStream(outFileName);
     
    //transfer bytes from the inputfile to the outputfile
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer))>0){
    myOutput.write(buffer, 0, length);
    }
     
    //Close the streams
    myOutput.flush();
    myOutput.close();
    myInput.close();
     
    }
     
    public void openDataBase() throws SQLException{
     
    //Open the database
    String myPath = DB_PATH + DB_NAME;
    myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
    }
     
    @Override
    public synchronized void close() {
     
    if(myDataBase != null)
    myDataBase.close();
     
    super.close();
     
    }
     
    @Override
    public void onCreate(SQLiteDatabase db) {
     
    }
     
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     
    }  


	public void insert_user(String data[]) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="insert into user (user_id,username,password,name,email,gender,phone,dob,profession,state,place,date,lastlogin,ad_notification) values ("+Integer.parseInt(data[0])+",'"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"','"+data[12]+"','"+data[13]+"')";
			db.execSQL(sql);
		
	}

	public void update_user(String data[]) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update user set password='"+data[1]+"',name='"+data[2]+"',email='"+data[3]+"',gender='"+data[4]+"',phone='"+data[5]+"',dob='"+data[6]+"',profession='"+data[7]+"',state='"+data[8]+"',place='"+data[9]+"',date='"+data[10]+"',lastlogin='"+data[11]+"',ad_notification='"+data[12]+"' where username='"+data[0]+"'";
		db.execSQL(sql);
	}
	public void insert_response(String data[]) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="insert into response (response_id,ad_id,ad_title,ad_post_date,sender_id,sender_name,sender_email,sender_phone,receiver_id,msg,sms,date,visibility,read,sync) values ('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"','"+data[12]+"','"+data[13]+"','"+data[14]+"')";
			db.execSQL(sql);
		
	}
	
//	public void update_response(String data[], String user_id) {
//		SQLiteDatabase db=this.getWritableDatabase();
//		String sql="update response set response_id='"+data[0]+"',ad_id='"+data[1]+"',ad_title='"+data[2]+"',ad_post_date='"+data[3]+"',sender_id='"+data[4]+"',sender_name='"+data[5]+"',sender_email='"+data[6]+"',sender_phone='"+data[7]+"',receiver_id='"+data[8]+"',msg='"+data[9]+"',sms='"+data[10]+"',date='"+data[11]+"',visibility='"+data[12]+"',read='"+data[13]+"',sync='"+data[14]+"' where user_id='"+user_id+"'";// values ('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"','"+data[12]+"','"+data[13]+"','"+data[14]+"')";
//		String sql1="insert or replace into response (response_id,ad_id,ad_title,ad_post_date,sender_id,sender_name,sender_email,sender_phone,receiver_id,msg,sms,date,visibility,read,sync) values ('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"','"+data[12]+"','"+data[13]+"','"+data[14]+"')";
//		db.execSQL(sql1);
//		
//	}
	
	public void insert_ad(String data[]) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="insert into ad (id,user_id,type,cat_id,city,company_id,fro_m,t_o,route,seats,departure_time,return_trip,return_time,title,desc,date,hits,enable,sync) values ('"+Integer.parseInt(data[0])+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"','"+data[12]+"','"+data[13]+"','"+data[14]+"','"+data[15]+"','"+data[16]+"','"+data[17]+"','"+data[18]+"')";
			db.execSQL(sql);
	}
	
	public void update_ad(String local_id,String id, String ad_date) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update ad set id='"+Integer.parseInt(id)+"' ,sync='1', date='"+ad_date+"' where local_id='"+local_id+"' ";
			db.execSQL(sql);
		
	}
	public void update_response(String local_id,String id, String date) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update response set response_id='"+id+"', sync='1', date = '"+ date +"' where local_response_id='"+local_id+"' ";
			db.execSQL(sql);
		
	}

	public void update_response_from_server(String id, String visibility) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update response set visibility='"+visibility+"' where response_id='"+id+"' ";
		db.execSQL(sql);
		
	}
	
	public void delete_response(String response_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update response set visibility ='0' and sync='0' where local_response_id='"+response_id+"' ";
			db.execSQL(sql);
		
	}

	// function for updating msgs deleted from server/ website
	public void update_response(String id) {
		SQLiteDatabase db=this.getWritableDatabase();
		String sql="update response set visibility = 0 where response_id = '"+id+"'";
		db.execSQL(sql);
	}

	// function for updating ads deleted from server/ website
		public void update_ad(String id) {
			SQLiteDatabase db=this.getWritableDatabase();
			String sql="update ad set enable = 0 where id = '"+id+"'";
			db.execSQL(sql);
		}
		
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
     
    }