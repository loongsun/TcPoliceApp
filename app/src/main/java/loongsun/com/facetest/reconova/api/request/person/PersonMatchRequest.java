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

import java.util.List;

/**
 * ���� 1��N �ȶ�����
 */
public class PersonMatchRequest extends FcsApiRequest {

    public PersonMatchRequest(FcsApiClient client) {
        super(client);
    }

    public PersonMatchRequest(FcsApiClient client, List<String> dbIds, String name, String idCard, String feature, Integer maxAge, Integer minAge, Integer gender, Integer page, Integer size, Double similarity, Integer top) {
        super(client);
        this.dbIds = dbIds;
        this.name = name;
        this.idCard = idCard;
        this.feature = feature;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.gender = gender;
        this.page = page;
        this.size = size;
        this.similarity = similarity;
        this.top = top;
    }

    /**
     * ��Ҫ������������id��������԰�Ƕ��ŷָ�
     */
    private List<String> dbIds;

    /**
     * ��Ա������֧��ģ������
     */
    private String name;

    /**
     * ���֤�ţ�֧��ģ������
     */
    private String idCard;

    /**
     * �������� base64�ַ���
     */
    private String feature;

    /**
     * ���������������
     */
    private Integer maxAge;

    /**
     * ���������������
     */
    private Integer minAge;

    /**
     * �Ա�
     *
     * @see
     */
    private Integer gender;

    /**
     * ��ҳҳ�룬Ĭ��1
     */
    private Integer page=0;

    /**
     * ��ҳ��С,Ĭ��10
     */
    private Integer size=10;

    /**
     * ���ƶ���ֵ 1~100
     */
    private Double similarity=70d;

    /**
     * ������¼���� Ĭ��30�����300
     */
    private Integer top=30;

    public List<String> getDbIds() {
        return dbIds;
    }

    public void setDbIds(List<String> dbIds) {
        this.dbIds = dbIds;
    }

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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    @Override
    public String getApiMethod() {
        return "POST";
    }

    @Override
    public MediaType getApiRequestContentType() {
        return MediaType.parse(Constants.MediaTypes.X_WWW_FORM_URLENCODED);
    }

    @Override
    public String getApiEndpoint() {
        return API_PREFIX + "person/match";
    }

    @Override
    public RequestBody getApiRequestBody() {
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("feature", this.feature)
                .add("dbIds", listToString())
                .add("page", Integer.toString(this.page))
                .add("size", Integer.toString(this.size))
                .add("similarity", Double.toString(this.similarity))
                .add("top", Integer.toString(this.top));

        if (this.name != null && !this.name.trim().isEmpty()) {
            bodyBuilder.add("name", this.name);
        }
        if (this.idCard != null && !this.idCard.trim().isEmpty()) {
            bodyBuilder.add("idCard", this.idCard);
        }
        if (this.maxAge != null) {
            bodyBuilder.add("maxAge", Integer.toString(this.maxAge));
        }
        if (this.minAge != null) {
            bodyBuilder.add("maxAge", Integer.toString(this.maxAge));
        }
        if (this.gender != null) {
            bodyBuilder.add("gender", Integer.toString(this.gender));
        }

        RequestBody body = bodyBuilder.build();
        return body;
    }

    @Override
    public void validateAndSetDefaults() throws IllegalParamException {
        if (dbIds == null || dbIds.isEmpty()) {
            throw new IllegalParamException("ȱ��dbIds�ֶ�");
        }
        if (feature == null || feature.trim().isEmpty()) {
            throw new IllegalParamException("ȱ��feature�ֶ�");
        }
        if (similarity == null) {
            throw new IllegalParamException("ȱ��similarity�ֶ�");
        }
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        if (top == null) {
            top = 30;
        }

    }

    /**
     * ͨ��list ��ȡ dbds �Զ��ŷָ���ַ���
     *
     * @return
     */
//    private String getDbIdString() {
//       return String.join(",", this.dbIds);
//        return null;
//    }
    public   String listToString(){
        if (dbIds==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : dbIds) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
