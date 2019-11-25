package org.elastos.hive;

public class HiveConnectOptions {
    protected enum HiveBackendType {
        HiveBackendType_IPFS,
        HiveBackendType_OneDrive,
        HiveBackendType_ownCloud,
        HiveDriveType_Butt
    }

    private HiveBackendType backendType ;

    protected HiveBackendType getBackendType(){
        return this.backendType;
    }

    protected void setBackendType(HiveBackendType backendType) {
        this.backendType = backendType;
    }
}
