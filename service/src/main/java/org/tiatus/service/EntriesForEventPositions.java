package org.tiatus.service;

import org.tiatus.entity.Entry;
import org.tiatus.entity.EventPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 13/04/2017.
 */
public class EntriesForEventPositions {
    private List<EventPosition> positions;
    private List<Entry> entries = new ArrayList<>();

    public List<EventPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<EventPosition> positions) {
        this.positions = positions;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }
}
