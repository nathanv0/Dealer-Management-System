package org.example.groupproject3;

import java.util.List;

public interface FileHandler {
    List<Dealer> getDealers(String fileName);
    void saveDealers(List<Dealer> dealers, String outputPath);
}
