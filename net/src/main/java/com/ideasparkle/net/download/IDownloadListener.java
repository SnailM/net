package com.ideasparkle.net.download;

public interface IDownloadListener {
    void downloadProgress(String key, long progress, long max, boolean isDown);
}
