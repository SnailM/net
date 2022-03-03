package com.ideasparkle.sample.login.bean;

import java.util.List;

public class BootResp {
    private List<SysConfig> serverConfigList;

    public List<SysConfig> getServerConfigList() {
        return serverConfigList;
    }

    public void setServerConfigList(List<SysConfig> serverConfigList) {
        this.serverConfigList = serverConfigList;
    }
}
