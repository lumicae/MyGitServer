package mvc.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	static final int BUFFER = 8192;     
	
    public void compress(String desc,String... srcName) {
    	File zipFile = new File(desc);
        ZipOutputStream out = null;     
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);     
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,     
                    new CRC32());     
            out = new ZipOutputStream(cos);     
            String basedir = "";   
            for (int i=0;i<srcName.length;i++){  
                compress(new File(srcName[i]), out, basedir);     
            }  
            out.close();    
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }   
    }     
    public void compress(String desc, String srcPathName) {    
    	File zipFile = new File(desc);
        File file = new File(srcPathName);     
        if (!file.exists())     
            throw new RuntimeException(srcPathName + "不存在！");     
        try {     
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);     
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,     
                    new CRC32());     
            ZipOutputStream out = new ZipOutputStream(cos);     
            String basedir = "";     
            compress(file, out, basedir);     
            out.close();     
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }     
    }     
    
    private void compress(File file, ZipOutputStream out, String basedir) {     
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {     
            System.out.println("压缩：" + basedir + file.getName());     
            this.compressDirectory(file, out, basedir);     
        } else {     
            System.out.println("压缩：" + basedir + file.getName());     
            this.compressFile(file, out, basedir);     
        }     
    }     
    
    /** 压缩一个目录 */    
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {     
        if (!dir.exists())     
            return;     
    
        File[] files = dir.listFiles();     
        for (int i = 0; i < files.length; i++) {     
            /* 递归 */    
            compress(files[i], out, basedir + dir.getName() + "/");     
        }     
    }     
    
    /** 压缩一个文件 */    
    private void compressFile(File file, ZipOutputStream out, String basedir) {     
        if (!file.exists()) {     
            return;     
        }     
        try {     
            BufferedInputStream bis = new BufferedInputStream(     
                    new FileInputStream(file));     
            ZipEntry entry = new ZipEntry(basedir + file.getName());     
            out.putNextEntry(entry);     
            int count;     
            byte data[] = new byte[BUFFER];     
            while ((count = bis.read(data, 0, BUFFER)) != -1) {     
                out.write(data, 0, count);     
            }     
            bis.close();     
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }     
    }
    
	/**
	 * 
	 * @param src 输入源zip路径 
	 * @param desc 输出路径（文件夹目录）  
	 * 
	 */
	public void zipDecompress(String src, String desc){
		long startTime=System.currentTimeMillis();  
        try {  
            ZipInputStream Zin=new ZipInputStream(new FileInputStream(  
                   src)); 
            BufferedInputStream Bin=new BufferedInputStream(Zin);  
            String Parent=desc; 
              
            ZipEntry entry ; 
           
            try {  
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
                	File Fout=new File(Parent,entry.getName());  
                    if(!Fout.exists()){  
                        (new File(Fout.getParent())).mkdirs();  
                    }  
                    FileOutputStream out=new FileOutputStream(Fout);  
                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
                    int b;  
                    while((b=Bin.read())!=-1){  
                        Bout.write(b);  
                    }  
                    Bout.close();  
                    out.close();  
                    System.out.println(Fout+"解压成功");      
                }  
                Bin.close();  
                Zin.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        long endTime=System.currentTimeMillis();  
        System.out.println("耗费时间： "+(endTime-startTime)+" ms");
	}
	 public static void main(String[] args) {     
	        ZipUtil zc = new ZipUtil();
	        String str1 = "E:\\locpg\\5\\caochenhui@locpg.hk\\6670_Inbox\\1\\1184160.eml";
	        String str2 = "E:\\locpg\\5\\caochenhui@locpg.hk\\6670_Inbox\\9\\1234608.eml";
	        String str3 = "E:\\locpg\\5\\chenghuabin@locpg.hk\\6910_Inbox\\15\\1234654.eml";
	        zc.compress("E:\\4.zip",str1,str2,str3);    
	    }  
}
