package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.ConversationProtocol.ServerEncoderDecoderImpl;
import bgu.spl.net.impl.ConversationProtocol.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

/**
 * The main class for the thread per client server
 */
public class TPCMain
{

    /**
     * The main method fo the thread per client server
     * @param args the arguments for main
     */
    public static void main(String[] args)
    {
        int port = Integer.parseInt(args[0]); // get the port

        Server.threadPerClient(port, BidiMessagingProtocolImpl::new, ServerEncoderDecoderImpl::new).serve(); // serve
    }
}
