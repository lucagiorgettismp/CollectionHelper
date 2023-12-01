package com.lucagiorgetti.surprix.ui.mainfragments.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum FilterType {
    CATEGORY, PRODUCER, YEAR, COMPLETION;

    public static List<FilterType> getValues(FilterSelectableType selectableType) {
        switch (selectableType) {
            case CATALOG_SET:
                return Collections.singletonList(COMPLETION);
            case SURPRISE:
                return Arrays.asList(CATEGORY, PRODUCER, YEAR);
        }

        return new ArrayList<>();
    }
}

