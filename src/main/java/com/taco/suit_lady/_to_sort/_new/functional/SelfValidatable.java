package com.taco.suit_lady._to_sort._new.functional;

import com.taco.suit_lady._to_sort._new.ValueGroup;

/**
 * <p>Contains a single method, {@link #isValid(boolean)}, that checks if the contents of the {@link SelfValidatable} are valid.</p>
 * <p>The primary purpose of {@link #isValid(boolean)} is to make it easier to identify bugs caused by a corrupt or otherwise improperly-constructed object.</p>
 * <p>For example, the following might cause an object instance to be invalid:</p>
 * <ol>
 *     <li>An object that is accessed asynchronously but does not have synchronization implemented properly (or at all).</li>
 *     <li>An object with dependencies that were incorrectly injected via {@code Spring}.</li>
 *     <li>
 *          An object that has specific rules that are mistakenly broken by a future change.<br>
 *          For Example:<br>
 *          The {@link ValueGroup} class requires both internal arrays — {@code values} and {@code keys} — to be of the same length.<br>
 *          If a future change makes it possible for the internal arrays to have different lengths,
 *          any {@link ValueGroup} instance that is created as such will be flagged as {@link #isValid(boolean) invalid.}
 *     </li>
 * </ol>
 */
public interface SelfValidatable
{
    /**
     * <p>Checks if this {@link SelfValidatable} is {@link #isValid(boolean) valid}.</p>
     * <p><i>See above for details.</i></p>
     *
     * @param throwException True if a detailed {@link RuntimeException Exception} should be thrown if this {@link SelfValidatable} is {@code invalid},
     *                       false if it should follow through with a {@code return value} depicting the validity instead.
     * @return True if this {@link SelfValidatable} is {@link #isValid(boolean) valid}, false if it is not.
     */
    boolean isValid(boolean throwException);
}
