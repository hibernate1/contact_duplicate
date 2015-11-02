package com.tos.contact_duplicacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.tos.contact_duplicacy.MainActivity.DownloadMusicfromInternet;
import com.tos.database.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShowResult extends Activity implements TotalListener {
	ExpandableListView expListView;
	String name, phone_number;
	AlertDialog alertDialog;
	public static CheckBox cb;
	static ArrayList<String> work_to_do;
	public static int check_flag = 0;
	static ArrayList<String> track;
	static public HashMap<String, String> operation_queue = new HashMap<String, String>();
	public static HashMap<String, ArrayList<String>> parent_children = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, String> children_function = new HashMap<String, String>();

	static Button bn;
	// static Button bn;
	String email;
	public static ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
	
	int child, check;
	int i;
	ProgressDialog progress;
	ArrayList<Integer> _id;
	static ExAdap listAdapter;
	private InterstitialAd interstitial;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac);
		expListView = (ExpandableListView) findViewById(R.id.expandable_list);
		work_to_do = new ArrayList<String>();
		work_to_do.clear();
		//ExAdap.selectedChildCheckBoxStates.clear();
	//	ExAdap.selectedParentCheckBoxesState.clear();
		//ExAdap.mGroupList.clear();
		bn = (Button) findViewById(R.id.operation_number);
		cb = (CheckBox) findViewById(R.id.selectAll);

		listAdapter = new ExAdap(this);

		interstitial = new InterstitialAd(ShowResult.this);
		interstitial.setAdUnitId("ca-app-pub-2623420051674563/8512097939");
		AdRequest adRequest = new AdRequest.Builder()

		// Add a test device to show Test Ads
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("ccd") // Random
																					// Text
				.build();
		interstitial.loadAd(adRequest);

		cb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cb.isChecked()) {
					System.out.println("Checked");

					for (i = 0; i < selectedChildCheckBoxStates.size(); i++) {
						for (int j = 0; j < selectedChildCheckBoxStates.get(i).size(); j++) {
							selectedChildCheckBoxStates.get(i).remove(j);

							selectedChildCheckBoxStates.get(i).add(j, true);

						}
					}
					Toast.makeText(ShowResult.this, "All are Selected", Toast.LENGTH_SHORT).show();
					listAdapter.notifyDataSetChanged();
					listAdapter.showTotal(0);
					check_flag = 1;
				} else {

					for (i = 0; i < selectedChildCheckBoxStates.size(); i++) {
						for (int j = 0; j < selectedChildCheckBoxStates.get(i).size(); j++) {
							selectedChildCheckBoxStates.get(i).remove(j);
							selectedChildCheckBoxStates.get(i).add(j, false);

						}
					}
					Toast.makeText(ShowResult.this, "All are Unselected", Toast.LENGTH_SHORT).show();
					listAdapter.notifyDataSetChanged();
					listAdapter.showTotal(0);
					check_flag = 0;

				}
			}
		});
		// String text = "<font color=#ffffff>Result \n</font> <font
		// color=#ffcc00 size=8 >Single contact will be created from each
		// checked set</font>";

		// tv.setText("");
		// ExpandableListAdapter listAdapter = new ExpandableListAdapter(this,
		// MainActivity.listDataHeader,
		// MainActivity.listDataChild);

		listAdapter.setmListener(this);
		// listView.setAdapter(adapter);
		expListView.setAdapter(listAdapter);
		// bn = (Button) findViewById(R.id.operation);
		expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			  @Override
			  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			      // Doing nothing
			    return true;
			  }
			});

		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				displayInterstitial();
			}
		});
		bn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/// 1 for delete operation
				// 2 for edit operation
				/// 3 for marge operation
				if (work_to_do.size() > 0)
					new LongOperation().execute("test");
				else
					Toast.makeText(ShowResult.this, "No Operation", Toast.LENGTH_SHORT).show();

			}
		});

	}
	
	
	public static void call_when_change_list_adapter()
	{
		listAdapter.notifyDataSetChanged();
		//NotifyTransactionStatusRequest
		
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setTitle("Message");
			alertbox.setMessage("Are you sure to Exit? ");

			alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});

			alertbox.setNeutralButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
				}
			});

			alertbox.show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		call_when_change_list_adapter();

	
	}
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		call_when_change_list_adapter();

	}

	@Override
	public void onTotalChanged(int sum) {
		// bn.setText("Total = " + sum);
	}

	@Override
	public void expandGroupEvent(int groupPosition, boolean isExpanded) {
		if (isExpanded)
			expListView.collapseGroup(groupPosition);
		else
			expListView.expandGroup(groupPosition);
	}
	
	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (work_to_do.size() > 0) {

				for (int ii = 0; ii < work_to_do.size(); ii++) {
					if (work_to_do.get(ii).split("-")[0].equals("0")) {
						Log.e("LOL", "Come to your deletation");

						String part[] = work_to_do.get(ii).split("-");
						String parts[] = part[2].split("\n");
						String final_name = parts[0];
						String final_phone = parts[1];
						System.out.println("WWWWW" + final_name + final_phone);
						DatabaseHandler bh = new DatabaseHandler(getBaseContext());
						_id = bh.get_list(final_name, final_phone);
						check = bh.getPriority(final_name, final_phone, _id);
						System.out.println("CCCCCC" + check);

						for (int i = 0; i < _id.size(); i++) {
							System.out.println("CCCCCC" + _id.get(i));
							if (_id.get(i) != check) {
								deleteContactById(_id.get(i));
							} else {
								long c = _id.get(i);
							}

						}


						Log.e("WWWWWWW", "delete operation works");

					}

					if (work_to_do.get(ii).split("-")[0].equals("1")) {
						Log.e("LOL", "Come to your edit");
						String part[] = work_to_do.get(ii).split("-");
						String parts[] = part[2].split("\n");
						String final_number = parts[0].trim();

						String final_name = parts[1].split("//")[0].trim();
						Log.e("LOL", "Come to your edit" + final_number + "  " + final_name);
						ArrayList<String> delete_name = new ArrayList<String>();
						String ch[] = parts[2].split("\\n");
						for (int j = 2; j < parts.length; j++) {
							delete_name.add(parts[j].split("//")[0].trim());
							Log.e("Delete", parts[j].split("//")[0].trim());
						}
						delete_contact_edit(delete_name, final_number);
						insert_contact(final_name,final_number,null,null);

					}
					if (work_to_do.get(ii).split("-")[0].equals("2")) {
						Log.e("LOL", "Come to your merge");
						String part[] = work_to_do.get(ii).split("-");
						String parts[] = part[2].split("\n");
						String final_name = parts[0].trim();
						String phone = "", home_phone = "", office_phone = "", email = "";
						ArrayList<String> number = new ArrayList<String>();
						
						if (parts.length == 2) {
							number.add(parts[1].trim());
							phone = parts[1].split(" ")[0].trim();
							home_phone = null;
							office_phone = null;
							email = null;
							number.add(parts[1].split(" ")[0].trim());
							System.out.println("CCCCC" + parts[1].trim());
						}
						if (parts.length == 3) {
							number.add(parts[1].trim());
							phone = parts[1].trim();
							home_phone = parts[2].split(" ")[0].trim();
							office_phone = null;
							email = null;
							number.add(parts[2].split(" ")[0].trim());
							System.out.println("CCCCC" + parts[1].trim() + parts[2].split(" ")[0].trim());
						}
						if (parts.length == 4 || part.length > 4) {
							number.add(parts[1].trim());
							phone = parts[1].trim();
							home_phone = parts[2].trim();
							office_phone = parts[2].split(" ")[0].trim();
							email = null;

							number.add(parts[3].trim());
							number.add(parts[3].split(" ")[0].trim());
							System.out.println(
									"CCCCC" + parts[1].trim() + parts[2].trim() + parts[3].split(" ")[0].trim());

						}
						if(part.length==4)
						{
							String del[]=part[3].split("\n");
							for(int i=0;i<del.length-1;i++)
							{
								number.add(del[i].trim());
							}
						}

						delete_contact_marge(final_name, number);
						insert_marge_operation(final_name, phone, home_phone, office_phone, email);

					}

				}
						}

			else {
				Toast.makeText(ShowResult.this, "No operation", Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// progress.dismiss();
			super.onPostExecute(result);
			progress.dismiss();

			AlertDialog.Builder builder = new AlertDialog.Builder(ShowResult.this);
			int c = MainActivity.count_ - getContacts_with_account("all");
			c= Math.abs(c);
			builder.setMessage("Present Contact(s) :" + getContacts_with_account("all") + "\n" + "Modified Contact(s) :"
					+ work_to_do.size() + "\n" + "Deleted Duplicate Contact(s): " + c).setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							
							
							interstitial.setAdListener(new AdListener() {
								public void onAdLoaded() {
									// Call displayInterstitial() function
									displayInterstitial();
								}
							});
							
							
							
							finish();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(ShowResult.this);
			progress.setTitle("Checking Contact!!");
			progress.setMessage("Please Wait!");
			progress.setCancelable(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	protected void insert_marge_operation(String name2, String string, String string2, String string3, String email) {
		// TODO Auto-generated method stub
		String DisplayName = name2;
		String MobileNumber = string;
		String HomeNumber = string2;
		String WorkNumber = string3;
		String emailID = email;
		System.out.println("MMMMM" + name2 + "  " + MobileNumber + "  " + HomeNumber);
		// String company = "bad";
		// String jobTitle = "abcd";

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		// ---------------canot test------------------------------
		/*
		 * ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
		 * .withValue(RawContacts.ACCOUNT_TYPE, "com.google")
		 * .withValue(RawContacts.ACCOUNT_NAME, "xxxxx@gmail.com").build());
		 */

		////
		/*
		 * ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
		 * .withValue(RawContacts.ACCOUNT_TYPE,
		 * "com.google").withValue(RawContacts.ACCOUNT_NAME, email).build());
		 */

		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
		}

		// ------------------------------------------------------ Home Numbers
		if (HomeNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
					.build());
		}

		// ------------------------------------------------------ Work Numbers
		if (WorkNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
					.build());
		}

		// ------------------------------------------------------ Email
		if (emailID != null && !emailID.trim().equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
							ContactsContract.CommonDataKinds.Email.TYPE_WORK)
					.build());
		}
		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private void delete_contact_marge(String name, ArrayList<String> final_number) {
		// TODO Auto-generated method stub
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<Integer> ch = dbh.delete_marge(name, final_number);

		for (int i = 0; i < ch.size(); i++) {

			long c = ch.get(i);
			deleteContactById(c);
		}
	}

	public void deleteContactById(long id) {
		Cursor cur = getContentResolver().query(Contacts.CONTENT_URI, null, Contacts._ID + "=" + id, null, null);
		// startManagingCursor(cur);
		while (cur.moveToNext()) {
			try {
				String lookupKey = cur.getString(cur.getColumnIndex(Contacts.LOOKUP_KEY));
				Uri uri = Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, lookupKey);
				getContentResolver().delete(uri, Contacts._ID + "=" + id, null);
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
		cur.close();
	}

	private void insert_contact(String name, String number, String email, String emial_to_save) {

		String DisplayName = name;
		String MobileNumber = number;
		// String HomeNumber = "1111";
		// String WorkNumber = "2222";
		String emailID = email;
		// String company = "bad";
		// String jobTitle = "abcd";

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		/*
		 * if (emial_to_save != null) {
		 * ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
		 * .withValue(RawContacts.ACCOUNT_TYPE,
		 * "com.google").withValue(RawContacts.ACCOUNT_NAME, emial_to_save)
		 * .build());
		 * 
		 * }
		 */

		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
			if (emailID != null) {
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
						.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
								ContactsContract.CommonDataKinds.Email.TYPE_WORK)
						.build());
			}

			try {
				getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void delete_contact_edit(ArrayList<String> name_test, String final_number) {
		// TODO Auto-generated method stub
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<Integer> ch = dbh.delete(name_test, final_number);

		for (int i = 0; i < ch.size(); i++) {

			long c = ch.get(i);
			deleteContactById(c);
		}
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

}
