package com.example.user.apiPath;

public final class ApiPath {
    public static final String VERSION = "/v1";
    public static final String BASE_PATH = "/api"+ VERSION + "/users";
    public static final String GET_USER_BY_ID = BASE_PATH + "/{id}";
    public static final String GET_ALL_USERS = BASE_PATH;
    public static final String UPDATE_USER = BASE_PATH + "/{id}";
    public static final String DELETE_USER = BASE_PATH + "/{id}";
    public static final String ADD_USER = BASE_PATH;

    private ApiPath() {
    }
}
