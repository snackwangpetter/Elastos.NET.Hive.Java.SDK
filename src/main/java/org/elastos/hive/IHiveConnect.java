package org.elastos.hive;

import java.util.concurrent.CompletableFuture;

public interface IHiveConnect {
    void establishConnect();
    void disConnect();
    void setEncryptKey(String encryptKey);
    IHiveFile getHiveFile(String filename , String key);
}
