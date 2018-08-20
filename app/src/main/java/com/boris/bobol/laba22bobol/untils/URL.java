package com.boris.bobol.laba22bobol.untils;

public class URL {
    static URL _url;
    String url = "http://192.168.0.143";
    private URL(){}


    public static URL getInstance(){
        if(_url == null)
            _url = new URL();
        return _url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
