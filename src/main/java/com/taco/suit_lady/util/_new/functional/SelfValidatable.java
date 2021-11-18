package com.taco.suit_lady.util._new.functional;

import com.taco.suit_lady.util._new.ValueChange;

/**
 * <p>Contains a single method, {@link #validate()}, that checks if the contents of the {@link SelfValidatable} are valid.</p>
 * <p>The primary purpose of {@link #validate()} is to make it easier to identify bugs caused by a corrupt or otherwise improperly-constructed object.</p>
 * <p>For example, the following might cause an object instance to be invalid:</p>
 * <ol>
 *     <li>An object that is accessed asynchronously but does not have synchronization implemented properly (or at all).</li>
 *     <li>An object with dependencies that were incorrectly injected via {@code Spring}.</li>
 *     <li>
 *          An object that has specific rules that are mistakenly broken by a future change.<br>
 *          For Example:<br>
 *          The {@link ValueChange} class requires both internal arrays — {@code values} and {@code keys} — to be of the same length.<br>
 *          If a future change makes it possible for the internal arrays to have different lengths,
 *          any {@link ValueChange} instance that is created as such will be flagged as {@link #validate() invalid.}
 *     </li>
 * </ol>
 */
public interface SelfValidatable
{
    /**
     * <p>Checks if this {@link SelfValidatable} is {@link #validate() valid}.</p>
     * <p><i>See above for details.</i></p>
     *
     * @return True if this {@link SelfValidatable} is {@link #validate() valid}, false if it is not.
     */
    boolean validate();
}
