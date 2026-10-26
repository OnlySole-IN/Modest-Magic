package com.onlysole.modest_magic.platform;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.platform.services.IPlatformHelper;
import com.onlysole.modest_magic.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRIES = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ModestMagic.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}