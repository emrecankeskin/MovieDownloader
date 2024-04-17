package org.MovieDownloader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class M3U8Downloader {

    public String baseUrlLink;
    public ArrayList<String> indexFileList;
    public ArrayList<List<String>> partedList;
    public List<String> partFileNames;

    public String path = Path.of("").toAbsolutePath().toString()+"\\video\\";

    VideoScraper scraper;
    VideoMerger merger;
    Utils utils;
    ThreadPoolExecutor executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(32);;

    public M3U8Downloader(){

        utils = new Utils();
        scraper = new VideoScraper(path);
        merger = new VideoMerger();
    }

    public void downloadFile(String m3u8UrlLink,String videoName) {
        try {
            //parse m3u8 file name for downloading
            //Then parse baseUrl for using this url to download other indexes
            System.out.println("[INFO] STARTED TO DOWNLOAD");
            String m3u8FileName = scraper.m3u8NameParser(m3u8UrlLink);
            baseUrlLink = scraper.urlLinkParser(m3u8UrlLink);

            scraper.writeToFile(m3u8FileName, m3u8UrlLink);
            indexFileList = scraper.getTsFilesList(m3u8FileName);

            scraper.downloadTsFiles(indexFileList,
                    baseUrlLink);

            partedList = utils.divide(indexFileList,32);
            for (int i = 0; i < 32; i++) {
                int index = i;
                executorPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        merger.merge(partedList.get(index), partFileNames.get(index));
                    }
                });
            }


            //wait for the finish executors merge all videos
            while (true) {
                if (executorPool.getCompletedTaskCount() == 32) {
                    executorPool.shutdownNow();
                    break;
                }
            }
            merger.merge(partFileNames, videoName);

        } catch (Exception e) {
        }
    }

}
