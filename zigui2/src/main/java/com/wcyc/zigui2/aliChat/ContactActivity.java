package com.wcyc.zigui2.aliChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.GetAllContactsReq;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.fragment.BitmapCache;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONObject;

/**
 * 通讯录Activity
 */
public class ContactActivity extends BaseActivity implements HttpRequestAsyncTaskListener {


    private AllContactListBean allContactList;
    private List<ClassList> classList;//群组list
    private List<List<ContactsList>> contactList;
    private List<ClassList> groupList;//群组list
    private ExpandableListView expandList, groupListView;
    private Context mcontext;
    private ContactActivity.ContactListAdapter adapter;
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
    private YWMessage msg;
    private YWIMKit imKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUserName(CCApplication.getInstance().getUserName());
        isParent = CCApplication.getInstance().isCurUserParent();
        initData();
        initView();
    }

    public void initData() {
        Intent intent = getIntent();
        msg = (YWMessage) intent.getSerializableExtra("msg");
        if (msg == null) {
            DataUtil.getToastShort("转发失败,请重新尝试");
            finish();
        }

        imKit = CCApplication.getInstance().getIMKit();
        mcontext = this;
        contactList = new ArrayList<List<ContactsList>>();
        allContactList = CCApplication.app.getAllContactList();
        if (allContactList != null && allContactList.getClassList() != null
                && allContactList.getClassList().size() > 0) {
            initAdapter();
        } else {
            getAllContact();
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

    public void initAdapter() {
        String hxUserName = CCApplication.getInstance().getUserName();
        classList = allContactList.getClassList();
        //去掉学校管理员群组
        if (groupList == null) {
            groupList = new ArrayList<ClassList>();
        } else {
            groupList.clear();
        }
        if (classList != null) {

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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private void initView() {
        TextView new_content = (TextView) findViewById(R.id.new_content);
        new_content.setText("通讯录");

        LinearLayout title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvNoContact = (TextView) findViewById(R.id.tv_no_message);

        if (mcontext != null) {
            search = (SearchView) ((Activity) mcontext).findViewById(R.id.search);
            if (search != null) {
                search.setQueryHint("姓名/姓名首字母/手机号");
                search.setIconifiedByDefault(false);
                search.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("allContact", allContactList);
                        ((BaseActivity) mcontext).newActivity(SearchContactActivity.class, bundle);
                    }

                });
            }

            searchButton = (Button) findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {

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
            adapter = new ContactActivity.ContactListAdapter();
            if (DataUtil.isMain()) {//副号不显示群组
                groupAdapter = new GroupListAdapter();
                if (groupListView != null) {
                    groupListView.setVisibility(View.VISIBLE);
                    groupListView.setAdapter(groupAdapter);
                    groupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
                                                    int arg3, long arg4) {
                            // TODO Auto-generated method stub

                            //群聊转发
                            ClassList group = groupList.get(arg3);
                            String groupName = group.getGroupName();
                            if (DataUtil.isNullorEmpty(groupName)) {
                                groupName = group.getClassName();
                            }
                            String content = group.getGroupId();

                            //群聊转发
                            if (DataUtil.isNullorEmpty(content)) {
                                DataUtil.isNullorEmpty("此群没有百川账号");
                            } else {
                                try {
                                    Long aLong = Long.valueOf(content);
                                    imKit.getConversationService()
                                            .forwardMsgToTribe(aLong.longValue()
                                                    , msg, new IWxCallback() {
                                                        @Override
                                                        public void onSuccess(Object... objects) {
                                                            DataUtil.getToast("转发成功");
                                                        }

                                                        @Override
                                                        public void onError(int i, String s) {
                                                            DataUtil.getToast("转发失败");
                                                        }

                                                        @Override
                                                        public void onProgress(int i) {

                                                        }
                                                    });
                                    mcontext.startActivity(imKit.getTribeChattingActivityIntent(aLong.longValue()));
                                } catch (Exception e) {

                                }
                            }
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
            expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView arg0, View arg1,
                                            int arg2, int arg3, long arg4) {
                    // TODO Auto-generated method stub
                    //转发到单聊
                    //获取到百川ID
                    ContactsList list = contactList.get(arg2).get(arg3);
                    String userId = list.getUserName();
                    if (imKit != null) {
                        if (imKit.getIMCore() != null) {
                            String loginId = imKit.getIMCore().getLoginUserId();
                            if (userId.equals(loginId)) {
                                IMNotificationUtils.getInstance().showToast(mcontext, "不能转发给自己");
                            }
                        }
                    }

                    if (DataUtil.isNullorEmpty(userId)) {
                        DataUtil.isNullorEmpty("接受者没有百川账号");
                    } else {

                        //转发给个人示例
                        IYWContact appContact = YWContactFactory.createAPPContact(userId, imKit.getIMCore().getAppKey());
                        imKit.getConversationService().forwardMsgToContact(appContact, msg,
                                new IWxCallback() {
                                    @Override
                                    public void onSuccess(Object... objects) {
                                        DataUtil.getToast("转发成功");
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        DataUtil.getToast("转发失败");
                                    }

                                    @Override
                                    public void onProgress(int i) {

                                    }
                                });
                        mcontext.startActivity(imKit.getChattingActivityIntent(userId));
                    }
                    return true;
                }

            });
        }
    }

    @Override
    protected void getMessage(String data) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        initView();
    }

    @Override
    public void onRequstComplete(String result) {
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
            ContactActivity.ViewHolder viewHolder;

            String url = "";//需要增加群头像字段
            if (convertView == null) {
                viewHolder = new ContactActivity.ViewHolder();
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
                viewHolder = (ContactActivity.ViewHolder) convertView.getTag();
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
            ContactActivity.ViewHolder holder;

            if (convertView == null) {
                holder = new ContactActivity.ViewHolder();

                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.group_item, null);
                holder.tvNum = (TextView) convertView.findViewById(R.id.num);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ContactActivity.ViewHolder) convertView.getTag();
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

    public class ContactListAdapter extends BaseExpandableListAdapter {
        public ContactListAdapter() {
            queue = Volley.newRequestQueue(ContactActivity.this);
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
            final ContactActivity.ViewHolder viewHolder;
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
                viewHolder = new ContactActivity.ViewHolder();
                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_contact_item, null);
                viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ContactActivity.ViewHolder) convertView.getTag();
            }
            viewHolder.avatar.setImageResource(R.drawable.pho_touxiang);
            viewHolder.avatar.setTag(file);

            if (!DataUtil.isNullorEmpty(file)) {
                String url = DataUtil.getIconURL(mcontext, file);
                ((BaseActivity) mcontext).getImageLoader().displayImage(url, viewHolder.avatar,
                        ((BaseActivity) mcontext).getImageLoaderOptions());
            }
            if (viewHolder != null) {
                viewHolder.tvName.setText(name);
                viewHolder.tvTitle.setText(title);
            }
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
            ContactActivity.ViewHolder holder;
            String string = classList.get(groupPos).getClassName();
            if (convertView == null) {
                holder = new ContactActivity.ViewHolder();

                convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.group_item, null);
                holder.tvNum = (TextView) convertView.findViewById(R.id.num);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ContactActivity.ViewHolder) convertView.getTag();
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

        private void setImage(final ContactActivity.ViewHolder holder, String file, String userName) {
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

    public String getUserName() {
        return userName;
    }

}
