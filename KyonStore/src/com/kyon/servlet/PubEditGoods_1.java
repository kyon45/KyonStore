package com.kyon.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.kyon.dao.PubDao;
import com.kyon.daoImpl.PubDaoImpl;

@WebServlet("/pub-edit-goods-1")
public class PubEditGoods_1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PubDao pd = new PubDaoImpl();
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置请求和响应编码格式
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		
		// 请求信息变量
    	String gId = "";
    	String gName = "";
    	String gInfo = "";
    	int gType = 0;
    	double gPrice = 0.0;
    	String gImg = "";
    	
		String webPath="";
		String webRoot=this.getServletContext().getRealPath("");

		//1.获取到上传的fileItem
			//创建DiskFileItemFactory工厂对象
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    		//设置文件的缓存目录，如果不存在则新创建一个
    	String tempPath=webRoot+File.separator+"tempFolder";   	
    	//System.out.println(tempPath);
    	File f=new File(tempPath);
    	if(!f.exists()) {
    		f.mkdirs();
    	}
    		//设置文件缓存路径
    	factory.setRepository(f);
    		//创建ServletFileUpload对象
    	ServletFileUpload fileUpload = new ServletFileUpload(factory);
    		//设置字符编码
    	fileUpload.setHeaderEncoding("utf-8");
    		//解析request，得到上传文件的FileItem对象
		try {
			List<FileItem> fileItems= fileUpload.parseRequest(req);
			
		//2.区分开文件还是普通字段
			//遍历集合，获取参数字段
			for(int i=0;i<fileItems.size();i++) {
				if(fileItems.get(i).isFormField()) {
					//依次获取请求数据gId、gName、gInfo、gType、gPrice
					String str = fileItems.get(i).getString();
					String fieldName = fileItems.get(i).getFieldName();
					//这里出现乱码iso-8859-1 与utf-8
					str=new String(str.getBytes("iso8859-1"),"utf-8");
					//打印参数
					//System.out.println("FormField["+fieldName+": "+str+"]");
					if("gId".equals(fieldName)) {
						gId = str;
					} else if ("gName".equals(fieldName)) {
						gName = str;
					} else if ("gInfo".equals(fieldName)) {
						gInfo = str;
					} else if ("gType".equals(fieldName)) {
						if(!"".equals(str))
							gType = Integer.parseInt(str);
					} else if ("gPrice".equals(fieldName)) {
						if(!"".equals(str))
							gPrice = Double.parseDouble(str);
					}
					//uNo=s;
				}
			}
			//遍历集合，获取文件
			for(int i=0;i<fileItems.size();i++) {
				if(fileItems.get(i).isFormField()) {//普通字段FileItem					
					// pass
				}
				else {//文件FileItem
					System.out.println("file: "+fileItems.get(i).getName());					
					
					//处理获取的上传的文件名后缀
						String afterDot=fileItems.get(i).getName();
					//截取文件名后缀
						afterDot=afterDot.substring(afterDot.lastIndexOf("."));
					//重新命名
						gImg = afterDot;  // 文件后缀格式
						String fileName=gId+afterDot;
						webPath=webRoot+File.separator
								+"img"+File.separator
								+"goods"+File.separator;
						String filePath=webPath+fileName;
					//创建文件
						File file=new File(filePath);
					//若没有这句话表示直接创建filePath文件夹，而不是文件	
						file.getParentFile().mkdirs();
						file.createNewFile();
					//获取上传的文件流
						InputStream in = fileItems.get(i).getInputStream();
					//打开创建好的文件
						FileOutputStream out =new FileOutputStream(file);
					//流对拷
						byte[] buffer= new byte[1024];//每次读取1KB
						int len;
					//开始读取
						while((len=in.read(buffer))>0)
							out.write(buffer, 0,len);
					//关闭流
						in.close();
						out.close();
					//删除临时文件
						fileItems.get(i).delete();	
						System.out.println("商品图片上传成功");
					
					
				}
				//综合所获取的字段
	
			}
			
		}catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.out.println("pub_edit_goods["+gId+","+gName+","+gInfo+","+gType+","+gPrice+","+gImg+"]");
		
    	// 处理请求信息
    	int success = 0;
    	try {
    		int flag = pd.editGoods(gId, gName, gInfo, gType, gPrice, gImg);
    		System.out.println(flag);
    		if(flag==0) success = 1;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// 响应处理结果
    	String str="{\"success\":"+success+"}";
    	resp.getWriter().write(str);
    	
	}

}
