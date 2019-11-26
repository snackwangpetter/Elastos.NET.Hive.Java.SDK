package org.elastos.hive;

import java.util.concurrent.CompletableFuture;

public interface IHiveConnect {
    void connect(Authenticator authenticator);
    void disConnect();
    void setEncryptKey(String encryptKey);
    IHiveFile createHiveFile(String filename , String cid);
}
