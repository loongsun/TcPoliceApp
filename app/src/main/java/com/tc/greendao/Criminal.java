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
    private String handleNumber;
    @Property
    private String number;
    @Property
    private String type;
    @Property
    private String area;
    @Property
    private int isLife;
    @Property
    private int isCrime;
    @Property
    private String startTime;
    @Property
    private String endTime;
    @Property
    private String place;
    @Property
    private String solveTime;
    @Property
    private String inquestPlace;
    @Property
    private String inquestReason;
    @Property
    private String caseProcess;
    @Property
    private String protectorName;
    @Property
    private String protectorComany;
    @Property
    private String protectMeasures;
    @Property
    private String protectTime;
    @Property
    private String spotCondition;
    @Property
    private String weatherCondition;
    @Property
    private String lightCondition;
    @Property
    private String spotConduct;
    @Property
    private String inquesterName;
    @Property
    private String spotPeople;
    @Property
    private String spotLeft;
    @Property
    private String inquestCondition;
    @Property
    private String victimName;
    @Property
    private String lossGoods;
    @Property
    private String recordTime;
    @Property
    private String injury;
    @Property
    private String witness;
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
