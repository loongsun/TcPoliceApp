/*
 * Copyright (c) 2017.  ��Ϊ��Ϣ��Ȩ����
 */

package loongsun.com.facetest.reconova.api.request.person;

import loongsun.com.facetest.reconova.api.Constants;
import loongsun.com.facetest.reconova.api.exception.IllegalParamException;
import loongsun.com.facetest.reconova.api.request.FcsApiRequest;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonRegisterRequest extends FcsApiRequest {

    public PersonRegisterRequest(FcsApiClient client) {
        super(client);
    }

    public PersonRegisterRequest(FcsApiClient client, String name, String idCard, int gender, long dbId, String imageData, Date birthday, Date validTo) {
        super(client);
        this.name = name;
        this.idCard = idCard;
        this.gender = gender;
        this.dbId = dbId;
        this.imageData = imageData;
        this.birthday = birthday;
        this.validTo = validTo;
    }

    /**
     * ��Ա����
     */
    private String name;

    /**
     * ֤����
     */
    private String idCard;

    /**
     * �Ա�(��ѡ)
     */
    private Integer gender;

    /**
     * ������id
     */
    private Long dbId;
    /**
     * ����ͼƬ (ֻ֧��jpeg) base64����
     */
    private String imageData;

    /**
     * ��������(��ѡ) yyyy-MM-dd
     */
    private Date birthday;

    /**
     * ��Ա��Ч������ yyyy-MM-dd
     */
    private Date validTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getApiMethod() {
        return "POST";
    }

    public MediaType getApiRequestContentType() {
        return MediaType.parse(Constants.MediaTypes.X_WWW_FORM_URLENCODED);
    }

    public String getApiEndpoint() {
        return API_PREFIX + "person/Base64";
    }

    @Override
    public void validateAndSetDefaults() throws IllegalParamException {
        if (this.dbId == null) {
            throw new IllegalParamException("ȱ��dbId�ֶ�");
        }
        if (this.imageData == null) {
            throw new IllegalParamException("ȱ��imageData�ֶ�");
        }
        if (this.name == null) {
            throw new IllegalParamException("ȱ��name�ֶ�");
        }
        if (this.validTo == null) {
            throw new IllegalParamException("ȱ��validTo�ֶ�");
        }
        if (this.gender == null) {
            gender = -2;
        }
    }

    public RequestBody getApiRequestBody() {

        String validToString = new SimpleDateFormat("yyyy-MM-dd").format(this.validTo);

        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("dbId", Long.toString(this.dbId))
                .add("imageData", this.imageData)
                .add("validTo", validToString)
                .add("name", this.name);

        if (this.idCard != null && !this.idCard.trim().isEmpty()) {
            bodyBuilder.add("idCard", this.idCard);
        }
        if (this.gender != null && this.gender > -1 && this.gender < 2) {
            bodyBuilder.add("gender", Integer.toString(this.gender));
        }
        if (this.birthday != null) {
            String birthdayString = new SimpleDateFormat("yyyy-MM-dd").format(this.birthday);
            bodyBuilder.add("birthday", birthdayString);
        }

        RequestBody body = bodyBuilder.build();
        return body;
    }
}
