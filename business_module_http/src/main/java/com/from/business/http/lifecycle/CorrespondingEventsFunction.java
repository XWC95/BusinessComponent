package com.from.business.http.lifecycle;

import com.uber.autodispose.OutsideScopeException;
import io.reactivex.functions.Function;

/**
 * A corresponding events function that acts as a normal {@link Function} but ensures a single event
 * type in the generic and tightens the possible exception thrown to {@link OutsideScopeException}.
 *
 * @param <E> the event type.
 */
public interface CorrespondingEventsFunction<E> extends Function<E, E> {

  /**
   * Given an event {@code event}, returns the next corresponding event that this lifecycle should
   * dispose on.
   *
   * @param event the source or start event.
   * @return the target event that should signal disposal.
   * @throws OutsideScopeException if the lifecycle exceeds its scope boundaries.
   */
  @Override E apply(E event) throws OutsideScopeException;
}
