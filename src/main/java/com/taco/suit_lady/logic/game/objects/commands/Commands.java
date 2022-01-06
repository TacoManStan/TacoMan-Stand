package com.taco.suit_lady.logic.game.objects.commands;

import com.taco.suit_lady.logic.game.objects.GameObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Commands {
    private Commands() { } //No Instance
    
    public static @NotNull Command newSimpleOrder(@NotNull GameObject owner, @NotNull Consumer<GameObject> order) {
        return newSimpleOrder(owner, null, order);
    }
    
    @Contract("_, _, _ -> new")
    public static @NotNull Command newSimpleOrder(@NotNull GameObject owner, @Nullable Predicate<GameObject> autoRemoveCondition, @NotNull Consumer<GameObject> order) {
        return new Command(owner, autoRemoveCondition) {
            @Override protected void step() {
                order.accept(getOwner());
            }
        };
    }
}
