package org.elastos.hive;

public interface IHiveConnect {
    void connect(Authenticator authenticator);
    void disConnect();
    void setEncryptKey(String encryptKey);
    <T extends HiveFile> T createHiveFile(String filename , String cid);
}
