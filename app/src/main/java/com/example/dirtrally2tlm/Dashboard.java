package com.example.dirtrally2tlm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    ClientThread clientThread;
    Thread clTr;
    TextView gearView;
    TextView rpmView;
    TextView distanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        rpmView = findViewById(R.id.rpm);
        gearView = findViewById(R.id.gear);
        distanceView = findViewById(R.id.dist);
        clientThread = new ClientThread(20777);
        clTr = new Thread(clientThread);
        clTr.start();

//         t = new Thread(){
//            public void run(){
//                while(!isInterrupted()){
//                    count++;
//                    try {
//                        Thread.sleep(10);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };
//         t.start();


    }

    class ClientThread implements Runnable {
        private int port;
        public ClientThread(int port) {
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
            try {
                DatagramSocket clientSocket = new DatagramSocket(this.port);
                byte[] buffer = new byte[65507];
                clientSocket.setSoTimeout(300000000);
                while (true) {
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    clientSocket.receive(datagramPacket);
                    List<Float> res = bytesToFloat(datagramPacket.getData());
                    int gear =Math.round(res.get(33));
                    int rpm = (Math.round(res.get(37))*10);
                    int currentDistance = Math.round(res.get(2));
                    int totalDistance = Math.round(res.get(61));
                    gearView.setText(String.valueOf(gear));
                    rpmView.setText(String.valueOf(rpm));
                    String d = currentDistance + "m of " + totalDistance + "m";
                    distanceView.setText(d);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}