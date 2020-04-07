package com.suntendy.queue.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncryptionUtil {
	/**
     * MD5加密
     * @param pwd
     * @return
     */
    public static String encodingMd5(String pwd){
		 try {  
		      
	         // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
	         MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
	         // 输入的字符串转换成字节数组  
	         byte[] inputByteArray = pwd.getBytes();  
	         // inputByteArray是输入字符串转换得到的字节数组  
	         messageDigest.update(inputByteArray);  
	         // 转换并返回结果，也是字节数组，包含16个元素  
	         byte[] resultByteArray = messageDigest.digest();  
	         // 字符数组转换成字符串返回  
	         return byteArrayToHex(resultByteArray);  
	      } catch (NoSuchAlgorithmException e) {  
	         return "";  
	      }  
	}
    private static String byteArrayToHex(byte[] byteArray) {  
        
        // 首先初始化一个字符数组，用来存放每个16进制字符  
        char[] hexDigits = {'7','1','9','3','a','h','e','o','8','m', 'A','Y','C','X','E','Z' };  
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
        char[] resultCharArray =new char[byteArray.length * 2];  
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
        int index = 0; 
        for (byte b : byteArray) {  
           resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
           resultCharArray[index++] = hexDigits[b& 0xf];  
        }
        // 字符数组组合成字符串返回  
        return new String(resultCharArray);  
    }
    
    private static String getLocalMac(InetAddress ia) throws SocketException {
		// TODO Auto-generated method stub
		//获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
    
    public static int yfJym(String bdjym, String jym) throws Exception {
		  bdjym=EncryptionUtil.encodingMd5(bdjym);
		  jym=EncryptionUtil.encodingMd5(jym);
		  System.out.println("本地校验码:"+bdjym+"-对比-云帆校验码:"+jym);
		  if(bdjym.equals(jym)){
			  return 0;
		  }else {
			  return 1;
		}
	  } 
    
    public static void main(String[] args) throws Exception{
    		String a="p6pzg9n64pe81xww06rfnl7s920190627000006697ALSKDSwe09JF0912kJDd01ODosdjs8";
    		InetAddress addr = InetAddress.getLocalHost();
			String[] serIP=addr.toString().split("/");
			String serverIP=serIP[1];
			String serverMark=getLocalMac(addr);
			String deptCodeTop="411300";
			String deptCodeTail="000000";
			String masterkeyEnd ="20660716";//授权码有效期止
			String masterkeys ="20190716";
		    String bdjym = serverIP+masterkeys+deptCodeTop+serverMark+masterkeyEnd+deptCodeTail;//本机校验码组合
		    System.out.println(bdjym);
            int key=yfJym(bdjym, bdjym);
            System.out.println(key);
			System.out.println("未加密========"+bdjym);
	}
}
