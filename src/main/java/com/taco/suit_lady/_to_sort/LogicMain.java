package com.taco.suit_lady._to_sort;

import org.springframework.stereotype.Component;

/**
 * Root, highest-level logic handler.
 * Nothing contained in LogicMain or elsewhere in its child components (or their children etc.) can contain View-specific values, operations, etc.
 * LogicMain will *eventually* be the backend, but for now, it will simply be a 100% decoupled module that *mimics* the backend control.
 * In the future, it is possible that having both a Node.js backend AND a Java backend would be most beneficial.
 * This would be great for a portfolio as it shows your ability to work with backend system using multiple technologies.
 * It would also remove the need to use 3rd party "API bridge" libraries that allow frontend systems that are designed to work better (or only) with a Node.js backend to communicate instead with your Java backend.
 * Regardless, everything - and I mean EVERYTHING - must be decoupled from the moment you start writing a module.
 */
@Component
public class LogicMain
{
}
