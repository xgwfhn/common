elcipse ����maven webapp����
https://www.cnblogs.com/hongmoshui/p/7994759.html
https://blog.csdn.net/m0_37292477/article/details/98625946

Ϊ��Ҫ��maven tomcat���������web���̣�
https://blog.csdn.net/FANTASY522272820/article/details/77053573
nexus2.x ���ð����Ʋֿ�
https://www.cnblogs.com/godwithus/p/8955824.html

web��Ŀ����ṹ
https://blog.csdn.net/weixin_41547486/article/details/81011788

����Ŀ����tomcat
����Ŀmaven install��  ����Ŀ��war��  ���õ�tomcat/webapp ��  ����tomcat
http://127.0.0.1:8080/maventomcatpublish/  ���ɷ���

maven tomcat ���  ��pom.xml ����
<!-- ����tomcat��� -->
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

��������:clean tomcat7:run

maven setting.xml �����ð����Ƶ�ַ
 <mirror>
            <id>nexus-aliyun</id>   <!--nexus ���õ�˽���ֿ�id -->
            <mirrorOf>*</mirrorOf>  <!--��ʾ���вֿⶼ���ʰ����Ʋֿ� -->
            <name>Nexus aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
  </mirror>
�����׶��������ʹ��tomcat �Ƽ���maven tomcat��� ,��Ϊmaven �ֿ�  ���ؽ���   �Ƽ�ʹ��nexus������Ʋֿ� ���ؽϿ�


��Ŀ�е�����
.settings/org.eclipse.wst.common.component
/.settings/.jsdtscope