//参考地址 https://blog.csdn.net/salmon_zhang/article/details/72231849
package com.xgw.utils;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTest {
private static final String path = "https://mp4.vjshi.com/2017-05-02/7ea68125122fe074b6fb09edfc702564.mp4";
//private static final String path = "http://127.0.0.1:8080/resource/images/sydj/701866488196538368_单位解除或终止劳动合同的证明.pdf";

public static void main(String[] args) throws Exception {
    /**
     * 1.获取目标文件的大小
     */
    int totalSize = new URL(path).openConnection().getContentLength();
    System.out.println("目标文件的总大小为："+totalSize+"B");
    /**
     *2. 确定开启几个线程
     *开启线程的总数=CPU核数+1；例如：CPU核数为4，则最多可开启5条线程
     */
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    System.out.println("CPU核数是："+availableProcessors);
    int threadCount = 3;

    /**
     * 3. 计算每个线程要下载多少个字节
     */
    int blockSize = totalSize/threadCount;

    //每次循环启动一条线程下载
    for(int threadId=0; threadId<3;threadId++){
        /**
         * 4.计算各个线程要下载的字节范围
         */
        //开始索引
        int startIndex = threadId * blockSize;
        //结束索引
        int endIndex = (threadId+1)* blockSize-1;
        //如果是最后一条线程（因为最后一条线程可能会长一点）
        if(threadId == (threadCount -1)){
            endIndex = totalSize -1;
        }
        /**
         * 5.启动子线程下载
         */
        new DownloadThread(threadId,startIndex,endIndex).start();
    }
}

//下载的线程类
private static class DownloadThread extends Thread{
    private int threadId;
    private int startIndex;
    private int endIndex;
    public DownloadThread(int threadId, int startIndex, int endIndex) {
        super();
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run(){
        System.out.println("第"+threadId+"条线程，下载索引："+startIndex+"~"+endIndex);
        //每条线程要去找服务器拿取目标段的数据
        try {
            //创建一个URL对象
            URL url = new URL(path);
            //开启网络连接
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //添加配置
            connection.setConnectTimeout(5000);
            /**
             * 6.获取目标文件的[startIndex,endIndex]范围
             */
            //告诉服务器，只要目标段的数据，这样就需要通过Http协议的请求头去设置(range:bytes=0-499 )
            connection.setRequestProperty("range", "bytes="+startIndex+"-"+endIndex);
            connection.connect();
            //获取响应码，注意，由于服务器返回的是文件的一部分，因此响应码不是200，而是206
            int responseCode = connection.getResponseCode();
            //判断响应码的值是否为206
            if (responseCode == 206) {
                //拿到目标段的数据
                InputStream is = connection.getInputStream();
                /**
                 * 7：创建一个RandomAccessFile对象，将返回的字节流写到文件指定的范围
                 */
                //获取文件的信息
                String fileName = getFileName(path);
                //rw：表示创建的文件即可读也可写。
                RandomAccessFile raf = new RandomAccessFile("d:/"+fileName, "rw");
                /**
                 * 注意：让raf写字节流之前，需要移动raf到指定的位置开始写
                 */
                raf.seek(startIndex);
                //将字节流数据写到file文件中
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len=is.read(buffer))!=-1){
                    raf.write(buffer, 0, len);
                }
                //关闭资源
                is.close();
                raf.close();
                System.out.println("第 "+ threadId +"条线程下载完成 !");

            } else {
                System.out.println("下载失败，响应码是："+responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//获取文件的名称
private static String getFileName(String path){
    //http://localhost:8080/test.txt
    int index = path.lastIndexOf("/");
    String fileName = path.substring(index+1);
    return fileName ;
    }
}
//版权声明：本文为CSDN博主「salmon_zhang」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/salmon_zhang/article/details/72231849