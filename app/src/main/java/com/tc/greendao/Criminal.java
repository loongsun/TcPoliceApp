package com.tc.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhao on 17-9-9.
 */

@Entity
public class Criminal {

    @Id
    private Long id;
    @Property
    private String handleNumber;//案件受理号
    @Property
    private String number;//案件编号
    @Property
    private String type;//案件类型
    @Property
    private String area;//作案区域
    @Property
    private int isLife; //是否命案
    @Property
    private int isCrime;//是否刑案
    @Property
    private String startTime;//案件开始时间
    @Property
    private String endTime;//发案终止时间
    @Property
    private String place;//发案地点
    @Property
    private String solveTime;//破案时间
    @Property
    private String inquestPlace;//勘验地点
    @Property
    private String inquestReason;//勘验事由
    @Property
    private String caseProcess;//案发过程
    @Property
    private String protectorName;//保护人
    @Property
    private String protectorComany;//保护人单位
    @Property
    private String protectMeasures;//保护措施
    @Property
    private String protectTime;//保护时间
    @Property
    private String spotCondition;//现场条件
    @Property
    private String weatherCondition;//天气状况
    @Property
    private String lightCondition;//光照条件
    @Property
    private String spotConduct;//现场指挥
    @Property
    private String inquesterName;//勘验检查人员
    @Property
    private String spotPeople;//其他到场人员
    @Property
    private String spotLeft;//现场遗留物
    @Property
    private String inquestCondition;//勘验情况
    @Property
    private String victimName;//被害人/报案人
    @Property
    private String lossGoods;//损失物品
    @Property
    private int recordTime;//录像分钟
    @Property
    private String injury;//伤亡情况
    @Property
    private String witness;//见证人
    @Generated(hash = 775105524)
    public Criminal(Long id, String handleNumber, String number, String type,
            String area, int isLife, int isCrime, String startTime, String endTime,
            String place, String solveTime, String inquestPlace,
            String inquestReason, String caseProcess, String protectorName,
            String protectorComany, String protectMeasures, String protectTime,
            String spotCondition, String weatherCondition, String lightCondition,
            String spotConduct, String inquesterName, String spotPeople,
            String spotLeft, String inquestCondition, String victimName,
            String lossGoods, int recordTime, String injury, String witness) {
        this.id = id;
        this.handleNumber = handleNumber;
        this.number = number;
        this.type = type;
        this.area = area;
        this.isLife = isLife;
        this.isCrime = isCrime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.solveTime = solveTime;
        this.inquestPlace = inquestPlace;
        this.inquestReason = inquestReason;
        this.caseProcess = caseProcess;
        this.protectorName = protectorName;
        this.protectorComany = protectorComany;
        this.protectMeasures = protectMeasures;
        this.protectTime = protectTime;
        this.spotCondition = spotCondition;
        this.weatherCondition = weatherCondition;
        this.lightCondition = lightCondition;
        this.spotConduct = spotConduct;
        this.inquesterName = inquesterName;
        this.spotPeople = spotPeople;
        this.spotLeft = spotLeft;
        this.inquestCondition = inquestCondition;
        this.victimName = victimName;
        this.lossGoods = lossGoods;
        this.recordTime = recordTime;
        this.injury = injury;
        this.witness = witness;
    }
    @Generated(hash = 1376794522)
    public Criminal() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHandleNumber() {
        return this.handleNumber;
    }
    public void setHandleNumber(String handleNumber) {
        this.handleNumber = handleNumber;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public int getIsLife() {
        return this.isLife;
    }
    public void setIsLife(int isLife) {
        this.isLife = isLife;
    }
    public int getIsCrime() {
        return this.isCrime;
    }
    public void setIsCrime(int isCrime) {
        this.isCrime = isCrime;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getPlace() {
        return this.place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getSolveTime() {
        return this.solveTime;
    }
    public void setSolveTime(String solveTime) {
        this.solveTime = solveTime;
    }
    public String getInquestPlace() {
        return this.inquestPlace;
    }
    public void setInquestPlace(String inquestPlace) {
        this.inquestPlace = inquestPlace;
    }
    public String getInquestReason() {
        return this.inquestReason;
    }
    public void setInquestReason(String inquestReason) {
        this.inquestReason = inquestReason;
    }
    public String getCaseProcess() {
        return this.caseProcess;
    }
    public void setCaseProcess(String caseProcess) {
        this.caseProcess = caseProcess;
    }
    public String getProtectorName() {
        return this.protectorName;
    }
    public void setProtectorName(String protectorName) {
        this.protectorName = protectorName;
    }
    public String getProtectorComany() {
        return this.protectorComany;
    }
    public void setProtectorComany(String protectorComany) {
        this.protectorComany = protectorComany;
    }
    public String getProtectMeasures() {
        return this.protectMeasures;
    }
    public void setProtectMeasures(String protectMeasures) {
        this.protectMeasures = protectMeasures;
    }
    public String getProtectTime() {
        return this.protectTime;
    }
    public void setProtectTime(String protectTime) {
        this.protectTime = protectTime;
    }
    public String getSpotCondition() {
        return this.spotCondition;
    }
    public void setSpotCondition(String spotCondition) {
        this.spotCondition = spotCondition;
    }
    public String getWeatherCondition() {
        return this.weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    public String getLightCondition() {
        return this.lightCondition;
    }
    public void setLightCondition(String lightCondition) {
        this.lightCondition = lightCondition;
    }
    public String getSpotConduct() {
        return this.spotConduct;
    }
    public void setSpotConduct(String spotConduct) {
        this.spotConduct = spotConduct;
    }
    public String getInquesterName() {
        return this.inquesterName;
    }
    public void setInquesterName(String inquesterName) {
        this.inquesterName = inquesterName;
    }
    public String getSpotPeople() {
        return this.spotPeople;
    }
    public void setSpotPeople(String spotPeople) {
        this.spotPeople = spotPeople;
    }
    public String getSpotLeft() {
        return this.spotLeft;
    }
    public void setSpotLeft(String spotLeft) {
        this.spotLeft = spotLeft;
    }
    public String getInquestCondition() {
        return this.inquestCondition;
    }
    public void setInquestCondition(String inquestCondition) {
        this.inquestCondition = inquestCondition;
    }
    public String getVictimName() {
        return this.victimName;
    }
    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }
    public String getLossGoods() {
        return this.lossGoods;
    }
    public void setLossGoods(String lossGoods) {
        this.lossGoods = lossGoods;
    }
    public int getRecordTime() {
        return this.recordTime;
    }
    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }
    public String getInjury() {
        return this.injury;
    }
    public void setInjury(String injury) {
        this.injury = injury;
    }
    public String getWitness() {
        return this.witness;
    }
    public void setWitness(String witness) {
        this.witness = witness;
    }






    



}
