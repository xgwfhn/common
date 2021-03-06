package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) 
@SpringBootTest
public class FileTest {
	
	@Test
	public  void getFileByFileCode()  {
			String surl="http://10.12.64.23:18063/rest/dispatch/file/fileDownLoad";
			String str=" {\r\n" + 
					"\"requestInfo\": {\r\n" + 
					"\"appId\": \"0t580p8jzv10bma4\",\r\n" + 
					"\"encryptData\": \"6ka6garpfci06ve9\",\r\n" + 
					"\"interfaceAction\": \"0\",\r\n" + 
					"\"interfaceMark\": \"fileDownLoad\",\r\n" + 
					"\"version\": \"\"\r\n" + 
					"  },\r\n" + 
					"\"searchInfo\": {\r\n" + 
					"\"fileCode\":\"695794563245641728\"\r\n" + 
					"  }\r\n" + 
					"}";

		try {
			URL url = new URL(surl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("POST");// 提交模式
		    //是否允许输入输出
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    //设置请求头里面的数据，以下设置用于解决http请求code415的问题
		    conn.setRequestProperty("Content-Type",
		            "application/json");
		    //链接地址
		    conn.connect();
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		    //发送参数
		    writer.write(str);
		    //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
		    writer.flush();
		    InputStream instream= conn.getInputStream();
		    File imageFile = new File("D:\\code\\aa11.pdf");
		    byte[] data=null;
			try {
				data = readInputStream(instream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //创建输出流 
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据 
            outStream.write(data);
            //关闭输出流 
            outStream.close();
		    writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串 
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕 
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来 
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度 
            outStream.write(buffer, 0, len);
        }
        //关闭输入流 
        inStream.close();
        //把outStream里的数据写入内存 
        return outStream.toByteArray();
	 }

}
