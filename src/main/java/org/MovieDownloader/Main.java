package org.MovieDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    //IMPLEMENT RE USE LINK FOR DIFFERENT SOURCES

    public static JFrame frame;
    public static JPanel panel;
    public static JLabel urlLabel;
    public static JLabel videoNameLabel;

    public static JButton closeButton;
    public static JButton startBrowserButton;
    public static JButton downloadButton;


    public static JTextField urlLinkField;
    public static JTextField videoNameField;
    public static JTextArea textArea;
    public static JScrollPane scrollPane;
    public static JComboBox<String> comboBox;
    public static DefaultComboBoxModel<String> model;

    public static String selectedQuality;
    public static String[] choices;
    public static String urlLink;
    public static String m3u8UrlLink;
    public static String videoName;
    public static String programPath;

    public static boolean isStartedBrowser = false;


    public static List<String> urlLinkList;
    public static List<String> partFileNames;

    public static VideoScraper scraper;
    public static VideoMerger merger;
    public static Partition partition;
    public static Utils utils;
    public static SeleniumInterceptor interceptor;
    public static PatternFactory patternFactory;
    public static M3U8Downloader m3U8Downloader;

    public static List<String> capturedLinks;
    public static HashMap<Integer,Pattern> m3u8Links;

    public static Thread linkThread;
    public static Matcher matcher;


    public static void main(String[] args) {

        utils = new Utils();
        patternFactory = new PatternFactory();
        interceptor = new SeleniumInterceptor();
        partition = new Partition(32);
        merger = new VideoMerger();
        m3U8Downloader = new M3U8Downloader();
        programPath = utils.getProgramPath();
        scraper = new VideoScraper(programPath);
        partFileNames = new ArrayList<>(32);
        linkThread = new Thread(){
            @Override
            public void run() {
                while(isStartedBrowser) {
                    capturedLinks = interceptor.getAllLinks();
                    for (String s : capturedLinks) {
                        textArea.append(s + "\n");
                    }
                    capturedLinks.clear();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        for(int i = 1; i<=32; i++){
            partFileNames.add("part"+i);
        }

        m3u8Links = patternFactory.getM3u8Links();
        choices = patternFactory.getQualityList();
        model = new DefaultComboBoxModel<>(choices);

        closeButton = new JButton("Close Browser");
        startBrowserButton = new JButton("Start Browser");
        downloadButton = new JButton("Download");

        panel = new JPanel();
        frame = new JFrame();

        urlLabel = new JLabel("Enter URL");
        videoNameLabel = new JLabel("Enter Video Name");
        urlLinkField = new JTextField(30);
        videoNameField = new JTextField(20);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(textArea);
        comboBox = new JComboBox<>();
        comboBox.setModel(model);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        panel.add(comboBox);
        panel.add(urlLabel);
        panel.add(urlLinkField);
        panel.add(videoNameLabel);
        panel.add(videoNameField);
        panel.add(downloadButton);
        panel.add(startBrowserButton);

        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setVisible(true);



        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isStartedBrowser = false;
                linkThread.interrupt();
                interceptor.closeBrowser();

                textArea.append("Browser Closed!!\n");
            }

        });
        startBrowserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                urlLink = urlLinkField.getText();
                if(urlLink.length() != 0) {
                    isStartedBrowser = true;
                    interceptor.openWebSite(urlLink);
                    linkThread.start();
                    textArea.append("\nBrowser Started!!\n");
                }else{
                    textArea.append("\nBEFORE STARTING THE BROWSER ENTER THE URL\n");
                }


            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                videoName = videoNameField.getText();
                selectedQuality = comboBox.getSelectedItem().toString();
                if(isStartedBrowser && videoName.length() > 0 && selectedQuality != null) {

                    urlLinkList = interceptor.getAllLinks();
                    for(int i = 0; i<urlLinkList.size(); i++) {
                        int quality = Integer.parseInt(selectedQuality);
                        matcher = m3u8Links.get(quality).matcher(urlLinkList.get(i));

                        if (matcher.find()) {
                            System.out.println("FOUND M3U8 LİNK");
                            m3u8UrlLink = matcher.group(0);
                            m3U8Downloader.downloadFile(m3u8UrlLink,videoName);
                            break;
                        }

                    }
                    if(m3u8UrlLink == null){
                        textArea.append("NOT FOUND A M3U8 LINK\n\n");
                    }

                }else{
                    textArea.append("BEFORE GETTING LINKS OPEN THE BROWSER \n");
                }
            }
        });
    }
}
