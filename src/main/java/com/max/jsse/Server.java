package com.max.jsse;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String[] SUPPORTED_PROTOCOLS = {
            "TLSv1.1",
            "TLSv1.2"
    };

    private static final String[] SUPPORTED_CIPHERS = {
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA"
    };

    private static final Splitter SPLITTER = Splitter.on(';');

    private Server() throws IOException {

        //Specifying the Keystore details
        System.setProperty("javax.net.ssl.keyStore", "server_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "611191");

        final int port = 9090;

        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        serverSocket.setEnabledProtocols(SUPPORTED_PROTOCOLS);
        serverSocket.setEnabledCipherSuites(SUPPORTED_CIPHERS);


        LOG.info("Server started at port {} with pid {}", port, ProcessHandle.current().pid());
        LOG.info("java version: {}", System.getProperty("java.version"));

        while (!Thread.currentThread().isInterrupted()) {

            try (Socket socket = serverSocket.accept()) {

                LOG.info("Client connected");

                try (InputStream in = socket.getInputStream();
                     InputStreamReader reader = new InputStreamReader(in, UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(reader);

                     OutputStream out = socket.getOutputStream();
                     OutputStreamWriter writer = new OutputStreamWriter(out, UTF_8);
                     BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

                    List<String> data = SPLITTER.splitToList(bufferedReader.readLine());

                    final int x = Integer.parseInt(data.get(0).trim());
                    final int y = Integer.parseInt(data.get(1).trim());

                    final String response = x + " + " + y + " = " + (x + y);

                    LOG.info("Received: x = {}, y = {}", x, y);

                    bufferedWriter.write(response);
                    bufferedWriter.newLine();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }
    }
}
