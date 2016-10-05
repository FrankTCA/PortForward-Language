package server;

public class Start {
  public static void main(String[] args) {
    if (args.length == 3) {
      Server server = new Server(args[0], args[1], args[2]);
      while (true) {
        int response = server.waitAndRespondToClientRequest();
        System.out.println()
      }
    } else {
      Date date = new Date();
      System.out.println("[" + date.toString() + "] FATAL: Invalid arguments. Please use arguments <root directory> <script directory> <database>");
    }
  }
}
