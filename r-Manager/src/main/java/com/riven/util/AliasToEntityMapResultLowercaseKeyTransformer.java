package com.riven.util;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import java.util.HashMap;
import java.util.Map;

public class AliasToEntityMapResultLowercaseKeyTransformer extends AliasedTupleSubsetResultTransformer {

    public static final AliasToEntityMapResultLowercaseKeyTransformer INSTANCE = new AliasToEntityMapResultLowercaseKeyTransformer();

    /**
     * Disallow instantiation of AliasToEntityMapResultTransformer.
     */
    private AliasToEntityMapResultLowercaseKeyTransformer() {
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map result = new HashMap(tuple.length);
        for ( int i=0; i<tuple.length; i++ ) {
            String alias = aliases[i];
            if ( alias!=null ) {
                result.put( alias.toLowerCase(), tuple[i] );
            }
        }
        return result;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    /**
     * Serialization hook for ensuring singleton uniqueing.
     *
     * @return The singleton instance : {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
