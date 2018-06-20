package com.wcyc.zigui2.newapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.TConfAddImageGvAdapter;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.RepairManAndTypeInfo;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.TConfigrChoiceClassActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.ToastUtil;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.ExpandGridView;
import com.wcyc.zigui2.widget.dialog.CustomListDialog;
import com.wcyc.zigui2.widget.popupwindow.CommonPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2018/4/9.
 */

public class ApplyForMaintainActivity extends BaseActivity implements View.OnClickListener, ImageUploadAsyncTaskListener {

    private String TAG = "TAG";


    private Context mContext;

    // 图片选择
    private ExpandGridView homework_addiv_gv;
    private TConfAddImageGvAdapter gvAdapter;

    //最大选择图片数量
    private static final int MAX_IMAGE = 6;

    private boolean is_compress;

    private static final int REQUEST_CODE = 100;

    private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
    private List<PictureURL> datas = new ArrayList<PictureURL>();// 发布消息时候，去相册选取图片后

    private ArrayList<String> transmitImagePaths = new ArrayList<String>();// 传递给服务器的集合

    private int nowImageNumber = 0;


    private TextView tvMtCancel, tvMtConfirm; //取消 和 确定

    private TextView tvMtName, tvMtType, tvRepairMan;  //申请人，维修类型，维修人

    private EditText etMtExplain;  //情况说明

    private SlipButton sbIsSMSNotify; //是否短信通知

    private boolean smsNotifyStatus = false; //是否短信通知状态

    private ImageView ivMtType, ivRepairMan;  //


    private CommonPopupWindow popupWindow;

    List<String> repairTypes = new ArrayList<String>();

    Map<String, List<String>> repairMans = new HashMap<String, List<String>>();

    private final int GET_TYPE_REPAIRMAN = 10001;
    private final int POST_PUBLISH_REPAIR = 10002;

    RepairManAndTypeInfo repairManAndTypeInfo = null;

    private List<String> attachementList = new ArrayList<String>();


    private String applyForName = "";


    private CustomDialog dialog;


    @Override
    protected void getMessage(String data) {
        Log.i(TAG, "请求返回:" + data);
        switch (action) {
            case GET_TYPE_REPAIRMAN:
                repairManAndTypeInfo = JsonUtils.fromJson(data.toString(), RepairManAndTypeInfo.class);
                if (null == repairManAndTypeInfo) {
                    DataUtil.getToast("获取维修类型失败");
                } else {
                    repairTypes.clear();
                    for (int i = 0; i < repairManAndTypeInfo.getRepairUserTypeList().size(); i++) {
                        repairTypes.add(repairManAndTypeInfo.getRepairUserTypeList().get(i).repairTypeName);
                        String[] str = repairManAndTypeInfo.getRepairUserTypeList().get(i).repairUserNames.split("，");
                        repairMans.put(repairManAndTypeInfo.getRepairUserTypeList().get(i).repairTypeName, Arrays.asList(str));
                    }
                }

                break;
            case POST_PUBLISH_REPAIR:
                // 请求结果
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 失败
                    ToastUtil.showLong(ApplyForMaintainActivity.this, ret.getServerResult().getResultMessage());
                } else {
                    ToastUtil.showLong(ApplyForMaintainActivity.this, "申请维修成功!");

                    // 通知BaseWebView  activity刷新数据
                    Intent broadcast = new Intent(
                            BaseWebviewActivity.INTENT_REFESH_DATA);
                    broadcast.putExtra("extraAction","showRepairList");
                    sendBroadcast(broadcast);

                    ApplyForMaintainActivity.this.finish();
                }
                break;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_maintain);
        mContext = this;
        getMtTypeAndRepairMan();
        initView();
        initEvents();
        isCanSubmit();
    }


    private void getMtTypeAndRepairMan() {
        JSONObject json = new JSONObject();
        try {
            json.put("applyTm", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print(TAG + "：" + json);
        queryPost(Constants.POST_TYPE_REPAIRMAN, json);
        action = GET_TYPE_REPAIRMAN;
    }


    //初始化View
    private void initView() {
        homework_addiv_gv = (ExpandGridView) findViewById(R.id.egvAddImage);
        gvAdapter = new TConfAddImageGvAdapter(this, imagePaths,
                getImageLoader(), handler);
        homework_addiv_gv.setAdapter(gvAdapter);

        tvMtCancel = (TextView) findViewById(R.id.tvMtCancel);
        tvMtCancel.setOnClickListener(this);
        tvMtConfirm = (TextView) findViewById(R.id.tvMtConfirm);
        tvMtConfirm.setOnClickListener(this);

        sbIsSMSNotify = (SlipButton) findViewById(R.id.sbIsSMSNotify);
        sbIsSMSNotify.setCheck(false);

        ivMtType = (ImageView) findViewById(R.id.ivMtType);
        ivMtType.setOnClickListener(this);
        ivRepairMan = (ImageView) findViewById(R.id.ivRepairMan);
        ivRepairMan.setOnClickListener(this);

        tvMtName = (TextView) findViewById(R.id.tvMtName);

        tvMtType = (TextView) findViewById(R.id.tvMtType);

        tvRepairMan = (TextView) findViewById(R.id.tvRepairMan);

        etMtExplain = (EditText) findViewById(R.id.etMtExplain);


        UserType curUser = CCApplication.getInstance().getPresentUser();
        applyForName = curUser.getTeacherName();
        tvMtName.setText(applyForName);

    }

    //注册事件
    private void initEvents() {

        sbIsSMSNotify.SetOnChangedListener(new SlipButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                smsNotifyStatus = CheckState;
            }
        });

        homework_addiv_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private PictureURL pictureURL;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (0 == imagePaths.size()) {// 第一次过去选择相片
                    Intent intentAdd = new Intent(ApplyForMaintainActivity.this,
                            SelectImageActivity.class);
                    intentAdd.putExtra("limit", MAX_IMAGE);
                    intentAdd.putStringArrayListExtra("addPic", imagePaths);
                    startActivityForResult(intentAdd, REQUEST_CODE);
                } else {
                    datas.clear();
                    for (int i = 0; i < imagePaths.size(); i++) {
                        pictureURL = new PictureURL();
                        pictureURL.setPictureURL("file://" + imagePaths.get(i));
                        datas.add(pictureURL);
                    }
                }
                if (0 != imagePaths.size() && position == imagePaths.size()) {// 非第一次选取相片
                    int limitImage = MAX_IMAGE;
                    if (limitImage <= MAX_IMAGE) {
                        Intent intentAdd = new Intent(ApplyForMaintainActivity.this,
                                SelectImageActivity.class);
                        intentAdd.putExtra("limit", limitImage);
                        intentAdd.putStringArrayListExtra("addPic", imagePaths);
                        intentAdd.putExtra("is_compress", is_compress);
                        startActivityForResult(intentAdd, REQUEST_CODE);
                    } else {
                        DataUtil.getToast("最多不能超过" + MAX_IMAGE + "张图片");
                    }
                }
                if (position != imagePaths.size()) {
                    // 查看照片
                    Intent intent = new Intent(ApplyForMaintainActivity.this,
                            ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                            (Serializable) datas);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
                            position);
                    startActivity(intent);
                }
            }
        });




        etMtExplain.addTextChangedListener(new TextWatcher() {
            String lastEditText="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastEditText=etMtExplain.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>200){
                    etMtExplain.setText(lastEditText);
                    etMtExplain.setSelection(etMtExplain.getText().length());
                    ToastUtil.showLong(ApplyForMaintainActivity.this,"情况说明不能超过200个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isCanSubmit();
            }
        });

    }


    //判断是否符合提交的状态
    private boolean isCanSubmit() {
        String tvRepairType = tvMtType.getText().toString().trim();
        String tvRepairName = tvRepairMan.getText().toString().trim();
        String txMtExplain = etMtExplain.getText().toString().trim();
        if (!TextUtils.isEmpty(tvRepairType) && !TextUtils.isEmpty(tvRepairName) && !TextUtils.isEmpty(txMtExplain)) {
//            tvMtConfirm.setClickable(true);
            tvMtConfirm.setTextColor(getResources().getColor(
                    R.color.font_darkblue));
            return true;
        } else {
//            tvMtConfirm.setClickable(false);
            tvMtConfirm.setTextColor(getResources().getColor(
                    R.color.font_lightgray));
            return false;
        }
    }

    //提交
    private void submit() {
        if (isCanSubmit()) {
            String tvRepairType = tvMtType.getText().toString().trim();
            String tvRepairName = tvRepairMan.getText().toString().trim();

            if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
                return;
            }

            if (DataUtil.isFastDoubleClick()) {// 多次点击判断 时间间隔两秒
                DataUtil.getToast("正在上传，请不要多次重复提交...");
                return;
            }

            if (imagePaths.size() > 0) {
                // 上传图片 如果成功执行... 如果失败执行....
                ImageUploadAsyncTask upload = new ImageUploadAsyncTask(this,
                        "1", imagePaths, Constants.UPLOAD_URL, this);
                upload.execute();
            } else {
                httpBusiInerface();
            }

        } else {
            ToastUtil.showLong(this, "申请信息不完整，请检查");

        }

    }

    private void httpBusiInerface() {
        String tvRepairType = tvMtType.getText().toString().trim();
        String tvRepairName = tvRepairMan.getText().toString().trim();

        UserType curUser = CCApplication.getInstance().getPresentUser();

        int typeCode = 0;
        int repairUserId = 0;

        for (int i = 0; i < repairManAndTypeInfo.getRepairUserTypeList().size(); i++) {
            if (tvRepairType.equals(repairManAndTypeInfo.getRepairUserTypeList().get(i).repairTypeName.trim())) {
                typeCode = repairManAndTypeInfo.getRepairUserTypeList().get(i).id;
                for (int j = 0; j < repairManAndTypeInfo.getRepairUserTypeList().get(i).getListCBR().size(); j++) {
                    if (tvRepairName.equals(repairManAndTypeInfo.getRepairUserTypeList().get(i).getListCBR().get(j).userName.trim())) {
                        repairUserId = repairManAndTypeInfo.getRepairUserTypeList().get(i).getListCBR().get(j).repairUserId;
                    }
                }
            }
        }


        JSONArray attachementArray=new JSONArray();
        if(null!=attachementList&&attachementList.size()>0){
            for (int i=0;i<attachementList.size();i++){
                attachementArray.put(attachementList.get(i));
            }
        }


        JSONObject json = new JSONObject();
        try {
            json.put("applyUserName", applyForName);
            json.put("repairTypeCode", typeCode);
            json.put("repairTypeName", tvRepairType);
            json.put("repairUserId", repairUserId);
            json.put("repairConten", etMtExplain.getText().toString());
            json.put("attachmentIdList", attachementArray);
            json.put("isSms", smsNotifyStatus?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queryPost(Constants.PUBLISH_REPAIR, json);
        action = POST_PUBLISH_REPAIR;

        Log.i(TAG, "json22:" + json);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (TConfigrChoiceClassActivity.BACK_RESULT == resultCode) {
        }
        if (RESULT_OK == resultCode) {
            ArrayList<String> images = data.getExtras().getStringArrayList(
                    "pic_paths");// 每次选择相片返回的数据
            if (null != images) {
                String title = "file://";
                transmitImagePaths.clear();
                imagePaths.clear();
                for (String str : images) {
                    transmitImagePaths.add(title + str);
                }
                imagePaths.addAll(images);
            }
            if (null != imagePaths) {
                nowImageNumber = imagePaths.size();
            }
            gvAdapter.setData(transmitImagePaths);
            gvAdapter.notifyDataSetChanged();
            is_compress = data.getBooleanExtra("is_compress", true);
        }
    }


    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            if (TConfAddImageGvAdapter.DELETE_IMAGES == msg.what) {
                int p = (Integer) msg.obj;
                imagePaths.remove(p);
                transmitImagePaths.remove(p);
                nowImageNumber = imagePaths.size();
                gvAdapter.notifyDataSetChanged();
            }

            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消退出
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认退出
                    dialog.dismiss();
                    ApplyForMaintainActivity.this.finish();//
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.ivMtType:
                if (repairTypes.size() <= 0) {
                    ToastUtil.showLong(this, "没有维修类型");
                } else {
//                    showDownPop(v);
                    showListDialog(tvMtType,1);
                }
                break;
            case R.id.ivRepairMan:
                if (TextUtils.isEmpty(tvMtType.getText().toString())) {
                    ToastUtil.showLong(this, "请先选择维修类型");
                } else {
//                    showDownPop(v);
                    showListDialog(tvRepairMan,2);
                }
                break;
            case R.id.tvMtConfirm:
                submit();
//                httpBusiInerface();
                break;

            case R.id.tvMtCancel:
                if (isCanCancel()) {
                    ApplyForMaintainActivity.this.finish();//
                }
                break;
        }

    }


    //检查是否可以退出
    private boolean isCanCancel() {
        String explain = etMtExplain.getText().toString();
        if (!TextUtils.isEmpty(explain)) {
            exitOrNot();
            return false;
        } else {
            return true;
        }
    }


    private void exitOrNot() {
        dialog = new CustomDialog(this, R.style.mystyle,
                R.layout.customdialog, handler);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle("退出此次编辑?");
        dialog.setContent("");

//		handler.
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isCanCancel()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }




    /**
     * 显示弹窗
     * @param view
     * @param type
     */
    Dialog listDialog;
    List<String> dialogDatas=null;
    public void showListDialog(final TextView view, final int type){

        if(listDialog!=null&&listDialog.isShowing()){
            listDialog.dismiss();
            listDialog=null;
        }

        String dialogTitle="维修类型";

        if(type==1){
            dialogDatas=repairTypes;
            dialogTitle="维修类型";
        }else {
            dialogDatas=repairMans.get(tvMtType.getText().toString().trim());
            if(dialogDatas==null){
                dialogDatas =new ArrayList<String>();
            }
            if(dialogDatas.size()<=0){
                dialogDatas.add("没有维修人");
            }
            dialogTitle="维修人";
        }

        listDialog= CustomListDialog.Builder(ApplyForMaintainActivity.this).getDefault()
                .view(R.layout.dialog_custom_list)
                .addViewOnclick(R.id.tvCancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listDialog.dismiss();
                    }
                })
                .setItemClick(new CustomListDialog.onClickItemListener() {
                    @Override
                    public void onClickItem(int position, String itemName) {
                        listDialog.dismiss();
                        view.setText(itemName);
                        if(type==1){
                            tvRepairMan.setText("");
                        }
                        isCanSubmit();
                    }
                })
                .setViewText(R.id.tvTitle,dialogTitle)
                .setListViewData(dialogDatas)
                .build();
        listDialog.show();
    }




    @Override
    public void onImageUploadCancelled() {
        ToastUtil.showLong(this, "发送失败，请稍后再试");
    }

    @Override
    public void onImageUploadComplete(String result) {
        Log.i(TAG,"onImageUploadComplete:"+result);
        UploadFileResult ret = JsonUtils.fromJson(result,
                UploadFileResult.class);
        attachementList.clear();
        try {

            Set<String> set = ret.getSuccFiles().keySet();
            for (String string : set) {
                attachementList.add(string);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        httpBusiInerface();

    }
}
