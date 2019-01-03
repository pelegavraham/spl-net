package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.ConversationProtocol.ServerEncoderDecoderImpl;
import bgu.spl.net.impl.ConversationProtocol.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

/**
 * The main class for the Reactor server
 */
public class ReactorMain
{

    /**
     * The main method fo the Reactor server
     * @param args the arguments for main
     */
    public static void main(String[] args)
    {
        int port = Integer.parseInt(args[0]); // get the port
        int nThreads = Integer.parseInt(args[1]); // get the number of threads

        Server.reactor(nThreads, port, BidiMessagingProtocolImpl::new, ServerEncoderDecoderImpl::new).serve(); // serve
    }
}