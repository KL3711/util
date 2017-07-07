package org.business.webservice.invdl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jodd.io.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.business.webservice.invdl.Contanst;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DataTransferUtil {
	
	private static Logger log = Logger.getLogger(DataTransferUtil.class);
	
	public static String JsonToBase64ZipFileStr(String fileName, Object json){
		String result = null;
		try{
			//创建工作目录
			File workDir = new File(Contanst.INVDL_INTERFACE_PATH);
			if(!workDir.exists()){
				workDir.mkdirs();
			}
			//创建json数据文件
			String filePath = Contanst.INVDL_INTERFACE_PATH + File.separator + fileName;
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file);
			fw.write(json.toString());
			fw.close();
			//压缩文件
			ZipUtil.zip(file);
			//压缩文件做base64
			File zipfile = new File(filePath + ZipUtil.ZIP_EXT);
			result = Base64.encode(FileUtils.readFileToByteArray(zipfile));
			if(file.delete()){
				log.info("12344" + "数据文件已删除！");
			}
			if(zipfile.delete()){
				log.info("12344" + "数据文件压缩包已删除！");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 使用zip进行压缩
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static String zipString(String str) {
		if (str == null){
			return null;
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gos = null;
		String compressedStr = null;
		try{
			out = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(out);
			gos.write(str.getBytes(Charset.forName("UTF-8")));
			gos.close();
			BASE64Encoder encoder = new BASE64Encoder();
			compressedStr = encoder.encode(out.toByteArray());
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressedStr
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzipString(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}
		String decompressed = null;
		try{
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] compressed = decoder.decodeBuffer(compressedStr);
			ByteArrayInputStream in = new ByteArrayInputStream(compressed);
			GZIPInputStream gis = new GZIPInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int i = -1;
			while((i = gis.read(buf))!=-1){
				out.write(buf,0,i);
			}
			decompressed = out.toString();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return decompressed;
	}
	
	
	public static void main(String[] args) {
//		String gzStr = zipString("[[\"这是一个\",\"2100000001\",\"00000903\",\"2017-02-13\",\"210106300420939\",\"1311\",\"222.87\"]]");
//		
//		System.out.println("gzStr:" + gzStr);
		String gzStr = DataTransferUtil.zipString("这是一个");
		System.out.println("gzStr:" + gzStr);
		if(gzStr.equals("H4sIAAAAAAAAALv66NTxS7t3fAMATAoyaQgAAAA=")){
			System.out.println("一样啊！！！");
		}
		System.out.println(gzStr);
		System.out.println(unzipString(gzStr));
		System.out.println("gzStr:" + unzipString("H4sIAAAAAAAAALv66NTxS7t3fAMATAoyaQgAAAA"));
		
	}
	
}
