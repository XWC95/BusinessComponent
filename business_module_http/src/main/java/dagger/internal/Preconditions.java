//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dagger.internal;

public final class Preconditions {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object errorMessageArg) {
        if (reference == null) {
            if (!errorMessageTemplate.contains("%s")) {
                throw new IllegalArgumentException("errorMessageTemplate has no format specifiers");
            } else if (errorMessageTemplate.indexOf("%s") != errorMessageTemplate.lastIndexOf("%s")) {
                throw new IllegalArgumentException("errorMessageTemplate has more than one format specifier");
            } else {
                String argString = errorMessageArg instanceof Class ? ((Class)errorMessageArg).getCanonicalName() : String.valueOf(errorMessageArg);
                throw new NullPointerException(errorMessageTemplate.replace("%s", argString));
            }
        } else {
            return reference;
        }
    }

    private Preconditions() {
    }
}
