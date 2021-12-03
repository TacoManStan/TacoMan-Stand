package com.taco.suit_lady.view.ui.ui_internal.contents_old;

public class DummyContentsInstancePane extends ContentPane<DummyInstance>
{
    public DummyContentsInstancePane()
    {
        super((oldContents, newContents) -> {
            if (newContents != null)
                return newContents.ui().getContainerPane();
            return null;
        });
    }
}
