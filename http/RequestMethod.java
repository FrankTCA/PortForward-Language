package http;

public class RequestMethod {
  
  int methodId;
  String methodName;
  
  public RequestMethod(String name) {
    methodName = name;
    if (name.equals("GET")) {
      methodId = 0;
    } else if (name.equals("POST")) {
      methodId = 1;
    } else {
      throw new NoSuchRequestException;
    }
  }
  
  public static RequestMethod getMethod(String address) {
    Gson json = new Gson();
    String databasePath = ServerSession.getDatabase();
    File usercache = new File(databasePath + "/usercache.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(usercache)));
    String line;
    String jsonToRead = "";
    while ((line = reader.readLine()) != null) {
      jsonToRead = jsonToRead + line;
    }
    JsonObject fromJson = json.fromJson(jsonToRead, JsonObject.class);
    String[] addresses = fromJson.get("addresses");
    String method;
    String[] methods;
    for (int x = addresses.length; x > 0; x--) {
      if (addresses[x].equals(address)) {
        methods = fromJson.get("methods");
        method = methods[x];
        RequestMethod returnStatement = new RequestMethod(method);
        return returnStatement;
      }
    }
  }
}
