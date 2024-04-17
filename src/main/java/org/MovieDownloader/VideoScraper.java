package org.MovieDownloader;
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VideoScraper {

    public ArrayList<String> indexList = new ArrayList<>();


    HttpURLConnection urlConnection;
    URL url;
    FileOutputStream fos;
    BufferedInputStream bis;
    String filePath;
    byte[] data;

    public VideoScraper(String filePath) {
        //FILE_PATH = FILE_PATH + videoName + "/";
        this.filePath = filePath;
    }

    public int writeToFile(String fileName, String urlName) {
        try{
            url = new URL(urlName);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            fos = new FileOutputStream(filePath+fileName);
            bis = new BufferedInputStream(urlConnection.getInputStream());
            data = new byte[32768];

            int count;
            while((count = bis.read(data, 0, 1024)) != -1) {
                fos.write(data, 0, count);
            }

            fos.close();
            bis.close();
            return 1;

        }catch (IOException e){
            return 0;
        }
    }



    public ArrayList<String> getTsFilesList(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath+fileName));
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH+"fileList.txt"));

        String line;
        while((line = bufferedReader.readLine()) != null) {
            if (line.contains(".ts") || line.contains("master")) {
                indexList.add(line);
                //bufferedWriter.write(line + "\n");
            }
        }

        bufferedReader.close();
        //bufferedWriter.close();

        return indexList;
    }


    /*
    * Parsing file name from give m3u8 link
    *
    *
    *
    * */
    public String m3u8NameParser(String url) {
        int urlIndex = url.length() - 1;
        String fileName = "";

        while(true){
            if(url.charAt(urlIndex) == '/'){

                fileName = fileName+url.substring(urlIndex+1);
                break;

            }else{
                urlIndex = urlIndex - 1;
            }
        }
        return fileName;
    }


    /*
    * Parsing url link for downloading .ts files
    *
    * */
    public String urlLinkParser(String url) {
        int urlIndex = url.length() - 1;

        String urlLink = "";

        while(true){
            if(url.charAt(urlIndex) == '/'){

                urlLink = urlLink+url.substring(0,urlIndex+1);
                break;

            }else{
                urlIndex = urlIndex - 1;
            }
        }
        return urlLink;
    }
    public void downloadTsFiles(ArrayList<String> indexList, String baseUrl) {
        int length = indexList.size();

        for(int i = 0; i<length; i++){
            System.out.println("[INFO] Downloading -> "+indexList.get(i));
            writeToFile(indexList.get(i),baseUrl+indexList.get(i));
        }
    }

    public ArrayList<String> getIndexList(){
        return indexList;
    }


}
