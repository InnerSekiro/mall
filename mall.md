

springboot版本：3.1.3























==========关于数据库==============

使用linux环境部署

=================================




==========docker========================================================================================================

I.安装

            1.网址：https://zhuanlan.zhihu.com/p/143156163
    
            2.运行docker：

                    systemctl c;

            3.查看docker状态（注意该窗口会阻塞）：

                    systemctl status docker

            4.查看docker现有镜像：

                    docker images

            5.设置docker开机自启动：

                    systemctl enable docker
            
            6.至此安装完成





II.配置docker阿里云镜像下载地址

            阿里云镜像加速器，按顺序执行以下操作：

                    sudo mkdir -p /etc/docker
                    sudo tee /etc/docker/daemon.json <<-'EOF'
                    {
                    "registry-mirrors": ["https://e6jcuw6y.mirror.aliyuncs.com"]
                    }
                    EOF
                    sudo systemctl daemon-reload

链接： https://developer.aliyun.com/article/1245481?spm=a2c6h.14164896.0.0.1a9a47c5JPNQiV&scm=20140722.S_community@@%E6%96%87%E7%AB%A0@@1245481._.ID_1245481-RL_docker%E9%98%BF%E9%87%8C%E4%BA%91%E9%95%9C%E5%83%8F-LOC_search~UND~community~UND~item-OR_ser-V_3-P0_1mysql

                    

========================================================================================================================













==========docker安装mysql================================================================================================
            

I.安装
            docker pull mysql

默认安装最新版mysql

            





II.创建mysql实例并运行

            docker run -itd --name mysql-test -p 3306:3306 -v /mydata/mysql/log:/var/log/mysql -v /mydata/mysql/data:/var/data/mysql -v /mydata/mysql/conf.d:/etc/mysql.d -e MYSQL_ROOT_PASSWORD=123456 mysql

此时，外部服务器可以通过3306端口访问mysql，其密码为123456
将mysql的日志log挂载到/mydata/mysql/log/mysql
将mysql的数据data挂载到/mydata/mysql/data/mysql
将mysql的配置文件conf.d挂载到/mydata/mysql/conf.d



III.连接远程数据库
由于本虚拟机ip地址为192.168.74.130，故远程连接时的ip地址要选用此ip
    
            192.168.74.130:3306
            root
            123456

IV.在linux用docker命令进入mysql

            docker exec -it [ID] /bin/bash

其中-it表示交互方法
[ID]为该容器id开头三个字母
/bin/bash表示进入mysql的内部

随后便可进入mysql的内部文件夹：
            
            /var/mysql/log
            /var/mysql/data
            /etc/mysql.d



V.在挂载文件夹写入配置文件my.cnf:

[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000
​
[client]
default-character-set=utf8
​
[mysql]
default-character-set=utf8

主要是把编码格式改为utf8
配置过后要重启容器

doc

进入mysql内部后，cd /etc/mysql.d
随后cat my.cnf就可以查看配置文件了


========================================================================================================================










==========docker安装redis================================================================================================

I.直接pull redis，创建实例并运行：

            docker run -itd --name redis-test -p 6379:6379 -v /mydata/redis/data:/data -v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf -d redis redis-server /etc/redis/redis.conf

此时，redis的端口6379映射到docker的6379，/data和redis.conf映射到指定位置
-d代表后台运行，以redis镜像文件启动redis服务，并加载后面的配置文件




II.进入redis内部，然后可以使用redis的指令

            docker exec -it redis redis-指令



III.持久化
在redis.conf里加上一行：

            appendonly yes

随后要重启redis
此时redis设置的数据，可以直接持久化






IV.连接
使用的ip是虚拟机的ip地址



========================================================================================================================




==========配置git=========================================================

安装git

在git bash中配置：

            git config --global user.name "InnerSekiro"
            git config --global user.email "a18290531268@163.com"
            
生成免密连接密钥：

            ssh-keygen -t rsa -C "a18290531268@163.com"

密钥位置会给出提示

=========================================================================






==========创建微服务模块===============================================================

最先要导入的是：Web/SpringWeb、SpringCloudRoutine/OpenFeign
所有模块的父包名（组织名）都应该是：com.katzenyasax.mall

要创建的模块有：

            product：商品
            order：订单
            member：人员
            coupon：优惠券
            ware：

注意外面应当有一个大型mall模块聚合上述所有模块
可以在创建之时创建一个空项目，但是选上maven，创建之后就有pom.xml了，把packaging改为pom
然后用<module>所有模块
注意之后要在maven里添加mall作为总服务，此时mall会包含所有微服务




在总服务的.gitignore添加：

            **/.mvn
            **/mvnw
            **/mvnw.cmd
            **/target/
            .idea
            **/.gitignore

表示上传git时忽略这些无用文件





上传到gitee：
在settings/plugins安装gitee插件
mall总服务git->commit->push







======================================================================================




==========数据库初始化=======================================

连接docker部署的mysql，都创建数据库：

            mall_oms：order
            mall_pms：product
            mall_sms：coupon
            mall_ums：member
            mall_wms：ware

ms为manage system的缩写




========================================================



==========使用开源项目模板创建后台管理系统==================================================================================================

人人开源：renren-fast、renren-fats-vue、renren-generator

renren-fast删掉.git，直接加入mall的包下，并在mall的pom.xml里加入module
根据fast目录下db里的sql创建数据库：

            mall_admin

在application.yml中设置默认环境为test，并将application-test.yml的连接设置做好
比如url、username、password啥的：

            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.74.128:3306/mall_admin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
            username: root
            password: 123456



运行人人fast的启动类，在浏览器输入localhost:8080/renren-fast，应该会返回：

            {"msg":"invalid token","code":401}

表示运行成功了








renren-fast-vue，前端工程用vscode打开

安装node.js
配置npm淘宝镜像：

            npm config set registry http://r.cnpmjs.org/

下载会快很多
然后到vscode控制台终端（因为是首次运行vue项目），安装npm：

            先安装chormedriver：npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
            再安装node-sass：npm install node-sass
            最后安装剩余依赖：npm install

他是根据前端文件下package.json的目录下载的
随后运行：

            npm run dev

会自动打开一个8001端口的浏览器，并显示：

            <% if (process.env.NODE_ENV === 'production') { %> <% }else { %> <% } %>



要点：

            1.node.js：10.16.3
            2.node-sass：4.9.2
            3.sass-loader：7.3.1

            4.先安装chormedriver：npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
            5.再安装node-sass：npm install node-sass
            6.最后安装剩余依赖：npm install


==================================================================================================================================





===========逆向工程==================================================

克隆renren-fast-generator，删除.git，导入mall总服务，标记为模块
加入mall的pom.xml中
注意再generator的pom文件中，parent springboot工程下添加：

            <relativePath/> <!-- lookup parent from repository -->
            


随后根据不同的数据库生成不同的代码，例如生成mall_pms的代码
首先更改application.yml文件：

            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.74.128:3306/mall_pms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
            username: root
            password: 123456

随后在generator.properties中配置：

            mainPath=com.katzenyasax            主包名
            package=com.katzenyasax.mall        包名
            moduleName=product                  模块名，因为mall_pms是product模块的数据库
            author=KatzenyaSax                  作者名
            email=a18290531268@163.com          邮箱
            tablePrefix=pms_                    表前缀，加上就是了

随后运行，比较慢

springboot遇到循环依赖问题而无法启动时：
在yml文件中添加：

            spring:
                main:
                    allow-circular-references: true



生成后下载文件，直接把文件里/main的java和resource复制到项目的product里

但是下载的包里面好多依赖都没有，因此创建一个公共的类，其作用应当是为所有微服务提供公共依赖
创建maven模块：mall-common

因此此时的mall-product需要依赖mall-common：

            <dependency>
                <groupId>com.katzenyasax</groupId>
                <artifactId>mall-commom</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>

然后到common里加上公共依赖
目前已知所需的公共依赖为：

            mybatis-plus
            lombok

随后在common模块加入com.katzenyasax.common.utils.R等工具包：
从renren fast复制需要的工具包，需要的是Query、R和PageUtils
复制过去后不再报错
            






测试一下，

            1.测试之前还要在common中加入mysql驱动的依赖

                    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.33</version>
                    </dependency>


              注意一下，用了mybatis plus就不能再用mybatis的任何依赖了
              除此之外，一定要注意，只需要mybatis-plus-boot-starter这个包，也即是：

                    <!-- https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-boot-starter -->
                    <!-- 这个是controller里的getById等方法要用的，也是mybatis plus的核心依赖 -->
                    <!-- 同时处理"Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required"的问题 -->
                    <!-- 出现找不到xml中方法的情况时，第一时间应想到是否只依赖了该包 -->
                    <dependency>
                        <groupId>com.baomidou</groupId>
                        <artifactId>mybatis-plus-boot-starter</artifactId>
                        <version>3.5.3.2</version>
                    </dependency>

              因为这个包是mybatis-plus包的强化版
    


            2.在product模块的resource下面添加application.yml:

                    spring:
                        datasource:
                            username: root
                            password: 123456
                            driver-class-name: com.mysql.cj.jdbc.Driver
                            url: jdbc:mysql://192.168.74.128:3306/mall_pms
    
            3.在product的启动类上加上注解MapperScan：
    
                    @MapperScan("com.katzenyasax.mall.product.dao")

            4.在application.yml配置mybatis plus：

                    mybatis-plus:
                        mapper-locations: classpath*:/mapper/**/*.xml

            5.yml中增加主键自增：

                    mybatis-plus:
                        global-config:
                            db-config:
                                id-type: auto

            6.开始测试，使用@Test注解进行

                    @Autowired
                    BrandService brandService;
                    @Test
                    void test01(){
                        BrandEntity brandEntity=new BrandEntity();
                        brandEntity.setName("华为！");
                        brandService.save(brandEntity);
                        System.out.println("保存成功");
                
                    }

              运行后，pms_brand里面多出一项：

                    1,华为！,,,,,

              即表示成功运行














之后对其他模块也进行逆向工程
基本上就是在yml中添加：

            spring:
                datasource:
                    username: root
                    password: 123456
                    driver-class-name: com.mysql.cj.jdbc.Driver
                    url: jdbc:mysql://192.168.74.128:3306/数据库
                
                mybatis-plus:
                    mapper-locations: classpath*:/mapper/**/*.xml
                    global-config:
                        db-config:
                            id-type: auto

然后启动类添加@MapperScan

            @MapperScan("com.katzenyasax.mall.模块名.dao")








运行：


coupon：

            http://localhost:6600/coupon/coupon/list

            {
                "msg": "success",
                "code": 0,
                "page": {
                    "totalCount": 0,
                    "pageSize": 10,
                    "totalPage": 0,
                    "currPage": 1,
                    "list": []
                }
            }


member:

            http://localhost:7700/member/member/list

            {
                "msg": "success",
                "code": 0,
                "page": {
                    "totalCount": 0,
                    "pageSize": 10,
                    "totalPage": 0,
                    "currPage": 1,
                    "list": []
                }
            }

如上
注意不要用7000、8000等作为端口号，浏览器会认为这是不安全的端口而自动屏蔽


========================================================================================================================





==========Spring Cloud======================================================================================

使用spring cloud alibaba

主要使用：

            Alibaba         Nacos           注册中心、配置中心
            Spring          Ribbon          负载均衡
            Spring          Feign           声明式HTTP客户端，远程调用
            Alibaba         Sentinel        限流、降级、熔断
            Spring          Gateway         网关
            Spring          Sleuth          调用链监控
            Alibaba         Seata           分布式解决案

在common引入依赖：

           <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                        <version>2022.0.0.0</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
            </dependencyManagement>

这是依赖管理，以后用这个包的组件不用再指定版本号


=============================================================================================================






========== nacos =============================================================================================================

            1.引入nacos依赖：

                    <!-- 引入 SpringMVC 相关依赖，并实现对其的自动配置 -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                        <version>3.1.3</version>
                    </dependency>
                    <!-- 引入 Spring Cloud Alibaba Nacos Discovery 相关依赖，将 Nacos 作为注册中心，并实现对其的自动配置 -->
                    <dependency>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
                    </dependency>

            2.下载nacos server
              可以部署在docker上
            
                2.1.安装：
    
                        docker pull nacos/nacos-server:v2.1.1

                2.2.创建网络容器

                        docker network create nacos_network

                2.3.启动

                        docker run --name nacos -e NACOS_AUTH_ENABLE=true -d -p 9848:9848 -p 8848:8848 -p 9849:9849 --network nacos_network -e MODE=standalone nacos/nacos-server:v2.1.1

    
                  通过名为nacos_network的网络容器，在端口8848上开启了一个nacos服务器，容器模式为standable
                  注意，虚拟机的ip地址仍为：192.168.74.130

                2.4.开启鉴权

                        docker exec -it ID /bin/bash
        
                        cd /home/nacos/conf

                        vim application.properties

                    在application.properties加入：

                        


                2.5.远程开启nacos客户端
                    在浏览器访问：    

                        http://192.168.74.128:8848/nacos

                2.6.将所有微服务加入配置中心
                    在yml文件中添加：

                        spring:
                            cloud:
                                nacos:
                                  discovery:
                                    server-addr: 192.168.74.128:8848                        这是连接nacos中心的地址
                                    password: nacos                                         
                                    username: nacos
                                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333         切记这里应当是命名空间的id，而不是名称
                            application:
                                name: mall-coupon                                           该服务的名称，这个不加上nacos不过注册



                2.7.在所有微服务启动类加入注解：

                        @EnableDiscoveryClient

                    表示允许客户端发现该服务







注意开启linux的8848端口：

            sudo ufw allow 8848/tcp


===================================================================================================================================================





=========== open feign 微服务间远程调用===================================================================================

依赖为spring-cloud-open-feign，创建项目时引入就行
还要引入一个依赖：

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-loadbalancer</artifactId>
                <version>3.1.3</version>
            </dependency>
    
这个是新版本必须的依赖，是nacos的负载均衡

原理就是在一个微服务内通过注解指定要调用的是哪个微服务的哪个方法，指定之后交给open feign让其实现远程调用就可以了
例如我创建一个TEST服务用于进行测试，这个TEST需要被注册进nacos，和要调用的微服务处于同一命名空间
也即是在其yml中添加：

            server:
                port: 4869
            spring:
                cloud:
                    nacos:
                        discovery:
                            server-addr: 192.168.74.128:8848
                            username: nacos
                            password: nacos
                            namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                application:
                    name: mall-TEST

并且启动类上还要加上
    
            @@EnableDiscoveryClient

注意TEST不需要数据库，因此在@SpringBootApplication加上：

            (exclude= {DataSourceAutoConfiguration.class})

即排除MybatisPlus强制要求连接数据源的傻逼规定


调用流程为：

            1.创建feign接口，定义在包下的feign子包内：

                    @FeignClient("mall-coupon")
                    public interface Feign_ShowCoupon {
                        @RequestMapping("/coupon/coupon/")
                        public String show();
                    }

              其中@FeignClient中的是，在nacos注册中心中的服务名，注意不是项目内微服务的名称
              而@RequestMapping内的则是要调用的方法的全路径名

            2.在TEST启动类上打开open feign功能，即添加注解：

                    @EnableFeignClients(basePackages = "com.katzenyasax.test.feign")

              括号内的是feign包的位置

            3.定义controller，远程调用方法

                @RestController
                public class Controller_ShowCoupon {
                    @Resource
                    Feign_ShowCoupon showCoupon;
                    @RequestMapping(value = "/showCoupon")
                    public R GetAllCoupons(){
                        return R.ok(showCoupon.show());
                    }
                }
                
            4.测试：
              输入 localhost:4869/showCoupon，浏览器返回：

                    {"msg":"Coupon","code":0}

              测试成功


========================================================================================================================












========== nacos 作为配置中心============================================================================================

引入依赖：

            <!-- 将 Nacos 作为注册配置中心 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            </dependency>

流程：

            1.在微服务的resource加入bootstrap.yml
              比application.yml的优先级更高，优先读取
              还要添加读取bootstrap的依赖：

                    <!-- https://mvnrepository.com/artifact/org.webjars/bootstrap -->
                    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-starter-bootstrap</artifactId>
                        <version>4.0.0</version>
                    </dependency>


            2.在bootstrap中添加：
                        
                    spring:
                        application:
                            name: 注册中心中的服务名
                        cloud:
                            nacos:
                                config:
                                    server-addr: 192.168.74.128:8848
                                            username: nacos
                                            password: nacos
                                            namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333

              注意config，不要写出discovery了
              而且特别注意的是，bootstrap中写过config了，application了就不能再写了，
              也就是discovery和config要分开
              否则@RefreshScope动态刷新不生效
              name也必须写上

            3.在bootstrap中添加：

                    a:
                        b:
                            c:
                                114514

            4.测试：

                    @RestController
                    public class Controller_ShowDataInBootstrap {
                        @Value("${a.b.c}")
                        String data;
                        @RequestMapping("/showData")
                        public R showDataInBootstrap(){
                            return R.ok(data);
                        }
                    }

              结果为：

                    {"msg":"114514","code":0}

结果证明是可以正常读取的
但是这种情况下，每一次更高配置文件都要求重新上线服务，很不方便，因此使用nacos的配置中心
springboot启动类运行后，会给出对应的服务在nacos配置中心上对应的配置文件的名称
在nacos配置中心创建这个名称的配置文件，就可以使用nacos管理配置文件了

注意nacos中配置文件的名称是该服务在nacos中的名称，后面加上yml或properties的后缀
例如TEST模块在nacos中的服务名为mall-TEST，那么配置文件的名称就应该是mall-TEST.properties

除此之外，要实现实时刷新，还需要在调用nacos配置文件的类上方打上注解@RefreshScope：

整个过程可以分为以下几点：

            1.加依赖

            2.加bootstrap，里面配置config
              application中的config删除

            3.调用nacos配置文件的类打上注解@RefreshScope
    
            4.nacos中心加配置文件，名字要和服务名一致


========================================================================================================================





========== nacos 配置中心细节 ==========================================================================

I.命名空间
用来配置隔离的，例如创建开发空间、生产空间等，对应开发环境、生产环境等


II.手动加载nacos中的配置文件到微服务
   在bootstrap加上：

            spring:
                cloud:
                    nacos:
                        config:
                            extension-configs[0]:
                                data-id: 配置文件的名称
                                group: 配置文件的group名
                                refresh: true 是否开启实时刷新

值得一提的是，extension-config本质上是一个list，因此中括号里面的数代表该加载的配置文件在list中的位置
这意味着，可以加载多个配置文件到微服务

不过一般情况下，还是把配置文件加载过去吧，免得出现一些bug，保险一点


=======================================================================================================













========== 网关 =================================================================================================

使用spring cloud提供的Gateway
基于nacos注册中心和配置中心，因此所有服务都要加入nacos

路由：网关将请求发往服务的过程

断言：网关判断请求应发往哪一个服务的行为

过滤器：网关对请求进行筛选，滤除不合法的请求



            1.创建网关服务mall-gateway
              创建时导入gateway依赖
              pom中依赖mall-common

            2.开启注册发现和nacos配置
              application上加：

                    server:
                        port: 10100
                    spring:
                        cloud:
                            nacos:
                                discovery:
                                    server-addr: 192.168.74.128:8848
                                        username: nacos
                                        password: nacos
                                        namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                        main:
                            web-application-type: reactive
                        application:
                            name: mall-Gateway

              其中，spring.main.web-application-type: reactive是定常

              其次注意Gateway不需要数据库，因此在@SpringBootApplication加上：
              
                    (exclude= {DataSourceAutoConfiguration.class})
                    jdk1.8使用：(exclude= {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
              
              即排除MybatisPlus自动配置连接池，强制要求连接数据源的傻逼规定

            3.我们要实现一个功能
              输入路径/bilibili时，页面跳转到bilibili官网
              在application中添加：

                    spring:
                     cloud:
                      gateway:
                       routes:
                        - id: "gateway-test-bilibili"
                          uri: "https://www.bilibili.com"
                          predicates:
                           - Path=/bilibili

              
            4.测试
              运行后，可以发现，实际上我们跳转到的地址是：

                    https://www.bilibili.com/bilibili

              也就是说实际上跳转到的是导向的uri，和填写的路径的拼接值

            5.不要断言：

                              predicates:
                                - Path=/*

              但不是说不要，断言是必须要有的，只不过我们可以在逻辑层面规定实际上是否需要断言
              那就直接跳转到了首页
              不过也有缺点，那就是首页没有图片等资源


======================================================================================================================










========== 商品服务I.三级分类：方法 =======================================================================================

首先导入数据到pms_category，表示商品的分类
商品种类分为3级，要求查出所有的分类，并根据父子关系进行组装

product服务的CategoryController中，没有对应的方法，因此自己定义一个：

            @RequestMapping("/list/tree")
            public R listTree(){}

我们已经知道了如何查出所有的分类，也就是对应的service中的list方法，但是我们希望的是得到以父子关系组织好了的结果
所以我们应该在service里定义一个方法listAsTree()，这样一来我们只需要在listTree中返回R的ok()方法得到的数据就行了：

            @RequestMapping("/list/tree")
            public R listTree(){
                List<CategoryEntity> categoryEntitiescategoryService.listAsTree();
                return R.ok().put("success", categoryEntities);
            }

所以我们应该在service的接口内添加方法：

            List<CategoryEntity> listAsTree();

随后在实现类中重写该方法：

            @Override
            public List<CategoryEntity> listAsTree() {
                return null;
            }

该方法功能是：     1.查出所有分类
                  2.组装

首先是查出所有分类，我们已经有自动生成的service提供的方法了
而且我们在重写方法时，不需要再手动注入mapper，因为实现类CategoryServiceImpl继承了ServiceImpl时，对左边的泛型传入了类型CategoryDao
且ServiceImpl声明了该泛型为baseMapper，其类型也就是传入的CategoryDao，也就是我们需要的mapper
所以我们直接使用继承下来的baseMapper就可以，不需要再@Autowired一个
故查出所有分类的方法就是：

            //查出所有分类
            List<CategoryEntity> entities=baseMapper.selectList(null);

接下来进行父子分类
首先应当说明的是，pms_category中的所有类别，第一项数字是其父，第二项数字是层级
重点就是父的数字，它严格对应表上的id，数字为0代表是最高层级

获取一级分类：

            //获取一级子类
            Stream<CategoryEntity> one=entities.stream();
            List<CategoryEntity> oneCategory=one.filter(categoryEntity -> categoryEntity.getCatLevel()==1).collect(Collectors.toList());

注意stream是线程安全的，他只能被用一次，不管是进行过滤还是转化成集合
我们得到了第一层级的分类，如何存储第二层级的分类？
最简单的方法是直接在bean类加上成员变量，一个集合，用来存储其所有子类:

            //所有子类
	        @TableField(exist = false)
	        private List<CategoryEntity> children=new ArrayList<>();

最终方法为：

            @Override
            public List<CategoryEntity> listAsTree() {
                //查出所有分类
                List<CategoryEntity> entities=baseMapper.selectList(null);
                //组装父子
                //获取一级子类
                List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1).toList();
                //获取所有二级子类
                List<CategoryEntity> twoCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==2).toList();
                //获取所有三级子类
                List<CategoryEntity> threeCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==3).toList();
                //从三级子类开始遍历，将三级子类组装到二级子类
                for(CategoryEntity category3:threeCategory){
                    for(CategoryEntity category2:twoCategory){
                        if(category3.getParentCid()==category2.getCatId()){
                            category2.getChildren().add(category3);
                        }
                    }
                }
                //从二级子类开始遍历，将二级子类组装到一级子类
                for(CategoryEntity category2:twoCategory){
                    for(CategoryEntity category1:oneCategory){
                        if(category2.getParentCid()==category1.getCatId()){
                            category1.getChildren().add(category2);
                        }
                    }
                }
                return oneCategory;
            }

最终传输到前端的数据格式为：

            {
            "msg": "success",
            "code": 0,
            "success": [
                {
                    "catId": 1,
                    "name": "图书、音像、电子书刊",
                    "parentCid": 0,
                    "catLevel": 1,
                    "showStatus": 1,
                    "sort": 0,
                    "icon": null,
                    "productUnit": null,
                    "productCount": 0,
                    "children": [
                        {
                            "catId": 22,
                            "name": "电子书刊",

                    ······

格外注意，msg、code只是通信状况的反馈，success内才是正确的数据


===========================================================================================================================





========== 商品服务I.三级分类：网关配置 =======================================================================================

首先登录renren fast vue的管理界面
注意要在vscode中打开，不要用cmd打开
否则登不上

创建一个一级目录：商品系统
会发现数据库表：sys_menu多出一项：

            31,0,商品系统,"","",0,editor,1

再在此目录创建一个菜单：商品分类
我们想要在此处实现的功能是：展示所有的商品分类，一级、二级、三级
url设置为：/product/category
该url的完整路径应该为：

            localhost:8800/product/category

请求路径上，系统会自动将其修改为product-category
所以我们要在前端工程src/views/modules下面创建：/product/category.vue
使用以下模板：

            {
                "Print to console": {
                    "prefix": "vue",
                    "body": [
                        "<!-- $1 -->",
                        "<template>",
                        "<div class='$2'>$5</div>",
                        "</template>",
                        "",
                        "<script>",
                        "//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）",
                        "//例如：import 《组件名称》 from '《组件路径》';",
                        "",
                        "export default {",
                        "//import引入的组件需要注入到对象中才能使用",
                        "components: {},",
                        "data() {",
                        "//这里存放数据",
                        "return {",
                        "",
                        "};",
                        "},",
                        "//监听属性 类似于data概念",
                        "computed: {},",
                        "//监控data中的数据变化",
                        "watch: {},",
                        "//方法集合",
                        "methods: {",
                        "",
                        "},",
                        "//生命周期 - 创建完成（可以访问当前this实例）",
                        "created() {",
                        "",
                        "},",
                        "//生命周期 - 挂载完成（可以访问DOM元素）",
                        "mounted() {",
                        "",
                        "},",
                        "beforeCreate() {}, //生命周期 - 创建之前",
                        "beforeMount() {}, //生命周期 - 挂载之前",
                        "beforeUpdate() {}, //生命周期 - 更新之前",
                        "updated() {}, //生命周期 - 更新之后",
                        "beforeDestroy() {}, //生命周期 - 销毁之前",
                        "destroyed() {}, //生命周期 - 销毁完成",
                        "activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发",
                        "}",
                        "</script>",
                        "<style scoped>",
                        "//@import url($3); 引入公共css类",
                        "$4",
                        "</style>"
                    ],
                    "description": "生成vue模板"
                },
                "http-get请求": {
            	"prefix": "httpget",
            	"body": [
            		"this.\\$http({",
            		"url: this.\\$http.adornUrl(''),",
            		"method: 'get',",
            		"params: this.\\$http.adornParams({})",
            		"}).then(({ data }) => {",
            		"})"
            	],
            	"description": "httpGET请求"
                },
                "http-post请求": {
            	"prefix": "httppost",
            	"body": [
            		"this.\\$http({",
            		"url: this.\\$http.adornUrl(''),",
            		"method: 'post',",
            		"data: this.\\$http.adornData(data, false)",
            		"}).then(({ data }) => { });" 
            	],
            	"description": "httpPOST请求"
                }
            }

在vscode中，设置成vue.js的模板
随后根据模板生成category.vue
便可以操作前端页面

为了显示树形结构，在element:https://element.eleme.cn 中查找树形结构，
否则el-tree替换div
复制粘贴，替换data和methods
运行后发现结构正确

删除data的数据，置为空，接下来要导入真正的数据


在method定义方法：

            getMenus(){
              this.$http({
                  url: this.$http.adorn('product/category/list/tree'),
                  method: get
              }).then(data=>{
                  console.log("成功获取到数据...",data)
              })
            }

并且在周期方法created中，直接this调用该方法



但是此时该方法有缺陷，那就是他访问的地址默认为：8080/renren-fast
这显然不是我们想要的地址，我们要的地址是localhost:8800/product/category
因此我们应该修改
并且为了满足以后访问更多端口的服务时，我们直接让其地址指向我们设定的网关mall-Gateway：localhost:10100/api
在前端中：static/config/index.html中修改地址：

              window.SITE_CONFIG['baseUrl'] = 'http://localhost:10100/api';

其中api表示这是前端发送来的请求，用于在请求层面区分是前端请求和后端请求，后期会将其抹除
这样运行后，请求直接发给了网关，网关直接跳转到其他页面，而没有经过renren-fast，故刷新页面后会被弹出登录，甚至都没有验证码，导致永远无法登录
对此我们的策略是：请求经过网关时，我们默认让请求转给renren-fast，故我们需要让renren-fast被nacos配置中心发现

配置renren-fast注册：

            1.引入依赖：
    
                    <!-- 引入 Spring Cloud Alibaba Nacos Discovery 相关依赖，将 Nacos 作为注册中心 -->
		            <dependency>
		            	<groupId>com.alibaba.cloud</groupId>
		            	<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
                        <version></version>
		            </dependency>
            
		            <!-- spring cloud 2020版本以后，负载均衡就成了这个包 -->
		            <!-- 要实现springboot整合nacos、open feign，这个包是必须的 -->
		            <dependency>
		            	<groupId>org.springframework.cloud</groupId>
		            	<artifactId>spring-cloud-starter-loadbalancer</artifactId>
		            	<version>3.1.3</version>
		            </dependency>

              一定要考虑负载均衡
              除此之外，还要考虑每个依赖的版本兼容问题
              SpringBoot: 2.6.6
              Alibaba Cloud: 2021.0.1.0
              

            2.再在application.yml中添加：

                    spring
                        cloud:
                            nacos:
                                config:
                                    server-addr: 192.168.74.128:8848
                                    username: nacos
                                    password: nacos
                                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                        application:
                            name: mall-Admin

            2.随后在启动类加上：

                    @EnableDiscoveryClient

            3.重启后就可以被发现了

配置网关，前端请求api默认路由到renren-fast:

                    - id: admin-route
                      uri: http://localhost:8080
                      predicates:
                        - Path= /api/**
                          filters:
                            - RewritePath=/api(?<segment>/?.*),/renren-fast/$\{segment}

注意要用http，不能用https
此时便可以查找到验证码了

首先必须明白，我们的页面是前后联调的，前端会指定一条url指向对应的后端，以此实现前后联调，而且那个url也就是index.js里面的那个

所以整体的运作过程是：我们输入的localhost:8001是前端页面，原本会通过index.js里的url发送一条请求，该请求直接指向后端localhost:8080/renren-fast
但是我们对其进行更改，发送的请求指向了网关localhost:10100/api
网关的逻辑是：将/api/xxxx转化为/renren-fast/xxxx，拼接到指定的uri也就是localhost:8080
因此会跳转到localhost:8080/renren-fast/xxxx
而我们设置的请求，api后面没有，因此实际上最终跳转的请求为：

            localhost:8080/renren-fast

也就是最开始，前端默认指向的后端地址

不过这样一来会发现，依然无法登录，这是跨域问题未解决













跨域

就是不能使用另一个网站的，非简单方法
因为前端登录界面，它发出的任何请求都是我们设定的：http://localhost:10100/api
但是我们不管是要读取数据还是直接访问的后端的域名，实际上为：http://localhost:8080
二者域名不同，因此会出现无法登录的情况

不过也好解决，过程就是：

            1.浏览器发出一条Option的请求到目标服务器

            2.服务器反馈浏览器是否允许访问、或使用方法

            3.浏览器获得许可后，发出真实请求

            4.服务器根据请求返回数据

可以使用nginx解决跨域，但是有点麻烦
实际上我们只需要认为使服务器允许option就可以了
首先应该想到的是网关，在网关里直接处理所有请求，直接放行就可以了:
在网关创建配置类：

            @Configuration
            public class Mall_CorsConfiguration {
                @Bean
                public CorsWebFilter corsWebFilter(){
                    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
                    CorsConfiguration corsConfiguration=new CorsConfiguration();
                    //配置跨域
                    corsConfiguration.addAllowedHeader("*");            //允许跨域的请求头
                    corsConfiguration.addAllowedMethod("*");            //允许跨域的请求方式
                    corsConfiguration.addAllowedOriginPattern("*");            //允许跨域的
                    corsConfiguration.setAllowCredentials(true);        //允许携带cookie跨域
                    source.registerCorsConfiguration("/**",corsConfiguration);
                    //表示在上述配置下，允许任意请求跨域
                    return new CorsWebFilter(source);
                }
            }

然后把renren-fast配置的跨域：src.main.java.io.renren.config.CorsConfig.java，注释掉接下来
然后重启管理系统和网关

这样就可以登陆进去了











同时，以后前端发送的所有请求：http://localhost:10100/api/
都相当于是：http://localhost:8080/renren-fast/
二者为同等效力




============================================================================================================================











========== 商品服务I.三级分类：树形分类的前端展示 =======================================================================================

在网关加入：

                    - id: product-route
                    uri: http://localhost:8800
                    predicates:
                      - Path=/api/product/**
                    filters:
                      - RewritePath=/api/(?<segment>/?.*),/$\{segment}

并在mall-Product的bootstrap中配置跨域，使用网关中的配置文件
此时重启gateway，访问：

            http://localhost:10100/api/product/category/list/tree

会返回:

            {"msg":"invalid token","code":401}

表示没有令牌访问，即没有生效
原因是上面的一个/api/** 也满足条件，而且处于第一顺位，因此实际上我们的请求被它拦截了
调整一下顺序就行了，而且把它放在最后面:

                    - id: product-route
                      uri: http://localhost:8800
                      predicates:
                        - Path=/api/product/**
                      filters:
                        - RewritePath=/api/(?<segment>/?.*),/$\{segment}
                
                    - 更多id ······
            
                    - id: admin-route
                      uri: http://localhost:8080
                      predicates:
                        - Path= /api/**
                      filters:
                        - RewritePath=/api/(?<segment>/?.*),/renren-fast/$\{segment}

如此一来便可以访问到：


            {"msg":"success","code":0,"success":[{"catId":1,"name":"图书、音像、电子书刊","parentCid":0,"catLevel":1,"showStatus":1,"sort":0,"icon":null,"productUnit":null,"productCount":0,"children":[{"catId":22,"name":"电子书刊","parentCid":1,"catLevel":2,"showStatus":1,"sort":0,"icon":null,"productUnit":null,"productCount":0,"children":[{"catId":165,"name":"电子书",  ······

能够服务到数据，接下来进行数据的树形分类：

            1.在template添加：

                    <el-tree:data="menus":props="defaultProps">

            2.在export default中添加data：

                    data() {
                        return {
                          menus: [],
                          defaultProps: {
                            children: "children",
                            label: "name"
                          }
                        };
                    },

            3.methods中添加：

                    getMenus() {
                      this.$http({
                        url: this.$http.adornUrl("/product/category/list/tree"),
                        method: "get"
                      }).then(({ data }) => {
                        console.log("成功获取到菜单数据...", data.success);
                        this.menus = data.success;
                      });
                    },

              注意data是通过get方法获取的原始json数据，而data.success则是筛选其中success的部分
              因为我们传输过去的数据，格式并发msg、code、data
              而是msg、code、success
              
            4.created周期方法中调用getMenus方法：

                    this.getMenus();

            



================================================================================================================================================================








========== 商品服务I.三级分类：树形分类的删除 =======================================================================================

路径为：http://localhost:10100/api/product/category/delete
请求方式为post，前端传输json到后端，后端将json的数据打包为对象，再进行逻辑处理

CategoryController中已有了一个delete方法：

            @RequestMapping("/delete")
            public R delete(@RequestBody Long[] catIds){
	        	categoryService.removeByIds(Arrays.asList(catIds));
                return R.ok();
            }

它请求的是一个数组，存放需要删
除的分类的catId号
因此前端要发送的数据应该为：

            [1432,1433]

而后端反馈的数据为：

            {
                "msg": "success",
                "code": 0
            }

代表删除成功，而数据库中这两条数据确实的没有了

但是有缺陷，那就是我们不知道要删除的数据是否被引用，那么擅自删除的话可能会引发严重的错误
因此我们需要在CategoryServiceImp中自定义一个方法，这个方法应当实现：判断数据是否被引用、根据判断结果决定是否删除、反馈情况
同时，为了保留数据，我们不可能真的去对数据进行物理删除，这样一来数据就真没了
所以我们一般只会对一条数据的某个用来控制是否显示的字段进行判断，从而决定该数据是否返回到前端
而我们的数据中，show_status就表示该数据是否被显示
如下：

            @Override
            public void hideByIds(List<Long> list) {
                //TODO 1.判断数据是否被引用
                //2.隐藏数据
                //  直接删除就可以了
                baseMapper.deleteBatchIds(list);
            }

上面的//TODO表示等待解决的功能，因为我们还不知道说明功能引用了数据，所以这些功能放到以后写

第一想法是什么？肯定是根据show_status来判断是否组装进返回数据啊？那这样一来，实际上我们的操作并非delete，而是变相的update
这个想法是好的，但是手动实现也比较麻烦了

所以mybatis-plus也想到了，我们可以直接通过delete来进行变相的update，
只需要在存储在表内的数据对应的Bean的判断项，也即是CategoryEntity的showStatus打上注解：

            @TableLogic
	        private Integer showStatus;

随后在product模块的application中添加：

            mybatis-plus:
                global-config:
                    db-config:
                        logic-delete-value: 0
                        logic-not-delete-value: 1

配置全局配置，用show_status表示是否删除
0表示删除，1表示不删除

而且listTree方法中保持原样就可以了，不需要再通过show_Status判断是否装配进返回数据
因为我们在获取一二三级菜单时，使用的是baseMapper调用的select，mybatis-plus根据配置自动过滤了不显示的数据


在application加入：

            logging:
                level:
                    com.katzenyasax.mall: debug

可以在控制台输出对应的sql语句
进行测试：

            RequestBody为：[1431]

控制台输出：

            ==>  Preparing: SELECT cat_id,name,parent_cid,cat_level,show_status,sort,icon,product_unit,product_count FROM pms_category WHERE show_status=1
            ==> Parameters:
            <==      Total: 1425
            ==>  Preparing: UPDATE pms_category SET show_status=0 WHERE cat_id IN ( ? ) AND show_status=1
            ==> Parameters: 1431(Long)
            <==    Updates: 1
            ==>  Preparing: SELECT cat_id,name,parent_cid,cat_level,show_status,sort,icon,product_unit,product_count FROM pms_category WHERE show_status=1
            ==> Parameters:
            <==      Total: 1424

第一个select是listTree方法
第二个update是更新我们要删除的数据的show_status字段
第三个select为刷新页面调用的listTree方法





================================================================================================================================================================








========== 商品服务I.三级分类：树形分类的新增 =======================================================================================


前端请求路径：

            http://localhost:10100/api/product/category/save

方式为post
请求体为：

            {name: "test3", parentCid: 1, catLevel: 2, showStatus

自动生成的代码就可以使用


=================================================================================================





========== 商品服务I.三级分类：树形分类的拖拽排序 =======================================================================================


前端请求路径：

            http://localhost:10100/api/product/category/update/sort

请求方式为post
请求体为：

            [{catId: 1, sort: 0}, {catId: 2, sort: 1}, {catId: 3, sort: 2}, {catId: 4, sort: 3},…]
                    0: {catId: 1, sort: 0}
                    1: {catId: 2, sort: 1}
                    2: {catId: 3, sort: 2}
                    3: {catId: 4, sort: 3}
                    4: {catId: 5, sort: 4}
                    5: {catId: 6, sort: 5}
                    6: {catId: 1434, sort: 6, parentCid: 0, catLevel: 1}
                    7: {catId: 7, sort: 7}
                    8: {catId: 8, sort: 8}
                    9: {catId: 9, sort: 9}
                    10: {catId: 10, sort: 10}
                    11: {catId: 11, sort: 11}
                    12: {catId: 12, sort: 12}
                    13: {catId: 13, sort: 13}
                    14: {catId: 14, sort: 14}
                    15: {catId: 15, sort: 15}
                    16: {catId: 16, sort: 16}
                    17: {catId: 17, sort: 17}
                    18: {catId: 18, sort: 18}
                    19: {catId: 19, sort: 19}
                    20: {catId: 20, sort: 20}
                    21: {catId: 21, sort: 21}
                    22: {catId: 1432, sort: 22}

在controller中定义一个方法：

            //拖拽功能排序
            @RequestMapping("/update/sort")
            public R updateSort(@RequestBody CategoryEntity[] category){
                categoryService.updateBatchById(Arrays.stream(category).toList());
                return R.ok();
            }

调用的是自动生成的方法updateBatchById，批量排序




=================================================================================================





========== 商品服务II.品牌管理 ====================================================================


BrandController：关于品牌的所有handler

BrandDao：所有映射文件

BrandEntity：品牌的实体类

BrandService，BrandServiceImpl：关于品牌的所有方法



插入所有数据：pms_brand

管理系统中在商品服务下添加品牌管理

路径为：/product/brand

使用自动生成的vue文件，注意把getDataList()加入到created()生命周期方法中




设置品牌显示
请求路径：

            http://localhost:10100/api/product/brand/update/status

数据格式：

            {brandId: 14, showStatus: 0}
            brandId: 14
            showStatus: 0

定义一个controller：

            //修改显示状态
            @RequestMapping("/update/status")
            @RequiresPermissions("product:brand:update")
            public R updateStatus(@RequestBody BrandEntity brand){
                brandService.updateById(brand);
                return R.ok();
            }

共用update就行了，因为这是只对show_status起作用的特殊update




=================================================================================================





========== 商品服务II.品牌管理：云存储服务 ====================================================================



让用户上传的文件存储在同一个服务中
同时还要让一些图片资源等存储进去，让前端系统自动获取文件并展示，后端则需要实现上传文件至云端的功能

配置阿里云，oss对象存储服务，创建bucket：

            katzenyasax-mall
            有地域，华北
            低频访问
            本地冗余存储
            公共读

            其他默认
            
此时该阿里云的地址为：https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com

用户登录名称 mall-K@1223293873697731.onaliyun.com
登录密码 wgv&?@OFhid8P3Ri?6WBnJYslaBXjLK9
access id：LTAI5tSMQjRn2aWaXWYYezqU
access key：4RTcGMYo6UGNGAlvoicr4bVgw3ysWH



创建一个微服务mall-third-party，用于实现第三方功能
加上spring web、open feign






整个文件上传的流程：

            1.用户在前端上传文件

            2.前端向服务器申请oss云端的密钥

            3.前端拿取密钥后，直接将文件存储在指定位置

有效防止了上传文件的操作占用服务器原本业务的资源




这种是先发到服务器，服务器再上传：

            1.引入依赖：

                    <!-- 阿里云oss依赖 -->
                    <dependency>
                        <groupId>com.aliyun.oss</groupId>
                        <artifactId>aliyun-sdk-oss</artifactId>
                        <version>3.15.1</version>
                    </dependency>
                    <!-- oss需要的依赖(jdk1.8以后才用加) -->
                    <dependency>
                        <groupId>javax.xml.bind</groupId>
                        <artifactId>jaxb-api</artifactId>
                        <version>2.3.1</version>
                    </dependency>
                    <dependency>
                        <groupId>javax.activation</groupId>
                        <artifactId>activation</artifactId>
                        <version>1.1.1</version>
                    </dependency>
                    <!-- no more than 2.3.3-->
                    <dependency>
                        <groupId>org.glassfish.jaxb</groupId>
                        <artifactId>jaxb-runtime</artifactId>
                        <version>2.3.3</version>
                    </dependency>

            2.方法：

                    @Test
                    public void upload(){
                        String endpoint = "oss-cn-beijing.aliyuncs.com";
                        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
                        String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
                        String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
                        String bucketName = "kaztenyasax-mall";
                        // 创建OSSClient实例。
                        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                        // 上传文件流。
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream("C:\\Users\\ASUS\\Desktop\\东方project\\A38FC81C7BFD7B637F9FD6A7B9F2EDF8.jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ossClient.putObject(bucketName, "test5.jpg", inputStream);
                        // 关闭OSSClient。
                        ossClient.shutdown();
                    }

成功上传





但是签后直传：

前端从服务器要密钥，前端直接上传

加入nacos注册中心，端口号：10200
命名空间：third

启动类加上:

            @EnableDiscoveryClient
            @SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
            jdk1.8加：@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})

依赖mall-common，依赖oss相关依赖，且注意将mall-common的该依赖删除

在nacos中为ThirdParty添加配置文件oss.yaml：

            spring:
                cloud:
                    alicloud:
                        access-key: LTAI5tSMQjRn2aWaXWYYezqU
                        secret-key: 4RTcGMYo6UGNGAlvoicr4bVgw3ysWH
                        oss:
                            endpoint: oss-cn-beijing.aliyuncs.com

随后在bootstrap添加：

            spring:
                cloud:
                    nacos:
                        config:
                            extension-config[0]:
                                data-id=oss.yml
                                group=DEFAULT_GROUP
                                refresh=true                                

此时测试类中：

            @Test
            public void upload(){
                String endpoint = "oss-cn-beijing.aliyuncs.com";
                // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
                String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
                String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
                String bucketName = "kaztenyasax-mall";
                // 创建OSSClient实例。
                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                // 上传文件流。
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream("C:\\Users\\ASUS\\Desktop\\东方project\\A38FC81C7BFD7B637F9FD6A7B9F2EDF8.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ossClient.putObject(bucketName, "test100.jpg", inputStream);
                // 关闭OSSClient。
                ossClient.shutdown();
            }

依然可用，文件上传成功








但是我们要用前端传啊，后端只是给前端传密钥的，怎么传？


            












==================================================================================================================================================







========== 商品服务II.品牌管理：品牌图片上传与显示 ====================================================================


将其加入网关，要达成的目的应该是：
访问：http://localhost:10100/api/thirdparty/oss/policy 时，相当于访问 http://localhost:10200/thirdoarty/oss/policy

                - id: oss-policy
                  uri: http://localhost:10200
                  predicates:
                    - Path= /api/oss/policy
                  filters:
                    - RewritePath= /api/(?<segment>/?.*),/$\{segment}

再次访问 http://localhost:10100/api/thirdparty/oss/policy  时也成功






但是在封装好的前端系统（谷粒商城文档）测试时，会显示跨域错误：

            Access to XMLHttpRequest at 'http://localhost:10100/api/thirdparty/oss/policy?t=1694670945521' from origin 'http://localhost:8001' has been blocked by CORS policy: The 'Access-Control-Allow-Origin' header contains multiple values 'http://localhost:8001, *', but only one is allowed.

因此在oss管理平台中设置跨域：

            *
            POST








失败，报跨域问题，有多个请求头
未知原因
手动控制数据库的图片url地址



=========================================================================================================================================================






========== 商品服务II.品牌管理：后端数据校验 ====================================================================


数据提交后端后，封装为BrandEntity，其中的数据虚经过校验

在BrandEntity中的字段添加注解，@NotBlank、@NotEmpty等
复杂场景下，使用@Pattern(regexp=" ", message="")，表示不符合正则表达式时，返回报错信息：

            @TableId
	        private Long brandId;

	        @NotBlank
	        private String name;

	        @URL
	        private String logo;

	        @NotBlank
	        private String descript;

	        @NotNull
	        private Integer showStatus;

	        @NotBlank
	        private String firstLetter;

	        @NotNull
	        private Integer sort;

对于String类型来说，一般使用@NotBlank
Integer一般用@NotNull



除此之外，还要在controller参数表加上@Valid，否则单独的字段注解无效
在接收数据的controller，即save方法的参数前加上：@Valid

            @RequestMapping("/save")
            @RequiresPermissions("product:brand:save")
            public R save(@RequestBody @Valid BrandEntity brand){
	        	brandService.save(brand);
                return R.ok();
            }

当保存时，会进行校验，若数据不符合校验规则，则不起效
除此之外，可以在错误时，手动报错：

            @RequestMapping("/save")
            @RequiresPermissions("product:brand:save")
            public R save(@RequestBody @Valid BrandEntity brand, BindingResult result){
                if(result.hasErrors()){
                    Map<String,String> res=new HashMap<>();
                    result.getFieldErrors().forEach((item)->{
                       String msg=item.getDefaultMessage();
                       String fld=item.getField();
                       res.put(fld,msg);
                    });
                    return R.error(400,"数据不合法").put("data",res);
                }
	        	brandService.save(brand);
                return R.ok();
            }

数据不合法时，使用一个Map存储所有报错信息，并返回前端




===================================================================================================






========== 商品服务II.品牌管理：统一异常处理 ====================================================================


用于集中处理数据异常
使用SpringMVC提供的@ControllerAdvice注解

            1.创建ControllerAdviceExcetion，添加注解：

                    @RestControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")

              表示扫描controller包中所有的类，对其所有异常进行处理

            2.将BrandController.save方法的所有异常处理的业务去除，只保留正常业务代码：

                    @RequestMapping("/save")
                    @RequiresPermissions("product:brand:save")
                    public R save(@RequestBody @Valid BrandEntity brand){
	                	brandService.save(brand);
                        return R.ok();
                    }

            3.将处理异常的业务放于ControllerAdviceException中：

                    @Slf4j                                                                                      //日志输出  
                    //@ControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
                    @RestControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
                    //包含了：ControllerAdvice和ResponseBody
                    public class ControllerAdviceException {
                        //@ResponseBody                                                                         //要以json格式返回数据
                        @ExceptionHandler(value = MethodArgumentNotValidException.class)                        //表示可处理的异常
                        public R handlerValidException(MethodArgumentNotValidException e){
                            log.error("数据不合法",e.getMessage(),e.getClass());                                 //日志输出异常

                            BindingResult result=e.getBindingResult();
                            Map<String,String> map=new HashMap<>();
                            result.getFieldErrors().forEach((item)->{
                                String msg=item.getDefaultMessage();
                                String fld=item.getField();
                                map.put(fld,msg);
                            });

                            return R.error(400,"数据不合法").put("data",map);
                        }
                    }

              处理的是MethodArgumentNotValidException，即数据不合法
              注意，原本应该加上注解ControllerAdvice和ResponseBody，分别表示：处理异常，和返回数据为json格式
              但是由于有整合的注解RestControllerAdvice，因此直接选用整合版
              注解BindingResult开始的就是原本BrandController中处理异常的业务

            


此时访问： 

            localhost:10100/api/product/brand/save

post一个：

            {
                 "name": "华为", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png"
            }

返回的异常：

            {
                "msg": "数据不合法",
                "code": 400,
                "data": {
                    "name": "需要是一个合法的URL",
                    "showStatus": "不能为null",
                    "sort": "不能为null",
                    "descript": "不能为空",
                    "firstLetter": "不能为空"
                }
            }

同时服务器也记录了错误日志：

            2023-09-14T17:16:29.868+08:00 ERROR 16524 --- [nio-8800-exec-1] c.k.m.p.e.ControllerAdviceException      : 数据不合法











为了保证错误的可溯源，我们可以在ControllerAdviceException中定义一个处理最大异常Throwable的handler：

            @ExceptionHandler(value = Throwable.class)
            public R handlerThrowable(Throwable e){
                return R.error();
            }

以后在controller中，可以放心大胆地抛出异常，异常都会被这个handler处理



为了可读性，推荐根据模块的不同、方法的不同自定义一些错误码，并放在common中，整个项目都能够使用这一标准错误码

在mall-common/src/main/java com.katzenyasax.common.exception.BizCodeEnume创建该表单，用以存放所有的错误码
错误码的规则：

            1. 错误码定义规则为5为数字
            2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
            3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
                错误码列表：
                10: 通用
                001：参数格式校验
                11: 商品
                12: 订单
                13: 购物车
                14: 物流

故handlerValidException可将return改写为：

            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",map);


==============================================================================================================================




================= 商品服务II.品牌管理：分组异常处理 ============================================================================



不同的业务，异常要求也不同

可以在校验注解括号加上group={}，例如：

            @TableId
	        @NotNull(message = "更新数据时，必须指定id",groups = {UpdateGroup.class})
	        @Null(message = "插入时，禁止指定id",groups = {InsertGroup.class})
	        private Long brandId;

所有组都是标记型接口，没有实际内容
接下来在不同的方法上加注解，例如BrandController的save：

            public R save(@RequestBody @Validated({InsertGroup.class}) BrandEntity brand)

表示该方法的分类为Insert
测试一下，此时为添加，如果说id为空，那么应该不会报错的：

            localhost:10100/api/product/brand/save

            {
                 "name": "华", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png",
                 "showStatus":1,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }

            {
                "msg": "success",
                "code": 0
            }

如果指定了id，那应该会报错：

            localhost:10100/api/product/brand/save

            {
                 "brandId":55,
                 "name": "华", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png",
                 "showStatus":1,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }
            
            {
                "msg": "参数格式校验失败",
                "code": 10001,
                "data": {
                    "brandId": "插入时，禁止指定id"
                }
            }

正确的



又例如图片地址，在指定的时候必须是一个URL，但是允许不指定，那么就：

            @URL(message = "品牌logo必须是合法的URL",groups = {InsertGroup.class})
	        private String logo;

其余情况不指定就行了
所以歧视BrandEntity的最终形态应该是：

            @TableId
	        @NotNull(message = "更新数据时，必须指定id",groups = {UpdateGroup.class})
	        @Null(message = "插入时，禁止指定id",groups = {InsertGroup.class})
	        private Long brandId;
	        /**
	         * 品牌名
	         */
	        @NotBlank(message = "插入时，必须指定name",groups = {InsertGroup.class})
	        private String name;
	        /**
	         * 品牌logo地址
	         */
	        @URL(message = "品牌logo必须是合法的URL",groups = {InsertGroup.class})
	        private String logo;
	        /**
	         * 介绍
	         */
	        @NotBlank(message = "插入时，禁止指定descript",groups = {InsertGroup.class})
	        private String descript;
	        /**
	         * 显示状态[0-不显示；1-显示]
	         */
	        @NotNull(message = "插入时，禁止指定showStatus",groups = {InsertGroup.class})
	        private Integer showStatus;
	        /**
	         * 检索首字母
	         */
	        @Pattern(regexp="[a-zA-Z]",message = "插入时，禁止指定firstLetter",groups = {InsertGroup.class})
	        private String firstLetter;
	        /**
	         * 排序
	         */
	        @NotNull(message = "插入时，禁止指定sort",groups = {InsertGroup.class})
	        @Min(value = 0,message = "排序必须大于等于0")
	        private Integer sort;

没有指定分组的字段，在@Validated生效时看作没有校验注解，即此时可指定可不指定


==============================================================================================================================




================= 商品服务II.品牌管理：自定义数据校验 ============================================================================



自定义校验，满足发展情况下的校验，例如我要校验showStatus，只能是0或1
而我要自定义一个注解，可以校验数据是否为我指定的一个数组内的值



            

那么流程为：
    
            1.编写自定义注解

                @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
                @Retention(RetentionPolicy.RUNTIME)
                @Documented
                @Constraint(validatedBy = {})
                public @interface NumbersIWant {
                    String message() default "{}";
                    Class<?>[] groups() default {};
                    Class<? extends Payload>[] payload() default {};
                    int[] value();
                }

              这是雏形

            2.编写校验器

                public class NumbersIWantConstraint implements ConstraintValidator<NumbersIWant,Integer> {
                    private Set<Integer> set=new HashSet<>();
                    @Override
                    public void initialize(NumbersIWant constraintAnnotation) {
                        int[] values=constraintAnnotation.value();
                        for(int n:values){
                            set.add(n);
                        }
                    }
                    @Override
                    public boolean isValid(Integer integer/** 这里的integer是提交的值 **/, ConstraintValidatorContext constraintValidatorContext) {
                        return set.contains(integer);
                    }
                }

              他必须实现一个接口ConstraintValidator<E,F>，E是标准，F是提交上来的数
              通过initialize方法初始化数据，isValid则正式判断提交的数据是否满足注解
              isValid由系统调用，会自动传入提交的数

            3.将二者联调：
              在NumbersIWant上指定校验器

                    @Constraint(validatedBy = {NumbersIWantConstraint.class})
    
              表示使用NumbersIWantConstraint这个校验器

测试一下，给showStatus加上：

	        @NumbersIWant(value = {0,1})
	        private Integer showStatus;

访问地址：

            localhost:10100/api/product/brand/save

请求体json为：

            {
                 "name": "华",
                 "showStatus":2,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }

反馈：

            {
                "msg": "参数格式校验失败",
                "code": 10001,
                "data": {
                    "showStatus": ???????
                }
            }


=========================================================================================================










========== 商品服务III.属性分组 =====================================================================================


商品系统/平台管理/属性分组

请求路径：
    
            /product/attrgroup/list/{catelogId}

方式为get
catelogId为0时默认查找所有
请求参数为

            {
                page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式
                key: '华为'//检索关键字
            }

返回结果：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
            			"attrGroupId": 0, //分组id
            			"attrGroupName": "string", //分组名
            			"catelogId": 0, //所属分类
            			"descript": "string", //描述
            			"icon": "string", //图标
            			"sort": 0 //排序
            			"catelogPath": [2,45,225] //分类完整路径
            		}]
            	}
            }



流程：

            1.在AttrGroupController中定义：

                     //根据id查找属性分组
                    @RequestMapping("list/{attrgroup}")
                    public R listAttrGroup(@RequestParam Map<String, Object> params,@PathVariable Integer attrgroup){
                        PageUtils page=attrGroupService.queryPage(params,attrgroup);
                        return R.ok().put("page",page);
                    }

            2.AttrGroupServiceImpl定义方法：

                    @Override
                    public PageUtils queryPage(Map<String, Object> params, Integer catelogId) {
                        if(catelogId==0){
                            String key=(String) params.get("key");
                            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().like("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
                            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
                            IPage<AttrGroupEntity> pageFinale = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
                            return new PageUtils(pageFinale);
                        }
                        else {
                            QueryWrapper<AttrGroupEntity> wrapper =new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
                            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
                            IPage<AttrGroupEntity> pageFinale=this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
                            return new PageUtils(pageFinale);
                            //catelogId不为0，但是key也不为空
                        }
                    }


              IPage集合，其构造为：

                     IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),new QueryWrapper<AttrGroupEntity>());

              有两个构造条件，Query就是前端传来的固定的param，决定了limit、page等分页标准
              第二个QueryWrapper则是匹配sql的校验器，查询结果必须满足该校验器，才能通过查询，而new一个空参的QueryWrapper，意思就是没有校验，任何结果都可通过查询

              对于wrapper，eq方法为判断是否相等，like则是模糊判断，条件之间可通过or()和and()方法进行连接，但是注意and()方法默认加括号



前端做好对接后（主要是categories需要把data.data改成data.success）

接下来在第三级菜单的children上加注解：

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
	        @TableField(exist = false)
	        private List<CategoryEntity> children=new ArrayList<>();
        
表示当children不为空集合时，返回的json数据才包含该字段，内容为空时，json不返回该字段
这是为了防止在第三级菜单时，由于检测到集合children[]的存在，而自动生成下一级菜单（此时用的不再是el-tree，故规则不同，el-tree我们可以写死只有三级）
而导致无法选择groupId



=========================================================================================================










========== 商品服务III.属性分组：修改页面数据回显 =====================================================================================




接下来会发现，修改时，无法回显groupId的路径，因此写一个方法回显groupId的路径
路径就加在AttrGroupEntity的成员变量中：

            @TableField(exist = false)			//不存在于数据库
	        private Long[] Path;

其中依次存放父——>子的categoryId
接下来在AttrGroupController中的返回属性分组信息的方法，即：

            @RequestMapping("/info/{attrGroupId}")
            @RequiresPermissions("product:attrgroup:info")
            public R info(@PathVariable("attrGroupId") Long attrGroupId)
    
因为回显进入修改界面时，默认发出请求：

            http://localhost:10100/api/product/attrgroup/info/

因此在info上添加：
因为是有关category的查询，因此使用category的service查询

            @RequestMapping("/info/{attrGroupId}")
            @RequiresPermissions("product:attrgroup:info")
            public R info(@PathVariable("attrGroupId") Long attrGroupId){
	        	AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
                Long attrGroupCatelogId=attrGroup.getCatelogId();
                //先获取分组id
                Long[] path=categoryService.getCategoryPath(attrGroupCatelogId);
                //再获取完整路径
                attrGroup.setPath(path);
                //设置回显路径
                return R.ok().put("attrGroup", attrGroup);
            }

随后定义方法：
        
            public Long[] getCategoryPath(Long id) {
                Long[] pathF=new Long[3];
                    //最多三级，因此数组长度为3
                pathF[2]=id;
                    //最后一个数就是该元素（孙子）的id
                CategoryEntity son = this.getById(id);
                    //儿子对象
                pathF[1]=son.getParentCid();
                    //数组第二个元素为儿子的id
                CategoryEntity father=this.getById(son.getParentCid());
                    //父亲对象
                pathF[0]=father.getParentCid();
                    //数组第一个元素为父亲的id
                return pathF;
                    //直接返回
            }
            
对于前端，还要将存储该数组的

            this.catelogPath=data.attrGroup.catelogPath
            
改为：

            this.catelogPath =  data.attrGroup.path;






至此功能完成
测试一下：

            http://localhost:10100/api/product/attrgroup/info/12?t=1694864825179

返回：

            {
                "msg": "success",
                "attrGroup": {
                    "attrGroupId": 12,
                    "attrGroupName": "1",
                    "sort": 1,
                    "descript": "1",
                    "icon": "1",
                    "catelogId": 269,
                    "path": [
                        3,
                        39,
                        269
                    ]
                },
                "code": 0
            }

至此功能完成


===============================================================================================================
























========== 分页 =====================================================================================


发现问题：
分类属性的下方，不会显示共几条数据，只能显示有多少页和每页多少条数据
故使用mybatis plus分页插件

使用mybatis plus的分页插件
官方文档：   https://baomidou.com/pages/2976a3/#spring-boot


在product模块内加上配置文件：config.MybatisPlusConfiguration

            @Configuration
            @EnableTransactionManagement
            @MapperScan(value = "com.katzenyasax.mall.product.dao")
            public class MybatisPlusConfiguration {
                @Bean
                    public MybatisPlusInterceptor mybatisPlusInterceptor() {
                        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
                        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
                        return interceptor;
                    }
            }

此时分类属性下面就有页数了






=========================================================================================================




========== 商品服务II.品牌管理 查询 =====================================================================================


模糊查询，更改BrandServiceImpl下面的方法为：

            @Override
            public PageUtils queryPage(Map<String, Object> params) {
                //获取关键字key
                String key=(String) params.get("key");
                log.info(String.valueOf((key==null)));
                //若关键字key不为空：
                if(key!=null){
        
                    //进行对name和descript的模糊匹配
                    QueryWrapper<BrandEntity> wrapper=new QueryWrapper<BrandEntity>().like("name",key).or().like("descript",key);
                    return new PageUtils(this.page(new Query<BrandEntity>().getPage(params),wrapper));
        
        
                }
                //若关键字不为空
                IPage<BrandEntity> page = this.page(
                        new Query<BrandEntity>().getPage(params),
                        new QueryWrapper<BrandEntity>()
                );
                return new PageUtils(page);
            }

结果是查到了
但其实不用判断key是否为空，即使为空的话也只是为数据添加了一个开放的判断标准罢了


=========================================================================================================










========== 商品服务IV.品牌和商品分类的级联 =====================================================================================


商品分类和品牌呈多对多的关系
一个品牌的所有商品可以有多种属性分类，一个商品分下类也可以有多个品牌的商品
对于这种情况，使用一张中间表单进行连接



请求路径：

            http://localhost:10100/api/product/categorybrandrelation/catelog/list

controller中定义：

            //获取商品关联
            @GetMapping(value = "/catelog/list")
            public R listCatelog(@RequestParam("brandId")int brandId){
                log.info("brandId: "+String.valueOf(brandId));
                List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                        new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
                return R.ok().put("data",data);
            }
        
        
        
            //获取品牌关联
            @GetMapping(value = "/brand/list")
            public R lisBrand(@RequestParam("catelogId")int catelogId){
                List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                        new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catelogId));
                return R.ok().put("data",data);
            }

使用了校验器，校验catelog_id或brand_id是否等于参数
根据参数返回值
总之这样就返回了关联关系，例如：

            http://localhost:10100/api/product/categorybrandrelation/catelog/list?t=1694917073908&brandId=9

此处brandId=9，页面返回：

            {
                "msg": "success",
                "code": 0,
                "data": [
                    {
                        "id": 13,
                        "brandId": 9,
                        "catelogId": 225,
                        "brandName": "华为",
                        "catelogName": "手机"
                    },
                    {
                        "id": 15,
                        "brandId": 9,
                        "catelogId": 250,
                        "brandName": "华为",
                        "catelogName": "平板电视"
                    },
                    {
                        "id": 16,
                        "brandId": 9,
                        "catelogId": 449,
                        "brandName": "华为",
                        "catelogName": "笔记本"
                    }
                ]
            }

=========================================================================================================










========== 商品服务IV.新增、保存品牌和商品分类的级联 =====================================================================================


请求路径：

            product/categorybrandrelation/save

请求参数：
    
            {"brandId":1,"catelogId":2}

响应：

            {
            	"msg": "success",
            	"code": 0
            }

表示，brandId为1的品牌，和catelogId为2的属性分类关联起来
同时要可以存入双方的name属性:

在controller中修改save方法中，调用的service的方法：

            categoryBrandRelationService.saveName(categoryBrandRelation);

save改为自定义方法saveName
随后在service接口和实现类中实现：

            //先要获取brandId和catelogId对应的name：
            //所以需要调用BrandService和CategoryService
            @Autowired
            BrandDao brandDao;
            @Autowired
            CategoryDao categoryDao;
            @Override
            public void saveName(CategoryBrandRelationEntity categoryBrandRelation) {
                //获取了name
                String brandName=brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
                String catelogName=categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
                //设置name
                categoryBrandRelation.setBrandName(brandName);
                categoryBrandRelation.setCatelogName(catelogName);
                //返回设置过name的关系对象
                this.save(categoryBrandRelation);
            }

直接使用了自动注入的dao层接口BrandDao和CategoryDao，来select双方的name
用service层的getById貌似有问题


===========================================================================================================================================










========== 商品服务IV.品牌和商品分类级联一致性问题 =====================================================================================


由于我们的relation表存储的是商品分类和品牌的关系，那么其数据应该和上述两张表的数据一致
也就是说，如果商品分类或者品牌中数据发生变动，relation表的数据也要发生变动（如果此时引用了上述两张表的数据的话）


这样一来就必须在Brand和Category的模块中去实现
若名字有所变动，如修改、删除，对应的关系表也要被修改、删除

所以我们在CategoryBrandRelationServiceImpl中定义实现类特有方法：

            public void updateCategory(Long catelogId,String  catelogName){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).filter(                            //id必须相同
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogName()!=catelogName).collect(                              //name必须不同，否则视为未变更
                                Collectors.toList());         
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setCatelogName(catelogName);
                    this.updateById(entity);
                }
        
            }
            public void updateBrand(Long brandId,String brandName){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).filter(                                //id必须相同
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandName()!=brandName).collect(                          //name必须不同，否则视为未更改
                        Collectors.toList());
                log.info("entities: "+entities.toString());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setBrandName(brandName);
                    log.info(entities.toString());
                    this.updateById(entity);
                }
            }
    
因此分别在CategoryController和BrandController中的update方法加上：

            @RequestMapping("/update")
            public R update(@RequestBody CategoryEntity category){
	        	categoryService.updateById(category);
                categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
            //加上调用relationService，通过category的id和name更新relation中的数据
                return R.ok();
            }

            @RequestMapping("/update")
            public R update(@RequestBody BrandEntity brand){
	        	brandService.updateById(brand);
                categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
            //加上调用relationService，通过brand的id和name更新relation中的数据
                return R.ok();
            }

其中categoryBrandRelationService是通过实现类自动注入的实现类的对象
可以调用其内部特有方法


整个原理就是：
当category或brand修改名字时，调用relation内部的方法，根据category或brand的id和name获取应该修改的值
判断是否应该修改，是根据stream的过滤器判断的，首先id相同，其次name必须不同，因为name不同就代表着本次并未修改name，故不需要再进行下面的操作
如果得到了应该修改的数据，会存到一个list中
遍历该list，每次修改name，然后使用updateById存入数据库
于是这样就完成了数据的同步








当然还有删除的一致性，删除某个category或brand后，对应id的relation的数据直接进行删除，不需要再经过name:

            public void deleteCategory(Long catelogId){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).collect(Collectors.toList());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setCatelogName(catelogName);
                    baseMapper.deleteById(entity);
                }
            }
            public void deleteBrand(Long brandId){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).collect(Collectors.toList());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setBrandName(brandName);
                    baseMapper.deleteById(entity);
                }
            }

随后在category和brand的controller的delete中调用方法就行了

            //逻辑删除
            @RequestMapping("/delete")
            public R deleteSafe(@RequestBody Long[] catIds){
                categoryService.hideByIds(Arrays.asList(catIds));
                for(Long id:catIds){
                    categoryBrandRelationService.deleteCategory(id);
                }
                return R.ok();
            }

            @RequestMapping("/delete")
            @RequiresPermissions("product:brand:delete")
            public R delete(@RequestBody Long[] brandIds){
	        	brandService.removeByIds(Arrays.asList(brandIds));
                for(Long id:brandIds){
                    categoryBrandRelationService.deleteBrand(id);
                }
                return R.ok();
            }

只不过注意category是要逻辑删除里调用就是了



======================================================================================================================================================================================





============== 商品服务V：查询规格参数和销售属性 =====================================================================================================================


二者共用同一个表，区别在于attr_type
为0：普通规格参数
为1：销售属性
为2：二者皆是


查询
参数规格：

            /product/attr/base/list/{catelogId}

销售属性：

            /product/attr/sale/list/{catelogId}
         

请求参数：

            {
                page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式
                key: '华为'//检索关键字
            }

响应结果：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
            			"attrId": 0, //属性id
            			"attrName": "string", //属性名
            			"attrType": 0, //属性类型，0-销售属性，1-基本属性
            			"catelogName": "手机/数码/手机", //所属分类名字
            			"groupName": "主体", //所属分组名字
            			"enable": 0, //是否启用
            			"icon": "string", //图标
            			"searchType": 0,//是否需要检索[0-不需要，1-需要]
            			"showDesc": 0,//是否展示在介绍上；0-否 1-是
            			"valueSelect": "string",//可选值列表[用逗号分隔]
            			"valueType": 0//值类型[0-为单个值，1-可以选择多个值]
            		}]
            	}
            }



但是有一个问题，就是查询参数的时候，由于参数实体AttrEntity里面并没有代表所属分类和所属分组的字段，所以后端如果以AttrEntity传输前端的话，其实无法展现这两个字段
这将会导致前端无法展示所属分类和所属分组

因此使用vo
即view object
用于封装一些自定义的数据

可以接收前端一次发来的一些单独的字段，将其封装为一个对象放入后端进行处理
也可以将后端要返回给前端的一些单独字段封装为一个对象

我们定义VO：

            @Data
            public class AttrVO_WithGroupNameAndCatelogName {
            @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                private String catelogName;         //分类名
                private String groupName;           //分组名
            }

我们本来的AttrEntity本来就有catelogId，因此catelogName可以直接获取
但是AttrEntity里面没有groupId？那就只能通过属性和参数的关系表来获取了
先用attrId从关系表中得到groupId，再从group表内获取groupName
完美



AttrController中，定义接口：

            /**
            * 
            * 获取普通的规格参数，和共用者
            * 
            * 
            * */
            @RequestMapping("/base/list/{catelogId}")
            public R baseList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
                PageUtils page = attrService.queryPageBase(params,catelogId);
                return R.ok().put("page", page);
            }
            /**
             *
             * 获取普通的销售属性，和公用者
             *
             *
             * */
            @RequestMapping("/sale/list/{catelogId}")
            public R saleList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
                PageUtils page = attrService.queryPageSale(params,catelogId);
                return R.ok().put("page", page);
            }

service中定义方法：

            /**
             *
             *
             * @param params
             * @param catelogId
             * @return
             *
             * 查询所有的普通参数
             * 查询所有attr_type为0或2的参数
             *
             *
             */
            @Override
            public PageUtils queryPageBase(Map<String, Object> params, Integer catelogId) {
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()).or().eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));
        
                if(!StringUtils.isEmpty((String)params.get("key"))){
                    wrapper.and(obj->obj.like(
                            "attr_name",params.get("key")).or().like(
                            "value_select",params.get("key")));
                }
                if(catelogId!=0) {
                    wrapper.and(obj->obj.eq("catelog_id",catelogId));
                }
                IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);
        
        
                //接下来要添加上分组名和分类名
                //将查询好的AttrEntity们放入list
                List<AttrEntity> list=page.getRecords();
                List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
                    AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
                    //创建vo对象
                    BeanUtil.copyProperties(attrEntity, vo);
                    //将attrEntity的所有基本属性复制到vo
        
                    //随后要为vo添加分组名和分类名
                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    //通过已知的catelogId获取对应的category对象
                    String catelogName = categoryEntity.getName();
                    //通过category对象，直接获取catelogName
                    vo.setCatelogName(catelogName);
        
                    AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                    //通过已知的attrId，获取属性和参数的关系对象
                    if(attrAttrgroupRelationEntity!=null) {
                    //一定要判断，否则如果查到attr_id在关系表内不存在的话就会报错而无法允许
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                        //通过关系对象，获取对应的attrGroupId，并以此获取对应的attrGroup对象
                        String groupName = attrGroupEntity.getAttrGroupName();
                        //通过获取的attrGroup对象直接获取groupName
                        vo.setGroupName(groupName);
                    }
                    return vo;
                }).collect(Collectors.toList());
        
        
                PageUtils pageUtils=new PageUtils(page);
                pageUtils.setList(finale);
                return pageUtils;
            }
        
        
            /**
             *
        
             * @param params
             * @param catelogId
             * @return
             *
             * 查询所有的销售属性
             * 查询所有attr_type为1或2的参数
             *
             */
        
            @Override
            public PageUtils queryPageSale(Map<String, Object> params, Integer catelogId) {
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()).or().eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));
                if(!StringUtils.isEmpty((String)params.get("key"))){
                    wrapper.and(obj->obj.like(
                            "attr_name",params.get("key")).or().like(
                            "value_select",params.get("key")));
                }
                if(catelogId!= ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                    wrapper.and(obj->obj.eq("catelog_id",catelogId));
                }
                IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);
        
                //接下来要添加上分组名和分类名
                //将查询好的AttrEntity们放入list
                List<AttrEntity> list=page.getRecords();
                List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
                    AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
                    //创建vo对象
                    BeanUtil.copyProperties(attrEntity, vo);
                    //将attrEntity的所有基本属性复制到vo
        
                    //随后要为vo添加分组名和分类名
                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    //通过已知的catelogId获取对应的category对象
                    String catelogName = categoryEntity.getName();
                    //通过category对象，直接获取catelogName
                    vo.setCatelogName(catelogName);
                    return vo;
                }).collect(Collectors.toList());
                PageUtils pageUtils=new PageUtils(page);
                pageUtils.setList(finale);
                return pageUtils;
            }

匹配顺序是：
    
            1.先匹配类型，即attr_type，匹配是普通规格参数还是销售属性，抑或是共用者
            2.随后匹配key，且必须匹配key，但是只需要名字和可选值能匹配就行
            3.根据是否传入了特定的catelogId，如果有则匹配
            4.根据最终匹配规则和param参数体得到符合要求的attrEntity

            5.将attrEntity复制到vo中
            6.根据是销售属性还是普通参数来获取catelogName和groupName，销售属性不需要catelogName
            7.将catelogName和groupName放入vo中，将vo封装为集合，并封装为pageUtil
            8.返回pageUtil




结果可以查询到





=======================================================================================================================================================


























============== 商品服务VI：属性和参数级联新增 =====================================================================================================================

请求参数：

            http://localhost:10100/api/product/attr/save

请求体：

            


新增参数时，需要选择其所属属性，因此需要两个参数，属性id和自增得到的参数id，存储到attr attrGroup relation的表内
这个过程应该在新增参数时进行

返回的数据，刚好比AttrEntity多了一个attrGroupId，但是我们不想也最好不要直接在AttrEntity里新增一个成员变量
那我们直接定义vo：

            @Data
            public class AttrVO_WithAttrGroupId {
                @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                //和属性对应的attrGroupId
                private Long attrGroupId;
            }

我们要接收attrGroupId，就不能接收AttrEntity，而要接收AttrVO
因此在controller中更改：

            @RequestMapping("/save")
            @RequiresPermissions("product:attr:save")
            public R save(@RequestBody AttrVO_WithAttrGroupId attr){
                attrService.saveByAttrVO(attr);
                return R.ok();
            }

自定义的一个方法saveByAttrVO，该方法不仅要实现新增参数attr的存储，还要实现参数attr和属性attrGroup双方关系的存储
方法：

            @Override
            public void saveByAttrVO(AttrVO_WithAttrGroupId attrVO) {
                //通过AttrVO对象来存储新增参数
                //通过根据attrVO的attrGroupId来存储属性和参数的关系
                //1.存储attr
                AttrEntity attrEntity=new AttrEntity();
                BeanUtil.copyProperties(attrVO,attrEntity);     //将attrVO的同名数据复制到attrEntity，也即是除了attrGroupId
                this.save(attrEntity);                          //存储
                //2.存储关系
                Long attrGroupId=attrVO.getAttrGroupId();
                Long attrId=attrEntity.getAttrId();
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
                attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
                attrAttrgroupRelationEntity.setAttrId(attrId);
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }

这样也可以存储成功
注意前端在选择分组时，必须先选择分类，才能通过分类的categoryId检索有哪些关联了该categoryId的attrGroupId







说一下整个商品服务的架构：

        1.首先，商品分为四个部分：分类、品牌、属性、参数

        2.分类就是category，代表商品的类别，存储在category

        3.品牌就是brand，代表商品的品牌，存储在brand

        4.分类和品牌为多对多的关系，中间以一张category_brand_relation进行关系的存储

        5.属性就是attrGroup，代表商品的固有属性，存储在attr_group

        6.参数就是attr(attribute)，代表商品固有属性的参数，存储在attr

        7.属性和参数为多对多的关系，中间以一张attr_attrGroup_relation存储




======================================================================================================================================================================================







============== 商品服务VI：属性和参数级联删除 =====================================================================================================================



要实现的功能是，任意删除一个属性或参数时，其在关系表内的相关数据也要删除




















=======================================================================================================================================================================






============== 魔法值 =====================================================================================================================


消除魔法值
魔法值就是未经定义的常数，比如：

             if(catelogId!=0) 

中的0就是魔法值
我们把他改成一个定义的常量就不是魔法值了：

             if(catelogId!=ATTR_TYPE_BASE)

就不是魔法值了



在common包下创建一个表单，专门用于存放魔法值:

            public class ProductConstant {
                public enum AttrEnum {
                    ATTR_TYPE_BASE(1,"普通参数"),
                    ATTR_TYPE_SALE(0,"销售属性"),
                    ATTR_TYPE_BOTH(2,"二者皆是");
                    Integer code;
                    String msg;
                    AttrEnum(Integer code,String msg){
                        this.code=code;
                        this.msg=msg;
                    }
                    public Integer getCode() {
                        return code;
                    }
                    public void setCode(Integer code) {
                        this.code = code;
                    }
                    public String getMsg() {
                        return msg;
                    }
                    public void setMsg(String msg) {
                        this.msg = msg;
                    }
                }
            }

随后可以将参数里面的魔法值替换为表单中的常量，例如：

            QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()).or().eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode())).and(obj->obj.like(
                                "attr_name",params.get("key")).or().like(
                                "value_select",params.get("key"))
                );

将原本的0、2等变成了常量，这就消除了魔法值





=======================================================================================================================================================================














============== 商品服务VI：规格参数和销售属性的修改页面的回显 =====================================================================================================================

请求路径：

            http://localhost:10100/api/product/attr/info/{attrId}



修改时，分类和属性的路径不会回显

属性分组，向前端返回一个attrGroupId
分类，向前端返回一个Long数组，依次存储祖父、父、子的catelogId

定义一个AttrVO_WithPaths:

            @Data
            public class AttrVO_WithGroupIdAndPaths {
                @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                private Long attrGroupId;
                private Long[] catelogPath;
            }

注意传递到前端时，变量名要和之前的前端名字一致
在AttrController中：

            /**
             * 专供参数修改的页面
             * 查询参数
             * 传出的对象比AttrEntity多了Long类型的attrGroupId，和一个Long类型数组
             * 分别表示所属属性、所属类型的完整路径
             *
             * 因此要使用AttrVO_WithGroupIdAndPaths
             *
             *
             */
            @RequestMapping("/info/{attrId}")
            @RequiresPermissions("product:attr:info")
            public R info(@PathVariable("attrId") Long attrId){
                AttrVO_WithGroupIdAndPaths attr = attrService.getAttrWithGroupIdAndPath(attrId);
                return R.ok().put("attr", attr);
            }

定义方法：

            /**
             * @param attrId
             * @return
             *
             * 通过attrId查询信息（专供参数修改页面）
             * 查询的信息使用AttrVO_WithGroupIdAndPaths返回前端
             * 原型为AttrEntity，但是多了attrGroupId和path
             * 分别代表所属属性，和所属分类的完整路径
             *
             */
            @Override
            public AttrVO_WithGroupIdAndPaths getAttrWithGroupIdAndPath(Long attrId) {
                //思路是，先获取AttrEntity，将其复制到一个AttrVO_WithGroupIdAndPaths
                //然后通过attrId，直接查询catelogId，因为attrEntity里面自带了catelogId
                //之后直接调用方法获取完整路径
                //然后，如果不单独为销售属性，则从关系表中根据attr_id获取groupId
                AttrEntity attrEntity=this.getById(attrId);
                AttrVO_WithGroupIdAndPaths vo=new AttrVO_WithGroupIdAndPaths();
                BeanUtil.copyProperties(attrEntity,vo);
                //完成了数据的复制

                Long catelogId = vo.getCatelogId();
                Long[] path = getCategoryPath(catelogId);
                vo.setCatelogPath(path);
                //获取了catelogId的完整路径

                if(vo.getAttrType()!=ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                    Long attrGroupId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId();
                    vo.setAttrGroupId(attrGroupId);
                    //获取了attrGroupId
                }
                //如果不是销售属性，添加分组
                return vo;
            }

其中获取三级分类路径的方法：

            /**
             * @param id
             * @return
             *
             * 根据一个第三级的商品分类，获取其完整路径
             * 调用方法的时CategoryService
             *
             */
            
            public Long[] getCategoryPath(Long id) {
                Long[] pathF=new Long[3];
                //最多三级，因此数组长度为3
                pathF[2]=id;
                //最后一个数就是该元素（孙子）的id
                CategoryEntity son = categoryService.getById(id);
                //儿子对象
                pathF[1]=son.getParentCid();
                //数组第二个元素为儿子的id
                CategoryEntity father=categoryService.getById(son.getParentCid());
                //父亲对象
                pathF[0]=father.getParentCid();
                //数组第一个元素为父亲的id
                return pathF;
                //直接返回
            }

完成功能


=======================================================================================================================================================================














============== 商品服务VI：规格参数和销售属性的修改 =====================================================================================================================



修改时，会发现无法修改参数的分组
请求路径：

            http://localhost:10100/api/product/attr/update

请求体是一个完整的AttrEntity对象外加一个attrGroupId
因此使用AttrVO_WithAttrGroupId进行接收



在AttrController中：

            /**
             * @param attr
             * @return
             *
             * 更新参数
             * 同时要保存参数属性分组，即groupId
             * 但是AttrEntity内没有attrGroupId
             * 所以要接收AttrVO_WithAttrGroupId
             */
            @RequestMapping("/update")
            @RequiresPermissions("product:attr:update")
            public R update(@RequestBody AttrVO_WithAttrGroupId vo){
                attrService.updateAttrWithGroupId(vo);
        
                return R.ok();
            }

在AttrServiceImpl中定义方法：

            /**
             *
             * @param vo
             *
             * 修改参数信息，同时修改groupId
             * 也即是不仅更新一个AttrEntity，还要更新attr对应的groupId
             * 所以也需要用到attrAttrGroupRelationDao，调用其update方法
             *
             * 此外整个过程需要一同进行一同失败
             * 因此要加上事务
             * @Transictional
             *
             *
             */
            @Transactional
            @Override
            public void updateAttrWithGroupId(AttrVO_WithAttrGroupId vo) {
                //获取AttrEntity，用于保存
                AttrEntity attrEntity=new AttrEntity();
                BeanUtil.copyProperties(vo,attrEntity);
                this.updateById(attrEntity);
        
                //获取关系对象，并存入数据
                Long attrGroupId=vo.getAttrGroupId();
                Long attrId=vo.getAttrId();
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
                attrAttrgroupRelationEntity.setAttrId(attrId);
                attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        
                //判断，如果欲修改的id在关系表中不存在，则说明参数原本并未和任何属性进行关联，故说明该操作其实是新增关系操作
                //若存在则为单词的修改关系操作
                //判断关系是否存在，使用selectCount方法
                Long count=attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",vo.getAttrId()));
                if(count!=0) {
                    //如果已存在该关系的话，修改
                    attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
                }
                else {
                    //若不存在该关系，新增
                    attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
                }
            }

测试结果也无误



===========================================================================================================================================================





============== 商品服务VII：属性与参数间的关联 =====================================================================================================================


属性与参数之间的关联
查询与一个属性进行关联的所有属性
请求路径

            http://localhost:10100/api/product/attrgroup/{attrGroupId}/noattr/relation

返回值：

            {
                "msg": "success",
                "code": 0,
                "data": [
                    {
                        "attrId": 4,
                        "attrName": "aad",
                        "searchType": 1,
                        "valueType": 1,
                        "icon": "qq",
                        "valueSelect": "v;q;w",
                        "attrType": 1,
                        "enable": 1,
                        "catelogId": 225,
                        "showDesc": 1
                    }
                ]
            }

注意数据的标识为data
以属性为视角出发
在AttrGroupController中：

            /**查找和属性发生关联的所有参数
             * 根据属性id，从关系表内获取所有与之关联的参数
             *
             * @param attrGroupId
             * @return
             *
             *
             *
             */
            @RequestMapping("/{attrGroupId}/attr/relation")
            public R listAttrRelation(@PathVariable Integer attrGroupId){
                List<AttrEntity> page=attrGroupService.getAttrRelatedWithGroup(attrGroupId);
        
                return R.ok().put("data",page);
            }

自定义方法getAttrRelatedWithGroup，标识查询与group关联的attr：

            /**
             *
             * @param attrGroupId
             * @return
             *
             * 查询所有和属性已关联的参数
             * 通过attrGroupId
             *
             * 返回值为AttrEntity的List集合
             *
             *
             */
            @Override
            public List<AttrEntity> getAttrRelatedWithGroup(Integer attrGroupId) {
                //思路是，先在关系表中查询所有相关对象relations，封装
                // 再遍历relations遍历获取attrId，不需要封装，直接在遍历中就获取attr集合封装
                
                List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
                //获取了所有的关系对象
                List<AttrEntity> attrS=new ArrayList<>();
                for(AttrAttrgroupRelationEntity entity:relations){
                    attrS.add(attrDao.selectById(entity.getAttrId()));
                }
                //获取了所有的attrIds
                
                return attrS;
            }

完成功能



===================================================================================================================================================================================================








============== 商品服务VII：查询与属性未关联的参数 =====================================================================================================================


请求路径：

            http://localhost:10100/api/product/attrgroup/{attrGroupId}/noattr/relation

发出请求后，返回与该attrGroupId未关联的所有参数

AttrGroupController：

            /**查找和属性未发生关联的所有参数
             * 根据属性id，从关系表内获取所有未与之关联的参数
             *
             * @param attrGroupId
             * @return R
             *
             */
            @RequestMapping("{attrGroupId}/noattr/relation")
            public R listAttrNOTRelation(@RequestParam Map<String, Object> params,@PathVariable Integer attrGroupId){
                PageUtils page=attrGroupService.getAttrRelatedNOTWithGroup(params,attrGroupId);
                return R.ok().put("page",page);
            }

自定义方法getAttrRelatedNOTWithGroup：

            /**
             *
             * @param attrGroupId
             * @return
             *
             * 查询所有和属性未发生关联关联的参数
             *
             * 专供属性的新增关联界面
             *
             * 返回值为AttrEntity的Page集合
             *
             *
             */
            @Override
            public PageUtils getAttrRelatedNOTWithGroup(@RequestParam Map<String, Object> params,Integer attrGroupId) {
                //首先得到groupEntity，别问先做
                //思路是，先用关系的dao，获取所有与groupId关联的关系对象，封装为relations
                //遍历关系对象的集合，attrId，封装成集合List
                //然后直接使用attrDao，根据param直接获取所有参数，这些参数的catelogId要和groupEntity的catelogId一致
                //在所有参数中剔除attrId在集合List内的参数，就得到了所有未关联的参数
                //0.获取该groupId对应的group实体
                AttrGroupEntity groupEntity=this.getById(attrGroupId);
                //1.获取所有与groupId关联的关系对象
                List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
                //2.封装attrId
                List<Long> attrIds=new ArrayList<>();
                for(AttrAttrgroupRelationEntity entity:relations){
                    attrIds.add(entity.getAttrId());
                }
                //3.获取全部参数，前提是要处于同一分类
                //4.剔除处于attrIds的所有attrId，
                // 但是要获取的是一个wrapper，也即是一个方案，后面直接根据该方案查询参数
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().eq("catelog_id",groupEntity.getCatelogId()).notIn("attr_id",attrIds);
                //4.1如果有模糊关键字key，还有匹配模糊搜索
                String key=(String) params.get("key");
                if(!StringUtils.isEmpty(key)){
                    wrapper.and((obj)->{obj.like("attr_id",key).or().like("attr_name",key);});
                }
                //5.根据params和wrapper，获取page对象
                IPage page=attrDao.selectPage(new Query<AttrEntity>().getPage(params),wrapper);
                PageUtils finale=new PageUtils(page);
                return finale;
            }

结果是可以查询得到

============================================================================================================================================================================================








============== 商品服务VII：新增属性与参数的关联 =====================================================================================================================


查询是可以查询了，但是没法新增

请求路径：

            http://localhost:10100/api/product/attrgroup/attr/relation

请求参数：

            [{attrId: 9, attrGroupId: 1}, {attrId: 10, attrGroupId: 1}]

是一个实体的集合
可以直接使用AttrAttrGroupRelation对象接收数据
但是最好定义一个完全相同的vo进行接收，AttrAttrGroupVO_JustReceiveData：

            @Data
            public class AttrAttrGroupVO_JustReceiveData {
                private static final long serialVersionUID = 1L;
                @TableId
                private Long id;
                private Long attrId;
                private Long attrGroupId;
                private Integer attrSort;
            }
    
AttrGroupController:

            /**
             *
             * @param vos
             * @return
             *
             *
             * 接收AttrAttrGroupVO_JustReceiveData
             * 根据其中的数据增加属性和参数的关系
             *
             *
             */
        
            @RequestMapping("attr/relation")
            public R addRelation(@RequestBody List<AttrAttrGroupVO_JustReceiveData> vos){
                attrGroupService.addRelation(vos);
                return R.ok();
            }

自定义方法addRelation：

            /**
             *
             * @param vos
             *
             * 接收的是vo的集合
             * 根据vo的集合内数据新增属性和参数关系
             *
             * 因此注入了attrAttrGroupRelationDao
             *
             */
        
            @Override
            public void addRelation(List<AttrAttrGroupVO_JustReceiveData> vos) {
                //思路是，遍历vos，创建关系对象传入每个vo的数据
                //将每个数据根据关系的dao，进行存入
                for(AttrAttrGroupVO_JustReceiveData vo:vos){
                    AttrAttrgroupRelationEntity relation=new AttrAttrgroupRelationEntity();
                    BeanUtils.copyProperties(vo,relation);
                    attrAttrgroupRelationDao.insert(relation);
                }
            }

            
可以了

======================================================================================================







============== 用户服务VIII：查询用户等级信息 =====================================================================================================================


在商品维护、























