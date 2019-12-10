package org.elastos.hive.vendors.onedrive;

import org.elastos.hive.AuthHelper;
import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.HiveFile;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.vendors.connection.ConnectionManager;
import org.elastos.hive.vendors.connection.model.BaseServiceConfig;

import java.util.concurrent.ExecutionException;

public class OneDriveConnect implements IHiveConnect {
    private static OneDriveConnect mOneDriveConnectInstance ;
    private static OneDriveConnectOptions oneDriveConnectOptions ;
    private static AuthHelper authHelper;



    private OneDriveConnect(OneDriveConnectOptions oneDriveConnectOptions){
//        OAuthEntry oAuthEntry = new OAuthEntry(oneDriveConnectOptions.getClientId(),
//                oneDriveConnectOptions.getScope(),oneDriveConnectOptions.getRedirectUrl());
//
//        if (oneDriveConnectOptions.getPersistent() == null){
//            oneDriveConnectOptions.setPersistent(new OneDriveAuthInfoStoreImpl(HiveConnectOptions.DEFAULT_STORE_PATH));
//        }
//
//        this.authHelper = new OneDriveAuthHelper(oAuthEntry,oneDriveConnectOptions.getPersistent());
    }

    public static IHiveConnect createInstance(HiveConnectOptions hiveConnectOptions){
        if (null == mOneDriveConnectInstance){
            mOneDriveConnectInstance = new OneDriveConnect((OneDriveConnectOptions) hiveConnectOptions);
        }
        oneDriveConnectOptions = (OneDriveConnectOptions) hiveConnectOptions;
        return mOneDriveConnectInstance;
    }

    public static IHiveConnect getInstance(){
        return mOneDriveConnectInstance ;
    }

    @Override
    public void connect(Authenticator authenticator) {
        try {
            BaseServiceConfig config = new BaseServiceConfig.Builder().build();
            ConnectionManager.resetOneDriveApi(OneDriveConstance.ONE_DRIVE_API_BASE_URL,config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (oneDriveConnectOptions.getPersistent() == null){
                oneDriveConnectOptions.setPersistent(new OneDriveAuthInfoStoreImpl(HiveConnectOptions.DEFAULT_STORE_PATH));
            }

            authHelper = new OneDriveAuthHelper(oneDriveConnectOptions.getClientId(),
                    oneDriveConnectOptions.getScope(),
                    oneDriveConnectOptions.getRedirectUrl(),
                    oneDriveConnectOptions.getPersistent());

            authHelper.loginAsync(authenticator).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disConnect() {

    }

    @Override
    public void setEncryptKey(String encryptKey) {

    }

    @Override
    public <T extends HiveFile> T createHiveFile(String filename, String cid) {
        return (T) new OneDriveFile(filename , authHelper);
    }
}
