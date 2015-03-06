package com.example.threelevelexpandablelistview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;

public abstract class ThreeLevelExpandableAdapter extends
		BaseExpandableListAdapter {

	public static final String TAG = "ThreeLevelExpandableAdapter";
	public Context mContext;

	private class CustExpListview extends ExpandableListView {

		public CustExpListview(Context context) {
			super(context);
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
					MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public ThreeLevelExpandableAdapter(Context context) {
		mContext = context;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildExpandableListAdapter carStyleAdapter = new ChildExpandableListAdapter(
				groupPosition, childPosition);
		CustExpListview SecondLevelexplv = new CustExpListview(mContext);
		SecondLevelexplv.setAdapter(carStyleAdapter);
		SecondLevelexplv.setGroupIndicator(null);
		return SecondLevelexplv;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ChildExpandableListAdapter extends BaseExpandableListAdapter
			implements OnItemClickListener {
		private int mFatherGroupPosition, mChildGroupPosition;

		public ChildExpandableListAdapter(int groupPosition, int childPosition) {
			mFatherGroupPosition = groupPosition;
			mChildGroupPosition = childPosition;
		}

		@Override
		public int getGroupCount() {
			return 1;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return ThreeLevelExpandableAdapter.this.getChild(
					mFatherGroupPosition, groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return getGrandChild(mFatherGroupPosition, groupPosition,
					childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			return getSecondLevleView(mFatherGroupPosition,
					mChildGroupPosition, isExpanded, convertView, parent);
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			GridView gridView = null;
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.car_model_grid,
						null);

				gridView = (GridView) convertView.findViewById(R.id.gridview);
				gridView.setNumColumns(2);// 设置每行列数
				gridView.setGravity(Gravity.CENTER);// 位置居中
				gridView.setHorizontalSpacing(10);// 水平间隔

				gridView.setAdapter(new GridAdapter());

				// 计算并设置gridView的高度
				final int rowHeightDp = 40;
				final float ROW_HEIGHT = mContext.getResources()
						.getDisplayMetrics().density * rowHeightDp;
				int grandChildCount=getThreeLevelCount(
						mFatherGroupPosition, mChildGroupPosition);
				int rowCount = (int) Math.ceil( (double)grandChildCount/ 2);
				final int GRID_HEIGHT = (int) (ROW_HEIGHT * rowCount);
				gridView.getLayoutParams().height = GRID_HEIGHT;
				gridView.setOnItemClickListener(this);
			}

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}

		private class GridAdapter extends BaseAdapter {
			@Override
			public int getCount() {
				return getThreeLevelCount(mFatherGroupPosition,
						mChildGroupPosition);
			}

			@Override
			public Object getItem(int position) {
				return getGrandChild(mFatherGroupPosition, mChildGroupPosition,
						position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return getThreeLevleView(mFatherGroupPosition,
						mChildGroupPosition, position, convertView, parent);
			}

		}

	}

	public abstract int getThreeLevelCount(int firstLevelPosition,
			int secondLevelPosition);

	public abstract View getSecondLevleView(int firstLevelPosition,
			int secondLevelPosition, boolean isExpanded, View convertView,
			ViewGroup parent);

	public abstract View getThreeLevleView(int firstLevelPosition,
			int secondLevelPosition, int ThreeLevelPosition, View convertView,
			ViewGroup parent);

	public abstract Object getGrandChild(int groupPosition, int childPosition,
			int grandChildPosition);
}
