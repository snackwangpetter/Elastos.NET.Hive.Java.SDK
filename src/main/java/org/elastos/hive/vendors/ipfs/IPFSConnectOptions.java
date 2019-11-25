package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.HiveConnectOptions;

public class IPFSConnectOptions extends HiveConnectOptions {
    class HiveRpcNode{
        String ipv4 ;
        String ipv6 ;
        String port ;
    }

    HiveRpcNode[] hiveRpcNodes;

    public IPFSConnectOptions(HiveRpcNode[] hiveRpcNodes) {
        this.hiveRpcNodes = hiveRpcNodes ;
        setBackendType(HiveBackendType.HiveBackendType_IPFS);
    }

    public HiveRpcNode[] getHiveRpcNodes() {
        return hiveRpcNodes;
    }
}
