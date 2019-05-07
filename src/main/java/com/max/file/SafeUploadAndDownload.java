package com.max.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SafeUploadAndDownload {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private SafeUploadAndDownload() throws Exception {

        String filePath = "/Users/mstepan/repo/iron-clud-java/pom.xml.zip";

        LOG.info("uncompressedSize: {} KB", ((double) zipFileUncompressedSize(filePath) / 1024));
    }

    /**
     * Calculate potential uncompressed zip size in bytes.
     */
    private static long zipFileUncompressedSize(String filePath) throws IOException {
        long uncompressedSize = 0L;

        try (ZipFile zipFile = new ZipFile(filePath)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                uncompressedSize += entries.nextElement().getSize();
            }
        }

        return uncompressedSize;
    }

    public static void main(String[] args) {
        try {
            new SafeUploadAndDownload();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }
    }
}
