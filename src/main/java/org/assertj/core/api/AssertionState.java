/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

/**
 * Base contract of all assertion objects: the minimum functionality that any assertion object should provide.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public interface AssertionState<SELF extends AssertionState<SELF, ACTUAL>, ACTUAL> extends Descriptable<SELF> {

	
  SELF myself();
	
  ACTUAL actual();
	
  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  SELF usingComparator(Comparator<? super ACTUAL> customComparator);

  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator, "Hobbit Race Comparator").isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @param customComparatorDescription comparator description to be used in assertion error messages
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription);

  /**
   * Revert to standard comparison for the incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling {@link #usingComparator(Comparator) usingComparator}.
   *
   * @return {@code this} assertion object.
   */
  SELF usingDefaultComparator();

  /**
   * In case of an assertion error, a thread dump will be printed to {@link System#err}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat("Messi").withThreadDumpOnError().isEqualTo("Ronaldo");</code></pre>
   *
   * will print a thread dump, something similar to this:
   * <pre>{@code "JDWP Command Reader"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "JDWP Event Helper Thread"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "JDWP Transport Listener: dt_socket"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "Signal Dispatcher"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "Finalizer"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
   * 		at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:189)
   *
   * "Reference Handler"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.Object.wait(Object.java:503)
   * 		at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:133)
   *
   * "main"
   * 	java.lang.Thread.State: RUNNABLE
   * 		at sun.management.ThreadImpl.dumpThreads0(Native Method)
   * 		at sun.management.ThreadImpl.dumpAllThreads(ThreadImpl.java:446)
   * 		at org.assertj.core.internal.Failures.threadDumpDescription(Failures.java:193)
   * 		at org.assertj.core.internal.Failures.printThreadDumpIfNeeded(Failures.java:141)
   * 		at org.assertj.core.internal.Failures.failure(Failures.java:91)
   * 		at org.assertj.core.internal.Objects.assertEqual(Objects.java:314)
   * 		at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:198)
   * 		at org.assertj.examples.ThreadDumpOnErrorExample.main(ThreadDumpOnErrorExample.java:28)}</pre>
   *
   * @return this assertion object.
   */
  SELF withThreadDumpOnError();

  /**
   * Use the given {@link Representation} to describe/represent values in AssertJ error messages.
   * <p>
   * The usual way to introduce a new {@link Representation} is to extend {@link StandardRepresentation}
   * and override any existing {@code toStringOf} methods that don't suit you. For example you can control
   * {@link Date} formatting by overriding {@link StandardRepresentation#toStringOf(Date)}).
   * <p>
   * You can also control other types format by overriding {@link StandardRepresentation#toStringOf(Object)})
   * calling your formatting method first and then fall back to the default representation by calling {@code super.toStringOf(Object)}.
   * <p>
   * Example :
   * <pre><code class='java'> private class Example {}
   *
   * private class CustomRepresentation extends StandardRepresentation {
   *
   *   // override needed to hook specific formatting
   *   {@literal @}Override
   *   public String toStringOf(Object o) {
   *     if (o instanceof Example) return "Example";
   *     // fall back to default formatting
   *     return super.toStringOf(o);
   *   }
   *
   *   // change String representation
   *   {@literal @}Override
   *   protected String toStringOf(String s) {
   *     return "$" + s + "$";
   *   }
   * }
   *
   * // next assertion fails with error : "expected:&lt;[null]&gt; but was:&lt;[Example]&gt;"
   * Example example = new Example();
   * assertThat(example).withRepresentation(new CustomRepresentation())
   *                    .isNull(); // example is not null !
   *
   * // next assertion fails ...
   * assertThat("foo").withRepresentation(new CustomRepresentation())
   *                  .startsWith("bar");
   * // ... with error :
   * Expecting:
   *  &lt;$foo$&gt;
   * to start with:
   *  &lt;$bar$&gt;</code></pre>
   *
   * @param representation Describe/represent values in AssertJ error messages.
   * @return this assertion object.
   */
  SELF withRepresentation(Representation representation);
}
