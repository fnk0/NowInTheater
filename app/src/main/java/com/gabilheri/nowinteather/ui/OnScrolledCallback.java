package com.gabilheri.nowinteather.ui;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/4/15.
 */
public interface OnScrolledCallback {

    /**
     * This method will get called every time we hit the bottom of the screen and need to call
     * the Api for more data
     *
     * @param page
     *      The current page to be called
     */
    void onScrolled(int page);
}
