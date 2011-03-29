package org.cccs.tfs.finder;

import java.util.List;
import java.util.Map;

/**
 * User: Craig Cook
 * Date: Apr 1, 2010
 * Time: 6:44:40 PM
 */
public interface Finder <T> {

    /**
     * List all persisted entities of this specific type.
     * @return
     */
    List<T> all();

    /**
     * Find an entity by its unique business key (or entity name).
     *
     * The semantic meaning of the name comparison is implementation dependant
     * (i.e., can relate to a meta-model name or object name, can use equals,
     * 'like' or any other approach as deemed fit for the purpose of filtering the results).
     * @param businessKey
     * @return
     */
    T find(final Object businessKey);

    /**
     * Construct a query for entities with the specified name, using key=value
     * for all supplied parameters.
     *
     * @param parameters
     * @return
     */
    List<T> filter(final Map<String, String[]> parameters);

    /**
     * Search for entities with the specified name, using `name like typeName'.
     *
     * The semantic meaning of the name comparison is implementation dependant
     * (i.e., can relate to a meta-model name or object name, can use equals,
     * 'like' or any other approach as deemed fit for the purpose of filtering the results).
     * @param name
     * @return
     */
    List<T> search(final String name);

    /**
     * Construct a query for entities with the specified name and parameters,
     * using `attributeName like attributeValue' for each parameter.
     *
     * @param parameters
     * @return
     */
    List<T> search(final Map<String, String[]> parameters);
}
