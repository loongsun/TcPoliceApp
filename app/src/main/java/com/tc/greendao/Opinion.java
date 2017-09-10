package com.tc.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhao on 17-9-10.
 */

@Entity
public class Opinion {

    @Id
    private Long id;
    @Property
    private String caseNumber;
    @Property
    private String metail;
    @Property
    private String type;
    @Property
    private String object;
    @Property
    private String location;
    @Property
    private String opportunity;
    @Property
    private String entrance;
    @Property
    private String method;
    @Property
    private String mode;
    @Property
    private String feature;
    @Property
    private String goal;
    @Property
    private String peopleNumber;
    @Property
    private String criminalPlace;
    @Property
    private String tool;
    @Property
    private String process;
    @Property
    private String criminalFeature;
    @Property
    private String ideaBase;
    @Property
    private String workOpinion;
    @Property
    private String spotCompany;
    @Property
    private String analyzer;
    @Generated(hash = 641507816)
    public Opinion(Long id, String caseNumber, String metail, String type,
            String object, String location, String opportunity, String entrance,
            String method, String mode, String feature, String goal,
            String peopleNumber, String criminalPlace, String tool, String process,
            String criminalFeature, String ideaBase, String workOpinion,
            String spotCompany, String analyzer) {
        this.id = id;
        this.caseNumber = caseNumber;
        this.metail = metail;
        this.type = type;
        this.object = object;
        this.location = location;
        this.opportunity = opportunity;
        this.entrance = entrance;
        this.method = method;
        this.mode = mode;
        this.feature = feature;
        this.goal = goal;
        this.peopleNumber = peopleNumber;
        this.criminalPlace = criminalPlace;
        this.tool = tool;
        this.process = process;
        this.criminalFeature = criminalFeature;
        this.ideaBase = ideaBase;
        this.workOpinion = workOpinion;
        this.spotCompany = spotCompany;
        this.analyzer = analyzer;
    }
    @Generated(hash = 1975932997)
    public Opinion() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCaseNumber() {
        return this.caseNumber;
    }
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
    public String getMetail() {
        return this.metail;
    }
    public void setMetail(String metail) {
        this.metail = metail;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getObject() {
        return this.object;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getOpportunity() {
        return this.opportunity;
    }
    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }
    public String getEntrance() {
        return this.entrance;
    }
    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }
    public String getMethod() {
        return this.method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMode() {
        return this.mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getFeature() {
        return this.feature;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }
    public String getGoal() {
        return this.goal;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }
    public String getPeopleNumber() {
        return this.peopleNumber;
    }
    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
    }
    public String getCriminalPlace() {
        return this.criminalPlace;
    }
    public void setCriminalPlace(String criminalPlace) {
        this.criminalPlace = criminalPlace;
    }
    public String getTool() {
        return this.tool;
    }
    public void setTool(String tool) {
        this.tool = tool;
    }
    public String getProcess() {
        return this.process;
    }
    public void setProcess(String process) {
        this.process = process;
    }
    public String getCriminalFeature() {
        return this.criminalFeature;
    }
    public void setCriminalFeature(String criminalFeature) {
        this.criminalFeature = criminalFeature;
    }
    public String getIdeaBase() {
        return this.ideaBase;
    }
    public void setIdeaBase(String ideaBase) {
        this.ideaBase = ideaBase;
    }
    public String getWorkOpinion() {
        return this.workOpinion;
    }
    public void setWorkOpinion(String workOpinion) {
        this.workOpinion = workOpinion;
    }
    public String getSpotCompany() {
        return this.spotCompany;
    }
    public void setSpotCompany(String spotCompany) {
        this.spotCompany = spotCompany;
    }
    public String getAnalyzer() {
        return this.analyzer;
    }
    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }




}
