package com.yc.demo.action;


import com.yc.demo.service.IModifyService;
import com.yc.demo.service.IQueryService;
import com.yc.framework.annotation.YCAutowired;
import com.yc.framework.annotation.YCController;
import com.yc.framework.annotation.YCRequestMapping;
import com.yc.framework.annotation.YCRequestParam;
import com.yc.framework.webmvc.servlet.YCModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 * @author Tom
 *
 */
@YCController
@YCRequestMapping("/web")
public class MyAction {

	@YCAutowired
	IQueryService queryService;
	@YCAutowired
	IModifyService modifyService;

	@YCRequestMapping("/query.json")
	public YCModelAndView query(HttpServletRequest request, HttpServletResponse response,
								@YCRequestParam("name") String name){
		String result = queryService.query(name);
		return out(response,result);
	}
	
	@YCRequestMapping("/add*.json")
	public YCModelAndView add(HttpServletRequest request,HttpServletResponse response,
			   @YCRequestParam("name") String name,@YCRequestParam("addr") String addr){
		String result = null;
		try {
			result = modifyService.add(name,addr);
			return out(response,result);
		} catch (Exception e) {
//			e.printStackTrace();
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("detail",e.getCause().getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
			model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
			return new YCModelAndView("500",model);
		}

	}
	
	@YCRequestMapping("/remove.json")
	public YCModelAndView remove(HttpServletRequest request,HttpServletResponse response,
		   @YCRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		return out(response,result);
	}
	
	@YCRequestMapping("/edit.json")
	public YCModelAndView edit(HttpServletRequest request,HttpServletResponse response,
			@YCRequestParam("id") Integer id,
			@YCRequestParam("name") String name){
		String result = modifyService.edit(id,name);
		return out(response,result);
	}
	
	
	
	private YCModelAndView out(HttpServletResponse resp,String str){
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
