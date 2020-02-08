package ro.andreidobrescu.declarativeadapterkt;

import ro.andreidobrescu.declarativeadapterkt.view.CellView;

public interface OnCellViewBindedListener
{
    void onCellViewBindedToModel(CellView cellView, Object model);
}