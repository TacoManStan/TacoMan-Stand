package com.taco.suit_lady.logic.game.objects.commands;

import com.taco.suit_lady.logic.game.execution.AutoManagedTickable;
import com.taco.suit_lady.logic.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * <p>{@link Command} objects are {@link AutoManagedTickable} instances tailed specifically for {@link GameObject GameObjects}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>For {@link Command} construction, either extend {@link Command} manually or use one of the factory methods found in the {@link Commands} utility class.</li>
 * </ol>
 */
public abstract class Command extends AutoManagedTickable<GameObject> {
    
    protected Command(@Nullable GameObject owner, @NotNull Predicate<GameObject> autoCancelCondition) {
        super(owner, autoCancelCondition);
    }
}
