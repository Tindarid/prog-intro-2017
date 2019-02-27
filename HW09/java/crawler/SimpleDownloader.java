package crawler;

import java.io.*;
import java.net.*;

public class SimpleDownloader implements Downloader, AutoCloseable {

    public SimpleDownloader() throws IOException {

    }

    public InputStream download(final String url) throws IOException {
        return (new URL(url)).openStream();
    }

    public void close() throws IOException {

    }
}
