package com.ideasparkle.sample.login;

class RequestServices {
    private static final RequestServices instance = new RequestServices();
    //LoginService
    private LoginService loginService;

    public static RequestServices getInstance() {
        return instance;
    }

    public LoginService getLoginService() {
        if (loginService == null) {
            loginService = LoginService.Factory.create();
        }
        return loginService;
    }
}
