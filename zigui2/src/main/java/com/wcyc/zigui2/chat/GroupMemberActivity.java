package com.wcyc.zigui2.chat;
/*群组成员*/

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.fragment.BitmapCache;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class GroupMemberActivity extends BaseActivity{
	private LinearLayout back;
	private GridView gridView;
	private MemberAdapter adapter;
	private String groupId,groupName;
	private AllContactListBean allContact;
	private List <ClassList> classList;
	private List<ContactsList> contactsList;
	private String userName; //环信用户名
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private RequestQueue queue;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupmember);
		initData();
		initView();
		initAdapter();
	}
	
	private void initData(){
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.pho_touxiang)
				.showImageOnFail(R.drawable.pho_touxiang).resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.pho_touxiang)
				.displayer(new FadeInBitmapDisplayer(300)).build();
//		imageLoader = ImageLoader.getInstance();
		queue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(queue, new BitmapCache());
		userName = CCApplication.app.getUserName();
		Intent intent = getIntent();
		groupId = intent.getStringExtra("groupId");
		groupName = intent.getStringExtra("groupName");
		allContact = CCApplication.getInstance().getAllContactList();
		classList = allContact.getClassList();
		for(ClassList classItem:classList){
			if(!DataUtil.isNullorEmpty(groupId)){
				if(groupId.equals(classItem.getGroupId())){
					contactsList = classItem.getContactsList();
				}
			}else{
				if(!DataUtil.isNullorEmpty(groupName)){
					if(groupName.equals(classItem.getClassName())){
						contactsList = classItem.getContactsList();
					}
				}
			}
		}

		//列表中去掉本人
		List<ContactsList> temp = DataUtil.removeSelfInContact(contactsList);
		contactsList = temp;
	}
	
	private void initView(){
		
		back = (LinearLayout) findViewById(R.id.title_back);
		back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.new_content)).setText(groupName);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		gridView = (GridView) findViewById(R.id.member);
		if(contactsList != null){
			String detail = String.format("(共%d人)", contactsList.size());
			((TextView)findViewById(R.id.num)).setText(detail);
		}
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ContactsList contact = (ContactsList) adapter.getItem(arg2);
				String nickName = contact.getHxNickName();
				System.out.println("onItemClick:"+arg2);
				Intent intent = new Intent(GroupMemberActivity.this,ContactDetail.class); 
				intent.putExtra("cellPhone", contact.getCellphone());
				intent.putExtra("userName", contact.getUserName());
				if(DataUtil.isNullorEmpty(nickName)){
					nickName = contact.getNickName();
				}
				intent.putExtra("userNick", nickName);
				intent.putExtra("userTitle", contact.getText());
				// 接受者头像
				intent.putExtra("avatarUrl", contact.getUserIconURL());
				startActivity(intent);
			}
			
		});
	}
	
	private void initAdapter(){
		adapter = new MemberAdapter();
		gridView.setAdapter(adapter);
	}
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		
	}
	
	public class MemberAdapter extends BaseAdapter{
	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(contactsList != null){
				return contactsList.size();
			}
			return 0;
		}
	
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if(contactsList != null){
				return contactsList.get(arg0);
			}
			return null;
		}
	
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			String name;
			final ContactsList contact = contactsList.get(arg0);
			String file = contact.getUserIconURL();
//			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(GroupMemberActivity.this)
						.inflate(R.layout.group_member_item, arg2, false);
				holder.avatar = (RoundImageView) arg1.findViewById(R.id.avatar);
				holder.tvName = (TextView) arg1.findViewById(R.id.name);
				arg1.setTag(holder);
//			}else{
//				holder = (ViewHolder) arg1.getTag();
//			}
//			holder.avatar.setTag(file);
			name = contact.getHxNickName();
			if(DataUtil.isNullorEmpty(name)){
				name = contact.getNickName();
			}

			setImage(contact,holder);
			holder.tvName.setText(name);
			return arg1;
		}

		private void setImage(ContactsList contact,final ViewHolder holder){
			final String file = contact.getUserIconURL();
			String url = null;
			String hxUserName = contact.getUserName();
//			String tag = (String) holder.avatar.getTag();
			if(file != null)
				url = DataUtil.getIconURL(file);
			if(LocalUtil.mBitMap != null && hxUserName != null && hxUserName.equals(userName)){
				holder.avatar.setImageBitmap(LocalUtil.mBitMap);
			}else{
				if(url != null){
					GroupMemberActivity.this.getImageLoader().displayImage(url, holder.avatar,
							(GroupMemberActivity.this).getImageLoaderOptions());


//					System.out.println("url:"+url);
//					if(tag != null && tag.equals(file)) {
//						imageLoader.get(url, new ImageListener(){
//
//							@Override
//							public void onErrorResponse(VolleyError arg0) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onResponse(ImageLoader.ImageContainer arg0, boolean arg1) {
//								// TODO Auto-generated method stub
////								String tag = (String) holder.avatar.getTag();
//
////								System.out.println("file:"+file+" tag:"+tag);
////								if(tag != null && tag.equals(file)){
//									holder.avatar.setImageBitmap(arg0.getBitmap());
////								}
//							}
//
//						});
//					}
				}else{
					holder.avatar.setBackgroundResource(R.drawable.pho_touxiang);
				}
			}
		}
		private class ViewHolder {
			/** 联系人名字 */
			TextView tvName;
			/** 用户头像 */
			RoundImageView avatar;
		}
	}
}