package com.wcyc.zigui2.newapp.bean;

import java.util.List;


public class ServiceExpiredBean extends NewBaseBean{

	/**
	 * 
	 */
	private List<ServiceInfo> serviceList;
	public class ServiceInfo{
		private String endDate;//产品到期日
		private String serviceExpired;//产品是否过期（0：已过期or从未购买过产品，1：正常）
		private String serviceId;
		private String serviceName;
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getServiceExpired() {
			return serviceExpired;
		}
		public void setServiceExpired(String serviceExpired) {
			this.serviceExpired = serviceExpired;
		}
		public String getServiceId() {
			return serviceId;
		}
		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}
		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
	}
	private static final long serialVersionUID = -4654011194029345566L;
	
	public List<ServiceInfo> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
	}

}