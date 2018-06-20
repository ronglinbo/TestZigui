package com.wcyc.zigui2.newapp.module.educationinfor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 教育资讯
 *
 * @author 郑国栋 2017-03-01
 * @version 2.0.12
 */
public class EducationInforActivity extends BaseActivity implements
        OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private RefreshListView1 education_infor_lv;
    private ArrayList<EducationInforBean> educationInforList;// 教育资讯list
    private EducationInforAdapter educationInforAdapter;
    private int k = 2;
    private int pages;// 总页数
    private ImageView no_data_iv;
    private String userid;
    private String userType;
    public static final String EDUCATION_REFESH_DATA = "com.wcyc.zigui.action.EDUCATION_REFESH_DATA";
    private int position = 0;//位置 第几个跳转的
    private static final int EDUCATION_DETAILS_CODE = 100;
    private int positiontemp = 0;
    private final int GET_EDUCATION_CIRCLES_LIST = 100;
    private final int GET_EDUCATION_CIRCLES_LIST_RDATA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_infor_activity);

        initView();
        initDatas();
        initEvents();

        IntentFilter mrefeshDataFilter = new IntentFilter(EDUCATION_REFESH_DATA);
        registerReceiver(refeshDataReceiver, mrefeshDataFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (RESULT_OK == resultCode) {
//				position=data.getIntExtra("position",0);
////				initDatas();
//				if(position>0){
//					if(education_infor_lv!=null&&educationInforAdapter!=null){
//						System.out.println("=b==position=="+position);
//						education_infor_lv.setSelection(position);
//						educationInforAdapter.notifyDataSetInvalidated();
//					}
//				}
                if (positiontemp > 0) {
                    int pageSizeRdata = (positiontemp - 1) / 10;
                    if (pageSizeRdata > 0) {
                        k = pageSizeRdata + 2;
                    } else {
                        k = 2;
                    }
                    pageSizeRdata = (pageSizeRdata + 1) * 10;
                    try {
                        userid = CCApplication.getInstance().getPresentUser()
                                .getUserId();
                        userType = CCApplication.getInstance().getPresentUser().getUserType();
                        JSONObject json = new JSONObject();
                        json.put("userId", userid);
                        json.put("userType", userType);//
                        json.put("curPage", 1);
                        json.put("pageSize", pageSizeRdata);
                        if ("3".equals(userType)) {
                            String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                            ;
                            json.put("studentId", studentId);
                        }

                        if (!DataUtil.isNetworkAvailable(EducationInforActivity.this)) {
                            DataUtil.getToast(getResources().getString(R.string.no_network));
                            return;
                        }

                        if (!isLoading()) {
                            System.out.println("==教育资讯入参==" + json);
                            action = GET_EDUCATION_CIRCLES_LIST_RDATA;
                            queryPost(Constants.GET_EDUCATION_CIRCLES_LIST, json);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 实例化组件
    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

        education_infor_lv = (RefreshListView1) findViewById(R.id.education_infor_lv);
        no_data_iv = (ImageView) findViewById(R.id.no_data_iv);// 无数据
    }

    // 初始化数据
    private void initDatas() {
        new_content.setText("教育资讯");
        k = 2;
        educationInforList = new ArrayList<EducationInforBean>();

        try {
            userid = CCApplication.getInstance().getPresentUser()
                    .getUserId();
            userType = CCApplication.getInstance().getPresentUser().getUserType();
            JSONObject json = new JSONObject();
            json.put("userId", userid);
            json.put("userType", userType);//
            json.put("curPage", 1);
            json.put("pageSize", 10);
            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                ;
                json.put("studentId", studentId);
            }

            if (!DataUtil.isNetworkAvailable(EducationInforActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("==教育资讯入参==" + json);
                action = GET_EDUCATION_CIRCLES_LIST;
                queryPost(Constants.GET_EDUCATION_CIRCLES_LIST, json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 设置点击效果监听器
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);

        education_infor_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //只有当外界没有显示进度条的时候这里才调用显示进度条
                if(DataUtil.pd == null || !DataUtil.pd.isShowing()){
                    DataUtil.showDialog(EducationInforActivity.this);
                }
//				Bundle bundle = new Bundle();
//				bundle.putInt("position", arg2);
//				System.out.println("=a==position=="+position);
//				bundle.putString("campuNewsId", educationInforList.get(arg2-1).getId());
//				bundle.putString("isRead", educationInforList.get(arg2-1).getIsRead());
//				newActivity(EducationDetailsActivity.class, bundle);// 去详情界面
                positiontemp = arg2;
                Intent intent = new Intent(EducationInforActivity.this, EducationDetailsActivity.class);
                intent.putExtra("position", arg2);
                intent.putExtra("campuNewsId", educationInforList.get(arg2 - 1).getId());
                intent.putExtra("isRead", educationInforList.get(arg2 - 1).getIsRead());

                int browseNo = educationInforList.get(arg2 - 1).getBrowseNo();
                browseNo += 1;
                educationInforList.get(arg2 - 1).setBrowseNo(browseNo);
                educationInforList.get(arg2 - 1).setIsRead("1");
                educationInforAdapter.notifyDataSetChanged();

                startActivityForResult(intent, EDUCATION_DETAILS_CODE);
            }
        });

        education_infor_lv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return false;
            }
        });
        education_infor_lv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {// 下拉刷新
                k = 2;
                position = 0;
                initDatas();
                education_infor_lv.hideHeaderView();// 收起下拉刷新
            }

            @Override
            public void onLoadingMore() {// 上拉加载更多
                if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToast("没有更多数据了");
                }
                education_infor_lv.hideFooterView();
            }

        });

    }

    // 加载更多数据
    public void loadData() {
        if (!DataUtil.isNetworkAvailable(getBaseContext())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        if (!isLoading()) {
            loadDataByIndex(k);
            k++;
        }
    }

    // 加载更多
    public void loadDataByIndex(int index) {

        JSONObject json = new JSONObject();
        try {
            json.put("userId", userid);
            json.put("userType", userType);//
            json.put("curPage", k);
            json.put("pageSize", "10");
            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                ;
                json.put("studentId", studentId);
            }

            if (!DataUtil.isNetworkAvailable(EducationInforActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("第" + k + "页" + "==教育资讯入参===" + json);
                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.GET_EDUCATION_CIRCLES_LIST).toString();
                String result = HttpHelper.httpPostJson(this, url, json);
                System.out.println("第" + k + "页" + "==教育资讯出参===" + result);
                ArrayList<EducationInforBean> educationInforList_more = new ArrayList<EducationInforBean>();
                JSONObject json3 = new JSONObject(result);
                String educationInforList = json3.getString("campuEcirclesList");
                JSONArray json2 = new JSONArray(educationInforList);
                for (int i = 0; i < json2.length(); i++) {
                    EducationInforBean educationInforBean = JsonUtils.fromJson(json2.get(i)
                            .toString(), EducationInforBean.class);
                    educationInforList_more.add(educationInforBean);
                }
                educationInforAdapter.addItem(educationInforList_more);
                educationInforAdapter.notifyDataSetChanged();

            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                EducationInforActivity.this.finish();
                break;
            default:
                break;

        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_EDUCATION_CIRCLES_LIST:
                System.out.println("===教育资讯出参=====" + data);
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {

                    try {
                        JSONObject json = new JSONObject(data);
                        if (json.has("totalPageNum")) {
                            String pagesStr = json.getString("totalPageNum");
                            pages = Integer.parseInt(pagesStr);
                        }
                        if (pages < 1) {
                            education_infor_lv.setVisibility(View.GONE);
                            no_data_iv.setVisibility(View.VISIBLE);
                        } else {
                            education_infor_lv.setVisibility(View.VISIBLE);
                            no_data_iv.setVisibility(View.GONE);
                            String campuEcirclesList = json.getString("campuEcirclesList");
                            JSONArray json2 = new JSONArray(campuEcirclesList);
                            for (int i = 0; i < json2.length(); i++) {
                                EducationInforBean educationInforBean = JsonUtils.fromJson(json2
                                        .get(i).toString(), EducationInforBean.class);
                                educationInforList.add(educationInforBean);
                            }
//
                            educationInforAdapter = new EducationInforAdapter(this,
                                    educationInforList);
                            education_infor_lv.setAdapter(educationInforAdapter);

                            if (position > 0) {
                                if (education_infor_lv != null && educationInforAdapter != null) {
                                    System.out.println("===position==" + position);
                                    education_infor_lv.setSelection(position);
                                    educationInforAdapter.notifyDataSetInvalidated();
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GET_EDUCATION_CIRCLES_LIST_RDATA:
                NewBaseBean retRdata = JsonUtils.fromJson(data, NewBaseBean.class);
                if (retRdata.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(retRdata.getServerResult().getResultMessage());
                } else {
                    try {
                        JSONObject json = new JSONObject(data);
                        String campuEcirclesList = json.getString("campuEcirclesList");
                        JSONArray json2 = new JSONArray(campuEcirclesList);
                        educationInforList.clear();
                        for (int i = 0; i < json2.length(); i++) {
                            EducationInforBean educationInforBean = JsonUtils.fromJson(json2
                                    .get(i).toString(), EducationInforBean.class);
                            educationInforList.add(educationInforBean);
                        }
                        education_infor_lv.setSelection(positiontemp);
                        educationInforAdapter.notifyDataSetInvalidated();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
//			initDatas();//其实浏览数不对没关系，这个反而不合理，如图是第62条新闻，这个会从第一条加载，二不是定位到第62条
            //listview.setSelection(int position);
            //adapter.notifyDataSetInvalidated();通知adapter数据有变化
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refeshDataReceiver);
    }
}
