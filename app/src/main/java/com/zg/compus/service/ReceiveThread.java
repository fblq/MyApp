package com.zg.compus.service;

import android.util.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by zhong.zhou on 16/4/13.
 */
public class ReceiveThread implements Callable<Object> {
    //线程处理的socket
    private Socket socket;
    //线程所处理的Socket对应的输入流
    private BufferedReader br;

    public ReceiveThread(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "uTf-8"));
    }

    @Override
    public Object call() throws Exception {
        String hexStr = "0123456789ABCDEF";
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.socket.getInetAddress().getHostAddress() + ".txt"));
        StringBuilder stringBuilder = new StringBuilder();
        char[] data = new char[6];
        while ((br.read(data, 0, 6)) != -1) {
            for (char c : data) {
                stringBuilder.append("-").append(hexStr.charAt(c & 0xf000)).append(hexStr.charAt(c & 0x0f00))
                        .append("-").append(hexStr.charAt(c & 0x00f0)).append(hexStr.charAt(c & 0x000f));
            }
            printWriter.print(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }
        return null;
    }
}
