package com.kyon.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kyon.dao.OrderDao;
import com.kyon.daoImpl.OrderDaoImpl;

@WebServlet("/user-remove-order")
public class UserRemoveOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	OrderDao od = new OrderDaoImpl();
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// ������������ʽ
		req.setCharacterEncoding("utf-8");
		// ������Ӧ�����ʽ����Ӧͷ
    	resp.setCharacterEncoding("utf-8");
    	resp.setContentType("text/html;charset=utf-8");
//    	resp.setHeader("Access-Control-Allow-Origin", "*");
    	
    	// ��ȡ������Ϣ
    	String oId = "";
    	if(req.getParameter("oId")!=null) {
    		oId = req.getParameter("oId");
    	}
    	
    	
    	// ����������Ϣ
    	int success = 0;
    	try {
    		success = od.userRemoveOrder(oId);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// ��Ӧ�������
    	String str="{\"success\":"+success+"}";
    	resp.getWriter().write(str);
	}

}