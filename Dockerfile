#  使用 Java 21 基础镜像
# eclipse-temurin 是一个高质量、社区维护的 OpenJDK 发行版
# alpine 版本体积更小
FROM eclipse-temurin:21-jdk-alpine

# 设置工作目录，后续的操作都会在这个目录下进行
WORKDIR /app

#  将构建好的 JAR 文件从本地复制到镜像中
#    将 'web3j-1.0-SNAPSHOT.jar' 替换成您 bootJar 任务生成的实际文件名
#    将其重命名为 app.jar 是一个好习惯，可以简化后续的命令
COPY build/libs/web3j-1.0-SNAPSHOT.jar app.jar

# 声明容器将监听 8080 端口 (这与 Spring Boot 默认端口一致)
EXPOSE 8080

# 3. 设置容器启动时要执行的命令
#    使用 ENTRYPOINT 来定义主命令，这是最佳实践
ENTRYPOINT ["java", "-jar", "app.jar"]