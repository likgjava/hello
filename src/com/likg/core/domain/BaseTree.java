package com.likg.core.domain;

import java.util.Set;

/**
 * 基础树对象接口，系统中所有树结构的实体类都要实现该接口
 * @author likg
 */
public interface BaseTree<T> extends BaseObject {
	
	/**
	 * 获取父节点
	 * @return
	 */
	T getParent();

	/**
	 * 设置父节点对象
	 * @param parent 父节点对象
	 */
	void setParent(T parent);
	
	/**
	 * 获取树节点的层级
	 * @return
	 */
	Short getTreeLevel();

	/**
	 * 设置树节点的层级
	 * @param treeLevel
	 */
	void setTreeLevel(Short treeLevel);

	/**
	 * 判断该节点是否为叶子节点
	 * @return
	 */
	Boolean getIsLeaf();

	/**
	 * 设置该节点是否为叶子节点
	 * @param isLeaf
	 */
	void setIsLeaf(Boolean isLeaf);
	
	/**
	 * 获取所有儿子节点
	 * @return
	 */
	Set<T> getChildren();

	/**
	 * 设置所有儿子节点数据
	 * @param children
	 */
	void setChildren(Set<T> children);

}
