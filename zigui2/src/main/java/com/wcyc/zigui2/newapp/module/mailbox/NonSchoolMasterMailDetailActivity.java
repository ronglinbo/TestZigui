/*
 * 文 件 名:MasterMailDetailActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-18
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.color;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.module.mailbox.MasterReplyMail.ReplyMailBody;
import com.wcyc.zigui2.newapp.module.notice.ShowAttachListAdapter;
import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//非学校管理员邮件详情
public class NonSchoolMasterMailDetailActivity extends BaseActivity
        implements ImageUploadAsyncTaskListener, HttpRequestAsyncTaskListener {

    private TextView tvTitle, tvDate, tvSender, tvContent, tvNotify, cancel, enter, tvType;
    private TextView tvReplied_Title, tvReplied_Sender, tvReplied_Date,
            tvReplied_Notify;
    private String date, attach, sender, emailTitle, content, title;
    private EditText replyContent, replyTitle;
    private LinearLayout back, llAttach;
    private ListView listView, listViewShow, replied_list;
    private Button addAttach, bType;
    private WebView webview, RepliedContent;
    private int /*type,*/pos;
    private MailInfo data;

    private AttachmentBean attachment;
    private String isReply;
    private boolean isRelied;
    private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
    private UploadFileResult ret;
    private static final int PICK_PICTURE = 100;

    private static final int GET_REPLY_MAIL = 0;
    private static final int REPLY_MAIL = 1;
    private static final int QUERY_ATTACHMENT = 2;
    private static final int QUERY_REPLY_ATTACHMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initAllKindView();
        initEvent();
    }

    protected void onResume() {
        super.onResume();
    }

    private void initAllKindView() {
        if (isRelied) {//已回复邮件界面
            setContentView(R.layout.activity_master_replied_mail_detail);
            if (attach != null && Integer.parseInt(attach) > 0) {
                getOriginAttachment();
            }
            initRepliedView();
            getReplyMail();
        } else {//未回复邮件界面
            setContentView(R.layout.activity_not_master_mail_detail);
            initView();
//			if(attach != null && Integer.parseInt(attach) > 0){
            getAttachment(QUERY_ATTACHMENT, data.getMailId());
//			}
        }
    }

    private void initView() {
        TextView tv = (TextView) findViewById(R.id.new_content);
        tv.setText("邮件详情");
        back = (LinearLayout) findViewById(R.id.title_back);
        if (back != null) back.setVisibility(View.VISIBLE);

        View view = findViewById(R.id.title_right_tv);
        if (view != null) view.setVisibility(View.GONE);

        llAttach = (LinearLayout) findViewById(R.id.ll_attachment);

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(emailTitle);
        tvDate = (TextView) findViewById(R.id.date);
        tvDate.setText(DataUtil.getShowTime(date));
        tvSender = (TextView) findViewById(R.id.sender);
        tvSender.setText(sender);

        webview = (WebView) findViewById(R.id.html_content);
        if (webview != null) {
            webview.getSettings().setSavePassword(false);
        }
        if (content.length() > 0) {
            if (webview != null) {
                webview.removeJavascriptInterface("searchBoxJavaBridge_");
                webview.removeJavascriptInterface("accessibility");
                webview.removeJavascriptInterface("accessibilityTraversal");
                new CustomWebView(this, webview, content, R.color.background_color);
//				DataUtil.showHtmlSetting(webview,content);

            }
        } else {
            webview.setVisibility(View.GONE);
        }

        replyContent = (EditText) findViewById(R.id.reply_content);
        addAttach = (Button) findViewById(R.id.add_attach);
        listView = (ListView) findViewById(R.id.attachList);//未上传的list
        listViewShow = (ListView) findViewById(R.id.attach_list);//服务器带过来的list
        bType = (Button) findViewById(R.id.type);
        String type = data.getTypeName();
        bType.setText(type);
//		tvType.setText(data.getTypeName());
        DataUtil.setTypeColor(bType, type);
    }

    /**
     * 获取图片资源
     */
    private ImageGetter imageGetter = new ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            // 根据id从资源文件中获取图片对象
            Drawable d;
            String url = DataUtil.getDownloadURL(source);
            d = ImageUtils.fetchDrawable(NonSchoolMasterMailDetailActivity.this, url);
            return d;
        }
    };

    private void initRepliedView() {

        tvReplied_Title = (TextView) findViewById(R.id.replied_title);
        tvReplied_Sender = (TextView) findViewById(R.id.replied_sender);
        tvReplied_Date = (TextView) findViewById(R.id.replied_date);
        replied_list = (ListView) findViewById(R.id.replied_attach_list);
        RepliedContent = (WebView) findViewById(R.id.replied_content);
        if (RepliedContent != null) {
            RepliedContent.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
            RepliedContent.getSettings().setSavePassword(false);
        }
        initView();

    }

    private void initUnReplyView() {
        cancel = (TextView) findViewById(R.id.title2_off);
        enter = (TextView) findViewById(R.id.title2_ok);
        enter.setTextColor(getResources().getColor(color.gray_normal));
        enter.setEnabled(false);
        initView();
        replyContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.length() > 0) {
                    enter.setTextColor(getResources().getColor(color.blue));
                    enter.setEnabled(true);
                } else {
                    enter.setTextColor(getResources().getColor(color.gray_normal));
                    enter.setEnabled(false);
                }
            }

        });
    }

    private void initEvent() {
        if (cancel != null) {
            cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    finish();
                }

            });
        }
        if (enter != null) {
            enter.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (!isRelied) {
                        if (imagePaths.size() == 0) {
                            replyMail();
                        } else {//先传图片
                            uploadFile();
                        }
                    } else {
                        finish();
                    }
                }

            });
        }
        if (addAttach != null) {
            addAttach.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.setClass(NonSchoolMasterMailDetailActivity.this, SelectImageActivity.class);
                    intent.putExtra("limit", 8);
                    intent.putStringArrayListExtra("addPic", imagePaths);
                    startActivityForResult(intent, PICK_PICTURE);
                }
            });
        }
        if (back != null) {
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    finish();
                }

            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PICTURE:
                    Bundle bundle = data.getExtras();
                    imagePaths = bundle.getStringArrayList("pic_paths");
                    data.getBooleanExtra("is_compress", true);
                    listView.setAdapter(new AttachmentListAdapter(this, imagePaths));
                    break;
            }
        }
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//			type = bundle.getInt("type");
            pos = bundle.getInt("pos");

            data = (MailInfo) bundle.getSerializable("data");
            emailTitle = data.getMailTile();
            sender = data.getMailSenderName();
            isReply = data.getIsReply();
            isRelied = isReply.equals("1");
            date = data.getPublishTime();
            attach = data.getAdditionNum();
            content = data.getMailContent();
        }
    }

    protected void uploadFile() {
        ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
                this, Constants.PIC_TYPE, imagePaths, Constants.UPLOAD_URL, this);
        upload.execute();
    }

    protected void getOriginAttachment() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", user.getUserId());
            json.put("schoolId", user.getSchoolId());

            json.put("attachementType", "02");
            json.put("buzzId", data.getMailId());
            System.out.println("json:" + json);
            new HttpRequestAsyncTask(json, this, this).execute(Constants.QUREY_ATTACHMENT);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void replyMail() {

        try {
            MasterReplyMailReq req = new MasterReplyMailReq();
            req.setUserId(user.getUserId());
            req.setSchoolId(user.getSchoolId());
            req.setMailId(data.getMailId());
            req.setReplyContent(replyContent.getText().toString());
            req.setReplyTitle(emailTitle);
            if (ret != null) {
                List<String> attachment = new ArrayList<String>();
                Set<String> set = ret.getSuccFiles().keySet();
                for (String string : set) {
                    attachment.add(string);
                }
                req.setAttchementNum(attachment.size() + "");
                req.setAttachmentList(attachment);
            } else {
                req.setAttchementNum("0");
            }
            req.setIsSms("0");
            Gson gson = new Gson();
            String string = gson.toJson(req);
            JSONObject json = new JSONObject(string);
            System.out.println("replyMail:" + json);
            queryPost(Constants.REPLY_MAIL_URL, json);
            action = REPLY_MAIL;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void parseReplyMail(String data) {
        System.out.println("parseReplyMail:" + data);
        NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            DataUtil.getToast("发布成功");
            finish();
        } else {
            DataUtil.getToast("发布失败");
        }
    }

    protected void getReplyMail() {
        JSONObject json = new JSONObject();
        try {
            json.put("mailId", data.getMailId());
            queryPost(Constants.GET_ANSWER_MAIL, json);
            action = GET_REPLY_MAIL;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void parseGetReplyMailData(String data) {
        MasterReplyMail reply = JsonUtils.fromJson(data, MasterReplyMail.class);
        if (reply.getAnwerList() != null) {
            ReplyMailBody body = reply.getAnwerList().get(0);//默认只有一个回复需修改

            String attach = body.getAdditionNo();
            tvReplied_Title.setText(body.getTitle());
            tvReplied_Date.setText(body.getAnswerTime());
            tvReplied_Sender.setText(body.getAnswerName());
            //tvReplied_Attach.setText(attach);
//			RepliedContent.setText(body.getContent());
//			RepliedContent.loadData(body.getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
            String content = body.getContent();
            new CustomWebView(this, RepliedContent, content);
            if (attach != null && Integer.parseInt(attach) > 0) {
                getAttachment(QUERY_REPLY_ATTACHMENT, body.getAnswerId());
            }
        }
    }

    protected void getAttachment(int action, String mailId) {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        try {
            json.put("userId", user.getUserId());
            json.put("schoolId", user.getSchoolId());
            if (isRelied) {
                json.put("attachementType", "03");
            } else {
                json.put("attachementType", "02");
            }
            json.put("buzzId", mailId);
            System.out.println("json:" + json);
            queryPost(Constants.QUREY_ATTACHMENT, json);
            this.action = action;
            DataUtil.Sleep(100);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void parseAttachment(int action, String data) {
        System.out.println("parseAttachment:" + data);
        attachment = JsonUtils.fromJson(data, AttachmentBean.class);
        String text = "";
        if (attachment != null) {
            List<Attachment> list = attachment.getAttachmentList();
            if (list != null) {
                for (Attachment temp : list) {
                    text += temp.getAttachementName();
                    text += " ";
                }
                if (list.size() > 0) llAttach.setVisibility(View.VISIBLE);
            }
        }

        if ("1".equals(isReply)) {
            if (replied_list != null)
                replied_list.setAdapter(new ShowAttachListAdapter(this, attachment.getAttachmentList()));
        } else {
            listViewShow.setAdapter(new ShowAttachListAdapter(this, attachment.getAttachmentList()));
        }
    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        System.out.println("data:" + data);
        switch (action) {
            case GET_REPLY_MAIL:
                parseGetReplyMailData(data);
//			if(attach != null && Integer.parseInt(attach) > 0){
//				getAttachment(QUERY_ATTACHMENT);
//			}
                break;
            case REPLY_MAIL:
                parseReplyMail(data);
                break;
            case QUERY_REPLY_ATTACHMENT:
            case QUERY_ATTACHMENT:
                parseAttachment(action, data);
                break;
        }
    }

    @Override
    public void onImageUploadCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onImageUploadComplete(String result) {
        // TODO Auto-generated method stub
        System.out.println("result:" + result);
        ret = JsonUtils.fromJson(result, UploadFileResult.class);
        replyMail();
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        attachment = JsonUtils.fromJson(result, AttachmentBean.class);
        String text = "";
        if (attachment != null) {
            List<Attachment> list = attachment.getAttachmentList();
            if (list != null) {
                for (Attachment temp : list) {
                    text += temp.getAttachementName();
                    text += " ";
                }
                if (list.size() > 0) llAttach.setVisibility(View.VISIBLE);
            }
        }

        listViewShow.setAdapter(new ShowAttachListAdapter(this, attachment.getAttachmentList()));
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }
}
