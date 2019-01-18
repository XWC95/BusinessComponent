package com.from.business.http.module.http;

import dagger.internal.Factory;
import com.from.business.http.log.FormatPrinter;
import dagger.internal.Preconditions;

public final class HttpConfigModuleFormatPrinterFactory
    implements Factory<FormatPrinter> {
    private final HttpConfigModule module;

    public HttpConfigModuleFormatPrinterFactory(HttpConfigModule module) {
        this.module = module;
    }

    @Override
    public FormatPrinter get() {
        return provideInstance(module);
    }

    public static FormatPrinter provideInstance(HttpConfigModule module) {
        return proxyProvideFormatPrinter(module);
    }

    public static HttpConfigModuleFormatPrinterFactory create(HttpConfigModule module) {
        return new HttpConfigModuleFormatPrinterFactory(module);
    }

    public static FormatPrinter proxyProvideFormatPrinter(HttpConfigModule instance) {
        return Preconditions.checkNotNull(
            instance.provideFormatPrinter(),
            "Cannot return null from a non-@Nullable @Provides method");
    }
}
