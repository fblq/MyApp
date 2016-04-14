package com.zg.compus.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by zhong.zhou on 16/4/13.
 */
public class ListenThread implements Callable<Object> {
    //listening port
    private int port;

    public ListenThread(int port) {
        this.port = port;
    }

    @Override
    public Object call() throws Exception {
        ServerSocket serverSocket = new ServerSocket(this.port);
        while (true) {
            //代码阻塞,将一直等待连接,直至成功
            Socket socket = serverSocket.accept();
            //产生ReceiveThread的执行结果
            Callable<Object> callable = new ReceiveThread(socket);
            //接收ReceiveThread的执行结果
            FutureTask<Object> futureTask = new FutureTask<Object>(callable);
            //异步执行ReceiveThread
            new Thread(futureTask).start();
        }
    }
}
