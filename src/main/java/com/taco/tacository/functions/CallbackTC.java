package com.taco.tacository.functions;

import java.util.concurrent.Callable;

/**
 * <p><b>A custom {@code Callback} implementation.</b></p>
 * <br><hr><br>
 * <p><b>Details</b></p>
 * <ul>
 *     <li>
 *         {@link CallbackTC} implementations are intentionally independent from existing {@code Callback} implementations.
 *         <ul>
 *             <li>This is to prevent any accidental misuse in existing frameworks.</li>
 *             <li>In other words, if an existing {@code Callback} implementation was used instead, any {@code Callback} instances constructed for use in the {@code Tacository Library} could also be mistakenly passed to unrelated functions.</li>
 *             <li>
 *                 {@link CallbackTC} implementations can also be used as a {@link Callable} from the {@link java.util.concurrent Standard JDK}.
 *                 <br>
 *                 -> Note that when used as a {@link Callable}, the {@link K Instruction Kit} is ignored due to {@link Callable#call() Unparameterized Call}.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>To be used <i>only</i> within the confines of the {@code Tacository} library and/or {@code Implementing Projects}.</li>
 * </ul>
 * <hr>
 */
public interface CallbackTC<K, R> extends Callable<R>
{
    /**
     * <p><b>Defines instructions for constructing an object of type {@code R}.</b></p>
     * <br>
     * <hr>
     * <br>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>Can optionally use additional data — a parameter of type {@code IK} — to offer improved flexibility and versatility to the {@code callback instructions}.</li>
     * </ul>
     * <hr>
     * <br>
     *
     * @param instructionKit An object containing any data required to construct the returned value.
     * @return An object of type R that may be determined based on the provided parameter value.
     */
    R call(K instructionKit);
}
