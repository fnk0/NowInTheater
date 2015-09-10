package com.gabilheri.nowinteather.base;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/3/15.
 */
public interface RxCallback<T> {

    /**
     * This method is called each time a item is emitted by our Observable
     *
     * @param data
     *      The emitted data
     */
    void onDataReady(T data);

    /**
     * Called when there is a error on our stream
     *
     * @param e
     *      The exception throw
     */
    void onDataError(Throwable e);
}
