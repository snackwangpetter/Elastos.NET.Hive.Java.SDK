package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.Persistent;
import org.elastos.hive.vendors.HiveRpcNode;

public class IPFSConnectOptions extends HiveConnectOptions {

    HiveRpcNode[] hiveRpcNodes;

    public IPFSConnectOptions(HiveRpcNode[] hiveRpcNodes) {
        this.hiveRpcNodes = hiveRpcNodes ;
        setBackendType(HiveBackendType.HiveBackendType_IPFS);
    }

//    public IPFSConnectOptions(HiveRpcNode[] hiveRpcNodes
//            , Authenticator authenticator) {
//        this.hiveRpcNodes = hiveRpcNodes ;
//        setBackendType(HiveBackendType.HiveBackendType_IPFS);
//        setAuthenticator(authenticator);
//    }
//
//    public IPFSConnectOptions(HiveRpcNode[] hiveRpcNodes
//            , Authenticator authenticator, Persistent persistent) {
//        this.hiveRpcNodes = hiveRpcNodes ;
//        setBackendType(HiveBackendType.HiveBackendType_IPFS);
//        setAuthenticator(authenticator);
//        setPersistent(persistent);
//    }

    public HiveRpcNode[] getHiveRpcNodes() {
        return hiveRpcNodes;
    }
}
