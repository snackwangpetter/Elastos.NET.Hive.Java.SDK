package org.elastos.hive;

import org.elastos.hive.vendors.ipfs.IPFSConnect;
import org.elastos.hive.vendors.onedrive.OneDriveConnect;

public class HiveClient {
    private static HiveClient mInstance ;

    private HiveClient(HiveClientOptions hiveOptions){
    }

    public static HiveClient createInstance(HiveClientOptions hiveOptions) {
        if (mInstance == null){
            mInstance = new HiveClient(hiveOptions);
        }

        return mInstance;
    }

    public static HiveClient getInstance() {
        return mInstance;
    }

    public void close() {
        mInstance = null ;
    }

    public IHiveConnect connect(HiveConnectOptions hiveConnectOptions) {
        HiveConnectOptions.HiveBackendType backendType = hiveConnectOptions.getBackendType();
        IHiveConnect hiveConnect = null ;
        switch (backendType){
            case HiveBackendType_IPFS:
                hiveConnect = IPFSConnect.createInstance(hiveConnectOptions);
                break;
            case HiveBackendType_OneDrive:
                hiveConnect = OneDriveConnect.createInstance(hiveConnectOptions);
                break;
            case HiveBackendType_ownCloud:
                break;
            case HiveDriveType_Butt:
                break;
            default:
                break;
        }
//        hiveConnect.establishConnect();
        return hiveConnect;
    }

    public int disConnect(IHiveConnect hiveConnect) {
        hiveConnect.disConnect();
        return 0;
    }
}
