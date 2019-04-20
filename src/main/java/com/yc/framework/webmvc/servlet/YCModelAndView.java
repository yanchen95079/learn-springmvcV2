package com.yc.framework.webmvc.servlet;

import java.util.Map;

/**
 * Created by Tom on 2019/4/13.
 */
public class YCModelAndView {

    private String viewName;
    private Map<String,?> model;

    public YCModelAndView(String viewName) { this.viewName = viewName; }

    public YCModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

//    public void setViewName(String viewName) {
//        this.viewName = viewName;
//    }

    public Map<String, ?> getModel() {
        return model;
    }

//    public void setModel(Map<String, ?> model) {
//        this.model = model;
//    }
}
