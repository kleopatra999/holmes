package com.wikia.reader.filters.text;/**
 * Author: Artur Dwornik
 * Date: 13.04.13
 * Time: 19:41
 */

import com.google.common.collect.Sets;
import com.wikia.reader.filters.CollectionFilterBase;
import com.wikia.reader.input.structured.WikiPageCategory;
import com.wikia.reader.input.structured.WikiPageStructure;
import com.wikia.reader.text.matrix.SparseMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CharacteristicCategoryPartExtractorFilter extends CollectionFilterBase<WikiPageStructure, SparseMatrix> {
    private static Logger logger = LoggerFactory.getLogger(CharacteristicCategoryPartExtractorFilter.class);
    private Set<String> characteristicParts;

    public CharacteristicCategoryPartExtractorFilter() {
        this(Sets.newHashSet("season"
                , "character"
                , "characters"
                , "episodes"
                , "missions"
                , "levels"
                , "people"
                , "episodes"
                , "games"
                , "creatures"
                , "units"
                , "novels"
                , "stories"
                , "playstation"
                , "xbox"
                , "enemies"
                , "weapons"
                , "actors"
                , "directors"
                , "celebrities"
                , "performers"
                , "writers"
                , "crew"
                , "cast"
                , "movies"
                , "items"
                , "armors"
                , "films"
                , "books"
                , "authors"
                , "rifles"));
    }

    public CharacteristicCategoryPartExtractorFilter(Set<String> characteristicParts) {
        super(WikiPageStructure.class, SparseMatrix.class);
        this.characteristicParts = characteristicParts;
    }

    @Override
    protected SparseMatrix doFilter(Collection<WikiPageStructure> params) {
        SparseMatrix matrix = new SparseMatrix();
        for(WikiPageStructure wikiPageStructure: params) {
            for(String part: characteristicParts) {
                int count = 0;
                for(WikiPageCategory category: wikiPageStructure.getCategories()) {
                    String name = category.getTitle();
                    name = name.trim().toLowerCase();
                    if (name.contains(part)) count++;
                }
                matrix.put(wikiPageStructure.getTitle(), "special:"+part, count);
            }
        }
        return matrix;
    }
}
