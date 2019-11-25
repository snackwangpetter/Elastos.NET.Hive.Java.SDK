package org.elastos.hive.vendors.onedrive;

import org.elastos.hive.AuthHelper;
import org.elastos.hive.HiveConnectOptions;

public class OneDriveConnectOptions extends HiveConnectOptions {

    private String clientId;
    private String scope;
    private String redirectUrl;
    private AuthHelper oneDriveAuthHelper ;

    public OneDriveConnectOptions(AuthHelper oneDriveAuthHelper) {
        setBackendType(HiveBackendType.HiveBackendType_OneDrive);
        this.oneDriveAuthHelper = oneDriveAuthHelper ;
    }
}
