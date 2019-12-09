elcipse 创建maven webapp工程
https://www.cnblogs.com/hongmoshui/p/7994759.html
https://blog.csdn.net/m0_37292477/article/details/98625946

为何要用maven tomcat插件来启动web工程？
https://blog.csdn.net/FANTASY522272820/article/details/77053573
nexus2.x 配置阿里云仓库
https://www.cnblogs.com/godwithus/p/8955824.html

maven tomcat 插件  在pom.xml 配置
<!-- 配置tomcat插件 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <configuration>
                    <port>8080</port>
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>

运行命令:clean tomcat7:run

maven setting.xml 中配置阿里云地址
 <mirror>
            <id>nexus-aliyun</id>   <!--nexus 配置的私服仓库id -->
            <mirrorOf>*</mirrorOf>  <!--表示所有仓库都访问阿里云仓库 -->
            <name>Nexus aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
  </mirror>
开发阶段如果打算使用tomcat 推荐用maven tomcat插件 ,因为maven 仓库  下载较慢   推荐使用nexus搭建阿里云仓库 下载较快

项目中的配置
.settings/org.eclipse.wst.common.component
/.settings/.jsdtscope