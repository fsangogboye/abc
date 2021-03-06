package org.jgroups.util;

import java.net.*;
import java.io.IOException;
import java.util.Map;

/**
 * Factory to create various types of sockets. For socket creation, a <em>service name</em> can be passed as argument:
 * an implementation could look up a service description (e.g. port) and create the socket, ignoring the passed port and
 * possibly also the bind address.<p/>
 * Ephemeral ports can be created by passing 0 as port, or (if the port is ignored), an implementation could pass in
 * a special service name (e.g. "EPHEMERAL"), this is implementation dependent.<p/>
 * The socket creation methods have the same parameter lists as the socket constructors, e.g.
 * {@link #createServerSocket(String, int, int)} is the same as {@link java.net.ServerSocket(int,int)}. 
 * @author Bela Ban
 * @version $Id: SocketFactory.java,v 1.1 2010/04/27 09:27:32 belaban Exp $
 */
public interface SocketFactory {

    // todo: should we include NIO socket channels too ?
    // todo: how should service names be structured ? jgroups.udp.unicast_port ?

    // todo: should we really include creation of java.net.Sockets ? They don't listen on incoming ports. This would only
    // be for socket configuration....
    Socket createSocket(String service_name) throws IOException;
    Socket createSocket(String service_name, String host, int port) throws IOException;
    Socket createSocket(String service_name, InetAddress address, int port) throws IOException;
    Socket createSocket(String service_name, String host, int port, InetAddress localAddr, int localPort) throws IOException;
    Socket createSocket(String service_name, InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException;

    ServerSocket createServerSocket(String service_name) throws IOException;
    ServerSocket createServerSocket(String service_name, int port) throws IOException;
    ServerSocket createServerSocket(String service_name, int port, int backlog) throws IOException;
    ServerSocket createServerSocket(String service_name, int port, int backlog, InetAddress bindAddr) throws IOException;

    DatagramSocket createDatagramSocket(String service_name) throws SocketException;
    DatagramSocket createDatagramSocket(String service_name, SocketAddress bindaddr) throws SocketException;
    DatagramSocket createDatagramSocket(String service_name, int port) throws SocketException;
    DatagramSocket createDatagramSocket(String service_name, int port, InetAddress laddr) throws SocketException;

    MulticastSocket createMulticastSocket(String service_name) throws IOException;
    MulticastSocket createMulticastSocket(String service_name, int port) throws IOException;
    MulticastSocket createMulticastSocket(String service_name, SocketAddress bindaddr) throws IOException;

    void close(Socket sock) throws IOException;
    void close(ServerSocket sock) throws IOException;
    void close(DatagramSocket sock);
    

    /**
     * Returns all open sockets. This method can be used to list or close all open sockets.
     * @return A map of open sockets; keys are Sockets, ServerSockets, DatagramSockets or MulticastSockets, values are
     * the service names.
     */
    Map<Object,String> getSockets();
}
