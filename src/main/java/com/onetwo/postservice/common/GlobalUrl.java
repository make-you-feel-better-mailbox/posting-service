package com.onetwo.postservice.common;

public class GlobalUrl {

    public static final String ROOT_URI = "/";
    public static final String UNDER_ROUTE = "/*";
    public static final String EVERY_UNDER_ROUTE = "/**";

    public static final String PATH_VARIABLE_POSTING_ID = "posting-id";
    public static final String PATH_VARIABLE_POSTING_ID_WITH_BRACE = "/{" + PATH_VARIABLE_POSTING_ID + "}";

    public static final String PATH_VARIABLE_USER_ID = "user-id";
    public static final String PATH_VARIABLE_USER_ID_WITH_BRACE = "/{" + PATH_VARIABLE_USER_ID + "}";

    public static final String PATH_VARIABLE_SLICE = "slice-number";
    public static final String PATH_VARIABLE_SLICE_WITH_BRACE = "/{" + PATH_VARIABLE_SLICE + "}";

    public static final String POSTING_ROOT = "/postings";
    public static final String POSTING_FILTER = POSTING_ROOT + "/filter";
}
