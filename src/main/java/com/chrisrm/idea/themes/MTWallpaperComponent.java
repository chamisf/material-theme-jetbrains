/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.idea.themes;

import com.chrisrm.idea.MTConfig;
import com.chrisrm.idea.config.ConfigNotifier;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.jetbrains.annotations.NotNull;

public final class MTWallpaperComponent implements ApplicationComponent {

  private MTConfig instance;

  @Override
  public void initComponent() {
    this.reloadWallpaper();
    instance = MTConfig.getInstance();
    ApplicationManager.getApplication().getMessageBus().connect()
                      .subscribe(ConfigNotifier.CONFIG_TOPIC, mtConfig -> this.reloadWallpaper());
  }

  private void reloadWallpaper() {
    final MTConfig mtConfig = MTConfig.getInstance();
    final String wallpaper = mtConfig.getWallpaper();

    //    String customBg = PropertiesComponent.getInstance().getValue(IdeBackgroundUtil.FRAME_PROP);

    if (mtConfig.isWallpaperSet() && wallpaper != null) {
      PropertiesComponent.getInstance().unsetValue(IdeBackgroundUtil.FRAME_PROP);
      PropertiesComponent.getInstance().setValue(IdeBackgroundUtil.FRAME_PROP, wallpaper);
      IdeBackgroundUtil.repaintAllWindows();
    } else if (mtConfig.isWallpaperSet() && wallpaper == null) {
      PropertiesComponent.getInstance().unsetValue(IdeBackgroundUtil.FRAME_PROP);
      IdeBackgroundUtil.repaintAllWindows();
    }
  }

  @Override
  public void disposeComponent() {
    if (instance.isWallpaperSet()) {
      PropertiesComponent.getInstance().unsetValue(IdeBackgroundUtil.FRAME_PROP);
    }
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "MTWallpaperComponent";
  }
}
