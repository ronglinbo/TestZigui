package com.wcyc.zigui2.newapp.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.PreferenceUtils;
import com.wcyc.zigui2.core.BaseActivity;

import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.SlipButton.OnChangedListener;

/**
 * 新版app 新消息提醒
 * 
 * @author yytan
 */
public class NewMsnActivity extends BaseActivity implements OnClickListener,
		ImageUploadAsyncTaskListener {

	private TextView new_content;
	private LinearLayout title_back;

	private SlipButton messageSwitch, voiceSwitch, shakeSwitch;
	private final static String SWITCH_OPEN = "1";// 开关打开
	private final static String SWITCH_CLOSE = "0";// 开关关闭
	private EMChatOptions chatOption;
	private String msgSwStr, voiceSwStr, shakeSwStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_msn);
		initView();
		initDatas();
		initEvents();
	}

	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);
		title_back = (LinearLayout) findViewById(R.id.title_back);

		messageSwitch = (SlipButton) findViewById(R.id.myset_message_btn);// 消息开关
		voiceSwitch = (SlipButton) findViewById(R.id.myset_message_voice_btn);// 声音开关
		shakeSwitch = (SlipButton) findViewById(R.id.myset_message_shake_btn);// 震动开关
		judgeSwitchButton();
	}

	/**
	 * 判断开关状态
	 */
	private void judgeSwitchButton() {
		chatOption = EMChatManager.getInstance().getChatOptions();
		if ("".equals(getIfMsg()) && "".equals(getIfShake())
				&& "".equals(getIfMsg())) {
			messageSwitch.setCheck(true);// 设置开关为开启状态
			msgSwStr = SWITCH_OPEN;
			voiceSwitch.setCheck(true);
			voiceSwStr = SWITCH_OPEN;
			// 设置环信接受消息有声音
			setVoice(chatOption, true);
			shakeSwitch.setCheck(true);
			shakeSwStr = SWITCH_OPEN;
			// 设置环信接受消息有震动
			setShake(chatOption, true);
			setEnable(chatOption, true);
		} else {
			if (SWITCH_CLOSE.equals(getIfMsg())) {
				messageSwitch.setCheck(false);
				msgSwStr = SWITCH_CLOSE;
				setEnable(chatOption, false);
			} else {
				messageSwitch.setCheck(true);
				msgSwStr = SWITCH_OPEN;
				setEnable(chatOption, true);
			}
			if (SWITCH_CLOSE.equals(getIfShake())) {
				shakeSwitch.setCheck(false);
				shakeSwStr = SWITCH_CLOSE;
				setShake(chatOption, false);
			} else {
				shakeSwitch.setCheck(true);
				shakeSwStr = SWITCH_OPEN;
				setShake(chatOption, true);
			}
			if (SWITCH_CLOSE.equals(getIfVoice())) {
				voiceSwitch.setCheck(false);
				voiceSwStr = SWITCH_CLOSE;
				setVoice(chatOption, false);
			} else {
				voiceSwitch.setCheck(true);
				voiceSwStr = SWITCH_OPEN;
				setVoice(chatOption, true);
			}
		}

	}

	/**
	 * 设置环信接收消息有没有声音
	 * 
	 * @param chatOptions
	 * @param flag
	 *            void
	 */
	private void setVoice(EMChatOptions chatOptions, boolean flag) {
		chatOptions.setNoticeBySound(flag);
		EMChatManager.getInstance().setChatOptions(chatOptions);
		PreferenceUtils.getInstance(this).setSettingMsgSound(flag);
	}
	
	/**
	 * 设置环信接收消息有没有震动
	 * 
	 * @param chatOptions
	 * @param flag
	 *            void
	 */
	private void setShake(EMChatOptions chatOptions, boolean flag) {
		chatOptions.setNoticedByVibrate(flag);
		EMChatManager.getInstance().setChatOptions(chatOptions);
		PreferenceUtils.getInstance(this).setSettingMsgVibrate(flag);
	}
	/**
	 * 设置环信是否接收消息
	 * 
	 * @param chatOptions
	 * @param flag
	 *            void
	 */
	private void setEnable(EMChatOptions chatOptions, boolean flag){
		chatOptions.setNotificationEnable(flag);
		EMChatManager.getInstance().setChatOptions(chatOptions);
		PreferenceUtils.getInstance(this).setSettingMsgNotification(flag);
	}
	
	private void initDatas() {
		title_back.setVisibility(View.VISIBLE);

	}

	private void initEvents() {
		title_back.setOnClickListener(this);
		new_content.setText("新消息提醒");

		messageSwitch.SetOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(boolean CheckState) {
				if (false == CheckState) {
					msgSwStr = SWITCH_CLOSE;
				} else if (true == CheckState) {
					msgSwStr = SWITCH_OPEN;
				}
				setEnable(chatOption,CheckState);
			}
		});

		shakeSwitch.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				if (false == CheckState) {
					setShake(chatOption, false);
					shakeSwStr = SWITCH_CLOSE;
				} else if (true == CheckState) {
					setShake(chatOption, true);
					shakeSwStr = SWITCH_OPEN;
				}

			}
		});

		voiceSwitch.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				if (false == CheckState) {
					setVoice(chatOption, false);
					voiceSwStr = SWITCH_CLOSE;
				} else if (true == CheckState) {
					setVoice(chatOption, true);
					voiceSwStr = SWITCH_OPEN;
				}

			}
		});
	}

	@Override
	public void onImageUploadComplete(String result, String ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onImageUploadCancelled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onImageUploadComplete(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			if (false == commitSetting(msgSwStr, voiceSwStr, shakeSwStr)) {
				DataUtil.getToast("设置失败");
			}
			NewMsnActivity.this.finish();
			newActivity(NewMySetActivity.class, null);
			break;

		default:
			break;
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK){
//            //这你写你的返回处理
//            return true;
//            }
		if (false == commitSetting(msgSwStr, voiceSwStr, shakeSwStr)) {
			DataUtil.getToast("设置失败");
		}
		NewMsnActivity.this.finish();
		newActivity(NewMySetActivity.class, null);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub

	}

}
