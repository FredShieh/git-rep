package com.scnu.lab.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Hibernate 实现的基类，实现了基本的 CRUD 和查询功能。
 * <p>
 * $Created Date: 2006-2-14 22:24:17, Updated Date: 2006-2-14 $
 * </p>
 */
@SuppressWarnings("all")
public abstract class BaseDAOHibernate4 {

	@Autowired
	private SessionFactory sessionFactory;

	protected abstract Class<?> getModelClass();

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void save(Object obj) {
		getSession().save(obj);
	}

	protected <T> T get(Class<T> clazz, String id) {
		return (T) getSession().get(clazz, id);
	}

	protected <T> T get(Class<T> clazz, int id) {
		return (T) getSession().get(clazz, id);
	}

	protected <T> List<T> findByProperty(Class<T> clazz, String property, Object value) {
		return getSession().createCriteria(clazz).add(Restrictions.eq(property, value)).list();
	}

	protected <T> List<T> findAll(Class<T> clazz) {
		return getSession().createCriteria(clazz).list();
	}

	/**
	 * 根据DetachedCriteria加载分页，指定页大小和起始位置
	 * 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	protected List<?> findPageByCriteria(final DetachedCriteria detachedCriteria, final int startRow, final int maxRow) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setFirstResult(startRow).setMaxResults(maxRow);

		return criteria.list();
	}

	protected void update(Object obj) {
		getSession().update(obj);
	}

	protected void saveOrUpdate(Object obj) {
		getSession().saveOrUpdate(obj);
	}

	/**
	 * 使用sql语句执行修改操作
	 * 
	 * @param sql : update sql 语句
	 * @param values : sql语句参数值
	 */
	protected void updateBySql(final String sql, final List<?> values) {
		Query query = getSession().createSQLQuery(sql);
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		query.executeUpdate();
	}

	/**
	 * 使用hql 语句进行操作 返回集合内容的第一个对象
	 * 
	 * @param hql : hql查询语句
	 * @param values : hql语句参数值
	 * @return Object
	 */
	protected Object getFirstObject(final String hql, final List<?> values) {
		if (values != null && values.size() > 0) {
			List<?> list = findByHQL(hql, values);
			if (list.size() == 0) {
				return null;
			}
			return list.get(0);
		}

		List<?> list = findListForPage(hql, values, 0, 1);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 执行HQL查询
	 * 
	 * @param hql 查询语句
	 * @return
	 */
	protected <T> List<T> findByHQL(String hql) {
		return findByHQL(hql, null);
	}

	/**
	 * 执行HQL查询
	 * 
	 * @param hql 语句
	 * @param values HQL语句参数值
	 * @return 结果集
	 */
	protected <T> List<T> findByHQL(final String hql, final List<?> values) {
		Query queryObject = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				queryObject.setParameter(i, values.get(i));
			}
		}
		return queryObject.list();
	}

	/**
	 * 根据表的ID获取一列数据，然后获取该列数据的某个属性的值（永远只会返回单个值）
	 * 
	 * @param id 记录ID
	 * @param returnProperty 属性名称
	 * @return 属性值
	 */
	public Object findSingePropertyById(final Integer id, final String returnProperty) {
		Criteria criteria = getSession().createCriteria(getModelClass());
		criteria.add(Restrictions.eq("id", id));
		criteria.setProjection(Projections.property(returnProperty));

		return criteria.uniqueResult();
	}

	/**
	 * 使用sql 语句进行操作
	 * 
	 * @param sql : sql查询语句
	 * @param values : 查询参数值 集合
	 * @return List
	 */
	protected <T> List<T> findListBySql(final String sql, final List<?> values) {
		return findListBySqlForPage(sql, values, null, null);
	}

	/**
	 * 使用sql 语句进行分页查询操作
	 * 
	 * @param sql : sql查询语句
	 * @param values : 查询参数值 集合
	 * @return List
	 */
	protected <T> List<T> findListBySqlForPage(final String sql, final List<?> values, final Integer firstRow, final Integer fetchSize) {
		Query query = getSession().createSQLQuery(sql);
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}

		if (firstRow != null) {
			query.setFirstResult(firstRow);
		}
		if (fetchSize != null) {
			query.setMaxResults(fetchSize);
		}

		return query.list();
	}

	protected void deleteById(Class<?> clazz, int id) {
		getSession().delete(get(clazz, id));
	}

	protected void delete(Object obj) {
		getSession().delete(obj);
	}

	/**
	 * 使用hql delete 语句进行操作
	 * 
	 * @param hql : hql删除语句
	 * @param values : hql语句参数
	 */
	protected void delete(final String hql, final List<?> values) {
		Query query = getSession().createQuery(hql);
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		query.executeUpdate();
	}

	/**
	 * 根据DetachedCriteria统计总数
	 * 
	 * @param detachedCriteria
	 * @return 总数
	 */
	public int countByCriteria(final DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		Integer count = (Integer) criteria.setProjection(Projections.count("id")).uniqueResult();
		if (count != null && count.intValue() > 0) {
			return count.intValue();
		}

		return 0;
	}

	/**
	 * 使用sql 语句进行统计操作
	 * 
	 * @param sql : 统计总数的sql语句
	 * @param values : sql语句参数值集合
	 * @return List
	 */
	protected int countBySQL(final String sql, final List<?> values) {
		List<Object> result = findListBySql(sql, values);
		if (result.isEmpty()) {
			return 0;
		}
		Object total = result.get(0);
		if (total instanceof Integer) {
			return (Integer) total;
		}
		if (total instanceof Long) {
			return ((Long) total).intValue();
		}
		if (total instanceof BigDecimal) {
			return ((BigDecimal) total).intValue();
		}
		if (total instanceof BigInteger) {
			return ((BigInteger) total).intValue();
		}
		return 0;
	}

	/**
	 * 使用hql 语句进行操作
	 * 
	 * @param hql : hql查询语句
	 * @param values : 查询语参数值
	 * @return List
	 */
	protected int countByHQL(final String hql, final List<?> values) {
		Object total = getFirstObject(hql, values);
		if (null == total) {
			return 0;
		}
		final boolean isInteger = total instanceof Integer;
		final boolean isLong = total instanceof Long;
		final boolean isBigDecimal = total instanceof BigDecimal;
		if (isInteger) {
			return (Integer) total;
		}
		if (isLong) {
			return ((Long) total).intValue();
		}
		if (isBigDecimal) {
			return ((BigDecimal) total).intValue();
		}
		return 0;
	}

	/**
	 * 通过Domain对象值，统计总数
	 * 
	 * @param example domain对象
	 * @param isLike String字段是否执行like查询 true：like查询 false:=查询
	 * @return Integer
	 */
	protected Integer countPojoTotal(final Object example, final boolean isLike) {
		Criteria criteria = getSession().createCriteria(getModelClass());
		if (null != example && !isLike) {
			criteria.add(Example.create(example));
		} else {
			criteria.add(Example.create(example).enableLike(MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.count("id"));

		return ((Long) criteria.uniqueResult()).intValue();
	}

	/**
	 * HQL语句执行分页查询操作 <br>
	 * 
	 * @param hql HQL查询语句
	 * @param values 语句参数值
	 * @param firstRow 起始行
	 * @param fetchSize 数据集大小
	 * @return 结果集
	 */
	public <T> List<T> findListForPage(final String hql, final List<?> values, final Integer firstRow, final Integer fetchSize) {
		return this.findListForPage(hql, values, firstRow, fetchSize, null);
	}

	/**
	 * HQL语句执行分页查询操作 <br>
	 * 此方法提供将结果集封装成值对象：Class resultTransforme
	 * 
	 * @param hql HQL查询语句
	 * @param values 语句参数值
	 * @param firstRow 起始行
	 * @param fetchSize 数据集大小
	 * @param resultTransforme 结果集封装对象class <br>
	 *            如果 resultTransforme 为空，将返回HQL查询的domain数据对象
	 * 
	 * @return 结果集
	 */
	protected <T> List<T> findListForPage(final String hql, final List<?> values, final Integer firstRow, final Integer fetchSize, final Class<T> resultTransforme) {
		Query query = getSession().createQuery(hql);
		if (resultTransforme != null) {
			query.setResultTransformer(Transformers.aliasToBean(resultTransforme));
		}
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		if (firstRow != null) {
			query.setFirstResult(firstRow);
		}
		if (fetchSize != null) {
			query.setMaxResults(fetchSize);
		}

		return query.list();
	}

	/**
	 * 根据对象属性查询数据集<br>
	 * 查询规则：int型数据按 等于 条件查找，String类型按 like 条件查询<br>
	 * 
	 * @param example 查询对象
	 * @param orders 排序设定 如果此参数为空，默认按照id升序排序
	 * @param startRow 起始行号
	 * @param maxRow 最大结果集
	 * @return 查询数据集
	 */
	protected <T> List<T> serach(final Object example, final List<Order> orders, final Integer startRow, final Integer maxRow) {
		Criteria criteria = getSession().createCriteria(getModelClass());
		if (example != null) {
			criteria.add(Example.create(example).enableLike(MatchMode.ANYWHERE));
		}

		if (startRow != null && maxRow != null) {
			criteria.setFirstResult(startRow);
			criteria.setMaxResults(maxRow);
		}

		if (orders == null || orders.size() < 1) {
			criteria.addOrder(Order.asc("id"));
		} else {
			for (Order order : orders) {
				criteria.addOrder(order);
			}
		}

		return criteria.list();
	}

	/**
	 * 根据对象属性查询符合条件的记录总数<br>
	 * 查询规则：int型数据按 等于 条件查找，String类型按 like 条件查询
	 * 
	 * @param example 查询对象
	 * @return 记录数
	 */
	protected int countSearch(final Object example) {
		Criteria criteria = getSession().createCriteria(getModelClass());
		if (example != null) {
			criteria.add(Example.create(example).enableLike(MatchMode.ANYWHERE));
		}
		Long count = (Long) criteria.setProjection(Projections.count("id")).uniqueResult();
		if (count != null && count.intValue() > 0) {
			return count.intValue();
		}

		return 0;
	}

	/**
	 * 通过属性查找
	 * 
	 * @param clazz
	 * @param field
	 * @param value
	 * @param orders
	 * @return
	 * @throws DAOException
	 */
	protected <T> List<T> findByProperty(Class<T> clazz, String field, Object value, Order... orders) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(field, value);
		return this.filterMapAndOrders2Query(clazz, filter, orders);
	}

	/**
	 * 组合查询:使用Map<String,Object>进行查找时,这里将条件拼装成可执行的HQL语句,支持如下: <br>
	 * 0.普通条件 name = 'zhangsan' <br>
	 * 1.关联条件 * user.name = 'zhangsan' <br>
	 * 2.通过Order[]连接 order by <br>
	 * 3.limit 进行结果限定
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> filterMapAndOrders2Query(Class<T> clazz, Map<String, Object> filter, Order[] orders) {
		StringBuffer hql = new StringBuffer();
		hql.append("From ").append(clazz.getName());

		if (null != filter && !filter.isEmpty()) {
			Set<String> keys = filter.keySet();
			hql.append(" where 1=1 ");
			for (Iterator<String> localIterator = keys.iterator(); localIterator.hasNext();) {
				String key = localIterator.next();
				if (!"limit".equalsIgnoreCase(key)) {
					hql.append(" and ").append(key).append("=").append(":" + (key.indexOf(".") >= 0 ? key.replaceAll(".", "_") : key));
				}
			}
		}

		if (null != orders) {
			for (Order order : orders) {
				if (order != null) {
					hql.append(" order by ").append(String.valueOf(order));
				}
			}
		}

		Query query = getSession().createQuery(hql.toString());
		if (filter != null && !filter.isEmpty()) {
			Set<String> keys = filter.keySet();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				if ("limit".equalsIgnoreCase(key)) {
					query.setMaxResults(Integer.parseInt(String.valueOf(filter.get("limit"))));
				} else {
					query.setParameter((key.indexOf(".") >= 0 ? key.replaceAll(".", "_") : key), filter.get(key));
				}
			}
		}
		return query.list();
	}
}