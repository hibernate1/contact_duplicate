package com.tos.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tos.contact_duplicacy.MainActivity;
import com.tos.contact_duplicacy.MainActivity;
import com.tos.contact_duplicacy.ShowResult;
import com.tos.listviewdetail.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	static HashMap<Integer, String> cache = new HashMap<Integer, String>();

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";

	// Contacts Table Columns names
	private static final String _ID = "_id";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_ACCOUNT = "account";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// 3rd argument to be passed is CursorFactory instance

	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + _ID + " INTEGER PRIMARY KEY," + KEY_ID
				+ " INTEGER," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT," + KEY_ACCOUNT
				+ " TEXT" + ")";
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	public SQLiteDatabase getDb() {
		return this.getWritableDatabase();
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, contact.getID());
		values.put(KEY_NAME, contact.getName()); // Contact Name
		values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
		values.put(KEY_EMAIL, contact.getEmail());// email
		values.put(KEY_ACCOUNT, contact.get_account());

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		// Log.e("TAG","Inserted");
		// 2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection
	}

	// Getting single contact
	/*
	 * Contact getContact(int id) { SQLiteDatabase db =
	 * this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME,
	 * KEY_PH_NO }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null,
	 * null, null, null); if (cursor != null) cursor.moveToFirst();
	 * 
	 * Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
	 * cursor.getString(1), cursor.getString(2)); // return contact return
	 * contact; }
	 * 
	 * // Getting All Contacts public List<Contact> getAllContacts() {
	 * List<Contact> contactList = new ArrayList<Contact>(); // Select All Query
	 * String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
	 * 
	 * SQLiteDatabase db = this.getWritableDatabase(); Cursor cursor =
	 * db.rawQuery(selectQuery, null);
	 * 
	 * // looping through all rows and adding to list if (cursor.moveToFirst())
	 * { do { Contact contact = new Contact();
	 * contact.setID(Integer.parseInt(cursor.getString(0)));
	 * contact.setName(cursor.getString(1));
	 * contact.setPhoneNumber(cursor.getString(2)); // Adding contact to list
	 * contactList.add(contact); } while (cursor.moveToNext()); }
	 * 
	 * // return contact list return contactList; }
	 * 
	 * // Updating single contact public int updateContact(Contact contact) {
	 * SQLiteDatabase db = this.getWritableDatabase();
	 * 
	 * ContentValues values = new ContentValues(); values.put(KEY_NAME,
	 * contact.getName()); values.put(KEY_PH_NO, contact.getPhoneNumber());
	 * 
	 * // updating row return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
	 * new String[] { String.valueOf(contact.getID()) }); }
	 * 
	 * // Deleting single contact public void deleteContact(Contact contact) {
	 * SQLiteDatabase db = this.getWritableDatabase(); db.delete(TABLE_CONTACTS,
	 * KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	 * db.close(); }
	 */

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int c = cursor.getCount();
		// cursor.close();

		// return count
		return c;
	}

	/// find dub email

	public String getDubEmail() {
		String number = "";
		String countQuery = "SELECT email, COUNT(*) TotalCount FROM contacts " + "GROUP BY email "
				+ "HAVING COUNT(*) > 1 " + "ORDER BY COUNT(*) DESC ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor.moveToFirst()) {
			System.out.println("XX" + "comes");
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");

			while (!cursor.isAfterLast()) {
				if (!cursor.getString(cursor.getColumnIndex("email")).trim().equals("")) {
					number = "Email:" + cursor.getString(cursor.getColumnIndex("email")) + "\n"
							+ cursor.getString(cursor.getColumnIndex("TotalCount")) + " times";
					MainActivity.ar.add(number);
					Log.e("XXX", number);
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();

		return number;
	}

	// find dub phone_number

	public String getNumberCount() {
		List<String> action = new ArrayList<String>();
		action.clear();
		int ii = 0;

		// action.add("Delete All");
		// action.add("Delete All except one");
		String number = "";
		MainActivity.testgroupData.add("Duplicate Contact");
		
		
		String countQuery = "SELECT name,phone_number, COUNT(*) TotalCount FROM contacts "
				+ "GROUP BY phone_number,name " + "HAVING COUNT(*) > 1 " + "ORDER BY COUNT(*) DESC ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor.moveToFirst()) {
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");
			int i = 0;

			while (!cursor.isAfterLast()) {
				if (!cursor.getString(cursor.getColumnIndex("phone_number")).trim().equals("")) {
					ii++;
					number = cursor.getString(cursor.getColumnIndex("name")) + "\n"
							+ cursor.getString(cursor.getColumnIndex("phone_number")).trim() + "\n"
							+ cursor.getString(cursor.getColumnIndex("TotalCount"));
					String number_1= cursor.getString(cursor.getColumnIndex("name")) + "\n"
							+ cursor.getString(cursor.getColumnIndex("phone_number")).trim() + "\n";
							
					// String number1 = "Number:" +
					// cursor.getString(cursor.getColumnIndex("name")) + "\n"
					// + cursor.getString(cursor.getColumnIndex("TotalCount")) +
					// " times";
					action.add(number + "-" + "times \n");
					// MainActivity.testChildData_1.add(number + "-" + "times
					// \n");
					MainActivity.testChildData_1.add(number + "-" + "times");
					MainActivity.testChildData_1_insert.add(number_1);
					MainActivity.testChildData_1_delete.add(cursor.getString(cursor.getColumnIndex("TotalCount"))+"-"+"times");
					
					Log.e("XXX", number);
				}
				// MainActivity.listDataChild.put(MainActivity.listDataHeader.get(i),
				// action);

				cursor.moveToNext();
			}

		}

		MainActivity.listDataHeader.add("Duplicate Contact  " + MainActivity.testChildData_1.size());
		MainActivity.size_info.add(MainActivity.testChildData_1.size());
		cursor.close();
		System.out.println("KKKKKKKKKKKKKKK" + action.size());
		if (action.size() == 0)
			action.add("No Duplicate Contact-");
		db.close();
		MainActivity.listDataChild.put(MainActivity.listDataHeader.get(0), action);
		MainActivity.info_detail.put(MainActivity.listDataHeader.get(0), action);

		return number;
	}

	public ArrayList<Integer> get_list(String name, String phn) {

		System.out.println("CCCCC" + name + phn);

		ArrayList<Integer> id = new ArrayList<Integer>();
		// ArrayList<String>number=new ArrayList<String>();
		String sql = "SELECT id from  contacts where name=" + "'" + name + "'" + " AND phone_number=" + "'" + phn + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");

			while (!cursor.isAfterLast()) {
				Log.e("XXX", cursor.getInt(cursor.getColumnIndex("id")) + "");
				id.add(cursor.getInt(cursor.getColumnIndex("id")));
				// number.add(cursor.getString(cursor.getColumnIndex("phone_number")));

				cursor.moveToNext();
			}
		}

		cursor.close();
		db.close();

		return id;

	}

	//// select distinct type,highest_unlocked_level from game_type o where
	//// highest_unlocked_level =(select highest_unlocked_level from game_type)

	public String getDuplicateNumberDifferentName() {
		List<String> action = new ArrayList<String>();
		int ii = 0;
		ArrayList<String> check_co = new ArrayList<String>();
		ArrayList<String> co_name_id = new ArrayList<String>();
		HashMap<String, String> cache_19 = new HashMap<String, String>();
		MainActivity.testgroupData.add("Duplicate Name");
		// action.add("Delete All");
		// action.add("Delete All except one");
		System.out.println("XXXXXX here");
		String number = "";
		String countQuery = "SELECT Distinct name,phone_number FROM contacts  where phone_number in"
				+ "( select phone_number from contacts group by phone_number having count(*)>1) ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		System.out.println("XXXXXX here" + cursor.getColumnCount());

		if (cursor.moveToFirst()) {
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");
			// int i = 0;

			while (!cursor.isAfterLast()) {

				if (!cursor.getString(cursor.getColumnIndex("phone_number")).trim().equals("")) {
					String ch = cursor.getString(cursor.getColumnIndex("phone_number")).trim();
					if (check_co.contains(ch)) {
						ii++;
						// check_co.add(ch);

						System.out.println("dub Cont:" + ch);
						// int k = check_co.indexOf(ch);

						String c = cache_19.get(ch);
						String res = c + "\n" + cursor.getString(cursor.getColumnIndex("name")) + " "
								+ "//will be deleted";

						cache_19.put(ch, res);

						System.out.println("dub Cont_id:" + cache.get(ch));

						if (!co_name_id.contains(ch))
							co_name_id.add(ch);

					}

					else {
						check_co.add(ch);
						// int k = check_co.indexOf(ch);
						cache_19.put(ch,
								ch + "\n" + cursor.getString(cursor.getColumnIndex("name")) + " //will remain");

					}
				}
				cursor.moveToNext();

			}
			db.close();
		}
		
		cursor.close();
		for (int i = 0; i < co_name_id.size(); i++) {
			action.add(cache_19.get(co_name_id.get(i)));
			MainActivity.testChildData_2.add(cache_19.get(co_name_id.get(i)));
			
			if(!cache_19.get(co_name_id.get(i)).isEmpty())
			{
				String rech="";
				String re_ch[]=cache_19.get(co_name_id.get(i)).split("\n");
				MainActivity.testChildData_2_insert.add(re_ch[0]+"\n"+re_ch[1]);
				for(int ij=2;ij<re_ch.length;ij++)
				{
					rech=rech+"\n"+re_ch[ij];
				}
				MainActivity.testChildData_2_delete.add(rech);
				
			}
			Log.e("Problem", cache_19.get(co_name_id.get(i)));
		}
		if (action.size() == 0) {
			action.add("No Duplicate Number found");
		}
		db.close();
		MainActivity.listDataHeader.add("Name Different & Number Same   " + ii);
		
		MainActivity.size_info.add(MainActivity.testChildData_2.size());
		MainActivity.listDataChild.put(MainActivity.listDataHeader.get(1), action);
		MainActivity.info_detail.put(MainActivity.listDataHeader.get(1), action);
		return number;
	}

	public int getPriority(String name2, String phone_number2, ArrayList<Integer> id) {
		// TODO Auto-generated method stub
		// ArrayList<Integer>id=new ArrayList<Integer>();
		// ArrayList<String>number=new ArrayList<String>();
		System.out.println("CCCCDDDDD" + name2 + " " + phone_number2 + "  " + id.size());
		SQLiteDatabase db = this.getReadableDatabase();
		for (int ii = 0; ii < id.size(); ii++) {
			String sql = "SELECT id from  contacts where name=" + "'" + name2 + "'" + " AND phone_number=" + "'"
					+ phone_number2 + "'" + "  AND id=" + id.get(ii) + " AND email like '%@%'";

			// int i=0;
			Cursor cursor = db.rawQuery(sql, null);
			System.out.println("Co" + cursor.getCount());
			if (cursor.moveToFirst()) {
				// final int contactEmailColumnIndex =

				int ch = cursor.getInt(cursor.getColumnIndex("id"));
				cursor.close();
				return ch;

				// cursor.moveToNext();

			}

		}
		/*
		 * String sql = "SELECT id from  contacts where name=" + "'" + name2 +
		 * "'" + " AND phone_number=" + "'" + phone_number2 + "'";
		 * 
		 * Cursor cursor1 = db.rawQuery(sql, null); if (cursor1.moveToFirst()) {
		 * int ch = cursor1.getInt(cursor1.getColumnIndex("id"));
		 * cursor1.close(); return ch; }
		 */
		// int ch = 0;
		db.close();
		return id.get(0);
	}

	public ArrayList<String> getEmail(String phone, ArrayList<String> name_test) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> email = new ArrayList<String>();

		for (int i = 0; i < name_test.size(); i++) {
			String sql = "Select email from contacts where phone_number=" + "'" + phone + "'" + " AND name=" + "'"
					+ name_test.get(i) + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
				email.add(cursor.getString(cursor.getColumnIndex("email")));
			}
			cursor.close();

		}
		db.close();
		System.out.println("email:" + email.size());
		return email;
	}

	public ArrayList<String> getEmail_name_number(String phone, String name) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> email = new ArrayList<String>();

		String sql = "Select email,account from contacts where phone_number=" + "'" + phone + "'" + " AND name=" + "'"
				+ name + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
			email.add(cursor.getString(cursor.getColumnIndex("email")) + "\n"
					+ cursor.getString(cursor.getColumnIndex("account")));
		}
		cursor.close();

		db.close();
		System.out.println("email:" + email.size());
		return email;
	}

	public ArrayList<Integer> delete(ArrayList<String> name_test, String final_number) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Integer> co_id = new ArrayList<Integer>();

		for (int i = 0; i < name_test.size(); i++) {
			System.out.println("delete" + name_test.get(i));
			String sql = "select id from contacts where phone_number=" + "'" + final_number + "'" + " AND name=" + "'"
					+ name_test.get(i) + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				co_id.add(cursor.getInt(cursor.getColumnIndex("id")));
				cursor.close();
			}

		}
		db.close();
		return co_id;

	}

	public String getDuplicateNameNotNumber() {
		// TODO Auto-generated method stub
		List<String> action = new ArrayList<String>();
		HashMap<Integer, String> cache_1 = new HashMap<Integer, String>();
		HashMap<String, String> cache_11 = new HashMap<String, String>();

		ArrayList<String> co_name_id = new ArrayList<String>();
		ArrayList<String> check_co = new ArrayList<String>();
		ArrayList<Integer> co_name = new ArrayList<Integer>();
		MainActivity.testgroupData.add("Duplicate Number");
		// action.add("Delete All");
		// action.add("Delete All except one");
		// System.out.println("XXXXXX here");
		String number = "";
		int ii = 0;
		String countQuery = "SELECT Distinct name,phone_number  FROM contacts  where name in"
				+ "( select name from contacts group by name having count(*)>1) ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// System.out.println("XXXXXX here"+cursor.getColumnCount());

		if (cursor.moveToFirst()) {
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");

			while (!cursor.isAfterLast()) {

				if (!cursor.getString(cursor.getColumnIndex("name")).trim().equals("")
						& !cursor.getString(cursor.getColumnIndex("phone_number")).trim().equals("")) {
					String ch = cursor.getString(cursor.getColumnIndex("name")).trim();
					if (check_co.contains(ch)) {
						ii++;
						String chh = cache_11.get(ch);
						String cf = chh + "\n" + cursor.getString(cursor.getColumnIndex("phone_number"));
						cache_11.put(ch, cf);
						if (!co_name_id.contains(ch))
							co_name_id.add(ch);

						// int
						// c=co_name.indexOf(cursor.getColumnIndex("phone_number"));
						// co_name.add(c, object);
					}

					else {
						check_co.add(ch);
						// int k=check_co.indexOf(ch);
						cache_11.put(ch, ch + "\n" + cursor.getString(cursor.getColumnIndex("phone_number")));

					}
				}
				cursor.moveToNext();

			}
		}

		cursor.close();
		db.close();
		for (int i = 0; i < co_name_id.size(); i++) {
			action.add(cache_11.get(co_name_id.get(i)));
			MainActivity.testChildData_3_insert.add(cache_11.get(co_name_id.get(i)));
			MainActivity.testChildData_3
					.add(cache_11.get(co_name_id.get(i)) + "  will be marged under " + co_name_id.get(i));
			//MainActivity.testChildData_3_insert.add(cache_11.get(co_name_id.get(i)));
			MainActivity.testChildData_3_delete.add("will merge under "+co_name_id.get(i));
		}
		if (action.size() == 0) {
			action.add("No Duplicate Number found");
		}
		MainActivity.listDataHeader.add("Name Same & Number Different  " + ii);
		
		MainActivity.size_info.add(MainActivity.testChildData_3.size());
		MainActivity.listDataChild.put(MainActivity.listDataHeader.get(2), action);
		MainActivity.info_detail.put(MainActivity.listDataHeader.get(2), action);

		return number;

		// return number;

		// return null;
	}

	public ArrayList<Integer> delete_marge(String name, ArrayList<String> final_number) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Integer> co_id = new ArrayList<Integer>();

		for (int i = 0; i < final_number.size(); i++) {
			// System.out.println("delete"+name_test.get(i));
			String sql = "select id from contacts where phone_number=" + "'" + final_number.get(i) + "'" + " AND name="
					+ "'" + name + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				co_id.add(cursor.getInt(cursor.getColumnIndex("id")));
				cursor.close();
			}

		}
		db.close();
		return co_id;
	}

	public ArrayList<String> getEmail_for_fixed_name(String name2, ArrayList<String> number_refine) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> email = new ArrayList<String>();

		for (int i = 0; i < number_refine.size(); i++) {
			String sql = "Select email from contacts where phone_number=" + "'" + number_refine.get(i) + ";'"
					+ " AND name=" + "'" + name2 + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
				email.add(cursor.getString(cursor.getColumnIndex("email")));
			}
			cursor.close();

		}
		db.close();
		System.out.println("email:" + email.size());
		return email;
	}
	
	
	
	public int get_id_from_name_number(String name,String number)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		int _id=0;
		// ArrayList<String> email = new ArrayList<String>();

		String sql = "Select id from contacts where phone_number=" + "'" + number + "'" + " AND name="
				+ "'" + name +"'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			// final int contactEmailColumnIndex =
			// dub.getColumnIndex("phone_number");

				//System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
				_id=cursor.getInt(cursor.getColumnIndex("id"));
				
			}
		

		cursor.close();
		db.close();
		return _id;
		
		
	}

	public ArrayList<String> get_account_name_number(String name, String number, String email) {
		ArrayList<String> email_ = new ArrayList<String>();
		if (email.trim().equals("")) {
			SQLiteDatabase db = this.getReadableDatabase();
			// ArrayList<String> email = new ArrayList<String>();

			String sql = "Select account from contacts where phone_number=" + "'" + number + "'" + " AND name="
					+ "'" + name +"'"+ " AND email !=" + "'" + email + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				// final int contactEmailColumnIndex =
				// dub.getColumnIndex("phone_number");

				while (!cursor.isAfterLast()) {
					//System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
					email_.add(cursor.getString(cursor.getColumnIndex("account")));
					cursor.moveToNext();
				}
			

			cursor.close();
			db.close();
			return email_;
			}
		}

			
		 else {
			 
			 
			 SQLiteDatabase db = this.getReadableDatabase();
				// ArrayList<String> email = new ArrayList<String>();

				String sql = "Select account from contacts where phone_number=" + "'" + number + "'" + " AND name="
						+ "'" + name +"'";
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					// final int contactEmailColumnIndex =
					// dub.getColumnIndex("phone_number");
					cursor.moveToNext();
					while (!cursor.isAfterLast()) {
						//System.out.println("email:" + cursor.getString(cursor.getColumnIndex("email")));
						email_.add(cursor.getString(cursor.getColumnIndex("account")));
						cursor.moveToNext();
					}
				

				cursor.close();
				db.close();
				return email_;
				}


		}
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> get_account_of_deleted_name_number(ArrayList<String> name_list, String phone) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> email = new ArrayList<String>();
		Log.e("Prob","comes"+phone);

		for (int i = 0; i < name_list.size(); i++) {
			String sql = "Select account from contacts where phone_number=" + "'" + phone + "'" + " AND name="
					+ "'" + name_list.get(i) +"'";
			Cursor cursor = db.rawQuery(sql, null);
		//	Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				System.out.println("email:" + cursor.getString(cursor.getColumnIndex("account")));
				email.add(cursor.getString(cursor.getColumnIndex("account")));
				Log.e("Prob",cursor.getString(cursor.getColumnIndex("account")));
			}
			cursor.close();

		}
		db.close();
		System.out.println("email:" + email.size());
		return email;
	}
}
