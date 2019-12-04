package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.IHiveFile;

public class IPFSConnect implements IHiveConnect {
    private static IPFSConnect mIPFSConnectInstance ;
    private IPFSConnectOptions ipfsConnectOptions ;
    private IPFSRpc ipfsRpc ;

    private IPFSConnect(IPFSConnectOptions ipfsConnectOptions){
        this.ipfsConnectOptions = ipfsConnectOptions ;
        ipfsRpc = new IPFSRpc(ipfsConnectOptions.hiveRpcNodes);

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
    public void connect(Authenticator authenticator) {

        try {
            ipfsRpc.checkReachable();
        } catch (Exception e) {
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
    public IHiveFile createHiveFile(String filename, String key) {
        return new IPFSFile(filename,key,ipfsRpc);
    }
}
