package com.asiainfo.base.entity.system;


import java.io.Serializable;

/**
 * 
* @ClassName: BasePair 
* @Description: TODO
* @date 2017年2月8日 下午2:40:27 
* @param <T>
* @param <K>
 */
public class BasePair<T,K> implements Serializable{
	private static final long serialVersionUID = 2634663247618202042L;
	private T value1;
	private K value2;
	/**
	 * 构造方法
	 */
	public BasePair() {
	}
	/**
	 * 构造方法
	 * @param value1
	 * @param value2
	 */
	public BasePair(T value1, K value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	/**
	 * <p>获取 value1
	 * @return the value1
	 */
	public T getValue1() {
		return value1;
	}
	/**
	 * <p>设置 value1
	 * @param value1 the value1 to set
	 */
	public void setValue1(T value1) {
		this.value1 = value1;
	}
	/**
	 * <p>获取 value2
	 * @return the value2
	 */
	public K getValue2() {
		return value2;
	}
	/**
	 * <p>设置 value2
	 * @param value2 the value2 to set
	 */
	public void setValue2(K value2) {
		this.value2 = value2;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BasePair)) return false;
		Object value1=((BasePair<?,?>)obj).value1;
		Object value2=((BasePair<?,?>)obj).value2;
		if((value1==this.value1||value1!=null&&value1.equals(this.value1))
		   &&(value2==this.value2||value2!=null&&value2.equals(this.value2))){
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		int hash=17;
		hash=hash*37+(value1==null?0:value1.hashCode());
		hash=hash*37+(value2==null?0:value2.hashCode());
		return hash;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BasePair [value1=" + value1 + ", value2=" + value2 + "]";
	}
}
