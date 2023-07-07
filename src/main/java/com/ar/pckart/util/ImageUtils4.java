package com.ar.pckart.util;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils4 {


    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorOutputStream compressorOutputStream = new DeflateCompressorOutputStream(outputStream);
        compressorOutputStream.write(data);
        compressorOutputStream.close();
        
        byte[] byteArray = outputStream.toByteArray();
        outputStream.close();
        return byteArray;
    }

    public static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] decompressedData = IOUtils.toByteArray(compressorInputStream);
        compressorInputStream.close();

        inputStream.close();
        return decompressedData;
    }

}