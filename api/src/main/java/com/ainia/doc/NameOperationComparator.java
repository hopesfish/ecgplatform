package com.ainia.doc;

import org.springframework.stereotype.Component;

import com.mangofactory.swagger.OperationComparator;
import com.wordnik.swagger.core.DocumentationOperation;

@Component
public class NameOperationComparator implements OperationComparator {
    @Override
    public int compare(DocumentationOperation first, DocumentationOperation second) {
        return first.getNickname().compareTo(second.getNickname());
    }
}
