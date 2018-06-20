package com.wcyc.zigui2.newapp.module.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.newapp.module.email.EmailDetailActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.widget.ChooseRolesList;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop.OnLongClick;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EmailItemFragment extends Fragment 
	implements OnLongClick, HttpRequestAsyncTaskListener{
	
	protected View view;
	protected RefreshListView listView;
//	protected List<EmailBean> list1;
	protected List<NewMailInfo> list;
	protected NewEmailBean email,allEmail;
	protected EmailListAdapter listAdapter;
	protected TextView tvNoMessage;
	protected GetAdapterDataListener listener;
	protected int type;
	private String emailType;
	protected static int pos;//当前选中的email位置
	protected boolean isReply;
	private int curPage = 1;
	
	public interface GetAdapterDataListener{
		List<NewMailInfo> GetAdapterData(int Type);
		EmailListAdapter getAdapter();
	}

	public EmailItemFragment(){

	}
	public ListView getListView(){
		return listView;
	}
	
	public TextView getNoMessageView(){
		return tvNoMessage;
	}

	public EmailListAdapter getListAdapter(){
		return listAdapter;
	}

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.inbox_fragment, null);
		initView();
		initEvent();
		return view;
	}

	public void onResume(){
		super.onResume();
		Bundle bundle = getArguments();
		if(bundle != null){
			type = bundle.getInt("type");
			System.out.println("type:" + type);
//			if(listener != null) {
//				System.out.println("listener:" + listener);
//				list = listener.GetAdapterData(type);
//				System.out.println("list:" + list);
//			}
		}
		initData();
		getEmailType();
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	public void onAttach(Activity activity){
		super.onAttach(activity);
		listener = (GetAdapterDataListener) activity;
	}
	
	public static Fragment newInstance(int type) {
		// TODO Auto-generated method stub
		Fragment fragment = new EmailItemFragment();
		Bundle args = new Bundle();
		args.putInt("index", type);
		args.putInt("type",type);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	
	private void initView(){
		listView = (RefreshListView) view.findViewById(R.id.email_list);
		tvNoMessage = (TextView) view.findViewById(R.id.tv_no_message);
	}

	private void initData(){
		if(listAdapter == null) {
			listAdapter = new EmailListAdapter(getActivity(), list, type);
			listView.setAdapter(listAdapter);
		}
		listAdapter.notifyDataSetChanged();
		System.out.println("initData listAdapter:" + listAdapter);
		if(listAdapter.isEmpty()){
			tvNoMessage.setVisibility(View.VISIBLE);
		}else{
			tvNoMessage.setVisibility(View.GONE);
		}
		getEmailType();
	}

	private void getEmailType(){
		switch(type){
			case EmailActivity.INBOX:
				emailType = "inbox";
				break;
			case EmailActivity.OUTBOX:
				emailType = "outbox";
				break;
			case EmailActivity.DRAFT:
				emailType = "draft";
				break;
			case EmailActivity.RECYCLE:
				emailType = "recycle";
				break;
		}
	}

	private void initEvent(){
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("email item onItemClick:"+arg2);
				Intent intent = null;

				intent = new Intent(getActivity(),EmailDetailActivity.class);

				Bundle bundle = new Bundle();
				bundle.putInt("type", type);

				NewMailInfo detail =  (NewMailInfo) arg0.getAdapter().getItem(arg2);
				detail.setIsRead("1");
				((EmailActivity)getActivity()).getAdapter().notifyDataSetChanged();
				allEmail = CCApplication.getInstance().getEmail(emailType);
				if(allEmail != null){
					detail = allEmail.getMailInfoList().get(arg2-1);
					if("0".equals(detail.getIsRead())) {
						detail.setIsRead("1");
						CCApplication.getInstance().setEmail(allEmail, emailType);
					}
				}
				bundle.putSerializable("data", detail);
				System.out.println("delItem:"+detail);
				intent.putExtras(bundle);
				startActivity(intent);
				pos = arg2;
			}
			
		});
		
		listView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onDownPullRefresh() {//下拉刷新
				// TODO Auto-generated method stub
				System.out.println("onDownPullRefresh curPage:"+curPage);
				if(curPage > 1){
					curPage--;
					list = email.getMailInfoList();
				}else{
					curPage = 1;
					((EmailActivity) getActivity()).getEmailList(curPage,type);
					DataUtil.getToast("已经是首页了");
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {//上拉加载更多
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage);
//				email = ((EmailActivity) getActivity()).getEmail();
				email = CCApplication.getInstance().getEmail(emailType);
				if(email == null) return;
				int count = 0;
				list = email.getMailInfoList();
				if(list != null) {
					count = list.size();
				}
				int totalPageNum = email.getTotalPageNum();
				int page = count / Constants.defaultPageSize;
				int left = count % Constants.defaultPageSize;
				if(left > 0 && page < totalPageNum - 1 || left == 0 && page < totalPageNum){
					((EmailActivity) getActivity()).getEmailList(++curPage,type);
//					loadMore(type,++curPage);
				}else{
					DataUtil.getToast("没有更多数据了");
				}

//				if(curPage < totalPageNum && curPage >= email.getPageNum()){
//					((EmailActivity) getActivity()).getEmailList(++curPage,type);
//				}else{
//					NewEmailBean email = CCApplication.getInstance().getEmail(emailType);
//					if(email != null)
//						list = email.getMailInfoList();
//				}
//				if(curPage >= totalPageNum){
//					DataUtil.getToast("没有更多数据了");
//				}
				listView.hideFooterView();
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("arg2: " + arg2 + " arg3: " + arg3);
				pos = (int) arg3;
				DeleteItemPop pop = new DeleteItemPop(getActivity(),EmailItemFragment.this);
				int[] location = {-1,-1};
				arg1.getLocationOnScreen(location);
				pop.showAtLocation(arg1, Gravity.NO_GRAVITY, location[0]+arg1.getWidth()/2, location[1]);  

				return true;
			}
			
		});
	}
	
	public void MoveEmailToRecycle(){
		System.out.println("MoveEmailToRecycle:"+pos);
		list.remove(pos);
		listAdapter.notifyDataSetChanged();
	}
	
	private void deleteEmail(NewMailInfo data){
		UserType user = CCApplication.getInstance().getPresentUser();
		String userId = "";
		if(user != null){
			userId = user.getUserId();
		}
		JSONObject json = new JSONObject();
		try {
			json.put("userID", userId);
			json.put("emailID",data.getId());
			json.put("curStatus", String.valueOf(type));
			json.put("statusByDel", "");
			json.put("queryRes", data.getQueryRes());
			json.put("userType",user.getUserType());
			System.out.println("删除邮件:"+json);
			new HttpRequestAsyncTask(json,this,getActivity())
				.execute(Constants.DELETE_EMAIL);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	@Override
	public void deleteItem() {
		// TODO Auto-generated method stub
//		list = listener.GetAdapterData(type);
// 		NewMailInfo data = list.get(pos);
		NewMailInfo data = (NewMailInfo) listAdapter.getItem(pos);
		deleteEmail(data);
	}

	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		System.out.println(result);
		NewBaseBean ret = JsonUtils.fromJson(result,NewBaseBean.class);
		if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
			NewMailInfo data = (NewMailInfo) listAdapter.getItem(pos);
			removeItem(data,emailType);
			if(emailType.equals("recycle")){
				DataUtil.getToast("删除成功");
			}else{
				DataUtil.getToast("已删除至回收站");
			}
		}else{
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		}
	}

	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}

	private void removeItem(NewMailInfo remove,String type){
		NewEmailBean email = CCApplication.getInstance().getEmail(type);
		List<NewMailInfo> list = email.getMailInfoList();
		if(list != null && remove != null){
			for(NewMailInfo item:list){
				String id = item.getId();
				if(id != null && id.equals(remove.getId())){
					list.remove(item);
					break;
				}
			}
		}
		CCApplication.getInstance().setEmail(email,type);
		listener.getAdapter().notifyDataSetChanged();
	}

	private void loadMore(int type,int curPage){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		String schoolId = user.getSchoolId();
		try {
			if (Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
				json.put("childId", user.getChildId());
			}
			json.put("userID", user.getUserId());
			json.put("schoolId", schoolId);
			json.put("status", type);
			json.put("curPage", curPage);
			json.put("pageSize", Constants.defaultPageSize);
		}catch (Exception e){
			e.printStackTrace();
		}

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Constants.SERVER_URL+"/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(CCApplication.getInstance().initClient())
				.build();
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json.toString());
		ApiManager apiManager = retrofit.create(ApiManager.class);
		final Call<NewEmailBean> call = apiManager.EmailList(body);
		final ProgressDialog pd = DataUtil.showProcess(getActivity());
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				call.cancel();
			}
		});
		call.enqueue(new Callback<NewEmailBean>() {

			@Override
			public void onResponse(Call<NewEmailBean> call, final Response<NewEmailBean> response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						parseMoreEmailList(response.body());
						pd.dismiss();
					}
				});

			}

			@Override
			public void onFailure(Call<NewEmailBean> call, Throwable t) {

			}
		});
	}

	private void parseMoreEmailList(NewEmailBean email){
		try{
			List<NewMailInfo> list = email.getMailInfoList();
			if(list != null) {
				listAdapter.addItem(email.getMailInfoList());
			}
			listAdapter.notifyDataSetChanged();
			TextView noMessage = getNoMessageView();
			if(listAdapter.isEmpty()){
				noMessage.setVisibility(View.VISIBLE);
			}else{
				noMessage.setVisibility(View.GONE);
			}
			CCApplication.getInstance().addEmail(email,((EmailActivity)getActivity()).getType(type));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}