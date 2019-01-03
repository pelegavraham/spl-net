package bgu.spl.net.impl.echo;

import bgu.spl.net.srv.Server;

public class EchoReactorServer implements Runnable
{
    @Override
    public void run()
    {
        int port = 7777; // get the port

        Server.threadPerClient(port, EchoProtocol::new, LineMessageEncoderDecoder::new).serve(); // serve
    }

    /**
     * The main method fo the reactor server
     * @param args the arguments for main
     */
    public static void main(String[] args)
    {
        //int port = Integer.parseInt(args[0]); // get the port
        int port = 7777; // get the port
        int numOfThreads = 5;

        Server.reactor(numOfThreads, port, EchoProtocol::new, LineMessageEncoderDecoder::new).serve(); // serve
    }
}
