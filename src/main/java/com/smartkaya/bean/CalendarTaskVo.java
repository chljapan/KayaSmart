package com.smartkaya.bean;

import java.util.Map;

/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class CalendarTaskVo {
    private String title;
    private String start;
    private String end;
    private String id;
    private Map<String, Object> extendedProps;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(Map<String, Object> extendedProps) {
        this.extendedProps = extendedProps;
    }
}
