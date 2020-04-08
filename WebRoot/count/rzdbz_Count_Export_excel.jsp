<%@ page language="java" pageEncoding="UTF-8"%>  
<%@ page import="java.io.*"%>  
<%@ page import="jxl.write.*"%> 
<%@ page import="jxl.write.WritableWorkbook,jxl.Workbook,jxl.write.WritableSheet,
				jxl.write.Label,com.suntendy.queue.util.ExcelUtil,java.util.*,com.suntendy.queue.count.domain.*,com.suntendy.queue.queue.domain.*"%>
<%@page import="com.suntendy.queue.queue.domain.Number"%>

<%  
	List<Number> list = (List<Number>)request.getSession().getAttribute("list");
    response.reset();  
    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
    response.setHeader("Content-Disposition", "attachment;filename=" + new String("人证对比值.xls".getBytes("gb2312"), "iso8859-1"));  
  
    OutputStream os = response.getOutputStream();  
  
    WritableWorkbook wwb = null;  
    wwb = Workbook.createWorkbook(os);//将 WritableWorkbook 写入到输出流  
    WritableSheet ws = wwb.createSheet("人证对比查询", 0);//创建第一个sheet  
   // ws.mergeCells(0, 0, 3, 0);//合并单元格  
  	ws.addCell(new Label(0, 1, "窗口号"));
	ws.addCell(new Label(1, 1, "顺序号"));  
	ws.addCell(new Label(2, 1, "身份证号码"));
	ws.addCell(new Label(3, 1, "人证对比值"));
	ws.addCell(new Label(4, 1, "姓名"));
	ws.addCell(new Label(5, 1, "ddd"));
	ws.addCell(new Label(6, 1, "sss"));
    for(int i=0;i<list.size();i++) {   
	    ws.addCell(new Label(0, i+3, list.get(i).getBarId()));
	    ws.addCell(new Label(1, i+3, list.get(i).getClientno()));
	    ws.addCell(new Label(2, i+3, list.get(i).getIDNumber()));
	    ws.addCell(new Label(3, i+3, list.get(i).getRzdbz()));
	    ws.addCell(new Label(4, i+3, list.get(i).getNameA()));
	    ws.addCell(new Label(5, i+3, list.get(i).getCarNumber()));
	    ws.addCell(new Label(6, i+3, list.get(i).getCarType()));
	  }
  
    os.flush();  
    wwb.write();  
    wwb.close();  
    os.close();
    out.clear();
	out = pageContext.pushBody();  
%>  