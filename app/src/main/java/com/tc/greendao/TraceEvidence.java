package com.tc.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhao on 17-9-10.
 */

@Entity
public class TraceEvidence {

    @Id
    private Long id;
    @Property
    private String caseNumber;
    @Property
    private String name;
    @Property
    private String feature;
    @Property
    private String number;
    @Property
    private String extractPart;
    @Property
    private String extractMethod;
    @Property
    private String extractPerson;
    @Property
    private String note;
    @Property
    private String witness;
    @Property
    private String extractTime;
    @Generated(hash = 712771475)
    public TraceEvidence(Long id, String caseNumber, String name, String feature,
            String number, String extractPart, String extractMethod,
            String extractPerson, String note, String witness, String extractTime) {
        this.id = id;
        this.caseNumber = caseNumber;
        this.name = name;
        this.feature = feature;
        this.number = number;
        this.extractPart = extractPart;
        this.extractMethod = extractMethod;
        this.extractPerson = extractPerson;
        this.note = note;
        this.witness = witness;
        this.extractTime = extractTime;
    }
    @Generated(hash = 316758901)
    public TraceEvidence() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFeature() {
        return this.feature;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getExtractPart() {
        return this.extractPart;
    }
    public void setExtractPart(String extractPart) {
        this.extractPart = extractPart;
    }
    public String getExtractMethod() {
        return this.extractMethod;
    }
    public void setExtractMethod(String extractMethod) {
        this.extractMethod = extractMethod;
    }
    public String getExtractPerson() {
        return this.extractPerson;
    }
    public void setExtractPerson(String extractPerson) {
        this.extractPerson = extractPerson;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getWitness() {
        return this.witness;
    }
    public void setWitness(String witness) {
        this.witness = witness;
    }
    public String getExtractTime() {
        return this.extractTime;
    }
    public void setExtractTime(String extractTime) {
        this.extractTime = extractTime;
    }
    
}
