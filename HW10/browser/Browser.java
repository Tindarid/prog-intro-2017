package browser;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.file.*;

public class Browser {
    private static char readNext(Reader in, StringBuilder temp) throws IOException {
        char c = (char) in.read();
        temp.append(c);
        return c;
    }

    public static void main(String [] argc) {
        if (argc.length != 2) {
            System.out.println("Please write site and depth");
            return;
        }
        String url = "http://" + argc[0] + "/index.html";
        int depth = Integer.valueOf(new Integer(argc[1]));
        HashSet<String> pages = new HashSet<String>();
        Deque<Integer> depthQ = new ArrayDeque<Integer>(); 
        Deque<String> pagesQ = new ArrayDeque<String>(); 
        depthQ.addLast(depth);
        pagesQ.addLast(url);
        File tempFile = new File("temp\\");
        String absolutePath = tempFile.getAbsolutePath();

        while(!pagesQ.isEmpty()) {
            depth = depthQ.pollFirst();
            url = pagesQ.pollFirst();
            if (pages.contains(url) || depth == 0) {
                continue;
            }
            try {
                URI uri = URI.create(url);
                BufferedReader in = new BufferedReader(new InputStreamReader((new URL(url)).openStream(), "utf-8"));
                StringBuilder contents = new StringBuilder();
                pages.add(url);
                try {
                    System.out.println("Downloaded: " + url);
                    char c;
                    while (in.ready()) {
                        c = readNext(in, contents);
                        if (c == '<') {
                            StringBuilder temp = new StringBuilder();
                            while (in.ready()) {
                                c = readNext(in, contents);
                                if (c == ' ' || c == '>') {
                                    break;
                                }
                                temp.append(c);
                            }
                            String tag = temp.toString();
                            if (tag.equals("a") || tag.equals("link")) {
                                String param = getParam(in, "href", contents);
                                if (param == null || pages.contains(param)) {
                                    continue;
                                } 
                                if(depth == 1) {
                                    contents.append(param + "\"");
                                    continue;
                                }
                                URI u = uri.resolve(param);
                                contents.append("file:///" + absolutePath + "/"+ u.getHost() + u.getPath() + "\"");
                                pagesQ.addLast(u.toURL().toString());
                                depthQ.addLast(depth - 1);
                            } else if(tag.equals("img") || tag.equals("script")) {
                                String param = getParam(in, "src", contents);
                                if (param == null) {
                                    continue;
                                }
                                URI u = uri.resolve(param);
                                contents.append("file:///" + absolutePath + "/"+ u.getHost() + u.getPath() + "\"");
                                writeSomething(u.toURL(), absolutePath + "/" + u.getHost() + u.getPath());
                            }
                        }
                    }
                } finally {
                    in.close();
                }
                File file = new File(absolutePath + "/" + uri.getHost() + uri.getPath());
                (new File(file.getParent())).mkdirs();
                if (file.isDirectory()) {
                    file = new File(file, "index.html");
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                try {
                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                    try {
                        out.write(contents.toString());
                    } finally {
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException: " + e.getMessage());
                } 
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static String getParam(Reader in, String find, StringBuilder contents) throws IOException {
        StringBuilder tempStr = new StringBuilder();
        char c;
        while(true) {
            while (in.ready()) {
                c = readNext(in, contents);
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
            c = readNext(in, contents);
            while (in.ready()) {
                c = (char) in.read();
                if (c == '"') {
                    break;
                }
                tempStr.append(c);
            }
            String value = tempStr.toString();
            tempStr.delete(0, tempStr.length());
            if (param.equals(find)) {
                return value;
            }
            contents.append(value + "\"");
        }
    }

    private static void writeSomething(URL url, String p) {
        try {
            File file = new File(p);
            (new File(file.getParent())).mkdirs();
            if (file.exists()) {
                return;
            }
            file.createNewFile();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                System.out.println("Downloaded: " + url.toString());
                try {
                    while (in.ready()) {
                        out.write(in.read());
                    }
                } finally {
                    in.close();
                }
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
