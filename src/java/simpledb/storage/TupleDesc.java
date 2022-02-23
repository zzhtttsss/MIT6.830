package simpledb.storage;

import simpledb.common.Type;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    ArrayList<TDItem> tdItems = new ArrayList<>();

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {

        // some code goes here
        return tdItems.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        for (int i = 0; i < typeAr.length; i++) {
            TDItem currentTDItem = new TDItem(typeAr[i], fieldAr[i]);
            tdItems.add(currentTDItem);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        for (int i = 0; i < typeAr.length; i++) {
            TDItem currentTDItem = new TDItem(typeAr[i], null);
            tdItems.add(currentTDItem);
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItems.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if (i < 0 || i >= this.tdItems.size()) {
            throw new NoSuchElementException();
        }
        return this.tdItems.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        if (i < 0 || i >= this.tdItems.size()) {
            throw new NoSuchElementException();
        }
        return tdItems.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        if (name == null) {
            throw new NoSuchElementException();
        }

        int index = -1;
        for (TDItem t : tdItems) {
            index++;
            if (name.equals(t.fieldName)) {
                return index;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int totalSize = 0;
        for (TDItem tdItem : tdItems) {
            totalSize += tdItem.fieldType.getLen();
        }
        return totalSize;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        int sum = td1.tdItems.size() + td2.tdItems.size();
        Type[] typeAr = new Type[sum];
        String[] fieldAr = new String[sum];
        for (int i = 0; i < td1.tdItems.size(); i++) {
            typeAr[i] = td1.tdItems.get(i).fieldType;
            fieldAr[i] = td1.tdItems.get(i).fieldName;
        }
        for (int i = 0; i < td2.tdItems.size(); i++) {
            typeAr[i + td1.tdItems.size()] = td2.tdItems.get(i).fieldType;
            fieldAr[i + td1.tdItems.size()] = td2.tdItems.get(i).fieldName;
        }

        TupleDesc tupleDesc = new TupleDesc(typeAr, fieldAr);
        return tupleDesc;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here
        TupleDesc tupleDesc;
        try {
            tupleDesc = (TupleDesc) o;
        }
        catch (ClassCastException e){
            return false;
        }

        if (tupleDesc.tdItems.size() != this.tdItems.size()) {
            return false;
        }

        for (int i = 0; i < tupleDesc.tdItems.size(); i++) {
            if (tupleDesc.tdItems.get(i).fieldType != this.tdItems.get(i).fieldType) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        int result = 17;
        for (TDItem tdItem : this.tdItems) {
            result += tdItem.fieldType.hashCode();
        }
        return result;
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < this.tdItems.size(); i++) {
            stringBuffer.append(this.tdItems.get(i).fieldType);
            stringBuffer.append("(");
            stringBuffer.append(this.tdItems.get(i).fieldName);
            stringBuffer.append(")");
            if (i != this.tdItems.size() - 1) {
                stringBuffer.append(",");
            }
        }

        return stringBuffer.toString();
    }
}
