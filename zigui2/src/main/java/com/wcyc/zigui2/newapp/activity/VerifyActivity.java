package com.wcyc.zigui2.newapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.ValidCodeBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyActivity extends BaseActivity {

    private Button btnEnter,btnGetCode;
    private EditText etCode;
    private View back;
    private String mobile;
    private static final int GET_CODE = 0;
    private static final int VALIDE_CODE = 1;

    @Override
    protected void getMessage(String data) {
        switch(action){
            case GET_CODE:
                parseGetCode(data);
                break;
            case VALIDE_CODE:
                parseValidCode(data);
                break;
        }
    }

    private void parseGetCode(String data){
        NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if(ret != null && ret.getServerResult() != null) {
            if (Constants.SUCCESS_CODE == ret.getServerResult().getResultCode()) {
                DataUtil.getToast("验证码已发送至您手机");
                runtime();
            } else {
                int code = ret.getServerResult().getResultCode();
                switch (code) {
                    case 402:
                        etCode.requestFocus();
                        break;
                    case Constants.ACCOUNT_DISABLE_CODE:
                        etCode.requestFocus();
                        DataUtil.getToast("账户被禁用，请与管理员联系");
                        return;
                    case 102003:
                        DataUtil.getToast("您输入的手机号不存在！");
                        return;
                }
                if(ret.getServerResult().getResultCode()==12000){
                    DataUtil.getToast("发送验证码失败");
                }else{
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                }


            }
        }
    }

    private void parseValidCode(String data){
        ValidCodeBean ret = JsonUtils.fromJson(data, ValidCodeBean.class);
        if(ret != null && ret.getServerResult() != null) {
            if (Constants.SUCCESS_CODE == ret.getServerResult().getResultCode()) {
                if ("success".equals(ret.getResultMessage())) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    DataUtil.getToast("验证码不正确！");
                }
            } else {
                DataUtil.getToast(ret.getServerResult().getResultMessage());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        parseIntent();
        initView();
        initEvent();
    }

    private void parseIntent(){
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
    }

    private void initView(){
        btnGetCode = (Button) findViewById(R.id.btn_send);
        btnEnter = (Button) findViewById(R.id.btn_confirm);
        etCode = (EditText) findViewById(R.id.et_verify);
        TextView title = (TextView) findViewById(R.id.title_text_2);
        title.setText("安全验证");
        title.setVisibility(View.VISIBLE);
        back = findViewById(R.id.title_back);
        getCodeButtonState(true);
        getCommitButtonState(false);
    }

    private void initEvent(){
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode(mobile);
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString();
                if(!DataUtil.isNullorEmpty(code)) {
                    validCode(code);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    getCommitButtonState(true);
                }else{
                    getCommitButtonState(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getCommitButtonState(boolean enable){
        if(enable){
            btnEnter.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
        }else {
            btnEnter.setBackgroundResource(R.drawable.shape_only_login_btn);
        }
        btnEnter.setClickable(enable);
    }

    private void getVerifyCode(String mobile){
        JSONObject json = new JSONObject();
        try {
            json.put("mobile",mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        action = GET_CODE;
        queryPost(Constants.GET_VERIFY_CODE,json);
    }

    private void runtime() {
        time = 60;
        getCodeButtonState(false);
        handler.removeCallbacks(runnable_time);
        handler.postAtTime(runnable_time, 1000);
    }

    private void getCodeButtonState(boolean enable){
        if(enable){
            btnGetCode.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
        }else{
            btnGetCode.setBackgroundResource(R.drawable.shape_only_login_btn);
        }
        btnGetCode.setClickable(enable);
    }
    //验证码倒计时功能
    private int time;
    Runnable runnable_time = new Runnable() {
        @Override
        public void run() {
            setTimeView();
            time -= 1;
            if(time > 0){
                handler.postDelayed(this, 1000);
            }else{
                btnGetCode.setText("重新获取");
                getCodeButtonState(true);
            }
        }
    };

    private void setTimeView(){
        btnGetCode.setText("重新获取"+time + "s");
    }

    //校验验证码
    private void validCode(String code){
        JSONObject json = new JSONObject();
        try{
            json.put("mobile", mobile);
            json.put("mobileCheckCode", code);
            action = VALIDE_CODE;
            queryPost(Constants.VALID_VERIFY_CODE,json);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
