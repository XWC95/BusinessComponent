package com.from.view.swipeback;

import java.util.List;

/**
 * @version 0.0.9
 * @since 2019-01
 */
public class SwipeExcludeOptions {
    private List<String> mClassNameList;

    private SwipeExcludeOptions(Builder builder) {
        this.mClassNameList = builder.mClassNameList;
    }

    public List<String> getClassNameList() {
        return mClassNameList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Builder() {
        }

        private List<String> mClassNameList;

        public Builder exclude(List<String> classNameList) {
            this.mClassNameList = classNameList;
            return this;
        }

        public SwipeExcludeOptions build() {
            return new SwipeExcludeOptions(this);
        }
    }
}
