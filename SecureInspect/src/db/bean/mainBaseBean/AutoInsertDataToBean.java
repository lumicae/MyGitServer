package db.bean.mainBaseBean;

import java.util.UUID;

public abstract class AutoInsertDataToBean implements MainBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8150804210137885686L;

	/*
	 * ���ù�������ʼ��ʵ���࣬��Ϊÿ����������Զ�����UUID
	 * */
	public AutoInsertDataToBean(){
		setId(UUID.randomUUID().toString());
	}
}
