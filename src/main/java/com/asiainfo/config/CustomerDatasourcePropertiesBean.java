package com.asiainfo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "autoconfiguration")
public class CustomerDatasourcePropertiesBean {
	/**
	* @author 
	* @className TestCondition.java
	* @date 2017年7月3日 上午10:31:11
	* @description 
	*/
	private String packagePath="";
	private String aliasesPackage="";
	private String resourcesPath="";
	public String getResourcesPath() {
		return resourcesPath;
	}
	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getAliasesPackage() {
		return aliasesPackage;
	}
	public void setAliasesPackage(String aliasesPackage) {
		this.aliasesPackage = aliasesPackage;
	}
	
}
