package com.tos.contact_duplicacy;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExAdap extends BaseExpandableListAdapter {

	private ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();

	/*
	 * Raw Data
	 */
	static int counter = 0;

	Context mContext;

	ViewHolder holder;
	public static ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();
	TotalListener mListener;

	public void setmListener(TotalListener mListener) {
		this.mListener = mListener;
	}

	public void setmGroupList(ArrayList<ArrayList<String>> mGroupList) {
		this.mGroupList = mGroupList;
	}

	class ViewHolder {
		// public CheckBox groupName;
		public ImageView iv11;
		public TextView dummyTextView; // View to expand or shrink the list
		public CheckBox childCheckBox;
		public TextView childText;
		public TextView childText_;
		public TextView count;

		public TextView check;
		public ImageView iv;
	}

	public ExAdap(Context context) {
		mContext = context;

		// Add raw data into Group List Array
		for (int i = 0; i < MainActivity.testgroupData.size(); i++) {
			ArrayList<String> prices = new ArrayList<>();
			Log.e("fuck size", MainActivity.testChildData_1.size() + "");
			Log.e("fuck size", MainActivity.testChildData_2.size() + "");
			Log.e("fuck size", MainActivity.testChildData_3.size() + "");
			if (i == 0) {
				for (int j = 0; j < MainActivity.testChildData_1.size(); j++) {
					prices.add(MainActivity.testChildData_1_insert.get(j) + "///"
							+ MainActivity.testChildData_1_delete.get(j));

				}
			}
			if (i == 1) {
				Log.e("Here", MainActivity.testChildData_2.size() + "");
				for (int jj = 0; jj < MainActivity.testChildData_2.size(); jj++) {
					prices.add(MainActivity.testChildData_2_insert.get(jj) + "///"
							+ MainActivity.testChildData_2_delete.get(jj));
				}
			}
			if (i == 2) {
				for (int j = 0; j < MainActivity.testChildData_3.size(); j++) {
					prices.add(MainActivity.testChildData_3_insert.get(j) + "///"
							+ MainActivity.testChildData_3_delete.get(j));
				}
			}

			mGroupList.add(i, prices);
		}
		initCheckStates(false);
	}

	/**
	 * Called to initialize the default check states of items
	 * 
	 * @param defaultState
	 *            : false
	 */
	private void initCheckStates(boolean defaultState) {
		for (int i = 0; i < mGroupList.size(); i++) {
			selectedParentCheckBoxesState.add(i, defaultState);
			ArrayList<Boolean> childStates = new ArrayList<>();
			for (int j = 0; j < mGroupList.get(i).size(); j++) {
				Log.e("fax", mGroupList.get(i).size() + "");
				childStates.add(defaultState);

			}

			ShowResult.selectedChildCheckBoxStates.add(i, childStates);
		}

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mGroupList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroupList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_layout, null);
			holder = new ViewHolder();
			holder.dummyTextView = (TextView) convertView.findViewById(R.id.dummy_txt_view);
			holder.iv = (ImageView) convertView.findViewById(R.id.acc_image);
			holder.iv11 = (ImageView) convertView.findViewById(R.id.iv11);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.dummyTextView.setText(MainActivity.testgroupData.get(groupPosition));
		holder.iv.setImageResource(MainActivity.testgroupData_image.get(groupPosition));
		holder.count.setText(getData(groupPosition) + "/" + MainActivity.size_info.get(groupPosition));

		/// here set the image////
		if (selectedParentCheckBoxesState.size() <= groupPosition) {
			selectedParentCheckBoxesState.add(groupPosition, false);
		} else {
			// holder.groupName.setChecked(selectedParentCheckBoxesState.get(groupPosition));
		}

		holder.iv11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// //Callback to expansion of group item

				boolean state = selectedParentCheckBoxesState.get(groupPosition);
				selectedParentCheckBoxesState.remove(groupPosition);
				selectedParentCheckBoxesState.add(groupPosition, state ? false : true);
				mListener.expandGroupEvent(groupPosition, isExpanded);
				if (state)
					holder.iv11.setImageResource(R.drawable.down);
				else
					holder.iv11.setImageResource(R.drawable.up);
				notifyDataSetChanged();

			}
		});

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_layout, null);
			holder = new ViewHolder();
			holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
			holder.childText = (TextView) convertView.findViewById(R.id.item_title);
			holder.childText_ = (TextView) convertView.findViewById(R.id.item_title_);
			holder.check = (TextView) convertView.findViewById(R.id.check_);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.childText.setText(mGroupList.get(groupPosition).get(childPosition).toString().split("///")[0]);
		holder.childText_.setText(mGroupList.get(groupPosition).get(childPosition).toString().split("///")[1]);
		// }

		for (int i = 0; i < mGroupList.size(); i++)
			for (int j = 0; j < ShowResult.selectedChildCheckBoxStates.get(i).size(); j++) {
				if (ShowResult.selectedChildCheckBoxStates.get(i).get(j))
					holder.childCheckBox.setChecked(true);
				else
					holder.childCheckBox.setChecked(false);
			}

		if (ShowResult.selectedChildCheckBoxStates.size() <= groupPosition) {
			ArrayList<Boolean> childState = new ArrayList<>();
			for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {
				if (childState.size() > childPosition)
					childState.add(childPosition, false);
				else
					childState.add(false);
			}
			if (ShowResult.selectedChildCheckBoxStates.size() > groupPosition) {
				ShowResult.selectedChildCheckBoxStates.add(groupPosition, childState);
			} else
				ShowResult.selectedChildCheckBoxStates.add(childState);
		} else {
			holder.childCheckBox
					.setChecked(ShowResult.selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
		}
		holder.childCheckBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean state = ShowResult.selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
				ShowResult.selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
				ShowResult.selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);
				Log.e("Group", groupPosition + "");
				notify_dataset_change();
				if (ShowResult.cb.isChecked()) {
					ShowResult.cb.setChecked(false);
				}
				showTotal(groupPosition);

			}
		});
		holder.check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (groupPosition == 0) {
					String childText = MainActivity.testChildData_1.get(childPosition);

					Intent intent = new Intent(mContext, DeleteSameContact.class);
					intent.putExtra("value", childText);
					mContext.startActivity(intent);

				}
				if (groupPosition == 1) {
					String childText = MainActivity.testChildData_2.get(childPosition);
					Intent intent = new Intent(mContext, EditContact.class);
					intent.putExtra("value", childText);
					intent.putExtra("pos", childPosition);
					mContext.startActivity(intent);

				}
				if (groupPosition == 2) {
					String childText = MainActivity.testChildData_3.get(childPosition);
					// Toast.makeText(mContext, childText,
					// Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext, MargeContact.class);
					intent.putExtra("value", childText);
					intent.putExtra("pos", childPosition);
					mContext.startActivity(intent);

				}

			}
		});

		return convertView;
	}

	public void notify_dataset_change() {
		this.notifyDataSetChanged();
	}

	public void showTotal(int groupPosition) {
		Log.e("Here come problem", groupPosition + "");
		int sum = 0;
		ArrayList<String> _child_1 = new ArrayList<String>();
		ArrayList<String> _child_2 = new ArrayList<String>();
		ArrayList<String> _child_3 = new ArrayList<String>();
		ShowResult.operation_queue.clear();
		for (int j = 0; j < ShowResult.selectedChildCheckBoxStates.size(); j++) {
			Log.d("TAG", "J = " + j);
			Log.e("Here come problem", groupPosition + "    " + j);
			for (int i = 0; i < ShowResult.selectedChildCheckBoxStates.get(j).size(); i++) {
				Log.e("Here come problem", groupPosition + "    " + i);
				Log.d("TAG", "I = " + i);

				if (ShowResult.selectedChildCheckBoxStates.get(j).get(i)) {
					sum++;

					// ShowResult.work_to_do.add(object)

					if (j == 0) {

						Log.e("MMM", "comes");

						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_1.get(i))) {
						} else {
							ShowResult.work_to_do.add(j + "-" + i + "-" + MainActivity.testChildData_1.get(i));
						}

					} /// insert the child_1
					if (j == 1) {
						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_2.get(i))) {
						} else {
							ShowResult.work_to_do.add(j + "-" + i + "-" + MainActivity.testChildData_2.get(i));
						}
					} /// insert the child_2
					if (j == 2) {
						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_3.get(i))) {
							// Toast.makeText(mContext, "Operation allready
							// inserted", Toast.LENGTH_SHORT).show();
						} else {
							ShowResult.work_to_do.add(j + "-" + i + "-" + MainActivity.testChildData_3.get(i));
						}
					} //// insert the child_3

				}

				else {

					if (j == 0) {

						Log.e("MMM", "comes");

						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_1.get(i))) {
							ShowResult.work_to_do.remove(ShowResult.work_to_do
									.indexOf(j + "-" + i + "-" + MainActivity.testChildData_1.get(i)));

						}

					}
					if (j == 1) {
						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_2.get(i))) {
							ShowResult.work_to_do.remove(ShowResult.work_to_do
									.indexOf(j + "-" + i + "-" + MainActivity.testChildData_2.get(i)));

						}
					} /// insert the child_2
					if (j == 2) {
						if (ShowResult.work_to_do.contains(j + "-" + i + "-" + MainActivity.testChildData_3.get(i))) {
							ShowResult.work_to_do.remove(ShowResult.work_to_do
									.indexOf(j + "-" + i + "-" + MainActivity.testChildData_3.get(i)));

						}
					}

				}

			}

		}

	
		String text = "<font color=#ffffff>Operation(s)</font> <font color=#ffcc00>" + ShowResult.work_to_do.size()
				+ "</font>";
		ShowResult.bn.setText(Html.fromHtml(text));

	}

	int getData(int group) {
		int k = 0;
		for (int i = 0; i < ShowResult.selectedChildCheckBoxStates.get(group).size(); i++) {
			// Log.e("Here come problem", groupPosition+" "+i);
			Log.d("TAG", "I = " + i);

			if (ShowResult.selectedChildCheckBoxStates.get(group).get(i)) {
				k++;
			}
		}
		return k;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
