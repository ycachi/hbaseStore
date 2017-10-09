package com.asiainfo.base.entity.system;

/**
 * 
* @ClassName: Config 
* @Description: TODO
* @author yuanchi
* @date 2017年2月8日 上午10:47:56
 */
public class Config
{
	
	private String syscode;
	private String configname;
	private String configdesc;
	private String paramname;
	private int valueseq;
	private String paramvalue;
	private String valuedesc;
	private String available;
	private String updatetime;
	private String rsrv_str1;
	private Integer configid;//add by caobo
	

	

	public Integer getConfigid() {
		return configid;
	}

	public void setConfigid(Integer configid) {
		this.configid = configid;
	}

	public String getSyscode()
	{
		return syscode;
	}
	
	public void setSyscode(String syscode)
	{
		this.syscode = syscode;
	}
	
	public String getConfigname()
	{
		return configname;
	}
	
	public void setConfigname(String configname)
	{
		this.configname = configname;
	}
	
	public String getConfigdesc()
	{
		return configdesc;
	}
	
	public void setConfigdesc(String configdesc)
	{
		this.configdesc = configdesc;
	}
	
	public String getParamname()
	{
		return paramname;
	}
	
	public void setParamname(String paramname)
	{
		this.paramname = paramname;
	}
	
	public int getValueseq()
	{
		return valueseq;
	}
	
	public void setValueseq(int valueseq)
	{
		this.valueseq = valueseq;
	}
	
	public String getParamvalue()
	{
		return paramvalue;
	}
	
	public void setParamvalue(String paramvalue)
	{
		this.paramvalue = paramvalue;
	}
	
	public String getValuedesc()
	{
		return valuedesc;
	}
	
	public void setValuedesc(String valuedesc)
	{
		this.valuedesc = valuedesc;
	}
	
	public String getAvailable()
	{
		return available;
	}
	
	public void setAvailable(String available)
	{
		this.available = available;
	}
	
	public String getUpdatetime()
	{
		return updatetime;
	}
	
	public void setUpdatetime(String updatetime)
	{
		this.updatetime = updatetime;
	}
	
	public String getRsrv_str1()
	{
		return rsrv_str1;
	}
	
	public void setRsrv_str1(String rsrv_str1)
	{
		this.rsrv_str1 = rsrv_str1;
	}
	
	
	
}
