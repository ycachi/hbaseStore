package com.asiainfo.ftp.pool;


/**
 * FTPClient配置类，封装了FTPClient的相关配置
 *
 * @author
 */
public class FTPClientConfigure
{
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean passiveMode = false;
    private String encoding;
    private int connectTimeout = 60000;
    private long keepAliveTimeOut = 60;
    private int keepAliveReplayTimeOut = 60000;
    private int bufferSize = 1024;
    private int threadNum;
    private int transferFileType;
    private boolean renameUploaded;
    private int retryTimes;

    @Override
    public String toString() {
        return "FTPClientConfigure{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passiveMode='" + passiveMode + '\'' +
                ", encoding='" + encoding + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", keepAliveTimeOut=" + keepAliveTimeOut +
                ", keepAliveReplayTimeOut=" + keepAliveReplayTimeOut +
                ", bufferSize=" + bufferSize +
                ", threadNum=" + threadNum +
                ", transferFileType=" + transferFileType +
                ", renameUploaded=" + renameUploaded +
                ", retryTimes=" + retryTimes +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getKeepAliveTimeOut() {
        return keepAliveTimeOut;
    }

    public void setKeepAliveTimeOut(long keepAliveTimeOut) {
        this.keepAliveTimeOut = keepAliveTimeOut;
    }

    public int getKeepAliveReplayTimeOut() {
        return keepAliveReplayTimeOut;
    }

    public void setKeepAliveReplayTimeOut(int keepAliveReplayTimeOut) {
        this.keepAliveReplayTimeOut = keepAliveReplayTimeOut;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getTransferFileType() {
        return transferFileType;
    }

    public void setTransferFileType(int transferFileType) {
        this.transferFileType = transferFileType;
    }

    public boolean isRenameUploaded() {
        return renameUploaded;
    }

    public void setRenameUploaded(boolean renameUploaded) {
        this.renameUploaded = renameUploaded;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
