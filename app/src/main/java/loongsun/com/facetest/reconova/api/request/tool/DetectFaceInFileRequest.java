/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.request.tool;


import loongsun.com.facetest.reconova.api.Constants;
import loongsun.com.facetest.reconova.api.exception.IllegalParamException;
import loongsun.com.facetest.reconova.api.request.FcsApiRequest;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

/**
 * 上传人脸图片 检测人脸
 * <p>
 * /api/v1/tool/detectFace
 */
public class DetectFaceInFileRequest extends FcsApiRequest {

    private static final String MEDIA_TYPE_JPEG = "image/jpeg";

    public DetectFaceInFileRequest(FcsApiClient client) {
        super(client);
    }

    public DetectFaceInFileRequest(FcsApiClient client, File file) {
        super(client);
        this.file = file;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public String getApiMethod() {
        return "POST";
    }

    public MediaType getApiRequestContentType() {
        return MediaType.parse(Constants.MediaTypes.MULTIPART_FORM_DATA);
    }

    public String getApiEndpoint() {
        return API_PREFIX + "tool/detectFaceInFile";
    }

    public RequestBody getApiRequestBody() {
        RequestBody body = new MultipartBody.Builder()
                .setType(getApiRequestContentType())
                .addFormDataPart(
                        "file",
                        this.file.getName(),
                        RequestBody.create(MediaType.parse(MEDIA_TYPE_JPEG), this.file)
                )
                .build();

        return body;
    }
    @Override
    public void validateAndSetDefaults() throws IllegalParamException {
        if (this.file == null || !this.file.exists()) {
            throw new IllegalParamException("文件不存在");
        }
    }
}
