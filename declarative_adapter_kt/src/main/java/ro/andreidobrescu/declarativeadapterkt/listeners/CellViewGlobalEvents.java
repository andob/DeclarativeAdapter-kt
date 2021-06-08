package ro.andreidobrescu.declarativeadapterkt.listeners;

import androidx.annotation.Nullable;

public class CellViewGlobalEvents
{
    private static @Nullable OnCellViewInflatedListener onCellViewInflatedListener;
    private static @Nullable OnCellViewBindedListener onCellViewBindedListener;

    public static @Nullable OnCellViewInflatedListener getOnCellViewInflatedListener()
    {
        return onCellViewInflatedListener;
    }

    public static void setOnCellViewInflatedListener(@Nullable OnCellViewInflatedListener onCellViewInflatedListener)
    {
        CellViewGlobalEvents.onCellViewInflatedListener = onCellViewInflatedListener;
    }

    public static @Nullable OnCellViewBindedListener getOnCellViewBindedListener()
    {
        return onCellViewBindedListener;
    }

    public static void setOnCellViewBindedListener(@Nullable OnCellViewBindedListener onCellViewBindedListener)
    {
        CellViewGlobalEvents.onCellViewBindedListener = onCellViewBindedListener;
    }
}
