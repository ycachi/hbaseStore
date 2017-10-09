package com.asiainfo.base.entity.system;


/**
 * enum通用类，主要针对数据库里的状态(0,1..)
 * @author wxlizhi
 *
 */
public enum CommonType {
    
    /**
     * 是否可用
     */
    ENABLE("0", "启用"),
    DISABLE("1", "禁用"),
    
    ENSHOW("1","显示"),
    DISSHOW("0","不显示");
    
    
    private String value;
    private String showName;
    private CommonType(String value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    public String getShowName() {
        return showName;
    }
    
	public String getValue()
	{
		return value;
	}
	public String getStringValue() {
        return String.valueOf(value);
    }
    
    public static CommonType[] getIsShowType() {
        CommonType[] statusTypeArr = new CommonType[2];
        statusTypeArr[0] = CommonType.ENSHOW;
        statusTypeArr[1] = CommonType.DISSHOW;
        return statusTypeArr;
    }
    
    public static CommonType[] getDelFlagType() {
        CommonType[] statusTypeArr = new CommonType[2];
        statusTypeArr[0] = CommonType.ENABLE;
        statusTypeArr[1] = CommonType.DISABLE;
        return statusTypeArr;
    }

}
