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
    private String handleNumber;//妗堜欢鍙楃悊鍙?
    @Property
    private String number;//妗堜欢缂栧彿
    @Property
    private String type;//妗堜欢绫诲瀷
    @Property
    private String area;//浣滄鍖哄煙
    @Property
    private int isLife; //鏄惁鍛芥
    @Property
    private int isCrime;//鏄惁鍒戞
    @Property
    private String startTime;//妗堜欢寮?濮嬫椂闂?
    @Property
    private String endTime;//鍙戞缁堟鏃堕棿
    @Property
    private String place;//鍙戞鍦扮偣
    @Property
    private String solveTime;//鐮存鏃堕棿
    @Property
    private String inquestPlace;//鍕橀獙鍦扮偣
    @Property
    private String inquestReason;//鍕橀獙浜嬬敱
    @Property
    private String caseProcess;//妗堝彂杩囩▼
    @Property
    private String protectorName;//淇濇姢浜?
    @Property
    private String protectorComany;//淇濇姢浜哄崟浣?
    @Property
    private String protectMeasures;//淇濇姢鎺柦
    @Property
    private String protectTime;//淇濇姢鏃堕棿
    @Property
    private String spotCondition;//鐜板満鏉′欢
    @Property
    private String weatherCondition;//澶╂皵鐘跺喌
    @Property
    private String lightCondition;//鍏夌収鏉′欢
    @Property
    private String spotConduct;//鐜板満鎸囨尌
    @Property
    private String inquesterName;//鍕橀獙妫?鏌ヤ汉鍛?
    @Property
    private String spotPeople;//鍏朵粬鍒板満浜哄憳
    @Property
    private String spotLeft;//鐜板満閬楃暀鐗?
    @Property
    private String inquestCondition;//鍕橀獙鎯呭喌
    @Property
    private String victimName;//琚浜?/鎶ユ浜?
    @Property
    private String lossGoods;//鎹熷け鐗╁搧
    @Property
    private String recordTime;//褰曞儚鍒嗛挓
    @Property
    private String injury;//浼や骸鎯呭喌
    @Property
    private String witness;//瑙佽瘉浜?
    @Generated(hash = 1825429760)
    public Criminal(Long id, String handleNumber, String number, String type,
            String area, int isLife, int isCrime, String startTime, String endTime,
            String place, String solveTime, String inquestPlace,
            String inquestReason, String caseProcess, String protectorName,
            String protectorComany, String protectMeasures, String protectTime,
            String spotCondition, String weatherCondition, String lightCondition,
            String spotConduct, String inquesterName, String spotPeople,
            String spotLeft, String inquestCondition, String victimName,
            String lossGoods, String recordTime, String injury, String witness) {
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
    public String getRecordTime() {
        return this.recordTime;
    }
    public void setRecordTime(String recordTime) {
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
