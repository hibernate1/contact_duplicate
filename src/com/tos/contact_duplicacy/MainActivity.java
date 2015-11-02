package com.tos.contact_duplicacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tos.database.DatabaseHandler;
import com.tos.listviewdetail.Contact;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.StaticLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView total_contact_tv;
	
	
	
	
	ArrayList<String> contact_list;
	static public ArrayList<String> groupItem = new ArrayList<String>();
	static public ArrayList<Object> childItem = new ArrayList<Object>();
	static public Map<String, List<String>> info_detail;

	static public ArrayList<String> testChildData_1 = new ArrayList<String>();
	static public ArrayList<String> testChildData_2 = new ArrayList<String>();
	static public ArrayList<String> testChildData_3 = new ArrayList<String>();
	
	static public ArrayList<String> testChildData_1_insert = new ArrayList<String>();
	static public ArrayList<String> testChildData_2_insert = new ArrayList<String>();
	static public ArrayList<String> testChildData_3_insert = new ArrayList<String>();
	
	static public ArrayList<String> testChildData_1_delete = new ArrayList<String>();
	static public ArrayList<String> testChildData_2_delete = new ArrayList<String>();
	static public ArrayList<String> testChildData_3_delete = new ArrayList<String>();
	static public ArrayList<Integer> size_info=new ArrayList<Integer>();
	
	
	
	static public ArrayList<String> testgroupData = new ArrayList<String>();
	static public ArrayList<Integer> testgroupData_image = new ArrayList<Integer>();


	Button marge;
	ListView lv;
	static ProgressDialog progress;
	static Context context;
	static String check_string = "all";
	static ArrayList<String> email;
	static ArrayList<String> number;
	public static List<String> listDataHeader;
	public static HashMap<String, List<String>> listDataChild;
	public static ArrayList<String> ar;
	public static int sim_contact;
	TextView accout_info;
	public static int count_;
	private InterstitialAd interstitial;
	AdRequest adRequest_1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		total_contact_tv = (TextView) findViewById(R.id.cont_no);
		marge = (Button) findViewById(R.id.margeAll);
		sim_contact = 0;
		info_detail = new HashMap<String, List<String>>();
		accout_info = (TextView) findViewById(R.id.account_flag);
		contact_list = new ArrayList<String>();
		number = new ArrayList<String>();

		count_ = getContacts_with_account("all");
		total_contact_tv.setText(getContacts_with_account("all") + "");
		testgroupData_image.add(R.drawable.same);
		testgroupData_image.add(R.drawable.name);
		testgroupData_image.add(R.drawable.number);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		allSIMContact();
		

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-2623420051674563/8512097939");
		adRequest_1 = new AdRequest.Builder()

		// Add a test device to show Test Ads
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("ccd") // Random
																					// Text
				.build();
		interstitial.loadAd(adRequest_1);

		context = this;
		all_account_contact_count(this);
		email = get(this);

		final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, contact_list);
		total_contact_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.dialog);

				ListView lv = (ListView) dialog.findViewById(R.id.list_acc);
				dialog.setCancelable(true);
				dialog.setTitle("All account");

				lv.setAdapter(adapter);
				dialog.show();

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub

						if (position < contact_list.size() - 2 && position >= 0) {
							Toast.makeText(getApplicationContext(), "u clicked  " + email.get(position),
									Toast.LENGTH_SHORT).show();
							check_string = email.get(position);
							accout_info.setText(check_string);
							total_contact_tv.setText(number.get(position));
							// marge.setText("Check "+email.get(position));

						}
						if (position == contact_list.size() - 2) {
							// this space is for sim
							check_string = "Devices";
							accout_info.setText(check_string);
							total_contact_tv.setText(number.get(position));
							// marge.setText("Check "+email.get(position));
						}
						if (position == contact_list.size() - 1) {
							check_string = "all";
							accout_info.setText("All Account");
							total_contact_tv.setText(getContacts_with_account("all") + "");
						}

						// total_contact_tv.setText(number.get(position));
						dialog.dismiss();

					}

				});

			}
		});

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		ar = new ArrayList<String>();
		int device_contact = getContacts_device(email) - sim_contact;
		number.add(device_contact + sim_contact + "");
		contact_list.add("Device + Sim Contacts   " + device_contact + " + " + sim_contact);
		contact_list.add("Total Contact(s)  " + getContacts_with_account("all"));

		marge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.testgroupData.clear();
				MainActivity.testChildData_1.clear();
				MainActivity.testChildData_2.clear();
				MainActivity.testChildData_3.clear();
				MainActivity.testChildData_1_insert.clear();
				MainActivity.testChildData_2_insert.clear();
				MainActivity.testChildData_3_insert.clear();
				
				MainActivity.testChildData_1_delete.clear();
				MainActivity.testChildData_2_delete.clear();
				MainActivity.testChildData_3_delete.clear();
				MainActivity.size_info.clear();
				
				/// check_string = "all";
				new DownloadMusicfromInternet().execute("test");
			}
		});
		AdView adView = (AdView) findViewById(R.id.adView_game);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

	}

	private void all_account_contact_count(Context c) {
		// TODO Auto-generated method stub
		ArrayList<String> email = get(c);
		for (int i = 0; i < email.size(); i++) {
			contact_list.add(email.get(i) + "    " + getContacts_with_account(email.get(i).toString()));
			number.add(getContacts_with_account(email.get(i).toString()) + "");
		}

		Log.e("XXXXX", getContacts_with_account("") + "");
	}

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("Lok", "XXXXXXXXXXXXXXXXXXXXXXXXXXXS");
		interstitial.loadAd(adRequest_1);

		
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				displayInterstitial();
			}
		});
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	

	public ArrayList<String> get(Context c) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		ArrayList<String> email = new ArrayList<String>();
		Account[] accounts = AccountManager.get(c).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				String possibleEmail = account.name;
				if (!email.contains(account.name))
					email.add(account.name);

				Log.e("Email", possibleEmail);
				// ...
			}
		}
		return email;
	}

	public int getContacts_with_account(String ch) {

		final String[] projection = new String[] { RawContacts.CONTACT_ID, RawContacts.DELETED };
		Cursor rawContacts = null;
		int total_contact;
		if (ch.equals("all")) {
			rawContacts = getContentResolver().query(RawContacts.CONTENT_URI, projection, null, null, null);
		} else {
			rawContacts = getContentResolver().query(RawContacts.CONTENT_URI, projection,
					RawContacts.ACCOUNT_NAME + "=" + "'" + ch + "'", null, null);
		}
		try {
			total_contact = rawContacts.getCount();

		} finally {
			rawContacts.close();
		}

		return total_contact;

	}
	
	
	

	public int getContacts_device(ArrayList<String> ch) {
		String res = "";

		for (int i = 0; i < ch.size(); i++) {
			if (i == ch.size() - 1) {
				res = res + RawContacts.ACCOUNT_NAME + "!=" + "'" + ch.get(i) + "'";
			} else {
				res = res + RawContacts.ACCOUNT_NAME + "!=" + "'" + ch.get(i) + "'" + " AND ";
			}
		}

		final String[] projection = new String[] { RawContacts.CONTACT_ID, RawContacts.DELETED };
		Cursor rawContacts = null;
		int total_contact;

		rawContacts = getContentResolver().query(RawContacts.CONTENT_URI, projection, res, null, null);
		int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);
		ArrayList<Integer> ar = new ArrayList<Integer>();
		/*
		 * try { if (rawContacts.moveToFirst()) { while
		 * (!rawContacts.isAfterLast()) { int contactId =
		 * rawContacts.getInt(contactIdColumnIndex); final boolean deleted =
		 * (rawContacts.getInt(deletedColumnIndex) == 1);
		 * 
		 * if (!deleted) { Log.e("Name", getName(contactId)); }
		 * rawContacts.moveToNext(); } } } finally {
		 * 
		 * }
		 */

		try {
			total_contact = rawContacts.getCount();

		} finally {
			rawContacts.close();
		}

		return total_contact;

	}

	private void allSIMContact() {
		Cursor cursorSim = null;
		try {
			String m_simPhonename = null;
			String m_simphoneNo = null;

			Uri simUri = Uri.parse("content://icc/adn");
			cursorSim = MainActivity.this.getContentResolver().query(simUri, null, null, null, null);

			Log.i("PhoneContact/Sim  ", "total  : " + cursorSim.getCount());
			// contact_list.add("PhoneContact/Sim " + cursorSim.getCount());
			sim_contact = cursorSim.getCount();

			/*
			 * while (cursorSim.moveToNext()) { m_simPhonename
			 * =cursorSim.getString(cursorSim.getColumnIndex("name"));
			 * m_simphoneNo =
			 * cursorSim.getString(cursorSim.getColumnIndex("number"));
			 * m_simphoneNo.replaceAll("\\D",""); m_simphoneNo.replaceAll("&",
			 * ""); m_simPhonename=m_simPhonename.replace("|","");
			 * 
			 * Log.i("PhoneContact", "name: "+m_simPhonename+" phone: "
			 * +m_simphoneNo); } } catch(Exception e) { e.printStackTrace(); }
			 */
			// }
		} finally {
			cursorSim.close();
		}
	}

	private String getName(int contactId) {
		String name = "";
		final String[] projection = new String[] { Contacts.DISPLAY_NAME };

		final Cursor contact = getContentResolver().query(Contacts.CONTENT_URI, projection, Contacts._ID + "=?",
				new String[] { String.valueOf(contactId) }, null);
		try {

			if (contact.moveToFirst()) {
				name = contact.getString(contact.getColumnIndex(Contacts.DISPLAY_NAME));
				if (name == null || name.trim().equals(""))
					name = "unknown";
				// contact.close();
			}
			contact.close();
		} finally {
			// this gets called even if there is an exception somewhere above
			if (contact != null)
				contact.close();
		}
		
		return name;

	}

	
	/*
	 * private static String getName(int contactId) { String name = ""; final
	 * String[] projection = new String[] { Contacts.DISPLAY_NAME };
	 * 
	 * final Cursor contact =
	 * getApplicationContext().getContentResolver().query(Contacts.CONTENT_URI,
	 * projection, Contacts._ID + "=?", new String[] { String.valueOf(contactId)
	 * }, null); try { if (contact.moveToFirst()) { name =
	 * contact.getString(contact.getColumnIndex(Contacts.DISPLAY_NAME)); //
	 * contact.close(); } contact.close(); } finally { // this gets called even
	 * if there is an exception somewhere above if (contact != null)
	 * contact.close(); } return name;
	 * 
	 * }
	 */

	private static String getEmail(int contactId) {
		String emailStr = "";
		final String[] projection = new String[] { Email.DATA, // use
				// Email.ADDRESS
				// for API-Level
				// 11+
				Email.TYPE };

		final Cursor email = context.getApplicationContext().getContentResolver().query(Email.CONTENT_URI, projection,
				Data.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) }, null);
		try {
			if (email.moveToFirst()) {
				final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);

				while (!email.isAfterLast()) {
					emailStr = emailStr + email.getString(contactEmailColumnIndex);
					email.moveToNext();
				}
			}
			email.close();
		} finally {
			// this gets called even if there is an exception somewhere above
			if (email != null)
				email.close();
		}

		return emailStr;

	}

	private static String getPhoneNumber(int contactId) {

		String phoneNumber = "";
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
		final Cursor phone = context.getApplicationContext().getContentResolver().query(Phone.CONTENT_URI, projection,
				Data.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) }, null);
		try {
			if (phone.moveToFirst()) {
				final int contactNumberColumnIndex = phone.getColumnIndex(Phone.DATA);

				// while (!phone.isAfterLast()) {
				phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex);
				// phone.moveToNext();
				phoneNumber.replace(" ", "");
				phoneNumber.replace("-", "");
				// }

			}
			phone.close();
		} finally {
			// this gets called even if there is an exception somewhere above
			if (phone != null)
				phone.close();
		}
		return phoneNumber;
	}

	private static String getAccoutName(int contactId) {

		String phoneNumber = "";
		final String[] projection = new String[] { RawContacts.ACCOUNT_NAME };
		final Cursor phone = context.getApplicationContext().getContentResolver().query(RawContacts.CONTENT_URI,
				projection, Data.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) }, null);
		try {
			if (phone.moveToFirst()) {
				final int contactNumberColumnIndex = phone.getColumnIndex(RawContacts.ACCOUNT_NAME);

					phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex);
					phone.moveToNext();
					System.out.println("Account" + phoneNumber);
				

			}
			phone.close();
		} finally {
			// this gets called even if there is an exception somewhere above
			if (phone != null)
				phone.close();
		}
		return phoneNumber;
	}

	public static String getDuplicateCount() {

		DatabaseHandler db = new DatabaseHandler(context);
		String number = db.getNumberCount();

		return number;
	}

	private String getDuplicateNumberNotName() {
		DatabaseHandler db = new DatabaseHandler(this);
		String number = db.getDuplicateNumberDifferentName();
		return null;
	}

	public static void getDuplicateNameNotNumber() {
		// TODO Auto-generated method stub

		DatabaseHandler db = new DatabaseHandler(context);
		String number = db.getDuplicateNameNotNumber();
		// ar.add(number);
		// return number;

	}

	public void openDialog() {
		final Dialog dialog = new Dialog(MainActivity.this); // context, this
																// etc.
		dialog.setContentView(R.layout.dialog_demo);
		dialog.setTitle("Want to Merge?");
		dialog.show();
		Button dialogButton = (Button) dialog.findViewById(R.id.dialog_ok);
		Button dialogButton_cancle = (Button) dialog.findViewById(R.id.dialog_cancel);
		dialogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				new DownloadMusicfromInternet().execute("test");
				// Toast.makeText(getApplicationContext(), "You clicked on OK",
				// Toast.LENGTH_SHORT).show();
			}
		});

		dialogButton_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

	}

	public void displayInterstitial() {
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				displayInterstitial();
			}
		});
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	class DownloadMusicfromInternet extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();

			progress = new ProgressDialog(MainActivity.this);
			progress.setMessage("Checking Duplicity in Selected Account(s)");

			progress.setCancelable(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// SaveAndCheck sac=new SaveAndCheck(getApplicationContext());
			System.out.println("comes");
			ar.clear();
			getContacts(check_string);
		
			getDuplicateCount();
			//publishProgress(values);

			getDuplicateNumberNotName();
			getDuplicateNameNotNumber();
			
			

			return null;
		}
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		     
            	progress.setMessage("Checking Duplicity in Selected Account(s)\n"+values[0]);
            
		}

		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress.dismiss();
			Intent in = new Intent(getApplicationContext(), ShowResult.class);
			startActivity(in);

			// setting list adapter

			// listAdapter = new ExpandableListAdapter(MainActivity.this,
			// listDataHeader, listDataChild);
			// expListView.setAdapter(listAdapter);
		}
		
		
		public void getContacts(String check_id) {

			// if()
			String codition = "";
			if (check_id.contains("@")) {
				codition = RawContacts.ACCOUNT_NAME + "=" + "'" + check_id + "'";
			} else if (check_id.equals("Devices")) {
				for (int i = 0; i < email.size(); i++) {
					if (i == email.size() - 1) {
						codition = codition + RawContacts.ACCOUNT_NAME + "!=" + "'" + email.get(i) + "'";
					} else {
						codition = codition + RawContacts.ACCOUNT_NAME + "!=" + "'" + email.get(i) + "'" + " AND ";
					}
				}
			} else if (check_id.equals("all"))
				codition = null;

			// ArrayList<HashMap<String, Object>> contacts = new
			// ArrayList<HashMap<String, Object>>();
			
			DatabaseHandler dbb = new DatabaseHandler(MainActivity.this);
			SQLiteDatabase db = dbb.getDb();
			dbb.onCreate(db);
			final String[] projection = new String[] { RawContacts.CONTACT_ID, RawContacts.DELETED };

			@SuppressWarnings("deprecation")
			final Cursor rawContacts = getContentResolver().query(RawContacts.CONTENT_URI, projection, codition, null,
					null);

			int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
			final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);
			int c = 0;
			ArrayList<Integer> ar = new ArrayList<Integer>();
			try {
				if (rawContacts.moveToFirst()) {
					while (!rawContacts.isAfterLast()) {
						int contactId = rawContacts.getInt(contactIdColumnIndex);
						final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

						if (!deleted) {
							/*
							 * HashMap<String, Object> contactInfo = new
							 * HashMap<String, Object>() { { put("contactId", "");
							 * put("name", ""); put("email", ""); put("address",
							 * ""); put("photo", ""); put("phone", ""); } };
							 * contactInfo.put("contactId", "" + contactId);
							 * contactInfo.put("name", getName(contactId));
							 * contactInfo.put("email", getEmail(contactId));
							 * //contactInfo.put("photo", getPhoto(contactId) !=
							 * null ? getPhoto(contactId) : "");
							 * //contactInfo.put("address", getAddress(contactId));
							 * contactInfo.put("phone", getPhoneNumber(contactId));
							 * contactInfo.put("isChecked", "false");
							 * contacts.add(contactInfo);
							 */
							if (ar.contains(contactId) == false) {
								ar.add(contactId);
								String name = getName(contactId).replaceAll("'", "_");
								name = getName(contactId).replaceAll("-", "_");
								String phone = getPhoneNumber(contactId).replace("-", "");
								phone = phone.replace(" ", "");
								phone = phone.replace("  ", "");
								Contact cn = new Contact(contactId, name, phone, getEmail(contactId),
										getAccoutName(contactId), false);

								Log.e("Name:", name);
								//progress.setMessage(name);
								publishProgress(name);
								String check = name;
								//progress.setMessage(name);
								
								// DatabaseHandler dbb = new DatabaseHandler(this);
								// System.out.println(
								// contactId + getName(contactId) +
								// getEmail(contactId));
								// +
								// "xx" + getPhoneNumber(contactId));
								System.out.println(ar.size() + "FFFFFF");
								dbb.addContact(cn);
								
								
								
							} else {
								// System.out.println(contactId+"DDDDD");
								System.out.println(ar.size() + "DDDDD");
							}
							c++;
						}
						rawContacts.moveToNext();
					}
				}
			} finally {
				try {
					if (rawContacts != null && !rawContacts.isClosed())
						rawContacts.close();
					if (db.isOpen())
						db.close();
				} catch (Exception ex) {
				}

			}

			rawContacts.close();
			// rawContacts.
			System.out.println("Total number of contact:" + c);
			// return contacts;
		}

	}

}
