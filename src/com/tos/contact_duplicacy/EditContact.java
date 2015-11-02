package com.tos.contact_duplicacy;

import java.util.ArrayList;

import com.tos.database.DatabaseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EditContact extends Activity {
	static private EditText name;
	private TextView phone_number;
	String name_;
	String phone_numb, phone;
	ImageView build;

	TextView iv;
	TextView email;
	Button iv_save;
	Context c;
	com.tos.contact_duplicacy.AutoCom au;
	// list_image

	// static ArrayList<String> edit_operation;
	ListView lv;
	ArrayList<String> name_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_history_1);
		c = this;
		ListView lv;
		final int child_position = getIntent().getExtras().getInt("pos");

		phone_number = (TextView) findViewById(R.id.edit_phone);
		build = (ImageView) findViewById(R.id.bulidtool);
		iv = (TextView) findViewById(R.id.list_image_delte);
		iv_save = (Button) findViewById(R.id.buttonsave);
		TextView Du = (TextView) findViewById(R.id.duration);
		name = (EditText) findViewById(R.id.title);
		iv_save.setVisibility(View.INVISIBLE);

		lv = (ListView) findViewById(R.id.lvA);
		String process_string = getIntent().getExtras().getString("value");

		String parts[] = process_string.split("\n");
		name_ = parts[1].split("//")[0];
		name.setText(name_);
		name.setEnabled(false);
		iv.setText(name_.charAt(0) + "");
		
		
	

		email = (TextView) findViewById(R.id.edit_email);
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<String> ch_email = db.getEmail_name_number(parts[0], name_);
		ArrayList<String> ch_account;
		name_list = new ArrayList<String>();

		for (int i = 2; i < parts.length; i++) {
			name_list.add(parts[i].split("//")[0].trim());
			Log.e("Prob", parts[i].split("//")[0].trim());
		}

		/*
		 * if(ch_email.size()>0) {
		 * email.setText(ch_email.get(0).split("\n")[0]); Du.setText("Save in "
		 * +ch_email.get(0).split("\n")[1]);
		 * 
		 * 
		 * 
		 * 
		 * }
		 */

		DatabaseHandler db_ = new DatabaseHandler(this);

		// name.selectI(parts[0]);v.
		phone = parts[0];
		phone_number.setText(phone);
		ch_account = db.get_account_of_deleted_name_number(name_list, phone);
		System.out.println("phone" + phone);
		DeleteAdp dp = new DeleteAdp(this, name_list, ch_account);
		// ArrayAdapter adapter10 = new ArrayAdapter(this,
		// android.R.layout.simple_list_item_1, chh);
		lv.setAdapter(dp);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String name_ = name_list.get(position);
				DatabaseHandler db = new DatabaseHandler(EditContact.this);
				int c = db.get_id_from_name_number(name_, phone);
				// Toast.makeText(EditContact.this, chh.get(position),
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(c));
				intent.setData(uri);
				EditContact.this.startActivity(intent);

			}
		});


		build.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				name.setEnabled(true);
				name.setFocusableInTouchMode(true);
				name.requestFocus();
				iv_save.setVisibility(View.VISIBLE);
				build.setVisibility(View.INVISIBLE);

			}
		});

		iv_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!name_.equals(name.getText().toString().trim())) {
					name_list.add(name_);
				}
				String del_str = "";
				String res = phone + "\n";
				res = res + name.getText().toString().replace("-", "_").replace("'", "_") + "//will remains";
				MainActivity.testChildData_2_insert.remove(child_position);
				MainActivity.testChildData_2_insert.add(res);
				for (int i = 0; i < name_list.size(); i++) {
					del_str = del_str +"\n"+ name_list.get(i) + "//will be deleted";
				}
				MainActivity.testChildData_2_delete.remove(child_position);
				MainActivity.testChildData_2_delete.add(child_position,del_str);
				res = res + del_str;
				MainActivity.testChildData_2.remove(child_position);
				MainActivity.testChildData_2.add(child_position,res);
				ShowResult.call_when_change_list_adapter();

				
				finish();
			}
		});

	}

}
