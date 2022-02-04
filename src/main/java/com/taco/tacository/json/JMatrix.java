package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonArray;

public interface JMatrix<T> extends JElement {
    
    T[][] jMatrixElements();
    
    Object convertElement(T jMatrixElement);
    
    @Override
    default Object getJValue() {
        final JsonArray columns = new JsonArray();
        final T[][] jMatrixElements = jMatrixElements();
        
        for (int i = 0; i < jMatrixElements.length; i++) {
            final JsonArray column = new JsonArray();
            for (int j = 0; j < jMatrixElements[i].length; j++)
                column.add(convertElement(jMatrixElements[i][j]));
            columns.add(column);
        }
        
        return columns;
    }
}
