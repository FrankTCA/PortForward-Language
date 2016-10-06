package http;

public class RequestMethod {
  public Request getMethod(String address) {
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
        Method returnStatement = new Method(method);
        return returnStatement;
      }
    }
  }
}
