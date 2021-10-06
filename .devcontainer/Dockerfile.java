FROM tomcat:9-jdk16-corretto as tomcat
FROM mcr.microsoft.com/vscode/devcontainers/java:0-16 as java
COPY --from=tomcat /usr/local/tomcat/ /usr/local/tomcat/