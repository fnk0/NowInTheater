
package com.gabilheri.nowinteather.data.endpoints;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class AlternateIds {

    @Expose
    private String imdb;

    /**
     * 
     * @return
     *     The imdb
     */
    public String getImdb() {
        return imdb;
    }

    /**
     * 
     * @param imdb
     *     The imdb
     */
    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

}
