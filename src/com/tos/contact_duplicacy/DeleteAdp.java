package com.tos.contact_duplicacy;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeleteAdp extends ArrayAdapter<String> {

	private final Activity context;
	private final ArrayList<String> web;
	private final ArrayList<String> email;

	public DeleteAdp(Activity context, ArrayList<String> web, ArrayList<String> email) {
		super(context, R.layout.list_item_on, web);
		this.context = context;
		this.web = web;
		this.email = email;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item_on, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.delete_name);
		TextView tv_mail=(TextView) rowView.findViewById(R.id.delete_from_contact);
		TextView tv_image=(TextView)rowView.findViewById(R.id.delete_image);

		//ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(web.get(position));
	//	tv_mail.setText(email.get(position));
		tv_image.setText(web.get(position).charAt(0)+"");
		Random rand=new Random();
		int r = (int)(1+ rand.nextInt() / 2f + 0.5);
		int g = (int)(10+ rand.nextInt() / 2f + 0.5);
		int b = (int)(30+ rand.nextInt() / 2f + 0.5);
		tv_image.setBackgroundColor(Color.rgb(r,g,b));
		

		//imageView.setImageResource(imageId[position]);
		return rowView;
	}
}