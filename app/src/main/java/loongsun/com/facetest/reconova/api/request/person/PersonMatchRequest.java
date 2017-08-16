/*
 * Copyright (c) 2017.  瑞为信息版权所有
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
 * 人脸 1：N 比对搜索
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
     * 需要搜索的人脸库id，多个库以半角逗号分割
     */
    private List<String> dbIds;

    /**
     * 人员姓名，支持模糊搜索
     */
    private String name;

    /**
     * 身份证号，支持模糊搜索
     */
    private String idCard;

    /**
     * 人脸特征 base64字符串
     */
    private String feature;

    /**
     * 年龄段搜索，上限
     */
    private Integer maxAge;

    /**
     * 年龄段搜索，下限
     */
    private Integer minAge;

    /**
     * 性别
     *
     * @see
     */
    private Integer gender;

    /**
     * 分页页码，默认1
     */
    private Integer page=0;

    /**
     * 分页大小,默认10
     */
    private Integer size=10;

    /**
     * 相似度阈值 1~100
     */
    private Double similarity=70d;

    /**
     * 搜索记录限制 默认30，最大300
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
            throw new IllegalParamException("缺少dbIds字段");
        }
        if (feature == null || feature.trim().isEmpty()) {
            throw new IllegalParamException("缺少feature字段");
        }
        if (similarity == null) {
            throw new IllegalParamException("缺少similarity字段");
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
     * 通过list 获取 dbds 以逗号分割的字符串
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
