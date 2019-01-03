package top.huzhurong.web.support.upload;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/28
 */
public interface MultipartFile extends InputStreamSource {
    /**
     * Return the name of the parameter in the multipart form.
     *
     * @return the name of the parameter (never {@code null} or empty)
     */
    String getName();

    /**
     * @return Return the original filename in the client's filesystem.
     */
    String getOriginalFilename();

    /**
     * @return the content type, or {@code null} if not defined
     * (or no file has been chosen in the multipart form)
     */
    String getContentType();

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    boolean isEmpty();

    /**
     * Return the size of the file in bytes.
     *
     * @return the size of the file, or 0 if empty
     */
    long getSize();
}
