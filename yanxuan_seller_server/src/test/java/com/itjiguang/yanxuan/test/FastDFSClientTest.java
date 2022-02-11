package com.itjiguang.yanxuan.test;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

public class FastDFSClientTest {
    public static void main(String[] args) throws IOException, MyException {

        ClientGlobal.init("E:\\yanxuan_workspace\\yanxuan_parent\\yanxuan_seller_server\\src\\main\\resources\\fastdfs\\client.properties");

        TrackerClient trackerClient = new TrackerClient();

        TrackerServer connection = trackerClient.getConnection();

        StorageServer storeStorage = trackerClient.getStoreStorage(connection);

        StorageClient storageClient = new StorageClient(connection, storeStorage);

        String[] jpgs = storageClient.upload_file("E:\\java资料\\yx\\yanxuan_05_资料_V1.0\\资料\\素材\\a10c1f3b8cdca326.jpg", "jpg", null);

        for (String str: jpgs) {
            System.out.println(str);
        }
    }
}
