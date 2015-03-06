package com.example.threelevelexpandablelistview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	private ArrayList<Level1> items;
	private OnItemClickListener mOnItemClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.list);
		listView.setGroupIndicator(null);
		items = new ArrayList<MainActivity.Level1>();
		for (int i = 0; i < 3; i++) {
			Level1 level1 = new Level1();
			level1.title = "Level1-" + i;
			items.add(level1);
			for (int j = 0; j < 4; j++) {
				Level2 level2 = new Level2();
				level2.title = "  Level2-" + j;
				level1.child.add(level2);
				for (int k = 0; k < 5; k++) {
					Level3 level3 = new Level3();
					level3.title = "Level3-" + k;
					level2.child.add(level3);
				}
			}
		}
		mOnItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG,"position:"+position);
			}
		};
		MainAdapter mMainAdapter = new MainAdapter(this, mOnItemClickListener);
		listView.setAdapter(mMainAdapter);
	}

	class Level1 {
		String title;
		ArrayList<Level2> child = new ArrayList<MainActivity.Level2>();
	}

	class Level2 {
		String title;
		ArrayList<Level3> child = new ArrayList<MainActivity.Level3>();
	}

	class Level3 {
		String title;
	}

	class MainAdapter extends ThreeLevelExpandableAdapter {

		public MainAdapter(Context context, OnItemClickListener litener) {
			super(context, litener);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return items.get(groupPosition).child.size();
		}

		@Override
		public Level1 getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public Level2 getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).child.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = new TextView(mContext);
			textView.setTextSize(30);
			textView.setTextColor(getResources().getColor(
					android.R.color.darker_gray));
			Level1 level1 = getGroup(groupPosition);
			textView.setText(level1.title);
			return textView;
		}

		@Override
		public int getThreeLevelCount(int firstLevelPosition,
				int secondLevelPosition) {
			return getGroup(firstLevelPosition).child.get(secondLevelPosition).child
					.size();
		}

		@Override
		public View getSecondLevleView(int firstLevelPosition,
				int secondLevelPosition, boolean isExpanded, View convertView,
				ViewGroup parent) {
			TextView textView = new TextView(mContext);
			textView.setWidth(600);
			textView.setTextSize(25);
			textView.setTextColor(getResources().getColor(
					android.R.color.secondary_text_dark));
			Level2 level2 = getChild(firstLevelPosition, secondLevelPosition);
			textView.setText(level2.title);
			return textView;
		}

		@Override
		public View getThreeLevleView(int firstLevelPosition,
				int secondLevelPosition, int ThreeLevelPosition,
				View convertView, ViewGroup parent) {
			TextView textView = new TextView(mContext);
			textView.setTextSize(20);
			textView.setTextColor(getResources().getColor(
					android.R.color.tertiary_text_dark));
			Level3 level3 = getGrandChild(firstLevelPosition,
					secondLevelPosition, ThreeLevelPosition);
			textView.setText(level3.title);
			return textView;
		}

		@Override
		public Level3 getGrandChild(int groupPosition, int childPosition,
				int grandChildPosition) {
			return getChild(groupPosition, childPosition).child
					.get(grandChildPosition);
		}

	}
}
