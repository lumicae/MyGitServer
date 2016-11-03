package db.bean.mainBaseBean;

import java.util.UUID;

public abstract class AutoInsertDataToBean implements MainBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8150804210137885686L;

	/*
	 * 利用构造器初始化实现类，并为每个领域对象自动加载UUID
	 * */
	public AutoInsertDataToBean(){
		setId(UUID.randomUUID().toString());
	}
}
