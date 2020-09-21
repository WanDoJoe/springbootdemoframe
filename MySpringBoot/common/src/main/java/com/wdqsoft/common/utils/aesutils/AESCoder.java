package com.wdqsoft.common.utils.aesutils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/** 
 * AES Coder<br/> 
 * secret key length:   128bit, default:    128 bit<br/> 
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
 * @author Aub 
 *  
 */  
public class AESCoder {  
      
    /** 
     * 密钥算法 
    */  
    private static final String KEY_ALGORITHM = "AES";  
      
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";  
      
    /** 
     * 初始化密钥
     *  
     * @return byte[] 密钥  
     * @throws Exception 
     */ 
    public static byte[] initSecretKey(String password) {  
    	//返回生成指定算法的秘密密钥的 KeyGenerator 对象 
        KeyGenerator kg = null;  
        try {  
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            return new byte[0];  
        }  
        //初始化此密钥生成器，使其具有确定的密钥大小  
        //AES 要求密钥长度为 128   
        kg.init(128, new SecureRandom(password.getBytes()));  
        //生成一个密钥  
        SecretKey  secretKey = kg.generateKey();  
        return secretKey.getEncoded();  
    }  
      
    /** 
     * 转换密钥 
     *  
     * @param key   二进制密钥 
     * @return 密钥 
     */  
    private static Key toKey(byte[] key){  
        //生成密钥  
        return new SecretKeySpec(key, KEY_ALGORITHM);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   二进制密钥 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   二进制密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //还原密钥  
        Key k = toKey(key);  
        return encrypt(data, k, cipherAlgorithm);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }  
      
      
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   二进制密钥 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   二进制密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //还原密钥  
        Key k = toKey(key);  
        return decrypt(data, k, cipherAlgorithm);  
    }  
  
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }  
      
    private static String  showByteArray(byte[] data){  
        if(null == data){  
            return null;  
        }  
        StringBuilder sb = new StringBuilder("{");  
        for(byte b:data){  
            sb.append(b).append(",");  
        }  
        sb.deleteCharAt(sb.length()-1);  
        sb.append("}");  
        return sb.toString();  
    } 
    
    /**
     * 用密钥与加密数据解密
     * @param keystr 十六进制key字符串
     * @param secretdata 加密后的数据
     * @return
     * @throws Exception
     */
    public static String getData(String keystr, String secretdata) throws Exception
    {
    	byte[] key = Hex.HexString2Bytes(keystr);
    	Key k = toKey(key);
    	byte[] encryptData = Hex.HexString2Bytes(secretdata);
    	byte[] decryptData = decrypt(encryptData, k); 
    	return new String(decryptData);
    }
    
    /**
     * 用密钥加密数据
     * @param keystr
     * @param data
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static String setData(String keystr, String data) throws Exception
    {
    	String secretdata = null;
    	byte[] key = Hex.HexString2Bytes(keystr);
    	Key k = toKey(key);
    	byte[] encryptData = encrypt(data.getBytes(), k); 
    	secretdata = Hex.encodeHexStr(encryptData);
    	return secretdata;
    }
    
    /**
     * 产生一个密钥
     * @return
     */
    public static String getKeyStr(String password)
    {
    	byte[] key = initSecretKey(password); 
    	return Hex.encodeHexStr(key);
    }
//     public static void main(String[] args) throws Exception {
//    	 String key=AESCoder.getKeyStr("sinosoft");//oa人数限制 key
////    	 String key=AESCoder.getKeyStr("sinosoftmobile");
////    	 String key=AESCoder.getKeyStr("70fa1292114d0711c3e835cb1cda5d7a");
//    	 System.out.println("加工后的密钥1:"+key);
////    	 String data="50"; //人数限制  人数
//         String data="2";
//    	 String secretdata=AESCoder.setData(key, data);
//    	 System.out.println("密钥加工后的数据1:"+secretdata);}
//    	 
//    	 String secretdata2 = AESCoder.setData(key, secretdata);
//    	 System.out.println("密钥加工后的数据2:"+secretdata2);
//    	 
//    	 
//    	 String jdata = AESCoder.getData(key, secretdata2);
//    	 System.out.println("一次解密后的数据1:"+ jdata);
//    	 String jdata2 = AESCoder.getData(key, jdata);
//    	 System.out.println("一次解密后的数据2:"+ jdata2);
//	}
//    public static void main(String[] args) throws Exception {  
//        byte[] key = initSecretKey();  
//        System.out.println("keystr"+Hex.encodeHexStr(key)); 
////        String s = "83cd253d73ae9fac603799344c156fa0";
////        byte[] key = Hex.HexString2Bytes(s);
//        System.out.println("key��"+showByteArray(key)); 
//        Key k = toKey(key);  
//          //{-87,-18,54,111,-91,20,-36,-32,-97,111,15,-96,71,-98,-5,-80}
//        String data ="150";  
//        System.out.println("加密前数据: string:"+data);  
//        System.out.println("加密前数据: byte[]:"+showByteArray(data.getBytes()));  
//        System.out.println();  
//        byte[] encryptData = encrypt(data.getBytes(), k);  
////        byte[] encryptData = Hex.HexString2Bytes("0d95758f57ae6547af6033c8dad5c8c2");
//        System.out.println("加密后数据: byte[]:"+showByteArray(encryptData));  
//        System.out.println("加密后数据: hexStr:"+Hex.encodeHexStr(encryptData));  
//        System.out.println();  
//        byte[] decryptData = decrypt(encryptData, k);  
//        System.out.println("解密后数据: byte[]:"+showByteArray(decryptData));  
//        System.out.println("解密后数据: string:"+new String(decryptData));  
//        
//        String  ss = getData(Hex.encodeHexStr(key),Hex.encodeHexStr(encryptData));
//        System.out.println("sss:"+ss);
//    }  
}