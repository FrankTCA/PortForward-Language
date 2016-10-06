package http;

public class Request {
  public static int HTTP_METHOD_GET = 0;
  public static int HTTP_METHOD_POST = 1;
  public static int HTTP_METHOD_HEAD = 2;
  public static int HTTP_METHOD_PUT = 3;
  public static int HTTP_METHOD_DELETE = 4;
  int methodId;
  String fileName;
  File fileInRequest;
  String parameters;
  public Request(int method, String file, String params) {
    methodId = method;
    fileName = file;
    fileInRequest = new File(Settings.getSetting("FILE.ROOT.STRING") + file);
    parameters = params;
  }
  
  public static Request getRequestFromUserCache(int id) {
    Gson json = new Gson();
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Settings.getSetting("FILE.DATABASE.STRING") + "usercache.json")));
    String line;
    String fullJson = "";
    while ((line = reader.readLine()) != null) {
      fullJson = fullJson + line;
    }
    JsonObject parsedJson = json.fromJson(fullJson, JsonObject.class);
    String[] methodStrs = parsedJson.get("methods");
    int methodId1;
    if (methodStrs[id] == "GET") {
      methodId1 = 0;
    } else if (methodStrs[id] == "POST") {
      methodId1 = 1;
    } else if (methodStrs[id] == "HEAD") {
      methodId1 = 2;
    } else if (methodStrs[id] == "PUT") {
      methodId1 = 3;
    } else if (methodStrs[id] == "DELETE") {
      methodId1 = 4;
    } else {
      throw new ArrayIndexOutOfBoundsException();
    }
    String[] files = parsedJson.get("files");
    String file = files[id];
    return new Request(methodId1, file);
  }
  
  public int sendRequestToServer(String inetAddress, int port) {
    Socket c = new Socket(inetAddress, port);
    PrintWriter out = new PrintWriter(c.getOutputStream, true);
    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream));
    switch (methodId) {
      case 0:
        out.write("GET " + fileName + "?" + parameters + " HTTP/1.1");
        out.write("Host: " + inetAddress);
        break;
      case 1:
        out.write("POST " + fileName + " HTTP/1.1");
        out.write(parameters);
        out.write("Host: " + inetAddress);
        break;
      case 2:
        out.write("HEAD " + fileName + "?" + parameters + " HTTP/1.1");
        out.write("Host: " + inetAddress);
        break;
      case 4:
        out.write("DELETE " + fileName + " HTTP/1.1");
        out.write("Host: " + inetAddress);
        break;
      default:
        System.out.println("Sorry, you cannot run PUT requests, or you the id limit is 4.");
    }
    
  }
}
