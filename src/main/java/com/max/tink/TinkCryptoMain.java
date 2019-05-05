package com.max.tink;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TinkCryptoMain {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String KEYSET_FILE = "my_keyset.json";

    private static final byte[] SALT = "data123".getBytes(UTF_8);

    private TinkCryptoMain() throws Exception {

        AeadConfig.register();

        // use existing key from file
        KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(KEYSET_FILE)));

        // get primitive
        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        // encrypt
        byte[] encoded = aead.encrypt("hello, world".getBytes(UTF_8), SALT);
        info(LOG, "encrypted: {}", () -> new String(encoded, UTF_8));

        // decode
        byte[] decoded = aead.decrypt(encoded, SALT);
        info(LOG, "decrypted: {}", () -> new String(decoded, UTF_8));
    }

    private static <T> void info(Logger log, String msg, Supplier<T> factory) {
        if (log.isInfoEnabled()) {
            LOG.info(msg, factory.get());
        }
    }

    public static void main(String[] args) {
        try {
            new TinkCryptoMain();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }
    }
}
