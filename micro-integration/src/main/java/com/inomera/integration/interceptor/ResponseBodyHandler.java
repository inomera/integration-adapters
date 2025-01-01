package com.inomera.integration.interceptor;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.HttpAdapterResponse;
import com.inomera.integration.type.BaseAdapter;

/**
 * Response handler function to give access to user to create an adapter response. This handler can
 * throw {@link AdapterException} and that will be handled by
 * {@link BaseAdapter} exception handlers.
 *
 * @param <O> http response body data type.
 * @param <T> Adapter response data type.
 * @author Burak Dogan Akyildiz, Turgay Can.
 * @see BaseAdapter execute method.
 */
@FunctionalInterface
public interface ResponseBodyHandler<O, T> {

    AdapterResponse<T> apply(HttpAdapterResponse<O> o) throws AdapterException;
}
