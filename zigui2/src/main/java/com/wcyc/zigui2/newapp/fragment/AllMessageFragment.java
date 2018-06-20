
package com.wcyc.zigui2.newapp.fragment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWCustomConversationBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.activity.LoginActivity;
import com.wcyc.zigui2.newapp.activity.NewEducationInfoActivity;
import com.wcyc.zigui2.newapp.activity.PayNoticeActivity;
import com.wcyc.zigui2.newapp.activity.PaymentListActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageDetailActivity;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.SetReadBean;
import com.wcyc.zigui2.newapp.bean.SystemAlertMessageBean;
import com.wcyc.zigui2.newapp.home.SchoolIntroductionActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.module.consume.OneCardActivity;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.newapp.module.leavemessage.LeaveMeassageActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsDetailsActivity;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.chat.ChatActivity;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;

import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean.HXUser;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;

import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.newapp.module.consume.NewConsumeActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.module.duty.NewDutyInquiryActivity;
import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.leave.NewMyLeaveActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryActivity;
import com.wcyc.zigui2.newapp.module.wages.NewWagesActivity;
import com.wcyc.zigui2.newapp.widget.MessageItemPop;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
//import com.wcyc.zigui2.newapp.widget.SlideListView.DelButtonClickListener;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.GoHtml5Function;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.SystemMessageDialog;


/**
 * 消息记录Fragment
 */
public class AllMessageFragment extends Fragment
        implements HttpRequestAsyncTaskListener, MessageItemPop.OnLongClick, IYWPushListener {

    private static final int ACTION_GET_SYSTEM_NEW = 4;
    private InputMethodManager inputMethodManager;

    private RefreshListView listView;
    private AllContactListBean contactList;
    private List<NewMessageBean> messageList;
    //	private ChatHistoryAdapter adapter;
    private AllMessageAdapter adapter;
    private ImageButton clearSearch;
    public RelativeLayout errorItem;
    public RelativeLayout goContactsList;
    public TextView errorText, tv_no_message;
    private boolean hidden;
    private String msgNumb;
    private Context mContext;
    private CustomDialog dialog;
    private static final int ACTION_UPDATE_MESSAGE_PAY_NOTICE = 7;
    private static final int ACTION_UPDATE_MESSAGE = 6;
    private static final int ACTION_CLEAR_MODEL = 1;
    private static final int ACTION_GET_MESSAGE = 2;
    private static final int ACTION_GET_CORRELATION_MESSAGE = 3;//关联消息
    private static final int ACTION_DEL_READ_MESSAGE = 3;
    private int action;
    private String type;
    private JsInterface jsInterface;
    private View view;
    //	private Advertisement ad;
    private WebView ad;
    private int pos;
    private SystemMessageDialog dialog1;
    //阿里百川消息类型
    private NewMessageBean aliMessageContainer;

//    private String[][] func =
//            {{"1", "系统消息"}, {"33", "订单催缴"}, {"34", "缴费"},
//
//                    {"2", "通知1"}, {"3", "资源状态改变消息"}, {"4", "版本更新"}, {"5", "续费提醒"},
//                    {"6", "成绩"}, {"7", "点评"}, {"8", "作业"}, {"9", "校园风采"},
//                    {"10", "班级动态"}, {"11", "考勤"}, {"12", "回复意见"}, {"13", MenuItem.CONSUME},
//                    {"14", "邮件"}, {"15", "待办事项"}, {"16", "工资条"}, {"17", "值班查询"},
//                    {"18", "校长信箱"}, {"19", "日志"}, {"20", "总结"}, {"21", "考试"},
//                    {"22", "业务办理"}, {"23", "学生请假单"}, {"24", "业务办理"}, {"25", "业务办理"}, {"29", "班牌留言"}};

    private String[] messageTypeList = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34"};

    //    //消息列表 Item 点击之后会跳转到相应的Activity
//    private HashMap activities = new HashMap() {
//        {
//            put(MenuItem.NOTICE/*"通知1"*/, NotifyActivity.class);
//            put(MenuItem.SCHOOLMAIL/*"校长信箱"*/, SchoolMasterMailActivity.class);
//            put("总结"/*"总结"*/, SummaryActivity.class);
//            put("日志"/*"日志"*/, DailyRecordActivity.class);
//            put(MenuItem.EMAIL/*"邮件"*/, EmailActivity.class);
//            put("工资条"/*"工资条"*/, NewWagesActivity.class);//
//            put(MenuItem.LEAVE/*"请假条"*/, NewMyLeaveActivity.class);//
//            put("值班查询"/*"值班查询"*/, NewDutyInquiryActivity.class);
//            put(MenuItem.CONSUME, NewConsumeActivity.class);
//            put(MenuItem.DYNAMICS/*"班级动态"*/, NewClassDynamicsActivity.class);
//            put(MenuItem.SCHOOLNEWS/*"校园新闻"*/, NewSchoolNewsActivity.class);
////            put("教育资讯", EducationInforActivity.class);
//            put("教育资讯"/*"教育资讯"*/, NewEducationInfoActivity.class); //新版的教育资讯页面
//            put(MenuItem.LEAVE_MESSAGE/*"班牌留言"*/, LeaveMeassageActivity.class);
//
//            put("系统消息"/*"系统消息"*/, SystemMessageActivity.class);
//            put("订单催缴"/*"订单催缴"*/, PayNoticeActivity.class);
//            put(MenuItem.PAYMENT/*"缴费"*/, PaymentListActivity.class);
//        }
//    };
    private IYWConversationService conversationService;

    public static Fragment newInstance(int index) {
        Fragment fragment = new AllMessageFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        //fragment.setIndex(index);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_fragment_conversation_history, null);
        return view;
    }


    public void onDestroyView() {
        if (ad != null) {
            ad.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ad.clearHistory();

            ((ViewGroup) ad.getParent()).removeView(ad);
            ad.destroy();
            ad = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        if (refreshBanner != null && mContext != null) {
            mContext.unregisterReceiver(refreshBanner);
            refreshBanner = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 当Activity不可见的时候停止切换
        //ad.stopAd();
        if (message != null) {
            mContext.unregisterReceiver(message);
            message = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//		ad.startAd();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ChatLoginService.LOAD_ALL_MESSAGE);
        getActivity().registerReceiver(message, filter);
//		getCorrelationMessage();

        IntentFilter mrefeshDataFilter = new IntentFilter(HomeFragment.INTENT_SWITCH_USER);
        getActivity().registerReceiver(refreshBanner, mrefeshDataFilter);

        //获取到系统弹窗
        getSystemMessage();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mContext = getActivity();
        //ad = new Advertisement();

        ad = (WebView) getView().findViewById(R.id.ad);
        ad.getSettings().setAppCacheEnabled(false);    //取消缓存
//        ad.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);    //设置缓存模式
        ad.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        ad.getSettings().setJavaScriptEnabled(true);    //设置WebView属性，能够执行JavaScript脚本
        jsInterface = new JsInterface();
        ad.addJavascriptInterface(jsInterface, "android");
        ad.setWebViewClient(new WebViewClient() {        //web 视图
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        ad.setBackgroundColor(Color.parseColor("#eeeeee")); // 设置背景色
        ad.getBackground().setAlpha(1); // 设置填充透明度 范围：0-255

        //去掉纵向滚动条
        ad.setVerticalScrollBarEnabled(false);

        loadBanner();

        initAliChat();
//        ad.loadUrl(Constants.AD_SLIDE_URL);    // 加载需要显示的网页

        //ad.initAd(view,mContext);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);

        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        //没有消息时的显示
        tv_no_message = (TextView) getView().findViewById(R.id.tv_no_message);
        // contact list
        //contactList = CCApplication.getInstance().getContactList();
        contactList = CCApplication.getInstance().getAllContactList();
        listView = (RefreshListView) getView().findViewById(R.id.list);
        initEvent();
    }

    private void loadBanner() {
        UserType user = CCApplication.getInstance().getPresentUser();
        HashMap<String, String> para = new HashMap<>();

        NewMemberBean newMemberBean = CCApplication.app.getMemberInfo();
        if (null == newMemberBean) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("conflict", true);
            mContext.startActivity(intent);
            return;
        }
        String imgAuthId = Constants.AUTHID + "@" + ((BaseActivity) getActivity()).getDeviceID()
                + "@" + newMemberBean.getAccId();
        para.put("X-School-Id", user.getSchoolId());
        para.put("X-mobile-Type", "android");
        para.put("imgAuthId", imgAuthId);
        String url = Constants.URL + "/app_slideshow/homeSlideshow.do";
        ad.loadUrl(url, para);
    }

    /*
            * 负责和js通信的所有方法.
     */
    public class JsInterface {
        //学校简介
        @JavascriptInterface
        public void openADUrl(String linkedType, String linkedUrl, String relationId) {
            //学校简介
            if (linkedType.equals("4")) {
                Intent intent = new Intent(getActivity(), SchoolIntroductionActivity.class);
                intent.putExtra("url", linkedUrl);
                intent.putExtra("title", "学校简介");
                getActivity().startActivity(intent);
            }
            //教育资讯链接
            if (linkedType.equals("0")) {
                Intent intent = new Intent(getActivity(), EducationDetailsActivity.class);
                intent.putExtra("campuNewsId", relationId);
                intent.putExtra("title", "教育资讯");
                getActivity().startActivity(intent);
            }
            //校园新闻链接
            if (linkedType.equals("1")) {
                Intent intent = new Intent(getActivity(), NewSchoolNewsDetailsActivity.class);
                intent.putExtra("campuNewsId", relationId);
                intent.putExtra("title", "校园新闻");
                getActivity().startActivity(intent);
            }

            //外部链接
            if (linkedType.equals("2")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(linkedUrl);
                intent.setData(content_url);
                startActivity(intent);

            }
        }


    }

    private void initEvent() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewMessageBean message = adapter.getItem(position - 1);

                if ("chat".equals(message.getMessageType())) {
                    HXUser user = message.getHxUser();
                    String userName = user.getUserName();
                    String userNick = user.getNickName();
                    String avatarUrl = user.getIconUrl();
                    String cellPhone = user.getCellPhone();
                    String userTitle = user.getTitle();
                    int chatType = user.getChatType();
                    String hxUserName = CCApplication.getInstance().getUserName();
                    message.setCount("0");
                    if (userName.equals(hxUserName))
                        Toast.makeText(getActivity(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
                    else {
                        // 进入聊天页面
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("chatType", chatType);
                        intent.putExtra("userNick", userNick);
                        intent.putExtra("avatar", avatarUrl);
                        intent.putExtra("cellPhone", cellPhone);
                        intent.putExtra("userTitle", userTitle);
                        if (chatType == ChatActivity.CHATTYPE_SINGLE) {
                            intent.putExtra("userId", userName);
                            startActivity(intent);
                        } else {
//							if(DataUtil.isInGroup(userName)) {
                            intent.putExtra("groupId", userName);
                            startActivity(intent);
//							}else{
//								DataUtil.getToast("您已被移出群组");
//							}
                        }
                    }

                    //如果是阿里百川.
                } else if (message.getMessageTypeName().equals("会话列表")
                        || message.getMessageType().equals("aLiChat")) {
                    MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();

                    if (!DataUtil.isNullorEmpty(detail.getHxUsername()) && !DataUtil.isNullorEmpty(detail.getHxPassword())) {
                        if (CCApplication.getInstance().getIMKit() == null) {
                            YWIMKit mIMKit = YWAPI.getIMKitInstance(detail.getHxUsername(), Constants.BAI_CHUAN_APPKEY);
                            CCApplication.getInstance().setIMKit(mIMKit);
                        }

                        Intent intent = CCApplication.getInstance().getIMKit().getConversationActivityIntent();
                        startActivity(intent);
                    } else {
                        DataUtil.getToastShort("没有聊天账号,请于管理员联系");
                    }

                } else {
                    gotoActivity(message.getMessageTypeName(), message.getMessageType());
                }

                ((HomeActivity) getActivity()).updateUnreadLabel();
            }
        });


        // 注册上下文菜单
//		registerForContextMenu(listView);

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        listView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onDownPullRefresh() {//下拉刷新
                // TODO Auto-generated method stub
                getNewMessage();
                ((HomeActivity) getActivity()).getModelRemind();
                listView.hideHeaderView();
            }

            @Override
            public void onLoadingMore() {//上拉加载更多
                // TODO Auto-generated method stub
                listView.hideFooterView();
            }

        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                pos = (int) arg3;

                NewMessageBean bean = messageList.get(pos);

//                DeleteItemPop pop = new DeleteItemPop(mContext, AllMessageFragment.this);

                //系统消息和缴费提醒 不能被删除
                if (bean.getMessageType().equals(Constants.SYSTEM_MESSAGE) && bean.getCount().equals("0")) {
                    return false;
                }

                if (bean.getMessageType().equals(Constants.PAY_NOTICE) && bean.getCount().equals("0")) {
                    return false;
                }

                MessageItemPop pop = new MessageItemPop(mContext, bean, AllMessageFragment.this);

                int[] location = {-1, -1};
                arg1.getLocationOnScreen(location);
                pop.showAtLocation(arg1, Gravity.NO_GRAVITY, arg1.getWidth() / 2 - 40, location[1]);
                return true;
            }

        });
    }

    /**
     * 点击Adapter的Item 跳转到相应的Activity页面
     *
     * @param messageTypeName
     * @param messageType
     */
    private void gotoActivity(String messageTypeName, String messageType) {
        Log.i("临时TAG", "messageTypeName:" + messageTypeName + "-----messageType:" + messageType);
        if (DataUtil.isNullorEmpty(messageTypeName)) {
            messageTypeName = DataUtil.convertTypeToName(messageType);
        }
        int functionNumber = MenuItem.nameToNumber(messageTypeName);
        boolean ret = CCApplication.app.CouldFunctionBeUseFromConfig(messageTypeName, functionNumber, 0);
        if (ret == true) {
            if (CCApplication.app.couldClearRemind(functionNumber)) {
                clearRemind(messageType);
            }

            Intent intent = new Intent();
            Class cls;
//			if(isAdmin()){
            cls = MenuItem.getMessageToClass(messageType);
            if (cls == null) {
                cls = MenuItem.getMessageToClass(messageTypeName);
            }

//            cls = (Class) activities.get(messageTypeName);
//			}else{
//				cls = (Class) activities1.get(messageTypeName);
//			}
            if (cls != null) {
                intent.setClass(mContext, cls);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                GoHtml5Function.goToHtmlApp(mContext, messageTypeName, MenuItem.nameToNumber(messageTypeName));
            }
        } else {
            chargePop(messageTypeName, functionNumber);
        }
    }

    //弹出充值提示框
    public void chargePop(String messageTypeName, int functionNumber) {
        dialog = new CustomDialog(mContext, R.style.mystyle,
                R.layout.customdialog, handler, messageTypeName, functionNumber);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle(getResources().getString(R.string.vip));
        dialog.setContent(getResources().getString(R.string.vip_tip));
    }

    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消退出
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认
                    if (getResources().getString(R.string.vip).equals(dialog.getTitle())) {
                        //充值  开通
                        Bundle bundle = msg.getData();
                        String para = bundle.getString("para");
                        Intent intent = new Intent(mContext, NewRechargeProductActivity.class);
                        intent.putExtra("module", para);
                        intent.putExtra("moduleNumber", MenuItem.COURSE_NUMBER);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 刷新页面.
     */
    public void refresh() {
        System.out.println("AllMesssageFragment refresh");
        NewMessageListBean all = CCApplication.getInstance().getMessageList();
        if (all != null) {
            messageList = all.getMessageList();
        } else {
            messageList = null;
        }
        initAdapter();
    }

    private void initAdapter() {
        if (messageList != null) {
            sortMessageByTime(messageList);
            adapter = new AllMessageAdapter(CCApplication.app.getApplicationContext(),
                    R.layout.row_all_message, messageList);
            if (listView != null) {
                listView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            //如果没有消息那么显示没有消息的界面
            if (adapter.isEmpty()) {
                if (tv_no_message != null) {
                    tv_no_message.setVisibility(View.VISIBLE);
                }
            } else {
                if (listView != null) {
                    listView.setVisibility(View.VISIBLE);
                }
                if (tv_no_message != null) {
                    tv_no_message.setVisibility(View.GONE);
                }
            }
        } else {
            if (listView != null) {
                listView.setVisibility(View.GONE);
            }
            if (tv_no_message != null) {
                tv_no_message.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        if (message == null) return digest;
        switch (message.getType()) {
            case LOCATION: // 位置消息

                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
//			digest = getString(context, R.string.picture)
//					+ imageBody.getFileName();
                //在消息列表界面中，图片消息不显示图片路径
                digest = getString(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                digest = txtBody.getMessage();
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    String getString(Context context, int resId) {
        if (context != null)
            return context.getResources().getString(resId);
        else
            return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("allMessageFragment loadMessage！！！");
        initMessage();
        getNewMessage();
        loadBanner();
    }

    /**
     * 获取到最新的系统消息
     */
    public void getSystemMessage() {
        JSONObject json = new JSONObject();
        try {
            UserType user = CCApplication.getInstance().getPresentUser();
            if (user != null) {
                json.put("userId", user.getUserId());
                String type = user.getUserType();
                json.put("userType", type);

                if (type.equals("3")) {
                    String childId = CCApplication.get().getPresentUser().getChildId();
                    json.put("studentId", childId);
                }
                String url = new StringBuilder(Constants.BASE_URL).append(
                        Constants.GET_SYSTEM_MESSAGE_BY_USER).toString();
                String result = HttpHelper.httpPostJson(this.getActivity(), url, json);
                parseAlertNews(result);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void parseAlertNews(String result) {
        if (DataUtil.isNullorEmpty(result)) {
            return;
        }
        SystemAlertMessageBean bean = JsonUtils.fromJson(result, SystemAlertMessageBean.class);
        if (bean.getServerResult().getResultCode() == 200 && bean.getBmc() != null) {


            final SystemAlertMessageBean.BmcBean bmc = bean.getBmc();
            String userId = CCApplication.getInstance().getPresentUser().getUserId();
            String userType = CCApplication.getInstance().getPresentUser().getUserType();
            String studentId = "";
            if (userType.equals("3")) {
                studentId = CCApplication.getInstance().getPresentUser().getChildId();
            }
            final int messageId = bmc.getId();
            final String detailURL = new StringBuilder(Constants.URL)
                    .append(Constants.SYSTEM_MESSAGE_DETAIL_URL)
                    .append("userId=")
                    .append(userId)
                    .append("&userType=")
                    .append(userType)
                    .append("&studentId=")
                    .append(studentId)
                    .append("&messageId=")
                    .append(bmc.getId()).toString();


            System.out.println("消息详情页面地址:" + detailURL);

            if (dialog1 == null) {
                dialog1 = new SystemMessageDialog(getActivity(), R.style.mystyle, R.layout.system_message_dialog);
            }

            //改成弹窗样式
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.show();

            Button bt_detail = (Button) dialog1.findViewById(R.id.cancel_btn);
            Button bt_know = (Button) dialog1.findViewById(R.id.confirm_btn);

            dialog1.setTitle(bean.getBmc().getTypeName());
            dialog1.setTime(bmc.getOperateTime());
            dialog1.setContent(bmc.getTitle());

            if (bt_detail != null) {
                bt_detail.setText("查看详情");
                bt_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", detailURL);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), SystemMessageDetailActivity.class);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        dialog1.dismiss();
                    }
                });
            }

            if (bt_know != null) {
                bt_know.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IKnow(messageId);
                        dialog1.dismiss();
                    }
                });
            }

        }

    }

    // 弹窗_我知道了
    private void IKnow(int messageId) {

        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("id", messageId);
                json.put("userType", user.getUserType());
                json.put("userId", user.getUserId());
                if (user.getUserType().equals("3")) {
                    String childId = CCApplication.getInstance().getPresentUser().getChildId();
                    json.put("studentId", childId);
                }
                System.out.println("弹窗设为已读:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String url = new StringBuilder(Constants.BASE_URL).append(
                    Constants.UPDATE_MESSAGE_APP_STATUS).toString();
            System.out.println("请求URL" + url);
            try {
                String result = HttpHelper.httpPostJson(mContext, url, json);
                System.out.println("弹窗设为已读:" + result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public AllMessageAdapter getAdapter() {
        return adapter;
    }

    public void getNewMessage() {
        JSONObject json = new JSONObject();
        try {
            UserType user = CCApplication.getInstance().getPresentUser();
            if (user != null) {
                json.put("userId", user.getUserId());
                String type = user.getUserType();
                json.put("userType", type);
                if (type.equals(Constants.PARENT_STR_TYPE)) {
                    json.put("childId", user.getChildId());
                }
                System.out.println("AllMessageFragment getNewMessage:" + json);
                new HttpRequestAsyncTask(json, this, getActivity(),true)
                        .execute(Constants.GET_ALL_MESSAGE_URL);
                action = ACTION_GET_MESSAGE;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getCorrelationMessage() {
        JSONObject json = new JSONObject();
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();

        UserType user = CCApplication.getInstance().getPresentUser();
        String userId = user.getUserId();
        String childId = user.getChildId();
        List<UserType> list = CCApplication.getInstance().getMemberInfo().getUserTypeList();
        if (detail == null) return;
        List<NewChild> childList = detail.getChildList();
        if (childList != null) {
            for (int i = 0; i < list.size(); i++) {
                try {
                    UserType other = list.get(i);
                    if (!childId.equals(other.getChildId())) {
                        if (user != null) {
                            json.put("userId", userId);
                            String type = other.getUserType();
                            json.put("userType", type);
                            if (!Constants.TEACHER_STR_TYPE.equals(type)) {
                                json.put("childId", other.getChildId());
                            }
                            System.out.println("getCorrelationMessage:" + json);
                            action = ACTION_GET_CORRELATION_MESSAGE;
                            new HttpRequestAsyncTask(json, this, getActivity())
                                    .execute(Constants.GET_CORRELATION_MESSAGE);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //只显示实现的模块消息
    private NewMessageListBean removeUnusedMessage(NewMessageListBean list) {
        List<NewMessageBean> messageList = list.getMessageList();
        NewMessageListBean ret = new NewMessageListBean();
        List<NewMessageBean> used = new ArrayList<NewMessageBean>();
        if (messageList != null) {
            for (NewMessageBean message : messageList) {
                for (String type : Constants.func) {
                    if (type.equals(message.getMessageType())) {
                        used.add(message);
                    }
                }
            }
        }
        ret.setMessageList(used);
        return ret;
    }

    //处理消息列表
    private NewMessageListBean handleMessage(NewMessageListBean list) {
        List<NewMessageBean> messageList = list.getMessageList();

        NewMessageListBean ret = new NewMessageListBean();
        List<NewMessageBean> handled = new ArrayList<NewMessageBean>();
        List<NewMessageBean> fill = new ArrayList<NewMessageBean>();

        if (messageList != null) {
            for (NewMessageBean message : messageList) {
                String name = message.getMessageTypeName();
                String messageType = message.getMessageType();
//                //填充消息名称
//                if (name == null) {
//                    for (int i = 0; i < messageTypeList.length; i++) {
//                        String type = messageTypeList[i];
//                        if (type.equals(messageType)) {
//                            message.setMessageTypeName(MenuItem.getMessageName());
//                        }
//                    }
//                }


                message.setMessageTypeName(MenuItem.getMessageName(messageType, name));
                if (!DataUtil.isFunctionEnable(MenuItem.nameToNumber(name)))
                    continue;

                //合并消息（22，24，25）为业务办理
                if (messageType.equals(Constants.LEAVE)//请假审批
                        || messageType.equals(Constants.GUARRANTEE)//维修处理
                        || messageType.equals(Constants.PRINT)) {//文印审批
                    message.setMessageTypeName("业务办理");
                    fill.add(message);
                } else {
//                    if (messageType.equals("23")) {
//                        message.setMessageTypeName("请假条");
//                    }
                    if (!Constants.hasEmailFunc) {
                        if (messageType.equals(Constants.EMAIL)) {
                            continue;
                        }
                    }
                    handled.add(message);
                }
            }
        }
        //合并消息（22，24，25）为业务办理
        int size = 0;
        if (fill != null && (size = fill.size()) > 0) {
            long[] all = new long[size];
            String type = "";
            long maxTime = 0;
            int pos = 0, totalNum = 0;

            for (int i = 0; i < size; i++) {
                String time = fill.get(i).getMessageTime();
                String count = fill.get(i).getCount();
                type += fill.get(i).getMessageType();
                type += ",";
                totalNum += Integer.parseInt(count);

                try {
                    all[i] = DataUtil.getLongDate(time);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //找到最新的一条
            for (int i = 0; i < all.length; i++) {
                if (all[i] > maxTime) {
                    maxTime = all[i];
                    pos = i;
                }
            }
            fill.get(pos).setCount(String.valueOf(totalNum));
            fill.get(pos).setMessageType(type);
            handled.add(fill.get(pos));
        }

        ret.setMessageList(handled);
        return ret;
    }

    private void parseData(String result) {
        NewMessageListBean list = null;
        NewMessageBean message = null;
        if (action == ACTION_CLEAR_MODEL) {
            DataUtil.ClearModelRemind(type);
            ((HomeActivity) getActivity()).updateUnreadLabel();
            return;
        } else if (action == ACTION_GET_MESSAGE) {
            System.out.println("获取最新消息列表结果:" + result);
            list = JsonUtils.fromJson(result, NewMessageListBean.class);
            if (list.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                if (list != null) {
                    list = handleMessage(list);
                }
                CCApplication.getInstance().setMessageList(list);
                NewMessageListBean all = CCApplication.getInstance().getMessageList();

                if (all != null) {
                    messageList = all.getMessageList();
                }

                //如果百川账号是null  就不要加载
                MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
                if (detail != null) {
                    if (!DataUtil.isNullorEmpty(detail.getHxUsername()) && !DataUtil.isNullorEmpty(detail.getHxPassword())) {
                        loadAliMessage();
                    }
                } else {
                    System.out.println("没有相应的百川账号,没有加载百川聊天");
                }

            }
        } else if (action == ACTION_GET_CORRELATION_MESSAGE) {//关联消息
            NewMessageBean ret = JsonUtils.fromJson(result, NewMessageBean.class);
            if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                if (ret.getMessageId() != null)
                    message = ret;
            }
        } else if (action == ACTION_UPDATE_MESSAGE || action == ACTION_UPDATE_MESSAGE_PAY_NOTICE) {
            SetReadBean bean = JsonUtils.fromJson(result, SetReadBean.class);
            if (bean.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {

                getNewMessage();
                ((HomeActivity) getActivity()).getModelRemind();

                System.out.println("刷新消息最新数据");
            } else {
                System.out.println("设为已读失败");
            }
        }

        NewMessageListBean all = CCApplication.getInstance().getMessageList();

        if (all != null) {
            messageList = all.getMessageList();
            if (messageList != null) {
                sortMessageByTime(messageList);
                adapter = new AllMessageAdapter(CCApplication.getInstance().getApplicationContext()
                        , 1, messageList);
                // 设置adapter
                if (listView != null) {
                    listView.setAdapter(adapter);
                }
                System.out.println("notifyDataSetChanged!!!");
                adapter.notifyDataSetChanged();
                if (list == null) {
                    list = new NewMessageListBean();
                }
                if (message != null) {
                    messageList.add(message);
                }
                list.setMessageList(messageList);
                CCApplication.getInstance().setMessageList(list);
                //如果没有消息那么显示没有消息的界面
                if (adapter.isEmpty()) {
                    if (tv_no_message != null)
                        tv_no_message.setVisibility(View.VISIBLE);
                } else {
                    if (tv_no_message != null)
                        tv_no_message.setVisibility(View.GONE);
                }
            } else {
                if (tv_no_message != null)
                    tv_no_message.setVisibility(View.VISIBLE);
            }
        } else {
            if (tv_no_message != null)
                tv_no_message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequstComplete(String result) {


        // TODO Auto-generated method stub
        parseData(result);
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            activity.updateUnreadLabel();
        }
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }


    private void clearMessage(String type) {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {

                if (user.getUserType().equals(Constants.PARENT_STR_TYPE)) {
                    json.put("userId", user.getChildId());
                    json.put("userType", Constants.STUDENT_STR_TYPE);
                } else {
                    json.put("userId", user.getUserId());
                    json.put("userType", user.getUserType());
                }
                json.put("messageType", type);
                action = ACTION_DEL_READ_MESSAGE;
                System.out.println("clearMessage:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, getActivity()).execute(Constants.DEL_READ_MESSAGE);
        }
    }

    //删除模块已读记录
    private void clearRemind(String type) {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                json.put("modelType", type);
                this.type = type;
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                action = ACTION_CLEAR_MODEL;
                System.out.println("删除模块已读记录:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, mContext).execute(Constants.DEL_MODEL_REMIND);
        }
    }

    public static boolean isAdmin() {
        UserType user = CCApplication.getInstance().getPresentUser();
        MemberDetailBean member = CCApplication.getInstance().getMemberDetail();
        if (user != null) {
            //家长不是管理员
            if ((Constants.PARENT_STR_TYPE).equals(user.getUserType())) {
                return false;
            }
        }
        if (member != null) {
            List<Role> roleList = member.getRoleList();
            if (roleList != null) {
                for (Role role : roleList) {
                    if (Constants.MAIL_ADMIN.equals(role.getRoleCode())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void sortMessageByTime(List<NewMessageBean> messageList) {
        try {
            Collections.sort(messageList, new Comparator<NewMessageBean>() {
                @Override
                public int compare(final NewMessageBean message1, final NewMessageBean message2) {
                    long time1 = 0;
                    long time2 = 0;
                    try {
                        time1 = DataUtil.getLongDate(message1.getMessageTime());
                        time2 = DataUtil.getLongDate(message2.getMessageTime());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (time1 == time2) {
                        return 0;
                    } else if (time1 < time2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem() {
        // TODO Auto-generated method stub
        doDelete(pos);
    }

    @Override
    public void readItem() {
        doRead(pos);
    }

    /**
     * 清除已读消息
     *
     * @param pos
     */
    private void doRead(int pos) {
        NewMessageBean item = adapter.getItem(pos);
        String messageType = item.getMessageType();

        //百川设为已读
        if ("aLiChat".equals(messageType)) {
            conversationService.markAllReaded();
        }

        String dataType;
        //合并消息（22，24，25）为业务办理
        if (messageType.equals(Constants.LEAVE)//请假审批
                || messageType.equals(Constants.GUARRANTEE)//维修处理
                || messageType.equals(Constants.PRINT)) {//文印审批
            dataType = new StringBuilder(Constants.LEAVE).append(",")
                    .append(Constants.GUARRANTEE).append(",")
                    .append(Constants.PRINT).toString();
        } else {
            dataType = messageType;
        }

        //如果是订单催缴  需要调用特定的方法
        JSONObject json = new JSONObject();
        if (messageType.equals(Constants.PAY_NOTICE)) {
            try {
                UserType user = CCApplication.getInstance().getPresentUser();
                String userType = user.getUserType();
                if (userType.equals("3")) {
                    json.put("studentId", user.getChildId());
                } else {
                    json.put("userId", user.getUserId());
                }
                json.put("userType", userType);
                json.put("schoolId", user.getSchoolId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            action = ACTION_UPDATE_MESSAGE_PAY_NOTICE;
            new HttpRequestAsyncTask(json, this, getActivity(), false).execute(Constants.UPDATE_MESSAGE_STATUS_PAY_NOTICE);
        } else {
            try {
                UserType user = CCApplication.getInstance().getPresentUser();
                String userType = user.getUserType();
                if (userType.equals("3")) {
                    json.put("userId", user.getChildId());
                } else {
                    json.put("userId", user.getUserId());
                }
                json.put("userType", userType);
                json.put("dataType", dataType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            action = ACTION_UPDATE_MESSAGE;
            new HttpRequestAsyncTask(json, this, getActivity(), false).execute(Constants.UPDATE_MESSAGE_STATUS);
        }
    }

    private void doDelete(int pos) {
        NewMessageBean message = adapter.getItem(pos);
        String type = message.getMessageType();
        String num = message.getCount();
        int count = Integer.parseInt(num);
//		int count = DataUtil.getUnreadStatus(type);
        if (count > 0) {
            DataUtil.getToast("未读消息不能删除");
        } else {

            //删除环信聊天item
            if ("chat".equals(type)) {
                EMChatManager.getInstance().deleteConversation(message.getHxUser().getUserName());
                //删除阿里百川聊天item
            } else if ("aLiChat".equals(type)) {
                conversationService.deleteAllConversation();

                //删除消息item
            } else {
                String[] types = type.split(",");
                if (types.length > 0) {
                    for (String str : types) {
                        clearMessage(str);
                    }
                } else {
                    clearMessage(message.getMessageType());
                }
            }

            messageList.remove(message);
            adapter.remove(message);
            adapter.notifyDataSetChanged();

            //如果没有消息那么显示没有消息的界面
            if (adapter.isEmpty()) {
                tv_no_message.setVisibility(View.VISIBLE);
            } else {
                tv_no_message.setVisibility(View.GONE);
            }

            // 更新消息未读数
            ((HomeActivity) getActivity()).updateUnreadLabel();
        }
    }

    BroadcastReceiver message = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("intent:" + intent);
            initMessage();
        }
    };


    BroadcastReceiver refreshBanner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("intent:" + intent);
            try {
                loadBanner();
            } catch (Exception e) {

            }
        }
    };


    private void initMessage() {
        NewMessageListBean all = CCApplication.getInstance().getMessageList();
        if (all != null) {
            messageList = all.getMessageList();
        } else {
            messageList = null;
        }
        if (messageList != null) {
            sortMessageByTime(messageList);
        }
        initAdapter();
    }

    /**
     * 加载阿里百川聊天信息
     * 需要添加登陆监听
     */
    public void initAliChat() {
        if (DataUtil.isMain() == false) return;//副号不加载聊天消息

        YWIMKit imKit = CCApplication.getInstance().getIMKit();
        if (imKit != null) {
            YWIMCore imCore = imKit.getIMCore();
            if (imCore != null) {
                conversationService = imCore.getConversationService();
                conversationService.addPushListener(this);
            } else {
                Log.d("super", "imCore为null");
            }
        } else {
            Log.d("super", "imKit为null");
        }
    }


    //把阿里消息存储到消息列表当中
    public void loadAliMessage() {
        System.out.println("加载百川聊天");
        initAliChat();

        //从百川服务器当中获取到最新一条聊天记录
        try {
            if (!conversationService.getConversationList().isEmpty()) {
                YWConversation conversation = conversationService.getConversationList().get(0);
                if (conversation != null) {
                    YWMessage ywMessage = conversation.getLastestMessage();
                    if (ywMessage != null) {
                        if (aliMessageContainer == null) {
                            aliMessageContainer = new NewMessageBean();
                        }
                        aliMessageContainer.setMessageTypeName("会话列表");
                        String content = getMessageContent(ywMessage);
                        aliMessageContainer.setMessageContent(content);
                        String time = DataUtil.getStringDateFromLong(ywMessage.getTime());
                        aliMessageContainer.setMessageTime(time);
                        aliMessageContainer.setMessageType("aLiChat");

                        //未读消息数
                        int unreadCount = conversationService.getAllUnreadCount();
                        aliMessageContainer.setCount(String.valueOf(unreadCount));

                        if (messageList == null) {
                            messageList = new ArrayList<>();
                        }
                        boolean isHave = messageList.contains(aliMessageContainer);
                        if (!isHave) {
                            messageList.add(aliMessageContainer);
                        }
                    } else {
                        Log.d("super", "ywMessage为null");
                    }
                } else {
                    Log.d("super", "conversation为null");
                }
            } else {
                Log.d("super", "conversationService为null");
                //刷新一下列表
            }
        } catch (Exception e) {
            Log.d("super", "报错");
        }

        NewMessageListBean message = CCApplication.getInstance().getMessageList();
        if (message == null) {
            message = new NewMessageListBean();
        }
        message.setMessageList(messageList);
        CCApplication.getInstance().setMessageList(message);

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            activity.updateUnreadLabel();
        }
    }

    /**
     * 单聊监听
     *
     * @param iywContact 联系人信息
     * @param ywMessage  百川消息
     */
    @Override
    public void onPushMessage(IYWContact iywContact, YWMessage ywMessage) {
        switchMessage(ywMessage);
    }

    /**
     * 群聊监听
     *
     * @param ywTribe   群聊信息
     * @param ywMessage 百川消息
     */
    @Override
    public void onPushMessage(YWTribe ywTribe, YWMessage ywMessage) {
        switchMessage(ywMessage);
    }


    /**
     * 将百川消息转化成App消息类型
     *
     * @param ywMessage
     */
    public void switchMessage(YWMessage ywMessage) {
        if (aliMessageContainer == null) {
            aliMessageContainer = new NewMessageBean();
        }
        aliMessageContainer.setMessageTypeName("会话列表");

        String content = getMessageContent(ywMessage);
        aliMessageContainer.setMessageContent(content);
        String time = DataUtil.getStringDateFromLong(ywMessage.getTime());
        aliMessageContainer.setMessageTime(time);
        aliMessageContainer.setMessageType("aLiChat");
        int unreadCount = conversationService.getAllUnreadCount();
        aliMessageContainer.setCount(String.valueOf(unreadCount));

        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        boolean isHave = messageList.contains(aliMessageContainer);
        if (!isHave) {
            messageList.add(aliMessageContainer);
        }
        sortMessageByTime(messageList);

        if (adapter != null) {
            adapter.refresh(messageList);
            adapter.notifyDataSetChanged();
        }

        NewMessageListBean message = CCApplication.getInstance().getMessageList();
        if (message == null) {
            message = new NewMessageListBean();
        }
        message.setMessageList(messageList);
        CCApplication.getInstance().setMessageList(message);

        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (homeActivity != null) {
            ((HomeActivity) getActivity()).updateUnreadLabel();
        }

    }

    /**
     * 获取消息类型
     *
     * @param ywMessage
     */
    private String getMessageContent(YWMessage ywMessage) {

        if (ywMessage == null) {
            return "";
        }

        String digest = "";
        int subType = ywMessage.getSubType();
        switch (subType) {

            //文本
            case YWMessage.SUB_MSG_TYPE.IM_TEXT:
                digest = ywMessage.getContent();
                break;

            //视频
            case YWMessage.SUB_MSG_TYPE.IM_VIDEO:
                digest = getString(getActivity(), R.string.video);
                break;

            //语音
            case YWMessage.SUB_MSG_TYPE.IM_AUDIO:
                digest = getString(getActivity(), R.string.voice);
                break;

            //定位
            case YWMessage.SUB_MSG_TYPE.IM_GEO:
                digest = getString(getActivity(), R.string.location);
                break;

            //图片
            case YWMessage.SUB_MSG_TYPE.IM_IMAGE:
                digest = getString(getActivity(), R.string.picture);
                break;

            //系统消息
            case YWMessage.SUB_MSG_TYPE.IM_SYSTEM:
                digest = getString(getActivity(), R.string.system_message);
                break;

            case YWMessage.SUB_MSG_TYPE.IM_SYSTEM_TIP:
                digest = getString(getActivity(), R.string.system_message);
                break;

            default:
                System.err.println("error, unknow type");
                digest = getString(getActivity(), R.string.new_news);
                break;
        }
        return digest;
    }

}
