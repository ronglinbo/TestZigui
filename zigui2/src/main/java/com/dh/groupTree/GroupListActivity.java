package com.dh.groupTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.activity.AlarmbuKongActivity;
import com.dh.activity.BackPlayActivity;
import com.dh.activity.OperateSoundTalk;
import com.dh.activity.RealPlayActivity;
import com.dh.baseclass.BaseActivity;
import com.dh.groupTree.GroupListAdapter.IOnCheckBoxClick;
import com.dh.groupTree.GroupListAdapter.IOnItemClickListener;
import com.dh.groupTree.GroupListGetTask.IOnSuccessListener;
import com.dh.groupTree.SearchChannelsAdapter.IOnSearchChannelsClick;
import com.dh.groupTree.bean.ChannelInfoExt;
import com.dh.groupTree.bean.TreeNode;
import com.dh.util.AppDefine;
import com.dh.view.PullDownListView;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.LoginActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MoniterListInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;


public class GroupListActivity extends BaseActivity implements OnClickListener, IOnItemClickListener,
         PullDownListView.OnRefreshListioner, IOnCheckBoxClick,
        IOnSearchChannelsClick, HttpRequestAsyncTaskListener, RefreshListView.OnRefreshListener {

    // 打印标签
    private static final String TAG = "GroupListActivity";

    // 标题栏
    //private CommonTitle mTitle = null;

    // 组织树控件
    private ListView mGroupsLv;

    // 点击预览出现控件
    private RelativeLayout mSearchRlt = null;

    // 搜索到的控件
    //private ListView mSearchGroupLv = null;

    // 搜索播放控件
    private Button mConfirmBtn = null;

    // list控件
    private RelativeLayout mGroupRlt = null;

    // no data
    private TextView mNoDataTv = null;

    // 点击预览控件
    //private RelativeLayout mRealPlayRlt = null;

    // 隐形搜索框控件
    //private EditText mSearchEt = null;

    // 显性搜索框控件
    //private AutoCompleteTextView mSearchAutoEt = null;

    // 搜索框view
    private View myView = null;

    // 清空历史框view
    private View clearView = null;

    // 框控件
    private Button mClearBtn = null;

    // 搜索通道列表adapter
    private SearchChannelsAdapter mSearchChannelAdapter = null;

    // 搜索框adapter
    private GroupListAdapter mGroupListAdapter = null;

    // 获取实例
    private GroupListManager mGroupListManager = null;

    // 选中的nodes
    private List<TreeNode> selectNnodes = null;

    // 下拉刷新对象
    private PullDownListView mPullDownView = null;

    private RefreshListView mRefeshView = null;
    // 获取的树信息
    private TreeNode root = null;

    // 消息对象
    private Handler mHandler = null;

    // 等待对话框
    private ProgressBar mWattingPb = null;

    // 从哪个页面过来 1： 从实时预览进入组织列表 2：从回放进入组织列:3: 从电子地图进入组织列表
    private int comeFrom = 0;

    // 只能选择一个页面
    private boolean chooseOne = false;

    // 根view
    private RelativeLayout mRootLlt = null;

    // 组织树帮助类
    //private GroupListHelper mGroupListHelper = null;

    // 选中的通道
    private ChannelInfoExt mChannelInfoExt = null;

    // 屏幕宽度
    private int screenWidth = 0;

    // 通道列表
    private List<ChannelInfoExt> channelInfoExtList = null;

    // 搜索通道列表
    private List<ChannelInfoExt> searchList = null;
    
    // 显示选中个数
    private TextView mSeletedChannelsNumTv = null;
    
    /** 更新列表消息(const value:1000) */
    public static final int MSG_GROUPLIST_UPDATELIST = 1000;
    
    /** 点击进入回放消息 */
    public static final int MSG_GROUP_TO_PLAYBACK = 1005;
    private static final int EXPAND_LEVEL = 1;
    private String deviceId;
    private String deviceName;
    private List<String> channelNameList = new ArrayList<String>();
    private String[] dialogList;
    private LinearLayout layLogout;
    private RelativeLayout noMoniter;
    private String mDeviceId;
    private CustomDialog dialog;
    private static int position;
    private MoniterListInfo info;
//    private TreeNode rootNode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_fragment);
        // 查找控件
        findViews();

        // 设置监听器
        setListener();

        // 初始化数据
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * <p>
     * 获取布局控件
     * </p>
     * 
     * @author fangzhihua 2014-5-6 下午2:27:25
     */
    private void findViews() {
    	View view = findViewById(R.id.groupList_title);
        TextView title = (TextView) view.findViewById(R.id.title_text_2);
        title.setVisibility(View.VISIBLE);
        UserType user = CCApplication.getInstance().getPresentUser();
        if(user != null && Constants.PARENT_STR_TYPE.equals(user.getUserType())){
            title.setText(R.string.parent_moniter);
        }else {
            title.setText(R.string.school_moniter);
        }
        noMoniter = (RelativeLayout) findViewById(R.id.no_moniter);
    	layLogout = (LinearLayout)view.findViewById(R.id.title_back);
        mRootLlt = (RelativeLayout) this.findViewById(R.id.group_list_rlt);
//        mGroupsLv = (ListView) this.findViewById(R.id.group_list);

        mConfirmBtn = (Button) this.findViewById(R.id.confirm_btn);
        mGroupRlt = (RelativeLayout) this.findViewById(R.id.group_rlt);
        mNoDataTv = (TextView) this.findViewById(R.id.search_no_data_tv);
        // 等待对话框布局
        mWattingPb = (ProgressBar) findViewById(R.id.grouplist_waitting_pb);
//        mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
        mRefeshView = (RefreshListView) findViewById(R.id.group_list);
    }

    /**
     * <p>
     * 设置控件的监听效果
     * </p>
     * 
     * @author fangzhihua 2014-5-6 下午2:34:04
     */
    private void setListener() {
    	layLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GroupListActivity.this.finish();
			}
		});
        // 开始播放控件点击
        mConfirmBtn.setOnClickListener(this);
        // 下拉刷新
//        mPullDownView.setRefreshListioner(this);
        mRefeshView.setOnRefreshListener(this);
    }
    
    private void showGroupList() {
        mSearchRlt.setVisibility(View.GONE);
        mSearchChannelAdapter.notifyDataSetChanged();
        mGroupRlt.setVisibility(View.VISIBLE);
    }

    /**
     * <p>
     * 初始化数据
     * </p>
     * 
     * @author fangzhihua 2014-5-7 上午10:05:54
     */
    private void initData() {

        Display display = this.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();

        mHandler = createHandler();
//        mGroupsLv = mPullDownView.mListView;
        mGroupListManager = GroupListManager.getInstance();
        mGroupListAdapter = new GroupListAdapter(this);
        
        mGroupListAdapter.setListner(this, this);
        updateSelectChannels();
        
//        mGroupsLv.setAdapter(mGroupListAdapter);
        mRefeshView.setAdapter(mGroupListAdapter);
        channelInfoExtList = mGroupListManager.getChannelList();
        getMoniterInfo();
//        getGroupList();
    }

    private void parseMoniterInfoList(){
        //摄像头数目
        int totalCount = 0;
        if(info == null || (info != null && info.getInfoIpcSchoolInfo() == null)){
            if (mWattingPb != null) {
                mWattingPb.setVisibility(View.GONE);
            }
            DataUtil.getToast("贵学校未对接监控！");
            return;
        }
        if(info != null && info.getUserGroupList() != null){
            List<MoniterListInfo.UserGroupList> groupList = info.getUserGroupList();
            if(groupList != null){
                root = new TreeNode("...","1");
                for(MoniterListInfo.UserGroupList item:groupList){
                    int count = 0;
                    TreeNode node = new TreeNode(item.getUser_group_name(),item.getId());
                    int type = Integer.parseInt(item.getType());
                    node.setType(type);
                    node.setParent(root);
                    List<MoniterListInfo.IPCList> ipcList = item.getIPCList();
                    if(ipcList != null){
                        for(MoniterListInfo.IPCList ipcItem:ipcList){
                            TreeNode ipcNode = new TreeNode(ipcItem.getName(),ipcItem.getIpcId());
                            type = Integer.parseInt(ipcItem.getType());
                            ipcNode.setType(type);
                            ipcNode.setParent(node);

                            ChannelInfoExt channelInfo = new ChannelInfoExt();
                            channelInfo.setType(type);
                            channelInfo.setSzName(ipcItem.getName());
                            channelInfo.setSzId(ipcItem.getIpcId());
                            channelInfo.setState(/*ipcItem.getStatus()*/1);
                            ipcNode.setChannelInfo(channelInfo);
                            node.add(ipcNode);
                            count++;
                            totalCount += count;
                        }
                    }
                    List<MoniterListInfo.AreaList> areaList = item.getAreaList();
                    if(areaList != null){
                        for(MoniterListInfo.AreaList areaItem:areaList){
                            TreeNode areaNode = new TreeNode(areaItem.getGroupName(),areaItem.getId()+"");
                            type = Integer.parseInt(areaItem.getType());
                            areaNode.setType(1);//区域的类型仍为1，而不是2
                            areaNode.setParent(node);
                            List<MoniterListInfo.IPCList> areaIpcList = areaItem.getAreaIPCList();
                            if(areaIpcList != null){
                                for(MoniterListInfo.IPCList areaIpcItem:areaIpcList){
                                    TreeNode areaIpc = new TreeNode(areaIpcItem.getName(),areaIpcItem.getIpcId());
                                    type = Integer.parseInt(areaIpcItem.getType());
                                    areaIpc.setType(type);
                                    areaIpc.setParent(areaNode);
                                    ChannelInfoExt channelInfo = new ChannelInfoExt();
                                    channelInfo.setType(type);
                                    channelInfo.setSzName(areaIpcItem.getName());
                                    channelInfo.setSzId(areaIpcItem.getIpcId());
                                    channelInfo.setState(/*areaIpcItem.getStatus()*/1);
                                    areaIpc.setChannelInfo(channelInfo);
                                    areaNode.add(areaIpc);
                                    count++;
                                    totalCount += count;
                                }
                            }
                            node.add(areaNode);
                        }
                    }
//                    if(count > 0) {
                        root.add(node);
//                    }
                }

                if (mWattingPb != null) {
                    mWattingPb.setVisibility(View.GONE);
                }
                if(totalCount == 0){
                    if(noMoniter != null){
                        noMoniter.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                if (root != null) {
                    mGroupListAdapter.clearDate();
                    //不显示头节点，直接显示头节点的孩子节点
//                    addFirstLevel();
                    mGroupListAdapter.addNode(root);
                    // 设置默认展开级别
                    mGroupListAdapter.setExpandLevel(EXPAND_LEVEL);
                    mGroupListAdapter.notifyDataSetChanged();
                }
            }
        }else{
            if (mWattingPb != null) {
                mWattingPb.setVisibility(View.GONE);
            }
            if(noMoniter != null){
                noMoniter.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * <p>
     * 获取组织列表
     * </p>
     * 
     * @author fangzhihua 2014-5-12 上午9:56:14
     */
    private void getGroupList() {
        root = mGroupListManager.getRootNode();
        if (root == null) {
            mWattingPb.setVisibility(View.VISIBLE);
        }

        if (mGroupListManager.getTask() != null) {
            mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
        }
        if (mGroupListManager.isFinish() && root != null) {
        	if (root.getChildren().size() == 0) {
        		 mGroupListManager.startGroupListGetTask();
        	}
        	Log.i(TAG, "getGroupList finished---" + root.getChildren().size());
            sendMessage(mHandler, MSG_GROUPLIST_UPDATELIST, 0, 0);
            return;
        } else if (root == null) {
            if (mGroupListManager.getTask() == null) {
                // 获取组织树任务
            	Log.i(TAG, "开始 执行GroupListGetTask");
                mGroupListManager.startGroupListGetTask();
                mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
            }
        }

    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     * 
     * @param field
     *            保存在sharedPreference中的字段名
     * @param autoCompleteTextView
     *            要操作的AutoCompleteTextView
     */
    private void saveHistory(String field, AutoCompleteTextView autoCompleteTextView) {
        String text = autoCompleteTextView.getText().toString();
        SharedPreferences sp = getSharedPreferences("search_history", 0);
        String longhistory = sp.getString(field, "");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
     * 
     * @param field
     *            保存在sharedPreference中的字段名
     * @param autoCompleteTextView
     *            要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field, AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("search_history", 0);
        String longhistory = sp.getString("history", "");
        String[] histories = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.group_list_auto_complete, histories);
        // 只保留最近的6条的记录
        if (histories.length > 6) {
            String[] newHistories = new String[6];
            System.arraycopy(histories, 0, newHistories, 0, 6);
            adapter = new ArrayAdapter<String>(this, R.layout.group_list_auto_complete, newHistories);
        }
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    /**
     * <p>
     * 清空历史记录
     * </p>
     * 
     * @author fangzhihua 2014年6月25日 下午4:11:29
     */
    private void clearHistory(AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("search_history", 0);
        Editor mEditor = sp.edit();
        mEditor.clear();
        mEditor.commit();
        String[] histories = null;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.group_list_auto_complete, histories);
        autoCompleteTextView.setAdapter(adapter);
    }

    /**
     * <p>
     * 创建消息对象
     * </p>
     * 
     * @author fangzhihua 2014-5-12 上午10:10:20
     * @return
     */
    private Handler createHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_GROUPLIST_UPDATELIST:
                        // 处理更新列表
//                        handleUpdateList();
                        break;
                    case MSG_GROUP_TO_PLAYBACK:
                        //handleClickPlayback(msg.obj, msg.arg1);     //处理点击回放
                    default:
                        break;
                }
            }

            /**
             * <p>
             * 处理更新列表
             * </p>
             * 
             * @author fangzhihua 2014年7月10日 上午9:07:35
             */
            private void handleUpdateList() {
                root = mGroupListManager.getRootNode();

                mGroupListManager.setOnSuccessListener(mIOnSuccessListener);

                // 这里表示刷新处理完成后把上面的加载刷新界面隐藏
                mPullDownView.onRefreshComplete();

                if (mWattingPb != null) {
                    mWattingPb.setVisibility(View.GONE);
                }

                mGroupListAdapter.clearDate();
                //不显示头节点，直接显示头节点的孩子节点
//                addFirstLevel();
                mGroupListAdapter.addNode(root);
                // 设置默认展开级别
                mGroupListAdapter.setExpandLevel(EXPAND_LEVEL);
                mGroupListAdapter.notifyDataSetChanged();
            }
        };
        return handler;
    }

    IOnSuccessListener mIOnSuccessListener = new IOnSuccessListener() {
        @Override
        public void onSuccess(final boolean success, final int errCode) {
        	
        	mHandler.post(new Runnable() {
				
				@Override
				public void run() {
		            // 清空任务
		            mGroupListManager.setTask(null);

		            if (mWattingPb != null) {
		                mWattingPb.setVisibility(View.GONE);
		            }
		            if (success) {
		                root = mGroupListManager.getRootNode();
		                if (root != null) {
		                    mGroupListAdapter.clearDate();
                            //不显示头节点，直接显示头节点的孩子节点
//                            addFirstLevel();
		                    mGroupListAdapter.addNode(root);
		                    // 设置默认展开级别
		                    mGroupListAdapter.setExpandLevel(EXPAND_LEVEL);
		                    mGroupListAdapter.notifyDataSetChanged();
		                } else {
		                    mGroupListAdapter.clearDate();
		                    mGroupListAdapter.notifyDataSetChanged();
		                }
		                //updateSelectChannels();
		            } else {
		                showToast(R.string.grouplist_getgroup_fail, errCode);
		            }

				}
			});
        }
    };

    private void addFirstLevel(){
        List<TreeNode> list = root.getChildren();
        if(list != null) {
            for (TreeNode item : list) {
                List<TreeNode> sublist = item.getChildren();
                if (sublist != null) {
                    for (TreeNode subItem : sublist) {
                        mGroupListAdapter.addNode(subItem);
                    }
                }
            }
        }
    }
	@Override
    public void onDestroy() {
        super.onDestroy();
      //  mGroupListHelper.closeSetTimePopupWindow();
        if(mGroupListManager.getRootNode() != null) {
        	mGroupListManager.setRootNode(null);
        }
        mGroupListAdapter.setAllUnExpanded();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void updateSelectChannels() {
        selectNnodes = mGroupListAdapter.getSeletedNodes();
        if (selectNnodes.size() > 32) {
            showToast(R.string.select_channel_limit_tv);
        }
    }

 

    @Override
    public void onItemClick(TreeNode treeNode, boolean isChecked, final int position) {

        if (treeNode.getType() == 2) { // 1：组 2：设备 3：通道
            mDeviceId = treeNode.getDeviceInfo().getDeviceId();
            deviceName = treeNode.getDeviceInfo().getDeviceName();
            //判断设备类型是否是报警主机
            int devType = treeNode.getDeviceInfo().getdeviceType();
            if(devType == 601) {                                                     //报警主机类型601
                dialogList = new String[] {"实时","回放", "布控报警", "报警主机"};
                Log.i("报警类型的设备名称是：", treeNode.getDeviceInfo().getDeviceName());  //如果点击是报警主机dialog 就再加一行
            } else {
                dialogList = new String[] {"实时","回放", "布控报警", "语音对讲"};
            }
            Log.i(TAG, "选择的设备mc是：" + deviceName + "选择的设备类型是：" + devType);
            //TODO 获取设备下面的通道
            //treeNode.getChannelInfo()
        }

        if (treeNode.getType() == 3) {                     //通道
            if (dialogList == null) {                  //没有设备的业务树
                dialogList = new String[] {"实时","回放", "布控报警", "语音对讲"};
            }
            Log.i(TAG, "tongdao is clicked");
            this.position = position;
            if(isUsingMobileNetwork()){
                popTip();
            }else{
                goRealPlay(position);
            }

        } else {
//            mGroupListAdapter.expandNode(root);
            if (mGroupListAdapter.ExpandOrCollapse(root,position)) {
//                mGroupsLv.setSelection(1);
            }
        }
    }

    private void goRealPlay(int position){
        ChannelInfoExt chnlInfoExt = ((TreeNode)mGroupListAdapter.getItem(position)).getChannelInfo();
        String channelName =  chnlInfoExt.getSzName();
        String channelId = chnlInfoExt.getSzId();
        String deviceId = chnlInfoExt.getDeviceId();
        Intent intent = new Intent();
        Log.i(TAG, "channelName channelId" + channelName + channelId);
        //跳转到实时
        //把通道名称传到RealPlayActivity显示
        if(chnlInfoExt != null) {
            intent.putExtra("channelName", channelName);
            intent.putExtra("channelId", channelId);
        }
        intent.setClass(GroupListActivity.this, RealPlayActivity.class);
        startActivity(intent);
    }


    private void popTip(){
        new AlertDialog.Builder(this)
                .setTitle("您当前处于移动数据网络，确定要打开监控画面吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goRealPlay(position);
                    }
                }).show();
    }

    //是否使用移动网络
    private boolean isUsingMobileNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null && info.isAvailable()){
            if("WIFI".equals(info.getTypeName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRefresh() {
//        getGroupList();
        getMoniterInfo();
        mPullDownView.onRefreshComplete();
//        getGroupList();
    	Log.i("GroupListActivity", "onRefresh..");
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mGroupListManager.getRootNode() != null) {
            	mGroupListManager.setRootNode(null);
            	Log.i("TAG", "onKeyDown");
            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSearchChannelsClick(ChannelInfoExt channelInfoExt, boolean flag) {
        // 保存历史搜索
      /*  saveHistory("history", mSearchAutoEt);
        // 隐藏搜索框
        InputMethodManager inputManager = (InputMethodManager) mSearchAutoEt.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(mSearchAutoEt.getWindowToken(), 0);
        }*/
        // 如果是多选，则不处理
        if (!flag) {
            return;
        }
        switch (comeFrom) {
            case AppDefine.FROM_LIVE_TO_GROUPLIST:

                Intent mIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppDefine.FROM_GROUPLIST, true);
                bundle.putBoolean(AppDefine.NEED_PLAY, true);
                bundle.putSerializable(AppDefine.SELECTED_CHANNEL, channelInfoExt);
                mIntent.putExtras(bundle);
                setResult(RESULT_FIRST_USER, mIntent);
                finish();
                break;
            case AppDefine.FROM_PLAYBACK_TO_GROUPLIST:
                mChannelInfoExt = channelInfoExt;
                break;
            case AppDefine.FROM_GIS_TO_GROUPLIST:
                Intent mIntent2 = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(AppDefine.SELECTED_CHANNEL, channelInfoExt);
                mIntent2.putExtras(bundle2);
                setResult(RESULT_OK, mIntent2);
                finish();
                break;
            default:
                break;
        }
    }

	@Override
	public void onCheckBoxClick(TreeNode treeNode, boolean isChecked,
			int position) {
		
	}

	@Override
	public void onClick(View arg0) {
		
	}

    //获取监控服务器和摄像头列表
    private void getMoniterInfo(){
        JSONObject json = new JSONObject();
        String url = new StringBuilder(Constants.SERVER_URL)
                .append(Constants.GET_MONITER_LIST).toString();
        String result;
        UserType user = CCApplication.app.getPresentUser();
        if(user == null) return;
        try {
            String userType = user.getUserType();
            json.put("userId", user.getUserId());
            json.put("userType", userType);
            json.put("schoolId", user.getSchoolId());
            if (Constants.PARENT_STR_TYPE.equals(userType)) {
                json.put("studentId", user.getChildId());
            }
            new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_MONITER_LIST);
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequstComplete(String result) {
        info = JsonUtils.fromJson(result, MoniterListInfo.class);
        if(info != null && info.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
            if(info.getInfoIpcSchoolInfo() != null) {
                parseMoniterInfoList();
                CCApplication.getInstance().setMoniterInfo(info);
            }
            if(info == null || (info != null && info.getInfoIpcSchoolInfo() == null)){
                if (mWattingPb != null) {
                    mWattingPb.setVisibility(View.GONE);
                }
//                DataUtil.getToast("贵学校未对接监控！");
                if(noMoniter != null) {
                    noMoniter.setVisibility(View.VISIBLE);
                    TextView tip = (TextView) noMoniter.findViewById(R.id.tip);
                    if (tip != null) {
                        tip.setText(R.string.no_moniter_service);
                    }
                }
                return;
            }
        }else{
            DataUtil.getToast("获取监控信息错误");
        }
    }

    @Override
    public void onRequstCancelled() {

    }

    @Override
    public void onDownPullRefresh() {
        getMoniterInfo();
        mRefeshView.hideHeaderView();
    }

    @Override
    public void onLoadingMore() {
        mRefeshView.hideFooterView();
    }
}
