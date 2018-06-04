package crawler;

import java.io.*;
import java.util.*;
import java.net.*;

public class SimpleWebCrawler implements WebCrawler {
    private Downloader downloader;

    public SimpleWebCrawler(Downloader temp) throws IOException {
        downloader = temp;
    }

    public Page crawl(String url, int depth) {
        Map<String, Page> pages = new HashMap<String, Page>();
        Map<String, Image> images = new HashMap<String, Image>();
        Map<String, List<String>> childs = new LinkedHashMap<String, List<String>>();
        Deque<String> q = new ArrayDeque<String>();
        Deque<Integer> dq = new ArrayDeque<Integer>();

        q.push(url);
        dq.push(depth);
        String SaveUrl = url;
        while (!q.isEmpty()) {
            List<String> curImages = new ArrayList<String>();
            url = q.pollFirst();
            depth = dq.pollFirst();
            if (pages.containsKey(url)) {
                //continue;
            } else if (depth == 0) {
                Page p = new Page(url, "");
                pages.put(url, p);
            } else {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(downloader.download(url), "utf-8"));
                    URL curURL = new URL(url);
                    try {
                        char c;
                        while (in.ready()) {
                            StringBuilder tempStr = new StringBuilder();
                            c = (char) in.read();
                            if (c == (char) -1) {
                                break;
                            } else if (Character.isWhitespace(c)) {
                                continue;
                            } else if (c == '<') {
                                while (true) {
                                    c = (char) in.read(); 
                                    if (Character.isWhitespace(c) && c != ' ') {
                                        continue;
                                    }
                                    if (c == ' ' || c == '>') {
                                        break;
                                    }
                                    tempStr.append(c);
                                }
                                String temp = tempStr.toString();
                                tempStr.delete(0, tempStr.length());
                                if (temp.equals("title")) {
                                    while (in.ready()) {
                                        c = (char) in.read();
                                        if (c == '<') {
                                            StringBuilder tempStr2 = new StringBuilder();
                                            while (in.ready()) {
                                                c = (char) in.read();
                                                if (c == '>') {
                                                    break;
                                                }
                                                tempStr2.append(c);
                                            }
                                            if (tempStr2.toString().equals("/title")) {
                                                break;
                                            } else {
                                                tempStr.append("<" + tempStr2.toString() + ">");
                                                continue;
                                            }
                                        }
                                        tempStr.append(c);
                                    }
                                    pages.put(url, new Page(url, noHtmlCodes(tempStr.toString())));
                                } else if (temp.equals("!--")) {
                                    while (in.ready()) {
                                        while (c != '-') {
                                            c = (char) in.read();
                                        }
                                        c = (char) in.read();
                                        if (c == '-') {
                                            c = (char) in.read();
                                            if (c == '>') {
                                                break;
                                            }
                                        }
                                    }
                                } else if (temp.equals("a")) {
                                    temp = getParam(in, "href");
                                    if (temp == null) {
                                        continue;
                                    }
                                    temp = transform(curURL, temp);
                                    if (temp == null) {
                                        continue;
                                    }
                                    temp = noHtmlCodes(temp);
                                    q.addLast(temp);
                                    dq.addLast(depth - 1);
                                    if (!childs.containsKey(url)) {
                                        childs.put(url, new ArrayList<String>());
                                    }
                                    childs.get(url).add(temp);
                                } else if (temp.equals("img")) {
                                    temp = getParam(in, "src");
                                    if (temp == null) {
                                        continue;
                                    }
                                    temp = transform(curURL, temp);
                                    if (temp == null) {
                                        continue;
                                    }
                                    String imageName = getImageName(temp);
                                    if (!images.containsKey(temp)) {
                                        images.put(temp, new Image(temp, imageName));
                                        InputStream inImage = downloader.download(temp);
                                        try {
                                            OutputStream outImage = new FileOutputStream(imageName);
                                            try {
                                                byte[] buffer = new byte[1 << 16];
                                                int r;
                                                while ((r = inImage.read(buffer)) != -1) {
                                                    for (int i = 0; i < r; i++) {
                                                        outImage.write(buffer[i]);
                                                    }
                                                }
                                            } finally {
                                                outImage.close();
                                            }
                                        } catch (FileNotFoundException e) {
                                            System.out.println(e.getMessage());
                                        } finally {
                                            inImage.close();
                                        }
                                    }
                                    curImages.add(temp);
                                }
                            }
                        }
                    } finally {
                        in.close();
                    }
                    for (String tempImage : curImages) {
                        pages.get(url).addImage(images.get(tempImage));
                    }
                } catch(IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                    Page p = new Page(url, "");
                    pages.put(url, p);
                }
            }
        }
        for (String key : pages.keySet()) {
            if (childs.containsKey(key)) {
                for (String child : childs.get(key)) {
                    pages.get(key).addLink(pages.get(child));
                }
            }
        }
        return pages.get(SaveUrl);
    }

    private String getParam(Reader in, String find) throws IOException {
        StringBuilder tempStr = new StringBuilder();
        String ans = null;
        char c;
        while(ans == null) {
            while (in.ready()) {
                c = (char) in.read();
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (c == '=') {
                   break;
                } 
                if (c == '>') {
                    return null;
                }
                tempStr.append(c);
            }
            String param = tempStr.toString();
            tempStr.delete(0, tempStr.length());
            c = (char) in.read();
            while (Character.isWhitespace(c)) {
                c = (char) in.read();
            }
            while (in.ready()) {
                c = (char) in.read();
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (c == '"') {
                    break;
                }
                tempStr.append(c);
            }
            String value = tempStr.toString();
            tempStr.delete(0, tempStr.length());
            if (param.equals(find)) {
                ans = value;
            }
        }
        return ans;
    }

    private String transform(URL url, String temp) {
        try { 
            temp = (new URL(url, temp)).toString();
            int j = 0;
            while (j < temp.length() && temp.charAt(j) != '#') {
                j++;
            }
            return temp.substring(0, j);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private String noHtmlCodes(String temp) {
        return temp.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0").replaceAll("&reg;", "\u00AE");
    }

    private String getImageName(String name) {
        int save = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/') {
                save = i;
            }
        }
        return name.hashCode() + name.substring(save + 1, name.length());
    }
}
