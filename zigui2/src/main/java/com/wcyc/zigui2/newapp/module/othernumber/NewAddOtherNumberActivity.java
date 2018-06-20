package com.wcyc.zigui2.newapp.module.othernumber;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.ChildRelationTypeBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.SpinnerButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加副号页面
 *
 * @author gdzehng
 * @version 2.06
 */
public class NewAddOtherNumberActivity extends BaseActivity implements OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private EditText add_other_number_et;
    private ImageView add_other_number_delete_icon;
    private SpinnerButton spinnerButton;
    private ListView spinnerListView;// 与小孩关系listview
    private List<ChildRelationTypeBean> childRelationTypeList;
    private int childRelationType_i = -1;
    private Button add_other_number_btn;
    private static final int PARENT_MOBILE_ADD = 101;
    private static final int GET_RELATION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_add_other_number);
        initView();
        initDatas();
        initEvents();

        inputState();
    }

    /**
     * 实例化标题按钮组件
     */
    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);
        new_content = (TextView) findViewById(R.id.new_content);
        spinnerButton = (SpinnerButton) findViewById(R.id.child_relation_spinner);

        add_other_number_et = (EditText) findViewById(R.id.add_other_number_et);
        add_other_number_delete_icon = (ImageView) findViewById(R.id.add_other_number_delete_icon);
        TextFilter textFilter1 = new TextFilter(add_other_number_et);
        textFilter1.setEditeTextClearListener(add_other_number_et,
                add_other_number_delete_icon);

        add_other_number_btn = (Button) findViewById(R.id.add_other_number_btn);
        add_other_number_btn.setOnClickListener(this);
        add_other_number_btn.setClickable(false);
        add_other_number_btn.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);

    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        // title_btn.setText(R.string.myset_about);
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("添加副号");
        try {
            JSONObject json = new JSONObject();
			json.put("parentCode", "GXM");
            if (!DataUtil.isNetworkAvailable(NewAddOtherNumberActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                NewAddOtherNumberActivity.this.finish();
                return;
            }
            if (!isLoading()) {
                System.out.println("获取所有关系入参json=====" + json);
                queryPost(Constants.GET_RELATION_CODE, json);
                action = GET_RELATION_CODE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 设置点击效果监听器
     */
    private void initEvents() {
        title_back.setOnClickListener(this);

    }


    // 设置选择班级 adapter 下拉列表
    class MySpinnerAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MySpinnerAdapter() {
            super();
            inflater = LayoutInflater.from(NewAddOtherNumberActivity.this);
        }

        @Override
        public int getCount() {

            if (childRelationTypeList != null) {
                return childRelationTypeList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {

            return childRelationTypeList.get(position);

        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder = null;
            if (convertView == null) {
                // 实例化控件
                viewholder = new ViewHolder();
                // 配置单个item的布局
                convertView = inflater.inflate(R.layout.new_class_list_name,
                        parent, false);//打气筒设置布局
                viewholder.bzr_class_name = (TextView) convertView.findViewById(R.id.class_name);//找到要显示  的控件
                // 设置标签
                convertView.setTag(viewholder);
            } else {
                // 获得标签 如果已经实例化则用历史记录
                viewholder = (ViewHolder) convertView.getTag();
            }

            viewholder.bzr_class_name.setText(childRelationTypeList.get(position).getConfigName());// 设置显示
            return convertView;
        }

        private class ViewHolder {
            TextView bzr_class_name;
        }

    }


    /**
     * 得到信息
     *
     * @param data 数据
     */
    @Override
    protected void getMessage(String data) {
        switch (action) {
            case PARENT_MOBILE_ADD:
                System.out.println("添加副号出参data=====" + data);
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {
                    DataUtil.getToast("添加副号成功");
                    Intent broadcast = new Intent(NewOtherNumberActivity.INTENT_REFRESH_DATA_Other_Number);
                    sendBroadcast(broadcast);
                    NewAddOtherNumberActivity.this.finish();
                }
                break;
            case GET_RELATION_CODE:
                System.out.println("获取所有关系出参data=====" + data);
                NewBaseBean ret2 = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret2.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret2.getServerResult().getResultMessage());
                } else {
                    try {
                        JSONObject json = new JSONObject(data);
                        childRelationTypeList = new ArrayList<ChildRelationTypeBean>();
                        String relationShipList = json.getString("relationShipList");
                        JSONArray json2 = new JSONArray(relationShipList);
//                        JSONArray json2 = json.getJSONArray("relationShipList");
                        for (int i = 0; i < json2.length(); i++) {
                            ChildRelationTypeBean childRelationTypeBean=JsonUtils.fromJson(json2
                                    .get(i).toString(),ChildRelationTypeBean.class);
                            childRelationTypeList.add(childRelationTypeBean);
                        }

                        // 下拉列表
                        spinnerButton.showAble(true);
                        spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
                                new SpinnerButton.ViewCreatedListener() {
                                    @Override
                                    public void onViewCreated(View v) {
                                        spinnerListView = (ListView) v
                                                .findViewById(R.id.spinner_lv);
                                    }
                                });

                        final MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter();
                        spinnerListView.setAdapter(spinnerAdapter);// 添加打气筒
                        spinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                    long arg3) {
                                spinnerButton.dismiss();
                                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                                    DataUtil.getToast(getResources().getString(R.string.no_network));
                                    return;
                                }
                                // 显示
                                spinnerButton.setText(childRelationTypeList.get(arg2).getConfigName());

                                childRelationType_i = arg2;// 标记选择的是哪个
                            }

                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    /**
     * 点击效果. 当点击视图中的标题按钮，则表明
     *
     * @param v 点击的视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewAddOtherNumberActivity.this.finish();
                break;
            case R.id.add_other_number_btn:
                String add_other_number_et_str = add_other_number_et.getText().toString();
                if(!DataUtil.checkPhone(add_other_number_et_str)){
                    DataUtil.getToast("请输入正确的手机号码");
                }else{
                    String childId = CCApplication.getInstance().getPresentUser().getChildId();
                    try {
                        JSONObject json = new JSONObject();
                        json.put("relationTypeCode", childRelationTypeList.get(childRelationType_i).getConfigCode());
                        json.put("relationType", childRelationTypeList.get(childRelationType_i).getConfigName());
                        json.put("parentMoblie", add_other_number_et_str);
                        json.put("childId", childId);
                        json.put("schoolId", schoolId);
                        if (!DataUtil.isNetworkAvailable(NewAddOtherNumberActivity.this)) {
                            DataUtil.getToast(getResources().getString(R.string.no_network));
                            return;
                        }
                        if (!isLoading()) {
                            System.out.println("添加副号入参json=====" + json);
                            queryPost(Constants.PARENT_MOBILE_ADD, json);
                            action = PARENT_MOBILE_ADD;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            default:
                break;
        }
    }

    private void inputState() {
        add_other_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String spinnerButton_str = spinnerButton.getText().toString().trim();

                Boolean alreadyChooseChildRelation = false;
                if (!"".equals(spinnerButton_str)) {
                    alreadyChooseChildRelation = true;
                }

                if (alreadyChooseChildRelation && s.length() > 0) {
                    add_other_number_btn.setClickable(true);
                    add_other_number_btn.setBackgroundResource(R.drawable.btn_blue_selector);
                } else {
                    add_other_number_btn.setClickable(false);
                    add_other_number_btn.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
                }

            }

        });

        spinnerButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String spinnerButton_str = spinnerButton.getText().toString().trim();
                String add_other_number_et_str = add_other_number_et.getText().toString();

                Boolean alreadyChooseChildRelation = false;
                if (!"".equals(spinnerButton_str)) {
                    alreadyChooseChildRelation = true;
                }

                if (alreadyChooseChildRelation && add_other_number_et_str.length() > 0) {
                    add_other_number_btn.setClickable(true);
                    add_other_number_btn.setBackgroundResource(R.drawable.btn_blue_selector);
                } else {
                    add_other_number_btn.setClickable(false);
                    add_other_number_btn.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
                }

            }
        });
    }

}
