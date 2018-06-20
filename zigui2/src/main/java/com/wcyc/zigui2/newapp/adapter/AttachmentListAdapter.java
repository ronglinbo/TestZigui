package com.wcyc.zigui2.newapp.adapter;

import java.io.File;
import java.util.List;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//发布时选择附件的列表
public class AttachmentListAdapter extends BaseAdapter {

	private List<String> fileList;
	private Context mContext;
	private long sizeAllLong=0;
	private String sizeAllStr = "";

	public AttachmentListAdapter(Context mContext, List<String> fileList) {
		this.mContext = mContext;
		this.fileList = fileList;

		try {
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					String file = fileList.get(i);
					long size = 0;

					size = DataUtil.getFileSize(new File(file));

					sizeAllLong += size;

				}
				sizeAllStr = DataUtil.FormetFileSize(sizeAllLong);
			} else {
				System.out.println("=====还没选择图片===");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (fileList != null)
			return fileList.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return fileList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.attachment_list_item, null);
		}
		TextView filename = (TextView) arg1.findViewById(R.id.attach_name);
		TextView fileSize = (TextView) arg1.findViewById(R.id.tv_attach_size);
		ImageView del = (ImageView) arg1.findViewById(R.id.iv_del);
		String file = fileList.get(arg0);
		long size = 0;
		try {
			size = DataUtil.getFileSize(new File(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String name = file.substring(file.lastIndexOf("/") + 1, file.length());
		filename.setText(name);
		fileSize.setText(DataUtil.FormetFileSize(size));
		arg1.setTag(arg0);
		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
					fileList.remove(arg0);
					notifyDataSetChanged();
				}catch (Exception e){
					e.printStackTrace();
				}
			}

		});
		return arg1;
	}

}