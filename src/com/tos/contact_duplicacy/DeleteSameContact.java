package com.tos.contact_duplicacy;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.tos.database.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteSameContact extends Activity{
	static private TextView name;
	private TextView phone_number;
	//Spinner savein;
	String phone_numb,phone ;
	
	TextView iv;
	TextView email;
	ArrayList<String>chh;
	//list_image
	
	//static ArrayList<String> edit_operation;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_history);
		ListView lv;
		
		phone_number = (TextView) findViewById(R.id.edit_phone);
		iv=(TextView)findViewById(R.id.list_image_delte);
		TextView Du=(TextView)findViewById(R.id.duration);
			name = (TextView) findViewById(R.id.title);
			
			lv=(ListView)findViewById(R.id.lvA);
			String process_string = getIntent().getExtras().getString("value");
		    Log.e("Crash", process_string);
			String parts[]=process_string.split("\n");
			 name.setText(parts[0]);
	            iv.setText(parts[0].charAt(0)+"");
			   
			email=(TextView)findViewById(R.id.edit_email);
			DatabaseHandler db=new DatabaseHandler(this);
			ArrayList<String> ch_email=db.getEmail_name_number(parts[1], parts[0]);
			ArrayList<String> ch_account;
			if(ch_email.size()>0)
			{
				email.setText(ch_email.get(0).split("\n")[0]);
				Du.setText("Save in "+ch_email.get(0).split("\n")[1]);
				DatabaseHandler db_=new DatabaseHandler(this);
				 ch_account=db.get_account_name_number(parts[0], parts[1],ch_email.get(0));
				
				
				
			}
			else
			{
				 ch_account=db.get_account_name_number(parts[0], parts[1],"");
			}
           
			//name.selectI(parts[0]);v.
			String ch[]=parts[2].split("-");
			int k=Integer.parseInt(ch[0]);
			System.out.println("XXXXXX"+k);
			phone = parts[1];
			phone_number.setText(phone);
			
			System.out.println("phone"+phone);
			chh=new ArrayList<String>();
			for(int i=0;i<k-1;i++)
				chh.add(parts[0]);
			DeleteAdp dp=new DeleteAdp(this,chh,ch_account);
			//ArrayAdapter adapter10 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chh);
		    lv.setAdapter(dp);
		    
		    
		    lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					String name_=chh.get(position);
					DatabaseHandler db=new DatabaseHandler(DeleteSameContact.this);
					int c=db.get_id_from_name_number(name_, phone);
					Toast.makeText(DeleteSameContact.this, chh.get(position), Toast.LENGTH_SHORT).show();
					 Intent intent = new Intent(Intent.ACTION_VIEW);
					    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(c));
					    intent.setData(uri);
					DeleteSameContact.this.startActivity(intent);
					
				}
			});
			
			
			

	}

}
