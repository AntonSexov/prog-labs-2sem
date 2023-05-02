package ifmo.network;

import ifmo.requests.Request;
import ifmo.utils.*;
import ifmo.commands.Command;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPServer{
    private ServerSocketChannel serverSocketChannel;
    private int port = 3333;
    protected SocketChannel clientSocket;
    private Logger logger = Logger.getLogger("logger");

    public void start(HashMap<String, Command> map, CollectionHandler collectionHandler){
        openServerSocket();
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(serverSocketChannel!=null){
            logger.log(Level.INFO,"Ожидание подключения...");
            try{
                this.clientSocket = serverSocketChannel.accept();
                logger.log(Level.FINER, "Подключение успешно");
                // processRequest(map);
                executorService.submit(new RequestHandler(map, clientSocket, logger));
            } catch (IOException ioe) {
                logger.log(Level.SEVERE,"Не удалось подключиться к клиенту: ", ioe.getMessage());
            }
        }
        closeServerSocket();
        executorService.shutdown();
    }

    private void openServerSocket() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Ошибка при открытии соединения", e.getMessage());
        }
    }

    private void closeServerSocket() {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Ошибка при закрытии соединения", e.getMessage());
        }
    }

    public Socket getClientSocket(){
        return clientSocket.socket();
    }

    private static class RequestHandler implements Runnable {
        private HashMap<String, Command> map;
        private SocketChannel clientSocket;
        private Logger logger;
        
        public RequestHandler(HashMap<String, Command> map, SocketChannel clientSocket, Logger logger) {
            this.map = map;
            this.clientSocket = clientSocket;
            this.logger = logger;
        }
    
        @Override
        public void run() {
            try {
                processRequest(map);
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Ошибка при обработке запроса: ", e.getMessage());
            }
        }
    
        private boolean processRequest(HashMap<String, Command> map) throws IOException, ClassNotFoundException {
            ObjectInput objectInput = new ObjectInputStream(clientSocket.socket().getInputStream());
            Request request = (Request) objectInput.readObject();
            if(map.containsKey(request.getCommandName())){
                map.get(request.getCommandName()).execute(request);
            } else {
                logger.log(Level.SEVERE,"Неизвестная полученная команда");
            }
            objectInput.close();
            return true;
        }
    
    }
}
