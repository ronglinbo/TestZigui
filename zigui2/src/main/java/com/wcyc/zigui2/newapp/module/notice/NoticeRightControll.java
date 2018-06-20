package com.wcyc.zigui2.newapp.module.notice;

import java.util.List;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.utils.Constants;

public class NoticeRightControll{
	/**是否有发布通知的权限*/
	public static boolean hasPublishNoticeRight(){
		MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
		boolean isParent = CCApplication.getInstance().isCurUserParent();
		if(isParent == true){
			return false;
		}
		if(detail != null){
			List<Role> list = detail.getRoleList();
			if(list != null){
				for(Role item:list){
					if(Constants.DEPT_ADMIN.equals(item.getRoleCode())
						||Constants.GRADE_LEADER.equals(item.getRoleCode())
						||Constants.SCHOOL_LEADER.equals(item.getRoleCode())
						||Constants.SCHOOL_ADMIN.equals(item.getRoleCode())
						||Constants.CLASS_HEADER.equals(item.getRoleCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**是否有发布通知给所有用户的权限*/
	public static boolean hasPublishNoticeToAllUserRight(){
		MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
		boolean isParent = CCApplication.getInstance().isCurUserParent();
		if(isParent == true){
			return false;
		}
		if(detail != null){
			List<Role> list = detail.getRoleList();
			if(list != null){
				for(Role item:list){
					if(Constants.DEPT_ADMIN.equals(item.getRoleCode())
						||Constants.GRADE_LEADER.equals(item.getRoleCode())
						||Constants.SCHOOL_LEADER.equals(item.getRoleCode())
						||Constants.SCHOOL_ADMIN.equals(item.getRoleCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
	/**学校管理员、校级领导：可查看、删除所有人发的通知。*/
	public static  boolean hasDeleteAllNoticeRight(){
		MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
		boolean isParent = CCApplication.getInstance().isCurUserParent();
		if(isParent == true){
			return false;
		}
		if(detail != null){
			List<Role> list = detail.getRoleList();
			if(list != null){
				for(Role item:list){
					if(Constants.SCHOOL_LEADER.equals(item.getRoleCode())
						||Constants.SCHOOL_ADMIN.equals(item.getRoleCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
}