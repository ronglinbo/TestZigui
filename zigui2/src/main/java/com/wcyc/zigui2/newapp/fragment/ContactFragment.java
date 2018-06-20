/*
 * 文 件 名:ContactFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-02-23
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.mobileim.YWIMKit;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.ContactGroupDetail;
import com.wcyc.zigui2.newapp.adapter.ContactAdapter;
import com.wcyc.zigui2.bean.Classes;
import com.wcyc.zigui2.bean.MemberBean;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.chat.ChatActivity;
import com.wcyc.zigui2.chat.ContactDetail;
import com.wcyc.zigui2.chat.ContactlistActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.ContactsMap;
import com.wcyc.zigui2.newapp.bean.GetAllContactsReq;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

//通讯录
public class ContactFragment extends Fragment
        implements HttpRequestAsyncTaskListener {
//	private NewMemberBean member;

    private AllContactListBean allContactList;
    private List<ClassList> classList;//群组list
    private List<List<ContactsList>> contactList;
    private List<ClassList> groupList;//群组list
    private ExpandableListView expandList, groupListView;
    private Context mcontext;
    private ContactListAdapter adapter;
    private GroupListAdapter groupAdapter;
    private TextView tvNoContact;
    private String userName;
    private SearchView search;
    private View searchView;
    private Button searchButton;
    private ListView result;
    private boolean isParent;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private View view;

    DisplayImageOptions build;

    public static Fragment newInstance(int index) {
        Fragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        //fragment.setIndex(index);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        view = inflater.inflate(R.layout.new_contactlist, container, false);
        tvNoContact = (TextView) view.findViewById(R.id.tv_no_message);
        return view;
    }

    public void onStart() {
        super.onStart();
        setUserName(CCApplication.getInstance().getUserName());
        System.out.println("ContactFragment context:" + getActivity());
        initView();
        isParent = CCApplication.getInstance().isCurUserParent();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initView() {
        if (mcontext != null) {
            search = (SearchView) ((Activity) mcontext).findViewById(R.id.search);
            if (search != null) {
                search.setQueryHint("姓名/姓名首字母/手机号");
                search.setIconifiedByDefault(false);
                search.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("allContact", allContactList);
                        ((BaseActivity) mcontext).newActivity(SearchContactActivity.class, bundle);
                    }

                });
            }

//			searchButton = (Button) ((Activity) mcontext).findViewById(R.id.searchButton);

            searchButton = (Button) view.findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("allContact", allContactList);
                    ((BaseActivity) mcontext).newActivity(SearchContactActivity.class, bundle);
                }

            });
            expandList = (ExpandableListView) ((Activity) mcontext).findViewById(android.R.id.list);
            groupListView = (ExpandableListView) ((Activity) mcontext).findViewById(R.id.group_listview);
            adapter = new ContactListAdapter();
            if (DataUtil.isMain()) {//副号不显示群组
                groupAdapter = new GroupListAdapter();
                if (groupListView != null) {
                    groupListView.setVisibility(View.VISIBLE);
                    groupListView.setAdapter(groupAdapter);
                    groupListView.setOnChildClickListener(new OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
                                                    int arg3, long arg4) {
                            // TODO Auto-generated method stub
                            ClassList group = groupList.get(arg3);
                            String groupName = group.getGroupName();
                            if (DataUtil.isNullorEmpty(groupName)) {
                                groupName = group.getClassName();
                            }
                            String groupId = group.getGroupId();

//							groupChatWithHx(group, groupName, groupId);

                            //跳转到百川页面
                            Intent intent = new Intent(mcontext, ContactGroupDetail.class);
                            intent.putExtra("groupId", groupId);
                            intent.putExtra("groupName", groupName);
                            startActivity(intent);
                            return false;
                        }
                    });
                }
            } else {
                if (groupListView != null) {
                    groupListView.setVisibility(View.GONE);
                }
            }

            if (expandList == null) return;

            expandList.setAdapter(adapter);
            if (adapter.isEmpty()) {
                tvNoContact.setVisibility(View.VISIBLE);
            }
            expandList.setOnChildClickListener(new OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView arg0, View arg1,
                                            int arg2, int arg3, long arg4) {
                    // TODO Auto-generated method stub

                    ContactsList list = contactList.get(arg2).get(arg3);
                    String nickName = list.getHxNickName();
                    System.out.println("onChildClick:" + arg1);
                    Intent intent = new Intent(mcontext, ContactDetail.class);
                    intent.putExtra("cellPhone", list.getCellphone());
                    intent.putExtra("userName", list.getUserName());
                    if (DataUtil.isNullorEmpty(nickName)) {
                        nickName = list.getNickName();
                    }
                    intent.putExtra("userNick", nickName);
                    intent.putExtra("userTitle", list.getText());
                    // 接受者头像
                    intent.putExtra("avatarUrl", list.getUserIconURL());
                    startActivity(intent);
                    return true;
                }

            });
        }
    }

    private void groupChatWithHx(ClassList group, String groupName, String groupId) {
        if (!DataUtil.isNullorEmpty(groupId)) {
//							if(DataUtil.isInGroup(groupId)){
            Intent intent = new Intent(mcontext, ChatActivity.class);
            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);

            intent.putExtra("userNick", groupName);
            intent.putExtra("groupId", group.getGroupId());
            startActivity(intent);
//							}else{
//								DataUtil.getToast("您已被移出群组");
//							}
        } else {
            DataUtil.getToast("没有环信账号，请联系管理员");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        initView();
    }

    public void initData() {
        mcontext = getActivity();
        contactList = new ArrayList<List<ContactsList>>();
        allContactList = CCApplication.app.getAllContactList();
        if (allContactList != null && allContactList.getClassList() != null
                && allContactList.getClassList().size() > 0) {
            initAdapter();
        } else {
            getAllContact();
        }
        initView();
    }

    void initAdapter() {
        String hxUserName = CCApplication.getInstance().getUserName();
//		NewMemberBean member = CCApplication.getInstance().getMemberInfo();
//		List<UserType> users = member.getUserTypeList();
        classList = allContactList.getClassList();
        //去掉学校管理员群组
        if (groupList == null) {
            groupList = new ArrayList<ClassList>();
        } else {
            groupList.clear();
        }
        if (classList != null) {

            //环信保留了教职工群
//			for (ClassList item : classList) {
//				if (!("学校管理员".equals(item.getClassName()) || "-1".equals(item.getClassID()))) {
//					groupList.add(item);
//				}
//			}

            //阿里百川去掉了所有教职工群
            for (ClassList item : classList) {
                if (!"学校管理员".equals(item.getClassName()) && !"所有教职工".equals(item.getClassName()) && !"-1".equals(item.getClassID())) {
                    groupList.add(item);
                }
            }
        }

        if (classList != null) {
            for (int i = 0; i < classList.size(); i++) {
                List<ContactsList> list = classList.get(i).getContactsList();
                if (list == null) {
                    classList.remove(i);
                    i--;
                } else {
                    //从联系人中去掉当前用户
                    List<ContactsList> temp = DataUtil.removeSelfInContact(list);
                    list = temp;
                    if (contactList != null) {
                        contactList.add(list);
                    }
                }
            }
        }
    }

    private void getAllContact() {

        GetAllContactsReq req = new GetAllContactsReq();
        UserType user = CCApplication.getInstance().getPresentUser();
        try {
            JSONObject json;
            List<String> classIdList = new ArrayList<String>();
            if (user.getUserType().equals(Constants.TEACHER_STR_TYPE)) {
                List<NewClasses> list = CCApplication.getInstance().getMemberDetail().getClassList();
                if (list != null) {
                    for (NewClasses classes : list) {
                        if (!isClassIdExist(classIdList, classes.getClassID()))//是否已存在list中
                            classIdList.add(classes.getClassID());
                    }
                }
            } else if (user.getUserType().equals(Constants.PARENT_STR_TYPE)) {
//				List<NewChild> list = CCApplication.getInstance().getMemberDetail().getChildList();
//				if(list != null){
//					for(NewChild child:list){
//						if(!isClassIdExist(classIdList,child.getChildClassID()))//是否已存在list中
//							classIdList.add(child.getChildClassID());
//					}
//				}else{
                classIdList.add(user.getClassId());
//				}
            }
            req.setSchoolId(user.getSchoolId());
            req.setUserId(user.getUserId());
            req.setUserType(user.getUserType());
            if (classIdList.size() > 0) {
                req.setClassIdList(classIdList);
            } else {
                classIdList.add("-1");
                req.setClassIdList(classIdList);
            }
            Gson gson = new Gson();
            String string = gson.toJson(req);
            json = new JSONObject(string);
            System.out.println("ContactFragment getAllContact json:" + json);
            new HttpRequestAsyncTask(json, this, mcontext)
                    .execute(Constants.GET_CONTACT_URL);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isClassIdExist(List<String> list, String classId) {
        for (String string : list) {
            if (string.equals(classId)) {
                return true;
            }
        }
        return false;
    }

    public class GroupListAdapter extends BaseExpandableListAdapter {

        @Override
        public Object getChild(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getChildId(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getChildView(int arg0, int arg1, boolean arg2, View convertView,
                                 ViewGroup arg4) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;

            String url = "";//需要增加群头像字段
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.row_group_contact, null);
                viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

                //使用默认的头像
//				((BaseActivity) mcontext).getImageLoader().displayImage(url, viewHolder.avatar,
//						((BaseActivity) mcontext).getImageLoaderOptions());
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvName.setText(groupList.get(arg1).getClassName());
            return convertView;
        }

        @Override
        public int getChildrenCount(int arg0) {
            // TODO Auto-generated method stub
            if (groupList != null) {
                return groupList.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return 1;
        }

        @Override
        public long getGroupId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getGroupView(int groupPos, boolean arg1, View convertView,
                                 ViewGroup arg3) {
            // TODO Auto-generated method stub
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.group_item, null);
                holder.tvNum = (TextView) convertView.findViewById(R.id.num);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (groupList != null) {
                holder.tvNum.setText(groupList.size() + "");
            } else {
                holder.tvNum.setText("0");
            }

            holder.tvName.setText("群组");
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    public class ContactListAdapter extends BaseExpandableListAdapter {
        public ContactListAdapter() {
            queue = Volley.newRequestQueue(getActivity());
            imageLoader = new ImageLoader(queue, new BitmapCache());
        }

        @Override
        public Object getChild(int groupPos, int childPos) {
            // TODO Auto-generated method stub
            return contactList.get(groupPos).get(childPos);
        }

        @Override
        public long getChildId(int groupPos, int childPos) {
            // TODO Auto-generated method stub
            return childPos;
        }

        @Override
        public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder viewHolder;
            String name;
//			final String tag;
            ContactsList contact = contactList.get(groupPos).get(childPos);
            String type = contact.getUserIdentity();
            if (Constants.PARENT_STR_TYPE.equals(type)) {
                name = contact.getHxNickName();
                if (DataUtil.isNullorEmpty(name)) {
                    name = contact.getNickName();
                }
            } else {
                name = contact.getNickName();
                if (DataUtil.isNullorEmpty(name)) {
                    name = contact.getHxNickName();
                }
            }
            String title = contact.getText();
            final String hxName = contact.getUserName();

            final String file = contact.getUserIconURL();

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_contact_item, null);
                viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvName.setText(name);
            viewHolder.tvTitle.setText(title);
            String url = DataUtil.getIconURL(mcontext, file);

            if (build == null) {
                build = new DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .showImageForEmptyUri(R.drawable.pho_touxiang)  // empty URI时显示的图片
                        .showImageOnFail(R.drawable.pho_touxiang)       // 不是图片文件 显示图片
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .considerExifParams(true)
                        .resetViewBeforeLoading(false)
                        .displayer(new FadeInBitmapDisplayer(300)).build();
            }

            ((BaseActivity) mcontext).getImageLoader().displayImage(url, viewHolder.avatar, build);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPos) {
            // TODO Auto-generated method stub
            if (contactList.get(groupPos) != null)
                return contactList.get(groupPos).size();
            return 0;
        }

        @Override
        public Object getGroup(int groupPos) {
            // TODO Auto-generated method stub
            return classList.get(groupPos);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            if (classList != null)
                return classList.size();
            return 0;
        }

        @Override
        public long getGroupId(int groupPos) {
            // TODO Auto-generated method stub
            return groupPos;
        }

        @Override
        public View getGroupView(int groupPos, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            String string = classList.get(groupPos).getClassName();
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.group_item, null);
                holder.tvNum = (TextView) convertView.findViewById(R.id.num);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (contactList != null && contactList.size() > 0 && contactList.get(groupPos) != null) {
                holder.tvNum.setText(contactList.get(groupPos).size() + "");
            } else {
                holder.tvNum.setText("0");
            }
            holder.tvName.setText(string);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return true;
        }

        private void setImage(final ViewHolder holder, String file, String userName) {
            if (file != null) {
                if (LocalUtil.mBitMap != null && userName != null && userName.equals(getUserName())) {
                    holder.avatar.setImageBitmap(LocalUtil.mBitMap);
                } else {
                    String tag = (String) holder.avatar.getTag();
//					if(tag != null && tag.equals(file)){
                    String url = DataUtil.getIconURL(mcontext, file);
                    System.out.println("url:" + url);
//						((BaseActivity) mcontext).getImageLoader().displayImage(url, holder.avatar, 
//								((BaseActivity) mcontext).getImageLoaderOptions());
                    ((BaseActivity) mcontext).getImageLoader().loadImage(url,
                            ((BaseActivity) mcontext).getImageLoaderOptions(),
                            new ImageLoadingListener() {

                                @Override
                                public void onLoadingCancelled(String arg0,
                                                               View arg1) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onLoadingComplete(String arg0,
                                                              View arg1, Bitmap arg2) {
                                    // TODO Auto-generated method stub
                                    holder.avatar.setImageBitmap(arg2);
                                }

                                @Override
                                public void onLoadingFailed(String arg0,
                                                            View arg1, FailReason arg2) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onLoadingStarted(String arg0,
                                                             View arg1) {
                                    // TODO Auto-generated method stub

                                }
                            });
//					}
                }
            }
        }
    }

    private static class ViewHolder {
        /**
         * 联系人名字
         */
        TextView tvName;
        TextView tvNum;
        /**
         * 头字符
         */
        TextView tvHeader;
        /**
         * 用户头像
         */
        RoundImageView avatar;
        /**
         * 职位
         */
        TextView tvTitle;
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        allContactList = JsonUtils.fromJson(result, AllContactListBean.class);
        if (allContactList.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            CCApplication.getInstance().setAllContactList(allContactList);
            initAdapter();
            adapter.notifyDataSetChanged();
            groupAdapter.notifyDataSetChanged();
        }

        if (adapter.isEmpty()) {
            tvNoContact.setVisibility(View.VISIBLE);
        } else {
            tvNoContact.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }

    public ContactListAdapter getAdapter() {
        return adapter;
    }

    public GroupListAdapter getGroupAdapter() {
        return groupAdapter;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}