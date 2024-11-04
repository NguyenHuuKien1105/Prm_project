package com.example.prm;

public class ClassItem {
    private String className;
    private String subject;

    public ClassItem(String className, String subject) {
        this.className = className;
        this.subject = subject;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}