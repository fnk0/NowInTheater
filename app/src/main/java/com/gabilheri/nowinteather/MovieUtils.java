package com.gabilheri.nowinteather;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MovieUtils {

    public static String getHighResPicUrl(String url) {
        String[] splitUrl = url.split(Const.PIC_INIT_URL);
        if(splitUrl.length > 1) {
            return String.format("http://%s%s", Const.PIC_INIT_URL, splitUrl[1]);
        } else {
            return url;
        }
    }

}
