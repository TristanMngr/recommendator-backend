package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.Form;

import java.util.Collections;
import java.util.List;

class Utils<T extends Form> {
    // sort a list of SpecialityAndMatchingConcepts object, based on the number of matching concepts
    void sortWithAttributes(List<T> list){
        Collections.sort(list, (first, second) -> {
            if (first.getMatching() > second.getMatching())
                return -1;
            else if (first.getMatching() == second.getMatching())
                return 0;
            else
                return 1;
        });
    }
}
