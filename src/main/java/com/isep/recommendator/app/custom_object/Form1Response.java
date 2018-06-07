package com.isep.recommendator.app.custom_object;


public class Form1Response implements Form{
    private int        matching;
    private boolean    select;
    private SpecialityWithMatchingJobs speciality;

    public Form1Response(boolean select, int matching, SpecialityWithMatchingJobs speciality) {
        this.speciality = speciality;
        this.matching = matching;
        this.select = select;
    }

    public SpecialityWithMatchingJobs getSpeciality() {
        return speciality;
    }

    public void setSpeciality(SpecialityWithMatchingJobs speciality) {
        this.speciality = speciality;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getMatching() {
        return matching;
    }

    public void setMatching(int matching) {
        this.matching = matching;
    }
}
