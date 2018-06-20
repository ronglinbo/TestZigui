package com.wcyc.zigui2.newapp.home;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;

/**
 * 关于页面类.
 * 
 * @author yytan
 * @version 2.0
 */
public class NewAboutActivity extends BaseActivity implements OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private TextView app_version;
	private TextView dianhua;
	private TextView guanwang;
	private TextView service_agreement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_about);
		initView();
		initDatas();
		initEvents();
	}

	/**
	 * 实例化标题按钮组件
	 */
	private void initView() {
		title_back = (LinearLayout) findViewById(R.id.title_back);
		dianhua = (TextView) findViewById(R.id.dianhua);
		guanwang = (TextView) findViewById(R.id.guanwang);
		new_content = (TextView) findViewById(R.id.new_content);
		app_version = (TextView) findViewById(R.id.app_version);
		service_agreement = (TextView)findViewById(R.id.service_agreement);
	}

	/**
	 * 设置点击事件监听器
	 */
	private void initEvents() {
		title_back.setOnClickListener(this);
		service_agreement.setOnClickListener(this);
		dianhua.setOnClickListener(this);
		guanwang.setOnClickListener(this);
		new_content.setText("关于");
	}

	/**
	 * 设置标题按钮的文字
	 */
	private void initDatas() {
		title_back.setVisibility(View.VISIBLE);
		String str=getVersion();
		app_version.setText(str);

//		String u="/getRelationCode";
//		try {
//			JSONObject json2 = new JSONObject();
//			json2.put("parentCode", "GXM");
//			String url = new StringBuilder(Constants.SERVER_URL).append(
//					u).toString();
//			String result = HttpHelper.httpPostJson(this, url, json2);
//			System.out.println("关系===" + result);
//		}catch (Exception e){
//			e.printStackTrace();
//		}

	}

	/**
	 * 得到信息
	 * 
	 * @param data
	 *            数据
	 */
	@Override
	protected void getMessage(String data) {

	}

	/**
	 * 点击事件. 当点击视图中的标题按钮，则表明
	 * 
	 * @param v
	 *            点击的视图
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewAboutActivity.this.finish();
			break;
		case R.id.dianhua:
			String dianhua_str=dianhua.getText().toString().trim();
			dianhua_str=dianhua_str.replaceAll("-", "");
			
			//用intent启动拨打电话  
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+dianhua_str));  
            startActivity(intent);
            break;
		case R.id.guanwang:
			String guanwang_str=guanwang.getText().toString().trim();
			
			 Uri uri = Uri.parse("http://"+guanwang_str);
			 System.out.println("====uri===="+uri);
			 
			 try {
				 Intent intent2 = new Intent(Intent.ACTION_VIEW,uri);
				 startActivity(intent2);
			} catch (Exception e) {
				DataUtil.getToast("您把手机内置默认浏览器删除了~~~");
			}
			 break;
			case R.id.service_agreement:
				newActivity(ServiceAgreementActivity.class, null);
				break;
		default:
			break;
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
//			return this.getString(R.string.version_name) + version;
			
			return "版本号 " + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}

}
