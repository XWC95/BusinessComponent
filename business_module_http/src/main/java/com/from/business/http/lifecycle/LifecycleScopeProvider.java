package com.from.business.http.lifecycle;

import com.uber.autodispose.ScopeProvider;
import com.uber.autodispose.internal.DoNotMock;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Nullable;

/**
 * A convenience interface that, when implemented, helps provide information to create {@link
 * ScopeProvider} implementations that resolve the next corresponding lifecycle event and construct
 * a {@link Completable} representation of it from the {@link #lifecycle()} stream.
 *
 * <p>Convenience resolver utilities for this can be found in {@link LifecycleScopes}.
 *
 * @param <E> the lifecycle event type.
 * @see LifecycleScopes
 */
@DoNotMock(value = "Use TestLifecycleScopeProvider instead")
public interface LifecycleScopeProvider<E> extends ScopeProvider {

  /**
   * @return a sequence of lifecycle events. Note that completion of this lifecycle will also
   * trigger disposal
   */
  @CheckReturnValue Observable<E> lifecycle();

  /**
   * @return a sequence of lifecycle events. It's recommended to back this with a static instance to
   * avoid unnecessary object allocation.
   */
  @CheckReturnValue CorrespondingEventsFunction<E> correspondingEvents();

  /**
   * @return the last seen lifecycle event, or {@code null} if none. Note that is {@code null} is
   * returned at subscribe-time, it will be used as a signal to throw a {@link
   * LifecycleNotStartedException}.
   */
  @Nullable E peekLifecycle();

  @Override default CompletableSource requestScope() {
    return LifecycleScopes.resolveScopeFromLifecycle(this);
  }
}
