package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

import java.awt.*;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/24 13:31
 * @Description:
 **/
public class ExtensionLoaderTest {

    @Test
    public void chooseSpiInstance(){
        ExtensionLoader<FrameParser> extensionLoader = ExtensionLoader.getExtensionLoader(FrameParser.class);
        FrameParser adaptive = extensionLoader.getAdaptiveExtension();
        adaptive.doParse("golang");
        FrameParser frameParser = extensionLoader.getExtension("def");
        frameParser.doParse("java");
    }
}
