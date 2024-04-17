package org.MovieDownloader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public String temp = "";
    public ArrayList<List<String>> partedList;
    public int divider;

    public String getProgramPath() {
        try {
            String path = new File(VideoDownloader.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getPath();
            for(int i = path.length()-1; 0<i; i--){
                if(path.charAt(i) == '/'  || path.charAt(i) == '\\'){
                    temp = temp+path.substring(0,i+1);
                    break;
                }
            }
        }catch (URISyntaxException e){
            return null;
        }

        return temp;
    }
    public ArrayList<List<String>>  divide(ArrayList<String> indexList,int nThread){

        partedList = new ArrayList<>();
        divider = indexList.size() / nThread;


        for(int i = 0; i< indexList.size(); i = i + divider){
            partedList.add(indexList.subList(i,Math.min(i+divider,indexList.size())));
        }
        return partedList;
    }
}
