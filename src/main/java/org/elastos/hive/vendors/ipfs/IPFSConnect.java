package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.IHiveFile;

public class IPFSConnect implements IHiveConnect {
    private static IPFSConnect mIPFSConnectInstance ;
    private IPFSConnect(IPFSConnectOptions ipfsConnectOptions){
    }

    public static IHiveConnect createInstance(HiveConnectOptions hiveConnectOptions){
        if (null == mIPFSConnectInstance){
            mIPFSConnectInstance = new IPFSConnect((IPFSConnectOptions) hiveConnectOptions);
        }
        return mIPFSConnectInstance;
    }

    public static IHiveConnect getInstance(){
        return  mIPFSConnectInstance;
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
        return new IPFSFiles(filename,key);
    }
}
