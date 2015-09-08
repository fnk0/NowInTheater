
package com.gabilheri.nowinteather.data.endpoints;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class ReleaseDates {

    @Expose
    private String theater;

    /**
     * 
     * @return
     *     The theater
     */
    public String getTheater() {
        return theater;
    }

    /**
     * 
     * @param theater
     *     The theater
     */
    public void setTheater(String theater) {
        this.theater = theater;
    }

}
