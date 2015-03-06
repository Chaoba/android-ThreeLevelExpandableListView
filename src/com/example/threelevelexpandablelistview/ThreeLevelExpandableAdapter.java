package com.example.threelevelexpandablelistview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;

/**
 * Base class that can let ExpandableListView show three level
 * 
 * @author Liyanshun
 * 
 */
public abstract class ThreeLevelExpandableAdapter extends
		BaseExpandableListAdapter {

	public static final String TAG = "ThreeLevelExpandableAdapter";
	public Context mContext;
	private OnItemClickListener mListener;
	private int mThreeLevelColumns = 2;

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

	public ThreeLevelExpandableAdapter(Context context,
			OnItemClickListener listener) {
		mContext = context;
		mListener = listener;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// for the second level, every child is a single ExpandableListView
		// which has only one child
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

	class ChildExpandableListAdapter extends BaseExpandableListAdapter {
		private int mFatherGroupPosition, mChildGroupPosition;

		public ChildExpandableListAdapter(int groupPosition, int childPosition) {
			mFatherGroupPosition = groupPosition;
			mChildGroupPosition = childPosition;
		}

		@Override
		public int getGroupCount() {
			// every second level has only one group,which will show second
			// level contents
			return 1;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// every second level has only one child that is a gridview,this
			// gridview will contains three level contents
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
				// change columns of the three level
				gridView.setNumColumns(mThreeLevelColumns);
				gridView.setGravity(Gravity.CENTER);
				gridView.setHorizontalSpacing(10);

				gridView.setAdapter(new GridAdapter());

				// set the height of each row of three level
				final int rowHeightDp = 40;
				final float ROW_HEIGHT = mContext.getResources()
						.getDisplayMetrics().density * rowHeightDp;
				int grandChildCount = getThreeLevelCount(mFatherGroupPosition,
						mChildGroupPosition);
				int rowCount = (int) Math.ceil((double) grandChildCount
						/ mThreeLevelColumns);
				final int GRID_HEIGHT = (int) (ROW_HEIGHT * rowCount);
				gridView.getLayoutParams().height = GRID_HEIGHT;
				if (mListener != null) {
					gridView.setOnItemClickListener(mListener);
				}
			}

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		/**
		 * The adapter for the third level gridview
		 * @author Liyanshun
		 *
		 */
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

	/**
	 * implement this method to get the count of the third level
	 * 
	 * @param firstLevelPosition
	 * @param secondLevelPosition
	 * @return the count of the third level
	 */
	public abstract int getThreeLevelCount(int firstLevelPosition,
			int secondLevelPosition);

	/**
	 * implement this method to get the view that will show on the second level
	 * 
	 * @param firstLevelPosition
	 * @param secondLevelPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return the view that will show on the second level
	 */
	public abstract View getSecondLevleView(int firstLevelPosition,
			int secondLevelPosition, boolean isExpanded, View convertView,
			ViewGroup parent);

	/**
	 * implement this method to get the view that will show on the third level
	 * 
	 * @param firstLevelPosition
	 * @param secondLevelPosition
	 * @param ThreeLevelPosition
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getThreeLevleView(int firstLevelPosition,
			int secondLevelPosition, int ThreeLevelPosition, View convertView,
			ViewGroup parent);

	/**
	 * implement this method to get the object on the third level
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @param grandChildPosition
	 * @return
	 */
	public abstract Object getGrandChild(int groupPosition, int childPosition,
			int grandChildPosition);
}
