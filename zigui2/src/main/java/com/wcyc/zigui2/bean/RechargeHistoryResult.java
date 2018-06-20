package com.wcyc.zigui2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请关注返回定义
 * @ClassName: RechargeHistoryResult 
 * @Description: 邀请关注返回定义 
 * @author xfliu
 * @date 2014-12-15
 *
 */
public class RechargeHistoryResult extends BaseBean implements Serializable{

	private String familyState;//家庭套餐状态（0：未开通；1：开通；）
	
	private int familyNum;//家庭成员数量
	
    private List<FamilyResult> familyList;//家庭成员列表

    private List<ApplyJoinResult> applyJoinList;//申请加入列表

	public String getFamilyState() {
		return familyState;
	}

	public void setFamilyState(String familyState) {
		this.familyState = familyState;
	}

	public int getFamilyNum() {
		return familyNum;
	}

	public void setFamilyNum(int familyNum) {
		this.familyNum = familyNum;
	}

	public List<FamilyResult> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<FamilyResult> familyList) {
		this.familyList = familyList;
	}

	public List<ApplyJoinResult> getApplyJoinList() {
		return applyJoinList;
	}

	public void setApplyJoinList(List<ApplyJoinResult> applyJoinList) {
		this.applyJoinList = applyJoinList;
	}
    
    
}
