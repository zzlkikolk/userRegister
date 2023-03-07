package com.jerry.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CreateBucketRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

public class AliOSSService {

//    public static void main(String[] args) {
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "pulic-img";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY, ACCESS_KEY_SECRET);
//
//        try {
//            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
//
//            // 如果创建存储空间的同时需要指定存储类型和数据容灾类型, 请参考如下代码。
//            // 此处以设置存储空间的存储类型为标准存储为例介绍。
//            //createBucketRequest.setStorageClass(StorageClass.Standard);
//            // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
//            //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
//            // 设置存储空间的权限为公共读，默认为私有。
//            //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
//
//            // 创建存储空间。
//            ossClient.createBucket(createBucketRequest);
//
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }

    public static void main(String[] args) {
        String bucketName = "pulic-img";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "user/head/1.png";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY, ACCESS_KEY_SECRET);

        try {
            String content = "Hello OSS";
            File file=new File("C:/Users/IG-PC-033/Desktop/openFile/lADPM3yRmnlqrN_NAVzNAVs_347_348.jpg");
            FileInputStream inputStream=new FileInputStream(file);
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
