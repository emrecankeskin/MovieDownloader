package org.MovieDownloader;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;

public class VideoMerger {

    //public String FILE_PATH = System.getProperty("user.dir")+"/";
    public String FILE_PATH;
    public MappedByteBuffer buffer;
    public File file;
    public Utils utils;
    public String path = Path.of("").toAbsolutePath().toString()+"\\video\\";




    public VideoMerger(){;
    }

    public void merge(List<String> fileList,String outputName){
        try{



            FileChannel outputChannel = new FileOutputStream(path+outputName).getChannel();
            FileChannel inputChannel;

            for(int i = 0; i<fileList.size(); i++) {
                inputChannel = new FileInputStream(path+fileList.get(i)).getChannel();
                file = new File(path+fileList.get(i));
                buffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputChannel.size());
                outputChannel.write(buffer);
                file.delete();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteAllFiles(List<String> fileList){
        for(int i = 0; i<fileList.size(); i++) {
            file = new File(path+fileList.get(i));
            file.delete();
        }
    }
    public void deleteFile(String fileName){
        file = new File(fileName);
        file.delete();
    }



}
