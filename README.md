# pisces

cross platform file transfer client/server

Java跨平台的大文件传输demo，基于Netty 的FileRegion模式和ChunkedFile模式，其中ChunkedFile 使用了SSL的安全传输方式。

# 编译打包
```
mvn clean package
```

target/pisces-bin.zip
```

pisces
├── bin
│   ├── client.bat  # windows 运行client（已测试）
│   ├── client.sh
│   ├── server.bat
│   └── server.sh  # 启动server的脚步文件（已测试）
├── conf
│   ├── config.properties   # 配置文件
│   ├── logback.xml         # 日志配置
│   └── ssl             
│       ├── client          # client的数字证书
│       │   ├── ca.crt
│       │   ├── ca.key
│       │   ├── client.crt
│       │   ├── client.csr
│       │   ├── client.key
│       │   └── pkcs8_client.key
│       └── server          # server的数字证书
│           ├── ca.crt
│           ├── ca.key
│           ├── pkcs8_server.key
│           ├── server.crt
│           ├── server.csr
│           └── server.key
└── lib
    ├── animal-sniffer-annotations-1.18.jar
    ├── checker-compat-qual-2.5.5.jar
    ├── commons-beanutils-1.9.3.jar
    ├── commons-codec-1.15.jar
    ├── commons-collections-3.2.2.jar
    ├── commons-configuration2-2.2.jar
    ├── commons-lang3-3.9.jar
    ├── commons-logging-1.2.jar
    ├── error_prone_annotations-2.3.3.jar
    ├── failureaccess-1.0.1.jar
    ├── fast-md5-2.7.1.jar
    ├── fastjson-1.2.29.jar
    ├── gson-2.8.6.jar
    ├── guava-28.1-android.jar
    ├── j2objc-annotations-1.3.jar
    ├── jsr305-3.0.2.jar
    ├── listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar
    ├── logback-classic-1.2.3.jar
    ├── logback-core-1.2.3.jar
    ├── lombok-1.18.16.jar
    ├── netty-all-4.1.42.Final.jar
    ├── pisces-1.0.jar
    ├── protobuf-java-3.11.0.jar
    ├── protobuf-java-util-3.11.0.jar
    └── slf4j-api-1.7.30.jar

6 directories, 43 files


```

#配置server的文件下载目录和client的保存目录

```

## ------------------ COMMON
## true - chunkedFile mode, false - FileRegion mode
ssl.enabled=false

## ----------------- SERVER
## server running on which ip and port
server.port=8899
server.host=192.168.0.100

# server source dir
server.dir=/Users/bbstone/Downloads/pisces/

## ----------------- CLIENT
# client target dir
#client.dir=d:\\mac\\myfiles\\


```



# 启动server
```
# cd pisces
# ./server.sh
```

# 启动client
```
# cd pisces
# client.bak
```

