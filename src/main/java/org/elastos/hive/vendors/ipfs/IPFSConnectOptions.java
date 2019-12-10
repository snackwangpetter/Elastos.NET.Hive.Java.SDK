package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.vendors.HiveRpcNode;

public class IPFSConnectOptions extends HiveConnectOptions {

    HiveRpcNode[] hiveRpcNodes;

    public IPFSConnectOptions(HiveRpcNode[] hiveRpcNodes) {
        this.hiveRpcNodes = hiveRpcNodes ;
        setBackendType(HiveBackendType.HiveBackendType_IPFS);
    }


    public HiveRpcNode[] getHiveRpcNodes() {
        return hiveRpcNodes;
    }
}
