package com.xgw.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
 
public class Demo {
	//定义全局变量address
	static String address = "http://10.12.64.23:18062/rest/dispatch/file/fileDownLoad";
	
	public static void main(String[] args) {
		//开启三个线程下载
		System.out.println("多线程下载开始");
		downloadFile(3);
		System.out.println("多线程下载");
	}
	
	/**
	 * 功能：计算每个线程下载的起始位置,并执行下载。
	 * @param 线程数
	 */
	public static void downloadFile(int threadCount){
		OutputStreamWriter writer=null;
		HttpURLConnection conn=null;
		try {
			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// 
			    conn.setDoInput(true);
			    conn.setDoOutput(true);
			    conn.setConnectTimeout(20*1000);
			    conn.setRequestProperty("Content-Type",
			            "application/json");
			    String str=" {\r\n" + 
						"\"requestInfo\": {\r\n" + 
						"\"appId\": \"0t580p8jzv10bma4\",\r\n" + 
						"\"encryptData\": \"6ka6garpfci06ve9\",\r\n" + 
						"\"interfaceAction\": \"0\",\r\n" + 
						"\"interfaceMark\": \"fileDownLoad\",\r\n" + 
						"\"version\": \"\"\r\n" + 
						"  },\r\n" + 
						"\"searchInfo\": {\r\n" + 
						"\"fileCode\":\"702239589362028544\"\r\n" + 
						//"\"fileCode\":\""+fileInfo[0]+"\"\r\n" + 
						"  }\r\n" + 
						"}";
				    writer = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
				    writer.write(str);
				    writer.flush();
			 // conn.connect();
			//获取文件字节总大小
			int fileLength = conn.getContentLength();
			System.out.println("文件总大小byte："+fileLength);
			System.out.println("文件总大小："+fileLength/1024/1024+"MB");
			int code = conn.getResponseCode();
			//conn.disconnect();
			//writer.close();
			if (code == 200) {
				//分配每个线程"平均"下载字节数
				int blockSize = fileLength/threadCount;
				//创建随机访问流，在本地为文件占位
				String outPath="D:\\sl\\space\\myEclipse\\4\\hk_hrss\\WebRoot\\resource\\images\\sydj\\test.pdf";
				RandomAccessFile raf = new RandomAccessFile(outPath, "rw");
				//重点：假设文件只有10字节，分3个线程来下载，理解理解for循环代码
				for (int threadId = 0; threadId < threadCount; threadId++) {
					int startIndex = threadId*blockSize;
					int endIndex = (threadId+1)*blockSize;
					if (threadId == threadCount-1) {
						endIndex = fileLength;
					}
					
					//每循环一次，开启一个线程执行下载,别漏了start()。
					new ThreadDownload(threadId, startIndex, endIndex).start();;
				}
				conn.disconnect();
				writer.close();
			}else{
				System.out.println("请求失败，服务器响应码："+code);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn!=null) {
				System.out.println("关闭连接");
				conn.disconnect();
			}
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 功能：开启线程下载，下载分配好的文件长度
	 * @author Administrator
	 * @param 线程ID，下载开始位置，结束位置，
	 */
	public static class ThreadDownload extends Thread{
		private int threadId;
		private int startIndex;
		private int endIndex;

		//构造方法接收3个参数初始化局部变量：线程ID、开始位置、结束位置
		public ThreadDownload(int threadId, int startIndex, int endIndex) {
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;	
		}
	
		@Override
		public void run(){
			OutputStreamWriter writer=null;
			RandomAccessFile raf=null;
			InputStream inputStream=null;
		     HttpURLConnection conn =null;

			try {

				System.out.println(threadId +"线程 开始请求连接");
				URL url = new URL(address);
			    conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(20*1000);
				conn.setReadTimeout(20*1000);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				 conn.setRequestProperty("Content-Type",
				            "application/json");
				 //conn.connect();
				System.out.println("线程"+threadId+"下载开始位置"+startIndex+" 结束位置："+endIndex);
				//设置请求头，请求部分资源下载
				conn.setRequestProperty("Range", "bytes:"+startIndex+"-"+endIndex);
				String str=" {\r\n" + 
						"\"requestInfo\": {\r\n" + 
						"\"appId\": \"0t580p8jzv10bma4\",\r\n" + 
						"\"encryptData\": \"6ka6garpfci06ve9\",\r\n" + 
						"\"interfaceAction\": \"0\",\r\n" + 
						"\"interfaceMark\": \"fileDownLoad\",\r\n" + 
						"\"version\": \"\"\r\n" + 
						"  },\r\n" + 
						"\"searchInfo\": {\r\n" + 
						"\"fileCode\":\"702239589362028544\"\r\n" + 
						//"\"fileCode\":\""+fileInfo[0]+"\"\r\n" + 
						"  }\r\n" + 
						"}";
				    writer = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
				    writer.write(str);
				    writer.flush();
				
				//服务器响应码206表示请求部分资源成功
				if (conn.getResponseCode()==200) {
				    inputStream = conn.getInputStream();
				    System.out.println("inputStream----"+inputStream);
				    System.out.println("conn----"+conn);

					String outPath="D:\\sl\\space\\myEclipse\\4\\hk_hrss\\WebRoot\\resource\\images\\sydj\\test.pdf";
					File file=new File(outPath);
				    raf = new RandomAccessFile(file, "rw");
					//查找写入开始位置
					raf.seek(startIndex);
					byte array[] = new byte[1024];
					int len = -1;
					while((len = inputStream.read(array))!=-1){
						raf.write(array, 0, len);
					}
					inputStream.close();
					raf.close();
					writer.close();
					conn.disconnect();

				}else{
					System.out.println("服务器响应码："+conn.getResponseCode());
				}
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(conn!=null) {
					conn.disconnect();
				}
				try {
					if(inputStream!=null) {
						inputStream.close();
					}
					if(raf!=null) {
						raf.close();
					}
					if(writer!=null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}