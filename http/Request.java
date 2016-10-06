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
  public Request(int method, String file) {
    methodId = method;
    fileName = file;
    fileInRequest = new File(Settings.getSetting("FILE.ROOT.STRING") + file);
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
}
