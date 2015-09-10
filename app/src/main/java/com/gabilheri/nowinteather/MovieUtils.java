package com.gabilheri.nowinteather;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
// final makes this class not be able to be extended
public final class MovieUtils {

    // Makes this class not be able to be Instantiated
    // i.e new MovieUtils()
    private MovieUtils() {}

    /**
     * Modifies the URL to get a full size image rather than a VERY small image
     * @param url
     *      The image URL
     * @return
     *      The modified URL
     */
    public static String getHighResPicUrl(String url) {
        String[] splitUrl = url.split(Const.PIC_INIT_URL);
        if(splitUrl.length > 1) {
            return String.format("http://%s%s", Const.PIC_INIT_URL, splitUrl[1]);
        } else {
            return url;
        }
    }
}
