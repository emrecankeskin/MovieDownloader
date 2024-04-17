package org.MovieDownloader;

import java.util.HashMap;
import java.util.regex.Pattern;

public class PatternFactory {
    public Pattern m3u81080Link = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\/1080\\/(index|1080|720|540|480|360)\\.m3u8");
    public Pattern m3u8720Link = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\/720\\/(index|1080|720|540|480|360)\\.m3u8");
    public Pattern m3u8480Link = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\/480\\/(index|1080|720|540|480|360)\\.m3u8");
    public Pattern m3u8360Link = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\/360\\/(index|1080|720|540|480|360)\\.m3u8");

    public String[] qualityList;

    public static Pattern vidMolyPattern = Pattern.compile("https?:\\/\\/(www\\.)?vidmoly.to\\/embed-[-a-zA-Z0-9@:%._\\+~#=]*.html");
    public static Pattern pubPlayerPattern = Pattern.compile("https:\\/\\/ngd01.filese.me/s/[-a-zA-Z0-9@:%._\\+~#=]*");


    public HashMap<Integer,Pattern> m3u8Links;

    public PatternFactory(){
        qualityList = new String[4];
        m3u8Links = new HashMap<>();

        qualityList[0] = "1080";
        qualityList[1] = "720";
        qualityList[2] = "480";
        qualityList[3] = "360";
        m3u8Links.put(1080,m3u81080Link);
        m3u8Links.put(720,m3u8720Link);
        m3u8Links.put(480,m3u8480Link);
        m3u8Links.put(360,m3u8360Link);

    }


    public HashMap<Integer,Pattern> getM3u8Links(){
        return m3u8Links;
    }

    public String[] getQualityList(){
        return qualityList;
    }





}
