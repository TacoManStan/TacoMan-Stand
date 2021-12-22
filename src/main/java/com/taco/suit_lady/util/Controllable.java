package com.taco.suit_lady.util;

import com.taco.suit_lady.ui.ui_internal.controllers.Controller;

public interface Controllable
{
    Class<? extends Controller> controllerClass();
}
