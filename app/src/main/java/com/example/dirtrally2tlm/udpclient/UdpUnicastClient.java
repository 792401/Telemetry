package com.example.dirtrally2tlm.udpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UdpUnicastClient implements Runnable {
    private final int port;

    public List<Float> getUdpResult() {
        return udpResult;
    }

    private List<Float> udpResult;
    public UdpUnicastClient(int port) {
        this.port = port;
    }





    public List<Float> bytesToFloat(byte[] byteArray) {
        int intLength = byteArray.length / 4;
        int byteLength = byteArray.length;
        List<Byte> bytes = new ArrayList<Byte>(byteLength);

        for (int i = 0; i < byteLength; i++) {
            bytes.add(byteArray[i]);
        }
        List<Float> floats = new ArrayList<>();
        for (int i = 0; i < intLength; i++) {
            byte[] currentBytes = new byte[4];
            List<Byte> listB = bytes.subList(i * 4, (i * 4) + 4);
            for (int j = 0; j < 4; j++) {
                currentBytes[j] = listB.get(j);
            }
            floats.add(ByteBuffer.wrap(currentBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat());
        }
        return floats;
    }

    @Override
    public void run() {
        try (DatagramSocket clientSocket = new DatagramSocket(port)) {
            byte[] buffer = new byte[65507];
            clientSocket.setSoTimeout(3000);
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(datagramPacket);
                this.udpResult = bytesToFloat(datagramPacket.getData());

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}