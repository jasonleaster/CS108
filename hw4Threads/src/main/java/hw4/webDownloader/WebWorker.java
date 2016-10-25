package hw4.webDownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebWorker extends Thread {

    private String  urlString;
    private int tableRow;
    private WebFrame frame;
    private String status;

    public WebWorker(String url, int row, WebFrame frame){
        this.urlString  = url;
        this.tableRow   = row;
        this.frame      = frame;
    }

    private void download(){
        /*
          This is the core web/download i/o code...
          */
        InputStream input = null;
        StringBuilder contents = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader  = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }
            System.out.println( contents.toString() );
            // Successful download if we get here
            this.status = "success";
        } catch(MalformedURLException ignored) {}
        catch(InterruptedException exception) {
            // YOUR CODE HERE
            // deal with interruption
            this.status = "exception happend :(";
        }catch(IOException ignored) {}
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try{
                if (input != null) input.close();
            }
            catch(IOException ignored) {}
        }
    }

    @Override
    public void run() {
        download();
        this.frame.releaseWorker(this.status, this.tableRow);
    }
}
