package server;

public class Server {
  File root;
  File scripts;
  File database;
  ServerSocket s;
  
  public Server(String rootDir, String scriptDir, String dataDir, int port) {
    root = new File(rootDir);
    scripts = new File(scriptDir);
