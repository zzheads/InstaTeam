package com.zzheads.instateam.model;//

// InstaTeam
// com.zzheads.instateam.model created by zzheads on 27.07.2016.
//
public enum Status {
    NOTSTARTED("Not Started","tag notstarted"),
    ACTIVE("Active","tag active"),
    ARCHIVED("Archived","tag archived");

    private final String name;
    private final String tag;

    Status(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }
}
