package com.tos.contact_duplicacy;

import java.util.ArrayList;

import com.tos.database.DatabaseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MargeContact extends Activity {
	CheckBox cb1, cb2, cb3, cb1_d, cb2_d, cb3_d;
	TextView tv_name;
	TextView tv_email;
	private static String name;
	ArrayList<String> number, number_refine, number_backup;
	ImageView iv_merge;
	Spinner email_to_save;
	String email_save;
	String phone, home_phne, office_phone;
	ListView lv;

	static ArrayList<String> delete_contact;
	ArrayList<String>name_gab;

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marge_update);
		name_gab=new ArrayList<String>();

		String process_string = getIntent().getExtras().getString("value");
		String process_string_only[]=process_string.split("-");
		final int child_position = getIntent().getExtras().getInt("pos");

		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb3 = (CheckBox) findViewById(R.id.checkBox3);
        lv=(ListView)findViewById(R.id.mod);
		iv_merge = (ImageView) findViewById(R.id.merge_build);
       /// iv_merge.setVisibility(View.GONE);
		tv_name = (TextView) findViewById(R.id.name_marge);
		delete_contact = new ArrayList<String>();

		number = new ArrayList<String>();
		number_refine = new ArrayList<String>();
		String parts[] = process_string_only[0].split("\n");
		name = parts[0];
		tv_name.setText(name);

		for (int i = 1; i < parts.length;) {

			if (i == parts.length - 1) {
				String num_[] = parts[i].split(" ");
				number_refine.add(num_[0]);
				
			} else {
				number_refine.add(parts[i]);
				
			}
			name_gab.add(name);
			i++;

		}
		ArrayList<String> email_1 = getEmail(name, number_refine);
		
		DeleteAdp dp=new DeleteAdp(this,name_gab,number_refine);
		//ArrayAdapter adapter10 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chh);
	    lv.setAdapter(dp);
	    
	    lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String name_=name;
				String phone_=number_refine.get(position);
				DatabaseHandler db=new DatabaseHandler(MargeContact.this);
				int c=db.get_id_from_name_number(name, phone_);
				//Toast.makeText(EditContact.this, chh.get(position), Toast.LENGTH_SHORT).show();
				 Intent intent = new Intent(Intent.ACTION_VIEW);
				    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(c));
				    intent.setData(uri);
				MargeContact.this.startActivity(intent);
				
			}
		});

	    

		// ArrayAdapter adap = new ArrayAdapter(this,
		// android.R.layout.simple_dropdown_item_1line, delete_contact);
		// lv.setAdapter(adap);
		// @SuppressWarnings("unchecked")
		// ArrayAdapter adapter = new ArrayAdapter(this,
		// android.R.layout.simple_dropdown_item_1line, email_1);
		// email.setAdapter(adapter);
		// ArrayAdapter adapter1 = new ArrayAdapter(this,
		// android.R.layout.simple_dropdown_item_1line, MainActivity.email);
		// email_to_save.setAdapter(adapter1);
		// String _email[] = MainActivity.email.get(0).split(";");

		// email_save = email_to_save.getSelectedItem().toString();

		// email_to_save.setText(_email[0]);
        if(number_refine.size()==1)
        {
        	cb1.setText(number_refine.get(0) + "- will be added");
        	phone=number_refine.get(0);
        	cb2.setVisibility(View.INVISIBLE);
        	cb3.setVisibility(View.INVISIBLE);
        	cb1.setChecked(true);
        	iv_merge.setVisibility(View.INVISIBLE);
        }
		if (number_refine.size() == 2) {
			cb1.setText(number_refine.get(0) + "- will be added");
			cb2.setText(number_refine.get(1) + "- will be added");
			cb3.setVisibility(View.INVISIBLE);
			cb1.setChecked(true);
			cb2.setChecked(true);

			String phone_noo[] = cb1.getText().toString().split("-");
			phone = phone_noo[0];
			String phone_no_[] = cb2.getText().toString().split("-");
			home_phne = phone_no_[0];
			// cb3.setText("");
			// number_refine.add(null);

		}
		if (number_refine.size() > 2) {
			cb1.setText(number_refine.get(0) + "- will be added");
			cb2.setText(number_refine.get(1) + "- will be added");
			cb3.setText(number_refine.get(2) + "- will be added");

			cb1.setChecked(true);
			cb2.setChecked(true);
			cb3.setChecked(true);
			cb2.setChecked(true);
			String phone_noo[] = cb1.getText().toString().split("-");
			phone = phone_noo[0];
			String phone_no_[] = cb2.getText().toString().split("-");
			home_phne = phone_no_[0];
		}

		cb1.setEnabled(false);
		cb2.setEnabled(false);
		cb3.setEnabled(false);

		iv_merge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(MargeContact.this);
				dialog.setContentView(R.layout.merge_dialog);
				dialog.setTitle("Edit your Contact");
				for (int i = 0; i < number_refine.size(); i++)
					delete_contact.add(number_refine.get(i));

				cb1_d = (CheckBox) dialog.findViewById(R.id.checkBox1);
				cb2_d = (CheckBox) dialog.findViewById(R.id.checkBox2);
				cb3_d = (CheckBox) dialog.findViewById(R.id.checkBox3);

				Button iv_merge_d = (Button) dialog.findViewById(R.id.merge_build);
				
				TextView tv_name_d = (TextView) dialog.findViewById(R.id.name_marge);
				tv_name_d.setText(name);

				if (number_refine.size() == 2) {
					cb1_d.setText(number_refine.get(0) + "- will be added");
					cb2_d.setText(number_refine.get(1) + "- will be added");
					cb3_d.setVisibility(View.INVISIBLE);
					cb1_d.setChecked(true);
					cb2_d.setChecked(true);
					cb3_d.setChecked(false);

					String phone_noo[] = cb1.getText().toString().split("-");
					phone = phone_noo[0];
					String phone_no_[] = cb2.getText().toString().split("-");
					home_phne = phone_no_[0];
					// cb3.setText("");
					// number_refine.add(null);

				}
				if (number_refine.size() > 2) {
					cb1_d.setText(number_refine.get(0) + "- will be added");
					cb2_d.setText(number_refine.get(1) + "- will be added");
					cb3_d.setText(number_refine.get(2) + "- will be added");

					cb1_d.setChecked(true);
					cb2_d.setChecked(true);
					cb3_d.setChecked(true);
					String phone_noo[] = cb1.getText().toString().split("-");
					phone = phone_noo[0];
					String phone_no_[] = cb2.getText().toString().split("-");
					home_phne = phone_no_[0];
				}

				cb1_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if (cb1_d.isChecked()) {
					//	Toast.makeText(getApplicationContext(), " 1 check", Toast.LENGTH_SHORT).show();

							if (!number_refine.contains(cb1_d.getText().toString().split("-")[0])) {
								number_refine.add(cb1_d.getText().toString().split("-")[0]);
							}
						} else if (!cb1_d.isChecked()) {
							Toast.makeText(getApplicationContext(), " Muri", Toast.LENGTH_SHORT).show();
							if (number_refine.contains(cb1_d.getText().toString().split("-")[0])) {
								number_refine.remove(cb1_d.getText().toString().split("-")[0]);
							}

						}

					}

				});

				cb2_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if (cb2_d.isChecked()) {
							if (!number_refine.contains(cb2_d.getText().toString().split("-")[0])) {
								number_refine.add(cb2_d.getText().toString().split("-")[0]);
							}
						} else {

							if (number_refine.contains(cb2_d.getText().toString().split("-")[0])) {
								number_refine.remove(cb2_d.getText().toString().split("-")[0]);
							}

						}

					}

				});

				cb3_d.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if (cb3_d.isChecked()) {
							if (!number_refine.contains(cb3_d.getText().toString().split("-")[0])) {
								number_refine.add(cb3_d.getText().toString().split("-")[0]);
							}
						} else {

							if (number_refine.contains(cb3_d.getText().toString().split("-")[0])) {
								number_refine.remove(cb3_d.getText().toString().split("-")[0]);
							}

						}
					}

				});

				iv_merge_d.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (!cb1_d.isChecked() && !cb2_d.isChecked() && !cb3_d.isChecked()) {
							Toast.makeText(MargeContact.this, "Select at least one number", Toast.LENGTH_SHORT).show();
						}

						else {
							MainActivity.testChildData_3.remove(child_position);
							String res = "";
							for (int i = 0; i < number_refine.size(); i++) {
								res += "\n" + number_refine.get(i);
								if (delete_contact.contains(number_refine.get(i)))
									delete_contact.remove(number_refine.get(i));
							}
							res = res + " will be merged";
							String del_res="-";
							if(!delete_contact.isEmpty())
							{
								for(int i=0;i<delete_contact.size();i++)
									del_res+="\n"+delete_contact.get(i);
								
								res=res+del_res+"\n will be deleted";
									
							}
							

							MainActivity.testChildData_3.add(child_position, name + res);
							ShowResult.call_when_change_list_adapter();
							dialog.dismiss();
							finish();
						}

					}
				});

				dialog.show();
			}
		});

	}

	private ArrayList<String> getEmail(String name2, ArrayList<String> number_refine2) {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
		ArrayList<String> ch = dbh.getEmail_for_fixed_name(name2, number_refine);

		return ch;
	}

}
