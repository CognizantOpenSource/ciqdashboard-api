package com.cognizant.idashboardapi.common;

import java.util.Arrays;
import java.util.List;

public class Constants {
    private Constants() {
    }

    /*DB Constants*/
    public static final String COLLECTION_FILTER_SOURCE = "source_";

    /*Token keys*/
    public static final String EXTERNAL_TOKEN = "external_token";
    public static final String TOKEN_EXPIRES_AT = "tokenExpiresAt";
    public static final String TOKEN_ID = "token-id";

    /*Constants*/
    public static final String GROUP_BY = "GroupBy";
    public static final String AGGREGATE_FIELDS = "Aggregate Fields";
    public static final String MATCHES = "Matches";
    public static final String FILTERS = "Filters";
    public static final List<String> NUMBER_FIELD_TYPES = Arrays.asList("int", "long", "decimal", "double");

    /*Error messages*/
    public static final String COLLECTION_S_NOT_AVAILABLE_IN_DB = "Collection(%s) not available in DB";

    public static final String PRIVATE_TOKEN = "PRIVATE-TOKEN";

    public static final int STATIC_MONTHS = 3;

    public static final String CHART_FIELD_NAME = "name";
    public static final String CHART_FIELD_VALUE = "value";
    public static final String CHART_FIELD_CHILDREN = "children";
    public static final String CHART_FIELD_ID = "id";
    public static final String CHART_FIELD_DATE = "date";
    public static final String CHART_FIELD_SERIES = "series";
    public static final String CHART_FIELD_DATE_FORMAT_YMD = "%Y-%m-%d";
    public static final String CHART_FIELD_DATE_FORMAT_STRING = "dd/MM/yyyy";
    public static final String UNDERSCORE_ID = "_id";
    public static final String DOLLAR_ID_S = "$_id.%s";
    public static final String DOLLAR = "$%s";

    
}
