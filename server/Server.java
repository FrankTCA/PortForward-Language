package server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {
  File root;
  File scripts;
  File database;
  ServerSocket s;
  
  public Server(String rootDir, String scriptDir, String dataDir, int port) {
    root = new File(rootDir);
    scripts = new File(scriptDir);
    database = new File(dataDir);
    s = new ServerSocket(port);
    Data date = new Date();
    System.out.println("[" + date.toString() + "] STANDARD: Server started on port " + port);
  }
  
  public int waitAndRespondToClientRequest() {
    Socket c = s.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
    PrintWriter out = new PrintWriter(c.getOutputStream, true);
    // read data sent from client.
    String line;
    String[] brokenLine;
    while ((line = in.readLine()) != null) {
      brokenLine = line.split(" ");
      // process HTTP get request
      if (brokenLine[0].equals("GET")) {
        File requestedFile = new File(root.getPath() + brokenLine[1]);
        if (!requestedFile.exists()) {
          out.write("404 Not Found\n");
          out.write("<!DOCTYPE html><html><head><title>HTTP Error 404</title></head><body><h1>404 - Not Found</h1><p>The file you requested was not found on this server, if you followed a link on this server, please contact the webmaster.</p><p>PortForward Server</p></body></html>");
          c.close();
          return 404;
        }
        // check if user is banned
        File bannedJson = new File(database.getPath() + "/banned.json");
        if (bannedJson.exists()) {
          BufferedReader bannedReader = new BufferedReader(new FileInputStream(bannedJson));
          String json = "";
          String lineA;
          while ((lineA = bannedReader.readLine()) != null) {
            json = json + lineA;
          }
          Json jsonReader = new Json();
          String[] ips = jsonReader.parseJson(json, String[].class);
          String ip = c.getInetAddress().toString();
          for (int x = ips.length; x > 0; x--) {
            if (ips[x].equals(ip)) {
              out.write("403 Forbidden\n");
              out.write("<!DOCTYPE html><html><head><title>HTTP Error 403</title></head><body><h1>403 - Forbidden</h1><p>You are banned from this server. :( If you belive this is an error, please contact the webmaster.</p><p>PortForward Server</p></body></html>");
              c.close();
              return 403;
            }
          }
          ZipFile pfar = new ZipFile(requestedFile);
          ZipInputStream zip = new ZipInputStream(new FileInputStream(requestedFile));
          ZipEntry entry = zip.getNextEntry();
          while (zip.getNextEntry() != null) {
            if (entry.getName().equals("info.pf")) {
              
