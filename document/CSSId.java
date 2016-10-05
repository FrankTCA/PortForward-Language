package document;

public class CSSId {
    String name;
    Webpage page;
    public CSSId(String id, Webpage page1) {
        page = page1;
        name = id;
    }
    
    public int getLocation() {
        String html = page.getHypertext();
        String[] splitString = html.split("class=\"" + name + "\"");
        String[] endedTag = splitString[1].split(">");
        int len = splitString[0].length() + endedTag[0].length();
        len++;
        return len;
    }
}
