package org.elastos.hive.vendors.onedrive;

import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.IHiveFile;

public class OneDriveConnect implements IHiveConnect {
    private static OneDriveConnect mOneDriveConnectInstance ;

    private OneDriveConnect(OneDriveConnectOptions oneDriveConnectOptions){
//        oneDriveConnectOptions.
    }

    public static IHiveConnect createInstance(HiveConnectOptions hiveConnectOptions){
        if (null == mOneDriveConnectInstance){
            mOneDriveConnectInstance = new OneDriveConnect((OneDriveConnectOptions) hiveConnectOptions);
        }
        return mOneDriveConnectInstance;
    }

    public static IHiveConnect getInstance(){
        return mOneDriveConnectInstance ;
    }

    @Override
    public void establishConnect() {

    }

    @Override
    public void disConnect() {

    }

    @Override
    public void setEncryptKey(String encryptKey) {

    }

    @Override
    public IHiveFile getHiveFile(String filename, String key) {
        return new OneDriveFiles(filename,key);
    }
}
