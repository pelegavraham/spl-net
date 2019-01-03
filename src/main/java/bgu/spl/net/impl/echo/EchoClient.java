package bgu.spl.net.impl.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient implements Runnable {

    private String host = "localhost";
    private int port = 7777;

    private String s;

    private EchoClient(String  s) {
        this.s = s;
    }

    public static void main(String[] args)
    {
        new Thread( new EchoClient("Hi!")).start();
        new Thread( new EchoClient("banana ")).start();
        new Thread( new EchoClient("chips")).start();
        new Thread( new EchoClient("Hello")).start();
        new Thread( new EchoClient("Pil")).start();
        new Thread( new EchoClient("T-rex")).start();
        new Thread( new EchoClient("WOW ")).start();
    }


    @Override
    public void run()
    {
        System.out.println("client started");
        String msg = "";
        int times = 1;
        do
        {
            msg+= s;

            //BufferedReader and BufferedWriter automatically using UTF-8 encoding
            try (Socket sock = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

                //System.out.println("sending message to server");
                out.write(msg);
                out.newLine();
                out.flush();

                //System.out.println("awaiting response");
                String line = in.readLine();
                System.out.println("message from server: " + line + " Sent(" + msg + ")");
                times++;
            } catch (IOException e) {
                System.out.println("ERROR!");
                times++;
            }
        } while (times < 6);
    }

}
