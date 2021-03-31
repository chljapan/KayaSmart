package com.smartkaya.api.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.smartkaya.bean.GridColumn;
/**
 * MapUtils Class
 * @author LiangChen　2018/4/27
 * @version 1.0.0
 */
public class MapUtils {

	public static class MapComparatorDesc implements Comparator<Map<String, Object>> {
		@Override
		public int compare(Map<String, Object> m1, Map<String, Object> m2) {
			Integer v1 = Integer.valueOf(m1.get("index").toString());
			Integer v2 = Integer.valueOf(m2.get("index").toString());
			if (v2 != null) {
				return v2.compareTo(v1);
			}
			return 0;
		}

		public static void compare(List<Map<String, Object>> columnsList, MapComparatorAsc mapComparatorAsc) {
			// TODO Auto-generated method stub

		}

	}
	public static class MapComparatorAsc implements Comparator<Map<String, Object>> {
		@Override
		public int compare(Map<String, Object> m1, Map<String, Object> m2) {
			Integer v1 = Integer.valueOf(m1.get("index").toString());
			Integer v2 = Integer.valueOf(m2.get("index").toString());
			if(v1 != null){
				return v1.compareTo(v2);
			}
			return 0;
		}

	}

	public static class ListComparatorDesc implements Comparator<GridColumn> {
		@Override
		public int compare(GridColumn co1,GridColumn co2) {
			Integer v1 = Integer.valueOf(co1.getIndex());
			Integer v2 = Integer.valueOf(co2.getIndex());
			if(v1 != null){
				return v1.compareTo(v2);
			}
			return 0;
		}

		public static void compare(List<GridColumn> columnsList, ListComparatorAsc listComparatorAsc) {
			// TODO Auto-generated method stub

		}
	}
	public static class ListComparatorAsc implements Comparator<GridColumn> {
		@Override
		public int compare(GridColumn co1,GridColumn co2) {
			Integer v1 = Integer.valueOf(co1.getIndex());
			Integer v2 = Integer.valueOf(co2.getIndex());
			if(v1 != null){
				return v1.compareTo(v2);
			}
			return 0;
		}
	}

	public static class IndexComparator implements Comparator<GridColumn> {
		@Override
		public int compare(GridColumn co1,GridColumn co2) {
			Integer v1 = Integer.valueOf(co1.getIndex());
			Integer v2 = Integer.valueOf(co2.getIndex());
			if(v1 != null){
				return v1.compareTo(v2);
			}
			return 0;
		}
	}
}
