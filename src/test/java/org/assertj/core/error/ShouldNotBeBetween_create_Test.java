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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeBetween.shouldNotBeBetween;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.DateUtil.parse;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldNotBeBetween#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Joel Costigliola
 */
public class ShouldNotBeBetween_create_Test {

  @Test
  public void should_create_error_message_with_period_boundaries_included() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeBetween(parse("2009-01-01"), parse("2011-01-01"), parse("2012-01-01"),
                                                     true, true);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   " <2009-01-01T00:00:00.000>%n" +
                                   "not to be in period:%n" +
                                   " [2011-01-01T00:00:00.000, 2012-01-01T00:00:00.000]"));
  }

  @Test
  public void should_create_error_message_with_period_lower_boundary_included() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeBetween(parse("2012-01-01"), parse("2011-01-01"), parse("2012-01-01"),
                                                     true, false);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   " <2012-01-01T00:00:00.000>%n" +
                                   "not to be in period:%n" +
                                   " [2011-01-01T00:00:00.000, 2012-01-01T00:00:00.000["));
  }

  @Test
  public void should_create_error_message_with_period_upper_boundary_included() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeBetween(parse("2011-01-01"), parse("2011-01-01"), parse("2012-01-01"),
                                                     false, true);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   " <2011-01-01T00:00:00.000>%n" +
                                   "not to be in period:%n" +
                                   " ]2011-01-01T00:00:00.000, 2012-01-01T00:00:00.000]"));
  }

  @Test
  public void should_create_error_message_with_period_boundaries_excluded() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeBetween(parse("2011-01-01"), parse("2011-01-01"), parse("2012-01-01"),
                                                     false, false);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   " <2011-01-01T00:00:00.000>%n" +
                                   "not to be in period:%n" +
                                   " ]2011-01-01T00:00:00.000, 2012-01-01T00:00:00.000["));
  }
}
