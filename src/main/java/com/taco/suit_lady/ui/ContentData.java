package com.taco.suit_lady.ui;

public class ContentData<T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>,
        F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>> { }
