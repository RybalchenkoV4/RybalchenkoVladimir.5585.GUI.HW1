package sever;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI(serverWindow, "Petya");
        new ClientGUI(serverWindow, "Vasya");
    }
}
