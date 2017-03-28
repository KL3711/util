package com.holytax.revenge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.holytax.eipp.common.entity.TAcUser;

/**
 * 反向工程实体类到sql语句
 * */
public class Revenge {
	
	public static <T> String revengeEntity(Class<T> clazz){
		Method[] methods = clazz.getDeclaredMethods();
		Table table = clazz.getAnnotation(Table.class);
		StringBuilder sb = new StringBuilder();
		String seq_sql = null;
		if(table!=null){
			sb.append("create table " + table.name() + "(\n");
			for(int i=0;i<methods.length;i++){
				Method method = methods[i];
				if(method.getName().contains("get")){
					if(method.getAnnotation(Id.class) != null){
						Column col = method.getAnnotation(Column.class);
						sb.append(col.name() + " ");
						sb.append("number("+col.precision()+","+col.scale()+") ");
						
						sb.append("primary key ");
						if(!col.nullable()){
							sb.append(" not null,\n");
						}
						//@SequenceGenerator(name="SEQ_AC_USER",allocationSize=1,initialValue=1, sequenceName="SEQ_AC_USER")
						SequenceGenerator seq = method.getAnnotation(SequenceGenerator.class);
						if(seq!=null){
							String seqName = seq.sequenceName();
							int seqAllocationSize = seq.allocationSize();
							int seqInitialValue = seq.initialValue();
							seq_sql = "create sequence " + seqName + " " +
											 "increment by " + seqAllocationSize + " " +
											 "start with " + seqInitialValue + ";\n";
						}
					}else{
						sb.append(revengeMethod(method) + ",\n");
					}
				}
			}
			sb.append(");" + "\r\n");
		}
		if(seq_sql!=null){
			sb.insert(0, seq_sql + "\r\n");
		}
		return sb.toString();
	}
	
//	@Id
//	@Column(name = "USERID", unique = true, nullable = false, precision = 10, scale = 0)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_AC_USER")
//	@SequenceGenerator(name="SEQ_AC_USER",allocationSize=1,initialValue=1, sequenceName="SEQ_AC_USER")
	public static String revengeMethod(Method method){
		StringBuilder sb = new StringBuilder();
		Column col = method.getAnnotation(Column.class);
		sb.append(col.name() + " ");
		if("java.lang.Long".equals(method.getReturnType().getName())){
			sb.append("number("+col.precision()+","+col.scale()+") ");
		}else if("java.lang.String".equals(method.getReturnType().getName())){
			sb.append("varchar2(" + col.length() + ") ");
		}else if("java.util.Date".equals(method.getReturnType().getName())){
			Temporal temporal = method.getAnnotation(Temporal.class);
			if(temporal.value().equals(TemporalType.TIMESTAMP)){
				sb.append("timestamp ");
			}
			//System.out.println(temporal.value().equals(TemporalType.TIMESTAMP));
		}
		if(!col.nullable()){
			sb.append(" not null");
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		//com.holytax.eipp.common.entity
		String packageName = TAcUser.class.getPackage().getName();
		try {
			FileWriter fw = new FileWriter(new File("D:/REVENGE_SQL.TXT"));
			Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
			while(dirs.hasMoreElements()){
				URL url = dirs.nextElement(); 
				if("file".equals(url.getProtocol())){
					String filePath = URLDecoder.decode(url.getFile(),"utf-8");
					File files = new File(filePath);
					for(File file : files.listFiles()){
						if(file.isFile()){
							String className = packageName + "." + file.getName().substring(0, file.getName().indexOf("."));
							System.out.println(className);
							fw.append(revengeEntity(Class.forName(className)) + "\r\n");
						}
					}
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		try {
//			Class.forName("");
//		} 
		//System.out.println(revengeEntity(TAcUser.class));
		//System.out.println(revengeEntity(TAcUser.class));
	}
	
}
