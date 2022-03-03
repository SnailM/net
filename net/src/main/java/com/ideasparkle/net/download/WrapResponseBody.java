package com.ideasparkle.net.download;

import androidx.annotation.Nullable;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;

public class WrapResponseBody extends ResponseBody {

    private ResponseBody orgRespBody;
    private int intervalTime;
    private String key;
    private IDownloadListener IDownloadListener;
    private BufferedSource bufferedSource;

    public WrapResponseBody(ResponseBody orgRespBody, int intervalTime, @Nullable String key, IDownloadListener IDownloadListener) {
        this.orgRespBody = orgRespBody;
        this.intervalTime = intervalTime;
        this.key = key;
        this.IDownloadListener = IDownloadListener;
    }

    @Override
    public MediaType contentType() {
        return orgRespBody.contentType();
    }

    @Override
    public long contentLength() {
        return orgRespBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(getSource(orgRespBody.source()));
        }
        return bufferedSource;
    }

    private Source getSource(Source source) {
        return new ForwardingSource(source) {
            private long receivedByte = 0L;//已读数据量
            private long lastRefreshTime = 0L;//最后一次刷新时间

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long readCount = super.read(sink, byteCount);
                receivedByte += readCount != -1 ? readCount : 0;
                long curTime = System.currentTimeMillis();
                if (IDownloadListener != null && (curTime - lastRefreshTime > intervalTime
                        || readCount == -1 || receivedByte == contentLength())) {
                    IDownloadListener.downloadProgress(key, receivedByte, contentLength(), readCount == -1);
                    lastRefreshTime = curTime;
                }
                return readCount;
            }

            @Override
            public Timeout timeout() {
                return super.timeout();
            }
        };
    }
}
