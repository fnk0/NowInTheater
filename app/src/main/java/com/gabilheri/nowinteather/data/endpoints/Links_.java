
package com.gabilheri.nowinteather.data.endpoints;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Links_ {

    @Expose
    private String self;
    @Expose
    private String next;
    @Expose
    private String alternate;

    /**
     * 
     * @return
     *     The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The next
     */
    public String getNext() {
        return next;
    }

    /**
     * 
     * @param next
     *     The next
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * 
     * @return
     *     The alternate
     */
    public String getAlternate() {
        return alternate;
    }

    /**
     * 
     * @param alternate
     *     The alternate
     */
    public void setAlternate(String alternate) {
        this.alternate = alternate;
    }

}
