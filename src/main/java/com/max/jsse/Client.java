package com.max.jsse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Client {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Random RAND = ThreadLocalRandom.current();

    private Client() throws IOException {

        // Specify the truststore details
        System.setProperty("javax.net.ssl.trustStore", "client_truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "611191");

        final int port = 9090;

        SocketFactory socketFac = SSLSocketFactory.getDefault();

        try (Socket socket = socketFac.createSocket(InetAddress.getLocalHost(), port)) {

            final int x = RAND.nextInt(1000);
            final int y = RAND.nextInt(1000);

            try (OutputStream out = socket.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(out, UTF_8);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer);

                 InputStream in = socket.getInputStream();
                 InputStreamReader reader = new InputStreamReader(in, UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {

                bufferedWriter.write(x + ";" + y);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                final String response = bufferedReader.readLine();
                LOG.info("Response: {}", response);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Client();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }
    }
}
