package bgu.spl.net.impl.echo;


import bgu.spl.net.srv.Server;

/**
 * The main class for the thread per client server
 */
public class EchoServer implements Runnable
{

    @Override
    public void run()
    {
        int port = 7777; // get the port

        Server.threadPerClient(port, EchoProtocol::new, LineMessageEncoderDecoder::new).serve(); // serve
    }

    /**
     * The main method fo the thread per client server
     * @param args the arguments for main
     */
    public static void main(String[] args)
    {
        //int port = Integer.parseInt(args[0]); // get the port
        int port = 7777; // get the port

        Server.threadPerClient(port, EchoProtocol::new, LineMessageEncoderDecoder::new).serve(); // serve
    }
}
