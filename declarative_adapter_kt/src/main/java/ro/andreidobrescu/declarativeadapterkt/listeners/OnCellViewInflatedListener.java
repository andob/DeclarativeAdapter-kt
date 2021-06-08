package ro.andreidobrescu.declarativeadapterkt.listeners;

import androidx.annotation.NonNull;
import ro.andreidobrescu.declarativeadapterkt.view.CellView;

@SuppressWarnings("rawtypes")
public interface OnCellViewInflatedListener
{
    void onCellViewInflated(@NonNull CellView cellView);
}
