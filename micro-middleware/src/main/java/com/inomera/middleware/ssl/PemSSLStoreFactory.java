package com.inomera.middleware.ssl;

import com.inomera.integration.config.model.PemSSLBundleProperties.Store;
import com.inomera.middleware.ssl.pem.PemSSLStore;
import java.io.IOException;

/**
 * Factory used to create a {@link PemSSLStore} from store properties.
 */
public interface PemSSLStoreFactory {

  PemSSLStore getPemSSLStore(String bundleName, String storePropertyName, Store storeProperties)
      throws IOException;
}
