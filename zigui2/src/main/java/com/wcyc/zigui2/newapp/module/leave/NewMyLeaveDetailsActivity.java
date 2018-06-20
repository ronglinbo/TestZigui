package com.wcyc.zigui2.newapp.module.leave;

import java.util.List;

import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.module.wages.NewWagesActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 请假详情
 * 
 * @author 郑国栋 2016-7-15
 * @version 2.0
 */
public class NewMyLeaveDetailsActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private NewMyLeaveBean newMyLeaveBean;
	private TextView leave_user_name;
	private TextView leave_type;
	private TextView leave_time;
	private TextView leave_long;
	private TextView leave_reason;
	private TextView title_right_tv;
	private CustomDialog dialog;
	private LinearLayout leave_adviser_ll;
	private TextView leave_is_agree_tv;
	private TextView leave_opinion_tv;
	private RelativeLayout leave_opinion_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_my_leave_details);
		initView();
		initDatas();
		initEvents();
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
		title_right_tv = (TextView) findViewById(R.id.title_right_tv);
		
		leave_user_name = (TextView) findViewById(R.id.leave_user_name);
		leave_type = (TextView) findViewById(R.id.leave_type);
		leave_time = (TextView) findViewById(R.id.leave_time);
		leave_long = (TextView) findViewById(R.id.leave_long);
		leave_reason = (TextView) findViewById(R.id.leave_reason);

		leave_adviser_ll = (LinearLayout) findViewById(R.id.leave_adviser_ll);//
		leave_is_agree_tv = (TextView) findViewById(R.id.leave_is_agree_tv);
		leave_opinion_tv = (TextView) findViewById(R.id.leave_opinion_tv);
		leave_opinion_ll = (RelativeLayout) findViewById(R.id.leave_opinion_ll);//
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("请假详情");// 
		newMyLeaveBean = (NewMyLeaveBean) getIntent().getSerializableExtra("newMyLeaveBean_i");

		String childName = CCApplication.getInstance().getPresentUser().getChildName();
		leave_user_name.setText(childName);
		String leaveTypeStr=newMyLeaveBean.getLeaveType();
		String leaveType="";
		if("1".equals(leaveTypeStr)){
			leaveType="事假";
		}else if("2".equals(leaveTypeStr)){
			leaveType="病假";
		}else if("3".equals(leaveTypeStr)){
			leaveType="丧假";
		}else if("4".equals(leaveTypeStr)){
			leaveType="探亲假";
		}else{
			leaveType="其他";
		}
		leave_type.setText(leaveType);
		
		String leaveStartTimeStr =newMyLeaveBean.getLeaveStartTime();
		String leaveStartTime=leaveStartTimeStr.substring(0, leaveStartTimeStr.lastIndexOf(":"));
		String leaveEndTimeStr =newMyLeaveBean.getLeaveEndTime();
		String leaveEndTime=leaveEndTimeStr.substring(0, leaveEndTimeStr.lastIndexOf(":"));
		leave_time.setText(leaveStartTime+"至"+leaveEndTime);
		leave_long.setText(newMyLeaveBean.getLeaveDays()+"天"+newMyLeaveBean.getLeaveHours()+"小时");
		leave_reason.setText(newMyLeaveBean.getReason());
		
		String statusStr = "";//0无效  1审批中  2同意  3不同意
		String status=newMyLeaveBean.getStatus();
		String commentsStr=newMyLeaveBean.getComments();
		if("0".equals(status)||"1".equals(status)){
			leave_adviser_ll.setVisibility(View.GONE);
		}else if("2".equals(status)){
			leave_adviser_ll.setVisibility(View.VISIBLE);
			statusStr="同意";
			leave_is_agree_tv.setText(statusStr);
			if(DataUtil.isNullorEmpty(commentsStr)){
				leave_opinion_ll.setVisibility(View.GONE);
			}else{
				leave_opinion_tv.setText(commentsStr);
			}
		}else if("3".equals(status)){
			leave_adviser_ll.setVisibility(View.VISIBLE);
			statusStr="不同意";
			leave_is_agree_tv.setText(statusStr);
			if(DataUtil.isNullorEmpty(commentsStr)){
				leave_opinion_ll.setVisibility(View.GONE);
			}else{
				leave_opinion_tv.setText(commentsStr);
			}
		}
		
		if("1".equals(newMyLeaveBean.getStatus())){
			title_right_tv.setVisibility(View.VISIBLE);
			title_right_tv.setText("删除");
			title_right_tv.setTextColor(getResources().getColor(R.color.leave_red));
		}else{
			title_right_tv.setVisibility(View.GONE);
		}
		
		
		//删除模块已读数，减一
		try {
			List<ModelRemind> remind = CCApplication.getInstance()
					.getModelRemindList().getMessageList();
			String newLeaveNumb = "";
			if (remind != null) {
				for (int i = 0; i < remind.size(); i++) {
					String remindType = remind.get(i).getType();
					if ("23".equals(remindType)) {
						newLeaveNumb = remind.get(i).getCount();
					}
				}
			}
			System.out.println("==newLeaveNumb==" + newLeaveNumb);
			String userId = CCApplication.getInstance().getPresentUser().getUserId();
			String userType = CCApplication.getInstance().getPresentUser().getUserType();
			String LeaveId=newMyLeaveBean.getId();
			if (!DataUtil.isNullorEmpty(newLeaveNumb)) {
				JSONObject jsonC = new JSONObject();
				jsonC.put("userId", userId);
				jsonC.put("userType", userType);
				jsonC.put("dataId", LeaveId);
				if ("3".equals(userType)) {
					String studentId = CCApplication.getInstance()
							.getPresentUser().getChildId();
					jsonC.put("studentId", studentId);
				}
				
				jsonC.put("modelType", "23");
				
				String urlC = new StringBuilder(Constants.SERVER_URL)
				.append(Constants.DEL_MODEL_REMIND_ZGD).toString();
				String resultC = HttpHelper.httpPostJson(this, urlC, jsonC);
				System.out.println("==resultC==="+resultC);
				// json对象 里面有属性ServerResult 请求结果
				NewBaseBean bb = JsonUtils.fromJson(resultC,
						NewBaseBean.class);
				if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
					System.out.println("===请假条模块最新动态-1已删除===");
					Intent broadcast = new Intent(
							NewWagesActivity.INTENT_REFESH_WAGES_DATA);
					sendBroadcast(broadcast);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置点击事件监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		
		title_right_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewMyLeaveDetailsActivity.this.finish();
			break;
		case R.id.title_right_tv:
			exitOrNot();
			break;
		default:
			break;

		}
	}
	
	private void exitOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("您的假条正在审批中，确定删除吗？");
		dialog.setContent("");
	}
	
	
	/**
	 * 控制CustomDialog按钮事件.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确定
				if (!DataUtil.isNetworkAvailable(NewMyLeaveDetailsActivity.this)) {
					DataUtil.getToast(getResources().getString(R.string.no_network));
					return;
				}
				
				if(DataUtil.isFastDoubleClick()){
					return;
				}
				
				if (!isLoading()) {
					httpLeaveDeleteInfo();
				}
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	public void httpLeaveDeleteInfo(){
		String childId =CCApplication.getInstance().getPresentUser().getChildId();
		String id=newMyLeaveBean.getId();
		
		try {
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("userId", childId);
			json.put("id", id);
			
			System.out.println("删除请假入参=====" + json);
			queryPost(Constants.LEAVE_DELETE_INFO, json);
		} catch (Exception e) {
		}
	}
	
	
	@Override
	protected void getMessage(String data) {
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			DataUtil.getToast("已删除！");
			Intent broadcast = new Intent(NewMyLeaveActivity.INTENT_REFRESH_DATA_LEAVE);
			sendBroadcast(broadcast);
			NewMyLeaveDetailsActivity.this.finish();
		}
	}
}
