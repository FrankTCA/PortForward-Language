package server;

public class Start {
  public static void main(String[] args) {
    if (args.length == 4) {
      int port = Integer.parseInt(args[3]);
      Server server = new Server(args[0], args[1], args[2], port);
      while (true) {
        int response = server.waitAndRespondToClientRequest();
        Date date = new Date();
        System.out.println("[" + date.toString() + "] STANDARD: HTTP response code " + response + " given to client request.");
      }
    } else {
      Date date = new Date();
      System.out.println("[" + date.toString() + "] FATAL: Invalid arguments. Please use arguments <root directory> <script directory> <database> <port number>");
    }
  }
}
