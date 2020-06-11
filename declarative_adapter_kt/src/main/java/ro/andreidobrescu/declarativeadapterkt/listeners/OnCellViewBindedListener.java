package ro.andreidobrescu.declarativeadapterkt.listeners;

import androidx.annotation.NonNull;
import ro.andreidobrescu.declarativeadapterkt.view.CellView;

public interface OnCellViewBindedListener
{
    void onCellViewBindedToModel(@NonNull CellView cellView, Object model);
}