package com.chaoyang805.chapterfour_customview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoyang805.chapterfour_customview.view.PinnedHeaderExpandableListView;
import com.chaoyang805.chapterfour_customview.view.StickyLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, PinnedHeaderExpandableListView.OnHeaderUpdateListener, StickyLayout.OnGiveUpTouchEventListener {

    private StickyLayout mStickyLayout;
    private PinnedHeaderExpandableListView mExpandableListView;
    private ArrayList<Group> mGroups;
    private ArrayList<List<People>> mChildren;
    private MyExpandableListAdapter mAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
        mExpandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
        initData();
        mAdapter = new MyExpandableListAdapter(this);
        mExpandableListView.setAdapter(mAdapter);

        for (int i = 0 ,count = mExpandableListView.getCount(); i < count; i++) {
            mExpandableListView.expandGroup(i);
        }
        mExpandableListView.setOnHeaderUpdateListener(this);
        mExpandableListView.setOnChildClickListener(this);
        mExpandableListView.setOnGroupClickListener(this);
        mStickyLayout.setOnGiveUpTouchEventListener(this);
    }

    private void initData() {
        mGroups = new ArrayList<>();
        Group group;
        for (int i = 0; i < 3; i++) {
            group = new Group();
            group.setTitle("group-" + i);
            mGroups.add(group);
        }
        mChildren = new ArrayList<>();
        for (int i = 0; i < mGroups.size(); i++) {
            ArrayList<People> childTemp;
            if (i == 0) {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 13; j++) {
                    People people = new People();
                    people.setName("yy-" + j);
                    people.setAge(30);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else if (i == 1) {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 8; j++) {
                    People people = new People();
                    people.setName("ff-" + j);
                    people.setAge(40);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else {
                childTemp = new ArrayList<People>();
                for (int j = 0; j < 23; j++) {
                    People people = new People();
                    people.setName("hh-" + j);
                    people.setAge(20);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            }
            mChildren.add(childTemp);
        }
    }

    // PinnedExpandableListView#onChildClickListener
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        Toast.makeText(this, mChildren.get(groupPosition).get(childPosition).getName(), Toast.LENGTH_SHORT).show();
        return false;
    }
    // PinnedExpandableListView#onGroupClickListener
    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        return false;
    }
    // PinnedExpandableListView#onHeaderUpdateListener
    @Override
    public View getPinnedHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT,
            AbsListView.LayoutParams.WRAP_CONTENT));
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPosition) {
        Group firstVisibleGroup = mAdapter.getGroup(firstVisibleGroupPosition);
        TextView textView = (TextView) headerView.findViewById(R.id.group);
        textView.setText(firstVisibleGroup.getTitle());
    }
    // StickyLayout#onGiveUpTouchEventListener
    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (mExpandableListView.getFirstVisiblePosition() == 0) {
            View view = mExpandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public MyExpandableListAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }



        @Override

        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildren.get(groupPosition).size();
        }

        @Override
        public Group getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        @Override
        public People getChild(int groupPosition, int childPosition) {
            return mChildren.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder;
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = mLayoutInflater.inflate(R.layout.group,parent, false);
                holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
                holder.mTextView = (TextView) convertView.findViewById(R.id.group);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.mTextView.setText(getGroup(groupPosition).getTitle());
            if (isExpanded) {
                holder.mImageView.setImageResource(R.drawable.expanded);
            } else {
                holder.mImageView.setImageResource(R.drawable.collapse);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ChildHolder holder = null;
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = mLayoutInflater.inflate(R.layout.child, parent, false);
                holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
                holder.mTvAddress = (TextView) convertView.findViewById(R.id.address);
                holder.mTvAge = (TextView) convertView.findViewById(R.id.age);
                holder.mTvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.mTvName.setText(getChild(groupPosition, childPosition).getName());
            holder.mTvAge.setText(getChild(groupPosition, childPosition).getAge() + "");
            holder.mTvAddress.setText(getChild(groupPosition, childPosition).getAddress());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }



    class GroupHolder {
        TextView mTextView;
        ImageView mImageView;
    }

    class ChildHolder {
        TextView mTvName;
        TextView mTvAge;
        TextView mTvAddress;
        ImageView mImageView;
    }
}
