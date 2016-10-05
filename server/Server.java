package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import document.Webpage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Server {
  File root;
  File scripts;
  File database;
  ServerSocket s;
  
  public Server(String rootDir, String scriptDir, String dataDir, int port) throws IOException {
    root = new File(rootDir);
    scripts = new File(scriptDir);
    database = new File(dataDir);
    s = new ServerSocket(port);
    Date date = new Date();
    System.out.println("[" + date.toString() + "] STANDARD: Server started on port " + port);
  }
  
  public int waitAndRespondToClientRequest() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Socket c = s.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
    PrintWriter out = new PrintWriter(c.getOutputStream(), true);
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
          BufferedReader bannedReader = new BufferedReader(new InputStreamReader(new FileInputStream(bannedJson)));
          String json = "";
          String lineA;
          while ((lineA = bannedReader.readLine()) != null) {
            json = json + lineA;
          }
          Gson jsonReader = new Gson();
          String[] ips = jsonReader.fromJson(json, String[].class);
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
            if (entry.getName().equals("info.json")) {
              BufferedReader infoReader = new BufferedReader(new InputStreamReader(pfar.getInputStream(entry)));
              String infoLine;
              String fullInfo = "";
              while ((infoLine = infoReader.readLine()) != null) {
                fullInfo = fullInfo + infoLine;
              }
              JsonObject objInfo = jsonReader.fromJson(fullInfo, JsonObject.class);
              JsonElement scriptNameEle = objInfo.get("script");
              String scriptName = scriptNameEle.getAsString();
              Webpage webpage = new Webpage(requestedFile);
              if (scriptName != null) {
                JsonElement mainEle = objInfo.get("main");
                String main = mainEle.getAsString();
                File scriptFileFile = new File(scripts.getPath() + scriptName);
                JarFile scriptFile = new JarFile(scripts.getPath() + scriptName);
                URLClassLoader scriptLoader = new URLClassLoader(new URL[]{scriptFileFile.toURI().toURL()}, this.getClass().getClassLoader());
                Class classToLoad = Class.forName(main, true, scriptLoader);
                Method method = classToLoad.getDeclaredMethod("onLoad");
                Object[] args = {new Webpage[]{webpage}};
                method.invoke(null, args);
              }
              out.write("200 OK\n");
              out.write(webpage.getHypertext());
              c.close();
              return 200;
            }
          }
        }
      }
    }
    return 400;
  }
}
