package ifmo.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class OutputSocketWriter implements Runnable {

    private final Socket socket;
    private final String msg;
    BlockingQueue<String> messageQueue;

    public OutputSocketWriter(Socket socket, BlockingQueue<String> messageQueue) throws InterruptedException{
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.msg = messageQueue.take();
    }

    @Override
    public void run() {
        try {
            if (!socket.isClosed()) {
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                outputStream.println(msg);
            } else {
                System.out.println("Сокет закрыт");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при отправке ответа: " + e.getMessage());
        }
    }
}