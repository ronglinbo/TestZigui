package com.wcyc.zigui2.newapp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class RepairManAndTypeInfo {

   public List<RepairUserTypeList> repairUserTypeList;
   public ServerResult serverResult;

   public List<RepairUserTypeList> getRepairUserTypeList() {
      if(null==repairUserTypeList){
         repairUserTypeList=new ArrayList<RepairUserTypeList>();
      }
      return repairUserTypeList;
   }

   public static class RepairUserTypeList{
      public int id;
      public List<ListCBR> listCBR;
      public String memo;
      public String operatorTime;
      public String repairTypeName;
      public String repairUserNames;
      public int schoolId;
      public int status;

      public List<ListCBR> getListCBR() {
         if(null==listCBR){
            listCBR=new ArrayList<ListCBR>();
         }
         return listCBR;
      }
   }

   public static class ListCBR{
      public int id;
      public String operatorTime;
      public int repairTypeId;
      public int repairUserId;
      public int status;
      public String userCode;
      public String userMobile;
      public String userName;
   }


}
