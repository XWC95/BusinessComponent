package com.from.business.http.lifecycle;

import com.uber.autodispose.OutsideScopeException;

/**
 * Signifies an error occurred due to execution starting after the lifecycle has ended.
 */
public class LifecycleEndedException extends OutsideScopeException {

  public LifecycleEndedException() {
    this("Lifecycle has ended!");
  }

  public LifecycleEndedException(String s) {
    super(s);
  }
}
