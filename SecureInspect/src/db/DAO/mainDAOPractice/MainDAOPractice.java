package db.DAO.mainDAOPractice;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import utils.PageView;

public interface MainDAOPractice<T>{
	
	/*����ʵ�屣��*/
	public int save(T entity);
	public int[] batchSave(Collection<T> entityCollection);
	public boolean cascadedSave(T entity);
	public boolean cascadedBatchSave(Collection<T> entityCollection);
	
	/*����ʵ��ɾ��*/
	public int delete(T entity);
	public int deleteById(Serializable id);
	public int[] batchDelete(Collection<T> entityCollection);
	public boolean cascadedDeleteById(Serializable id);
	public boolean cascadedBatchDelete(Collection<T> entityCollection);
	
	/*����ʵ�����*/
	public int update(T entity);
	public int[] batchUpdate(Collection<T> entityCollection);
	public boolean cascadedUpdate(T entity);
	public boolean cascadedBatchUpdate(Collection<T> entityCollection);
	public boolean cascadedSetActiveById(Serializable id, boolean active);
	
	/*����ʵ���ѯ*/
	public T findActivatedById(Serializable id);
	public T findInactivatedById(Serializable id);
	public T findBothById(Serializable id);
	public List<T> findAllActivated(Class<T> entityClass, Serializable order);
	public List<T> findAllInactivated(Class<T> entityClass, Serializable order);
	public List<T> findAll(Class<T> entityClass, Serializable order);
	public List<?> findByQuery(String sql, Class<?> entityClass);
	public List<?> findByQueryForList(String sql, Class<?> elementType);
	public T cascadedQueryById(Serializable id, boolean active, short rank, Serializable order);
	public T cascadedQueryBothById(Serializable id, short rank, Serializable order);
	public long getTotalItemsNumBySelectQuery(String sql);
	public boolean pagingFindBySelectQueryAndSortByRowidForSingleTable(String sql, Class<?> entityClass, PageView<T> pageView, Serializable colToSort, Serializable order);	
	public boolean pagingFindBySelectQueryAndSortByRownumForSingleTable(String sql, Class<?> entityClass, PageView<T> pageView);
	
	/*�Զ���SQL����*/
	public void executeBySql(String sql);
}
