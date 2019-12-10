package org.elastos.hive.vendors.onedrive;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.Persistent;

public class OneDriveConnectOptions extends HiveConnectOptions {

    private final String clientId;
    private final String scope;
    private final String redirectUrl;


    public OneDriveConnectOptions(String clientId, String scope, String redirectUrl , Authenticator authenticator) {
        this.clientId = clientId;
        this.scope = scope;
        this.redirectUrl = redirectUrl;
        setBackendType(HiveBackendType.HiveBackendType_OneDrive);
        setAuthenticator(authenticator);
//        setPersistent(persistent);
    }

//    public OneDriveConnectOptions(Authenticator authenticator) {
//        setBackendType(HiveBackendType.HiveBackendType_OneDrive);
//        setAuthenticator(authenticator);
//    }

//    public OneDriveConnectOptions(Authenticator authenticator , Persistent persistent) {
//        setBackendType(HiveBackendType.HiveBackendType_OneDrive);
//        setAuthenticator(authenticator);
//        setPersistent(persistent);
//    }

    public String getClientId() {
        return clientId;
    }

//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }

    public String getScope() {
        return scope;
    }

//    public void setScope(String scope) {
//        this.scope = scope;
//    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

//    public void setRedirectUrl(String redirectUrl) {
//        this.redirectUrl = redirectUrl;
//    }
}
