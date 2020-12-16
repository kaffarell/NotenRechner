package com.primal.notenrechner.db;

public class Subject {
    private int _id;
    private String subjectName;
    private String subjectGrades;

    public Subject(int id, String subjectName, String subjectGrades) {
        this.set_id(id);
        this.setSubjectName(subjectName);
        this.setSubjectGrades(subjectGrades);
    }

    public Subject() {}

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectGrades() {
        return subjectGrades;
    }

    public void setSubjectGrades(String subjectGrades) {
        this.subjectGrades = subjectGrades;
    }
}
