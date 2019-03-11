package com.hamaksoftware.mydota.utils;

import android.content.DialogInterface;

public interface IDialog {
    public abstract void PositiveMethod(DialogInterface dialog, int id);

    public abstract void NegativeMethod(DialogInterface dialog, int id);
}
