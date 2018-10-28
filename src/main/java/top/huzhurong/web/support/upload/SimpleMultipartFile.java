package top.huzhurong.web.support.upload;

import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/28
 */
public class SimpleMultipartFile implements MultipartFile {

    private FileUpload fileUpload;

    public SimpleMultipartFile() {

    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    @Override
    public String getName() {
        return this.fileUpload.getName();
    }

    @Override
    public String getOriginalFilename() {
        return fileUpload.getFilename();
    }

    @Override
    public String getContentType() {
        return fileUpload.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return fileUpload == null;
    }

    @Override
    public long getSize() {
        if (this.fileUpload == null) {
            return 0;
        }
        return fileUpload.length();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(fileUpload.getFile());
    }
}
