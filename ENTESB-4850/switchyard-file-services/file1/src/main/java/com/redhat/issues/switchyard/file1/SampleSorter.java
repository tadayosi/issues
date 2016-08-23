package com.redhat.issues.switchyard.file1;

import org.apache.camel.component.file.GenericFile;

import javax.inject.Named;
import java.util.Comparator;

@Named
public class SampleSorter<T> implements Comparator<GenericFile<T>> {
    @Override
    public int compare(GenericFile<T> o1, GenericFile<T> o2) {
        return o1.getFileName().compareTo(o2.getFileName());
    }
}
