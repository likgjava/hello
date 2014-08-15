package com.likg.core.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;

/**
 * 对集合进行操作的工具类，对common-lang中ArrayUtils和spring中CollectionUtils类的补充
 *
 * @author <a href="mailto:xiaojf@cntmi.com">xiaojf</a>
 */
@SuppressWarnings("unchecked")
public class CollectionUtils
{

    public CollectionUtils()
    {
    }

    /**
     * 给领域对象集合按指定属性排序
	 * @param objectsList 领域对象集合
	 * @param fieldName 用作排序的属性
	 * @param asc 是否按升序排列
	 */
	public static void sort(List objectsList, String fieldName,boolean asc) {
//		AlgorithmUtils utils = new AlgorithmUtils(); 
//		YISComparator yisComparator = utils.new YISComparator(fieldName,asc);
//		Collections.sort(objectsList, yisComparator);
	}
    /**
     * 查找字符串在数组中的位置
     *
     * @param array[] 数组
     * @param value 要查询的字符串
     * @return 值在数组中的位置，如果没查找或传入值为空返回-1
     */
    public static int find(String array[], String value)
    {
        if(array == null || value == null)
            return -1;
        for(int i = 0; i < array.length; i++)
            if(value.equals(array[i]))
                return i;

        return -1;
    }
    
    /**
     * 用指定字符串填充数组
     *
     * @param str 用来填充数组的字符串
     * @param length 生成数组的维度
     * @return 填充好的数组
     */
    public static String[] fillArray(String str, int length)
    {
        String result[] = new String[length];
        Arrays.fill(result, str);
        return result;
    }

    public static String[] toStringArray(Collection coll)
    {
        Object objects[] = coll.toArray();
        int length = objects.length;
        String result[] = new String[length];
        for(int i = 0; i < length; i++)
            result[i] = objects[i].toString();

        return result;
    }

    public static int[] toIntArray(Collection coll)
    {
        Iterator iter = coll.iterator();
        int arr[] = new int[coll.size()];
        int i = 0;
        while(iter.hasNext()) 
            arr[i++] = ((Integer)iter.next()).intValue();
        return arr;
    }

    public static Object[] typecast(Object array[], Object to[])
    {
        return Arrays.asList(array).toArray(to);
    }

    public static String[] slice(String strings[], int begin, int length)
    {
        String result[] = new String[length];
        for(int i = 0; i < length; i++)
            result[i] = strings[begin + i];

        return result;
    }

    public static Object[] slice(Object objects[], int begin, int length)
    {
        Object result[] = new Object[length];
        for(int i = 0; i < length; i++)
            result[i] = objects[begin + i];

        return result;
    }

    public static String[] join(String x[], String y[])
    {
        String result[] = new String[x.length + y.length];
        for(int i = 0; i < x.length; i++)
            result[i] = x[i];

        for(int i = 0; i < y.length; i++)
            result[i + x.length] = y[i];

        return result;
    }

    public static boolean isAllNegative(int array[])
    {
        for(int i = 0; i < array.length; i++)
            if(array[i] >= 0)
                return false;

        return true;
    }

    public static void addAll(Collection collection, Object array[])
    {
        for(int i = 0; i < array.length; i++)
            collection.add(array[i]);

    }

    public static String[] cloneSubarray(String a[], int from, int to)
    {
        int n = to - from;
        String result[] = new String[n];
        System.arraycopy(a, from, result, 0, n);
        return result;
    }

    public static void print(String a[][])
    {
        if(a != null)
        {
            StringBuffer result = new StringBuffer("Array printer:");
            for(int i = 0; i < a.length; i++)
            {
                result.append("\n").append(i).append(":");
                for(int j = 0; j < a[i].length; j++)
                    result.append(" [").append(a[i][j]).append("]");

            }

            //System.out.println(result.toString());
        } else
        {
            //System.out.println("Array printer: null");
        }
    }

    
    /** 
     * Description :  集合交叉
     * Create Date: 2010-8-6上午11:08:54 by liangxj  Modified Date: 2010-8-6上午11:08:54 by liangxj
     * @param  
     * @return  
     * @Exception   
     */
    public static List<ListOrderedMap> crossList(List<ListOrderedMap> sourceList, List<ListOrderedMap> colList, int crossColumn)
    {
        List<ListOrderedMap> newData = new LinkedList<ListOrderedMap>();
        ListOrderedMap rowData = null;
        for(int i = 0; i < sourceList.size(); i++)
        {
            ListOrderedMap obj = sourceList.get(i);
            if(rowData == null)
                rowData = new ListOrderedMap();
            else
            if(!objEquals(rowData, obj, crossColumn))
            {
                newData.add(rowData);
                rowData = new ListOrderedMap();
            }
            if(rowData.size() == 0)
            {
                for(int j = 0; j < crossColumn; j++)
                    rowData.put(obj.get(j), obj.getValue(j));

                for(int k = 0; k < colList.size(); k++)
                {
                    for(int j = crossColumn + 1; j < obj.size(); j++)
                        rowData.put((new StringBuilder(String.valueOf(((ListOrderedMap)colList.get(k)).getValue(0).toString()))).append(obj.get(j)).toString(), "");

                }

            }
            for(int k = 0; k < colList.size(); k++)
            {
                if(!((ListOrderedMap)colList.get(k)).getValue(0).toString().equals(obj.getValue(crossColumn).toString()))
                    continue;
                for(int j = crossColumn + 1; j < obj.size(); j++)
                    rowData.put((new StringBuilder(String.valueOf(((ListOrderedMap)colList.get(k)).getValue(0).toString()))).append(obj.get(j)).toString(), obj.getValue(j));

                break;
            }

        }

        if(rowData != null)
            newData.add(rowData);
        return newData;
    }
    
    private static boolean objEquals(ListOrderedMap obj1, ListOrderedMap obj2, int column)
    {
        String obj1Str = "";
        String obj2Str = "";
        for(int i = 0; i < column; i++)
        {
            obj1Str = (new StringBuilder(String.valueOf(obj1Str))).append(obj1.getValue(i)).toString();
            obj2Str = (new StringBuilder(String.valueOf(obj2Str))).append(obj2.getValue(i)).toString();
        }

        return obj1Str.equals(obj2Str);
    }
    
    public static List convertMapToList(Map map){
    	List list = new ArrayList();

        Object[] o = ((HashMap) map).keySet().toArray();

        Arrays.sort(o);

        for (int i = 0; i <= o.length-1; i++) {

            if (map.containsKey(String.valueOf(o[i]))) {

                list.add(map.get(String.valueOf(o[i])));

            } else {
                list.add(map.get(""));
            }
        }
        return list;
    }
    public static void main(String args[])
    {
        List resourceData = new ArrayList();
        ListOrderedMap col1 = new ListOrderedMap();
        col1.put("0", "4");
        col1.put("1", "基本参数");
        col1.put("2", "商品1");
        col1.put("3", "");
        resourceData.add(col1);
        ListOrderedMap col4 = new ListOrderedMap();
        col4.put("0", "1");
        col4.put("1", "cpu");
        col4.put("2", "商品1");
        col4.put("3", "2.1hz");
        resourceData.add(col4);
        ListOrderedMap col7 = new ListOrderedMap();
        col7.put("0", "1");
        col7.put("1", "内存");
        col7.put("2", "商品1");
        col7.put("3", "1G");
        resourceData.add(col7);
       
        ListOrderedMap col2 = new ListOrderedMap();
        col2.put("0", "4");
        col2.put("1", "基本参数");
        col2.put("2", "商品2");
        col2.put("3", "");
        resourceData.add(col2);
        ListOrderedMap col5 = new ListOrderedMap();
        col5.put("0", "1");
        col5.put("1", "cpu");
        col5.put("2", "商品2");
        col5.put("3", "2.0hz");
        resourceData.add(col5);
        ListOrderedMap col8 = new ListOrderedMap();
        col8.put("0", "1");
        col8.put("1", "内存");
        col8.put("2", "商品2");
        col8.put("3", "4G");
        resourceData.add(col8);
        
        ListOrderedMap col3 = new ListOrderedMap();
        col3.put("0", "4");
        col3.put("1", "基本参数");
        col3.put("2", "商品3");
        col3.put("3", "");
        resourceData.add(col3);
        ListOrderedMap col6 = new ListOrderedMap();
        col6.put("0", "1");
        col6.put("1", "cpu");
        col6.put("2", "商品3");
        col6.put("3", "1.8hz");
        resourceData.add(col6);
        ListOrderedMap col9 = new ListOrderedMap();
        col9.put("0", "1");
        col9.put("1", "内存");
        col9.put("2", "商品3");
        col9.put("3", "2G");
        resourceData.add(col9);
        
        List colData = new ArrayList();
        ListOrderedMap c1 = new ListOrderedMap();
        c1.put("0", "商品1");
        colData.add(c1);
        
        ListOrderedMap c2 = new ListOrderedMap();
        c2.put("1", "商品2");
        colData.add(c2);
        
        ListOrderedMap c3 = new ListOrderedMap();
        c3.put("2", "商品3");
        colData.add(c3);
        
        @SuppressWarnings("unused")
		List list = crossList(resourceData,colData,2);
    }
}