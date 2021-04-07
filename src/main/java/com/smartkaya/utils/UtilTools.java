package com.smartkaya.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class UtilTools {

    private static long orderNum = 0l;
    private static String date;

    /**
     * 生成编号(线程安全全局唯一)
     * 
     * @return
     */
    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyMMddHHmmsss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;
        ;
        return orderNo + "";
    }
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmpty(Object obj) {
		boolean ret = false;
		if (isNull(obj)) {
			ret = true;
		}
		if (obj instanceof String) {
			if (((String) obj).isEmpty()) {
				ret = true;
			}
		}
		return ret;
	}
//    /**
//     * 配置文件转换成Map返回
//     * 
//     * @return Map<String, Object>
//     * @throws Exception
//     */
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    public static Map<String, Object> getConfig() {
//        // 先读取config目录的，没有再加载classpath的
//        String outpath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
//
//        InputStream input = null;
//        String fileName = null;
//        Map<String, Object> object = new HashMap<String, Object>();
//        File[] files = null;
//        try {
//            files = getFiles(outpath, Constant.CONFIG_FILE);
//            // 判断config下是否有文件
//            if (files.length == 0) {
//                // 没有再加载classpath的
//                files = getFiles(UtilTools.class.getClassLoader().getResource("").getPath(), Constant.CONFIG_FILE);
//            }
//            //文件不存在报错
//            if (files.length == 0) {
//                throw new FileNotFoundException("The config file not exists!!!");
//            }
//            // 读取第一件
//            fileName = files[0].getName().toLowerCase();
//            input = new FileInputStream(files[0]);
//
//            if (fileName.contains(Constant.CONFIG_YML) || fileName.contains(Constant.CONFIG_YAML)) {
//                Yaml yaml = new Yaml();
//                Map<String, Object> load = (Map<String, Object>) yaml.load(input);
//                object = load;
//                object.put(Constant.CONFIG_TYPE, ConfigEnum.YAML.getCode());
//            } else {
//                Properties properties = new Properties();
//                properties.load(input);
//                object = new HashMap<String, Object>((Map) properties);
//                object.put(Constant.CONFIG_TYPE, ConfigEnum.PROPERTIES.getCode());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (input != null) {
//                    input.close();
//                }
//            } catch (IOException e) {
//                java.util.logging.Logger.getLogger(UtilTools.class.getName()).log(java.util.logging.Level.SEVERE, null,
//                        e);
//            }
//        }
//        return object;
//    }

    /**
     * 
     * 获取文件
     * 
     * 可以根据正则表达式查找
     * 
     * @param dir
     *            String 文件夹名称
     * 
     * @param s
     *            String 查找文件名，可带*.?进行模糊查询
     * 
     * @return File[] 找到的文件
     * 
     */
    public static File[] getFiles(String dir, String s) {
        // 开始的文件夹
        File file = new File(dir);
        s = s.replace('.', '#');
        s = s.replaceAll("#", "\\\\.");
        s = s.replace('*', '#');
        s = s.replaceAll("#", ".*");
        s = s.replace('?', '#');
        s = s.replaceAll("#", ".?");
        s = "^" + s + "$";
        Pattern p = Pattern.compile(s);
        ArrayList<?> list = filePattern(file, p);
        File[] rtn = new File[list.size()];
        list.toArray(rtn);
        return rtn;

    }

    /**
     * 
     * @param file
     *            File 起始文件夹
     * 
     * @param p
     *            Pattern 匹配类型
     * 
     * @return ArrayList 其文件夹下的文件夹
     * 
     */
    private static ArrayList<File> filePattern(File file, Pattern p) {
        if (file == null) {
            return null;
        } else if (file.isFile()) {
            Matcher fMatcher = p.matcher(file.getName());
            if (fMatcher.matches()) {
                ArrayList<File> list = new ArrayList<File>();
                list.add(file);
                return list;
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                ArrayList<File> list = new ArrayList<File>();
                for (int i = 0; i < files.length; i++) {
                    ArrayList<File> rlist = filePattern(files[i], p);
                    if (rlist != null) {
                        list.addAll(rlist);
                    }
                }
                return list;
            }
        }
        return null;

    }

    /**

     * Map转String工具

     * @param map

     * @param separator 分隔符

     * @param kvSplice  键值拼接符

     * @return

     */
    public static String mapToString(Map<?, ?> map, String separator, String kvSplice) {
        List<String> result = new ArrayList<>();
        map.entrySet().parallelStream().reduce(result, (first, second)->{
            first.add(second.getKey() + kvSplice + second.getValue());
            return first;
        }, (first, second)->{
            if (first == second) {
                return first;
            }
            first.addAll(second);
            return first;
        });
        return StringUtils.join(result, separator);

    }
}
