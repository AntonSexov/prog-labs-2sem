package ifmo;

import ifmo.commands.*;
import ifmo.data.User;
import ifmo.network.TCPClient;
import ifmo.utils.*;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        TCPClient client = new TCPClient();
        Command help = new Help();

        //TODO : registering + login
        User user = new User("test", "test");

        while(true){
            IOHandler.print("> ");
            String input = scanner.nextLine();
            client.sendRequest(input +" placeholderArg", user);
        }
    }
}