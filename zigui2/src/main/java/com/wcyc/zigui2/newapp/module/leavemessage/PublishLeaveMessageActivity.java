package com.wcyc.zigui2.newapp.module.leavemessage;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PublishLeaveMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView new_content;// 标题
    private TextView title_right_tv;// 右标题
    private TextView title_left_tv;// 左标题
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_leave_message);
        initView();
        initEvent();
    }

    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);// 右标题
        title_left_tv = (TextView) findViewById(R.id.title_left_tv);//左标题
        title_right_tv.setText("确定");
        title_right_tv.setTextSize(18);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setEnabled(false);
        title_right_tv.setTextColor(getResources().getColor(R.color.color_qianse));
        title_left_tv.setText("取消");
        title_left_tv.setTextSize(18);
        title_left_tv.setTextColor(getResources().getColor(R.color.color_checked));
        title_left_tv.setVisibility(View.VISIBLE);
        new_content.setText("我要留言");
        message = (EditText) findViewById(R.id.message);// 标题
    }

    private void initEvent() {
        title_right_tv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    title_right_tv.setEnabled(false);
                    title_right_tv.setTextColor(getResources().getColor(R.color.color_qianse));
                } else {
                    int length=s.length();
                    if (length> 500) {
                        DataUtil.getToastShort("留言内容不能超过500字");
                        message.setText(s.subSequence(0,500));
                        message.setSelection(500);
                    }
                    title_right_tv.setEnabled(true);
                    title_right_tv.setTextColor(getResources().getColor(R.color.color_checked));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_tv:
                PublishLeaveMessageActivity.this.finish();
                break;
            case R.id.title_right_tv:

                //                schoolId	String	学校ID
//                studentId	String	接收学生ID
//                relation	String	家长称谓
//                mobile	String	家长手机号码
//                createUserId	String	发布人ID（家长用户ID）
//                content	String	家长留言
                if (TextUtils.isEmpty(message.getText().toString())) {
                    DataUtil.getToastShort("留言信息不能为空");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
                        jsonObject.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
                        jsonObject.put("relation", CCApplication.getInstance().getPresentUser().getRelationTypeName());
                        jsonObject.put("mobile", CCApplication.getInstance().getMemberInfo().getMobile());
                        jsonObject.put("createUserId", CCApplication.getInstance().getPresentUser().getUserId());
                        jsonObject.put("content", message.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    queryPost(Constants.PUBLISH_PARENT_MESSAGE_BOARD, jsonObject);
                }

                break;
            default:
                break;

        }
    }

    @Override
    protected void getMessage(String data) {
       //CCApplication.getDaoinstant().getLeaveMessageDao();
        System.out.println("发布家长留言：" + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String code = jsonObject.getString("code");
            if (code.equals("200")) {
                DataUtil.getToastShort("留言成功");
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                //移除之前的 班牌留言activity
                Iterator<Activity> activityIterator = CCApplication.activityList.iterator();
                while (activityIterator.hasNext()) {
                    Activity activity = activityIterator.next();
                    if (activity instanceof LeaveMeassageActivity) {
                        activityIterator.remove();
                        activity.finish();
                    }
                }
                newActivity(LeaveMeassageActivity.class, bundle);
                finish();
            } else {
                //  jsonObject.getJSONObject("")
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
