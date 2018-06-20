/*
 * 文 件 名:DailyRecordDetailActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-07-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.summary;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
import com.wcyc.zigui2.newapp.module.notice.ShowAttachListAdapter;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean.SummaryDetail;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SummaryDetailActivity extends BaseActivity
        implements HttpRequestAsyncTaskListener {
    private TextView tvTitle, tvDesc, tvContent, tvNotify, tvReadTimes;
    private WebView webview;
    private String desc, title, content, readTimes;
    private LinearLayout view, attach;
    private ListView listview;
    private Button bType;
    private SummaryDetail detail;
    private ShowAttachListAdapter showAttach;
    private final int ACTION_GET_ATTACHMENT_LIST = 0;
    private final int ACTION_MARK_READ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);
        initData();
        initView();
        initEvent();
        markRead();
        getAttachement();
    }

    private void initView() {
        attach = (LinearLayout) findViewById(R.id.attach_ll);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(title);
        tvNotify = (TextView) findViewById(R.id.new_content);
        tvNotify.setText("总结详情");
        tvDesc = (TextView) findViewById(R.id.desc);
        tvDesc.setText(desc);
        bType = (Button) findViewById(R.id.type);
        System.out.println("type:" + detail.getSummaryType());
        bType.setText(detail.getSummaryType());
        setTypeColor(bType, detail.getSummaryType());
        webview = (WebView) findViewById(R.id.html_content);
       // System.out.println(content);
        if (content != null && content.length() > 0) {
//			DataUtil.showHtmlSetting(webview,content);
            new CustomWebView(this, webview, content);
        } else {
            webview.setVisibility(View.GONE);
        }
        view = (LinearLayout) findViewById(R.id.title_back);
        view.setVisibility(View.VISIBLE);
        tvReadTimes = (TextView) findViewById(R.id.read_times);
        tvReadTimes.setText(readTimes);
        listview = (ListView) findViewById(R.id.attach_list);
    }

    private void setTypeColor(Button view, String type) {
        if ("周期小结".equals(type)) {
            view.setBackgroundResource(R.drawable.btn_type_1);
        } else if ("月期总结".equals(type)) {
            view.setBackgroundColor(Color.rgb(79, 182, 118));
            view.setBackgroundResource(R.drawable.btn_type_2);
        } else if ("学期总结".equals(type)) {
            view.setBackgroundColor(Color.rgb(216, 102, 124));
            view.setBackgroundResource(R.drawable.btn_type_3);
        } else if ("学年总结".equals(type)) {
            view.setBackgroundColor(Color.rgb(202, 164, 62));
            view.setBackgroundResource(R.drawable.btn_type_4);
        } else if ("会议汇报".equals(type)) {
            view.setBackgroundColor(Color.rgb(245, 92, 157));
            view.setBackgroundResource(R.drawable.btn_type_5);
        } else if ("其他".equals(type)) {
            view.setBackgroundColor(Color.rgb(245, 92, 157));
            view.setBackgroundResource(R.drawable.btn_type_6);
        }
    }

    private void initEvent() {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }

        });

    }


    private void initData() {
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 0);
        detail = (SummaryDetail) intent.getExtras().getSerializable("detail");
        //detail = CCApplication.getInstance().getNotice().getNoticeList().get(pos);
        title = detail.getSummaryTitle();
        String time = detail.getSummaryTime();
        if (!DataUtil.isNullorEmpty(time)) {
            time = DataUtil.formatDate(time);
        }
        desc = detail.getDepartName() + " " + detail.getCreatorName() + " 发布于" + time;
        content = detail.getSummaryContent();
        readTimes = detail.getSummaryBrowseNum() + "";
    }

    private void getAttachement() {
        DataUtil.getAttachmentList(this,
                detail.getSummaryId() + "", Constants.SUMMARY_ATTACH, ACTION_GET_ATTACHMENT_LIST);
    }

    private void markRead() {
        UserType user = CCApplication.getInstance().getPresentUser();
        String userId = detail.getCreatorId() + "";
//		if(!userId.equals(user.getUserId())){//当前用户不是发布者
        JSONObject json = new JSONObject();

        try {

            json.put("userId", user.getUserId());
            json.put("summaryId", detail.getSummaryId());
            json.put("schoolId", user.getSchoolId());

            System.out.println("总结markRead:" + json);
            new HttpRequestAsyncTask(json, this, this).execute(Constants.SUMMARY_BROWSE);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		}
        //删除模块已读记录
        json = new JSONObject();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                json.put("dataId", detail.getSummaryId());
                json.put("modelType", Constants.SUMMARY);
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                System.out.println("删除模块已读记录:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, this).execute(Constants.DEL_MODEL_REMIND);
        }
        //删除消息记录
        if (user != null) {
            try {
                json = new JSONObject();
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                json.put("msgId", detail.getSummaryId());
                json.put("messageType", Constants.SUMMARY);
                System.out.println("删除消息记录:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, this).execute(Constants.DEL_READ_MESSAGE);
        }
    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        switch (action) {
            case ACTION_GET_ATTACHMENT_LIST:
                System.out.println("parseAttachment:" + data);
                AttachmentBean attach = JsonUtils.fromJson(data, AttachmentBean.class);
                List<Attachment> list = attach.getAttachmentList();
                detail.setAttachDetail(list);
                if (list != null && list.size() > 0) {
                    this.attach.setVisibility(View.VISIBLE);
                    listview.setAdapter(new ShowAttachListAdapter(this, detail.getAttachDetail(), "summary"));
                }

                break;
        }
    }

    public void handleOnClick(Attachment attach) {
        if (ImageUtils.isImage(attach.getAttachementName())) {
            ImageUtils.showImage(this, attach.getAttachementUrl());
        } else {
            AttachmentActionOption option = new AttachmentActionOption(this, attach);
            View view = findViewById(R.id.email_body);
            option.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            System.out.println("read success:" + result);
        }
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }
}
