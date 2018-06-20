package com.wcyc.zigui2.newapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.widget.NewPayPop;

public class NewWangFragment extends Fragment{
	View view ;
	private ListView new_service_time;
	
	public String[] s = new String[]{"一年服务","两年服务","两年6个月服务"};

	
	public static Fragment newInstance(int index) { 
		Fragment fragment = new NewWangFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.new_fragment_wang_list, null);
		new_service_time = (ListView) view.findViewById(R.id.new_service_time);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		final View view = getView();
		new_service_time.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,s));
		new_service_time.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewPayPop pp = new NewPayPop(getActivity(),1);
				pp.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				
			}
		});
	}
	
}