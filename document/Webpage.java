package document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Webpage {
    File archive;
    ZipEntry page;
    String hypertext = "";
    ZipFile pfarZip;
    ZipInputStream zip;
    public Webpage(File pfar) throws IOException {
        archive = pfar;
        pfarZip = new ZipFile(pfar);
        zip = new ZipInputStream(new FileInputStream(pfar));
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("page.html")) {
                InputStream input = pfarZip.getInputStream(entry);
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = inputReader.readLine()) != null) {
                    hypertext = hypertext + line;
                }
                page = entry;
            }
        }
    }
    
    public String append(String toAppend) {
        hypertext = hypertext + toAppend;
        return hypertext;
    }
    
    public String prepend(String toPrepend) {
        hypertext = toPrepend + hypertext;
        return hypertext;
    }
    
    public String getHypertext() {
        return hypertext;
    }
    
    public String setText(String toSet, String title) {
        hypertext = "<!DOCTYPE html><head><title>" + title + "</title></head><body>" + toSet + "</body></html>";
        return hypertext;
    }
    
    public void setWholePage(String toSet) {
        hypertext = toSet;
    }
    
    public String setTextById(CSSId id, String text) {
        int lengthToInner = id.getLocation();
        hypertext = hypertext.substring(0, lengthToInner) + text + hypertext.substring(lengthToInner + text.length(), hypertext.length());
        return hypertext;
    }
}
