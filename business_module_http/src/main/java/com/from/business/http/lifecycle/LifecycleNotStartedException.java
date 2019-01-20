package com.from.business.http.lifecycle;

import com.uber.autodispose.OutsideScopeException;

/**
 * Signifies an error occurred due to execution starting before the lifecycle has started.
 */
public class LifecycleNotStartedException extends OutsideScopeException {

  public LifecycleNotStartedException() {
    this("Lifecycle hasn't started!");
  }

  public LifecycleNotStartedException(String s) {
    super(s);
  }
}
