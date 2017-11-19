package com.lh.nexusunsky.baselib.log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nexusunsky
 */
public class Settings {
    private List<Interceptor> interceptors = new ArrayList<>();
    private String tag;

    public Settings add(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public Settings tag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() {
        return this.tag;
    }

}
