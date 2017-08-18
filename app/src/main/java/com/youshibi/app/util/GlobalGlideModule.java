package com.youshibi.app.util;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by Chu on 2017/7/22.
 */
@GlideModule
public class GlobalGlideModule extends AppGlideModule{

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
