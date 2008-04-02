/*
 * @(#)JSFBitSet.java	1.55 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

import java.io.*;

/**
 * Copied from JDK 1.4.  PENDING(edburns): remove this class once we can
 * safely depend on JDK1.4 and above.
 */

public class JSFBitSet implements Cloneable, java.io.Serializable {
    
    private final static int ADDRESS_BITS_PER_UNIT = 6;
    private final static int BITS_PER_UNIT = 1 << ADDRESS_BITS_PER_UNIT;
    private final static int BIT_INDEX_MASK = BITS_PER_UNIT - 1;

    
    private static final long WORD_MASK = 0xffffffffffffffffL;

    
    private long bits[];  // this should be called unit[]

    
    private transient int unitsInUse = 0;

    
    private static final long serialVersionUID = 7997698588986878753L;

    
    private static int unitIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_UNIT;
    }

    
    private static long bit(int bitIndex) {
        return 1L << (bitIndex & BIT_INDEX_MASK);
    }

    
    private void recalculateUnitsInUse() {
        // Traverse the bitset until a used unit is found
        int i;
        for (i = unitsInUse-1; i >= 0; i--)
	    if(bits[i] != 0)
		break;

        unitsInUse = i+1; // The new logical size
    }

    
    public JSFBitSet() {
	this(BITS_PER_UNIT);
    }

    
    public JSFBitSet(int nbits) {
	// nbits can't be negative; size 0 is OK
	if (nbits < 0)
	    throw new NegativeArraySizeException("nbits < 0: " + nbits);

	bits = new long[(unitIndex(nbits-1) + 1)];
    }

    
    private void ensureCapacity(int unitsRequired) {
	if (bits.length < unitsRequired) {
	    // Allocate larger of doubled size or required size
	    int request = Math.max(2 * bits.length, unitsRequired);
	    long newBits[] = new long[request];
	    System.arraycopy(bits, 0, newBits, 0, unitsInUse);
	    bits = newBits;
	}
    }

    
    public void flip(int bitIndex) {
	if (bitIndex < 0)
	    throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
        
	int unitIndex = unitIndex(bitIndex);
        int unitsRequired = unitIndex+1;

        if (unitsInUse < unitsRequired) {
            ensureCapacity(unitsRequired);
            bits[unitIndex] ^= bit(bitIndex);
            unitsInUse = unitsRequired;
        } else {
            bits[unitIndex] ^= bit(bitIndex);
            if (bits[unitsInUse-1] == 0)
                recalculateUnitsInUse();
        }
    }

    
    public void flip(int fromIndex, int toIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
        if (toIndex < 0)
	    throw new IndexOutOfBoundsException("toIndex < 0: " + toIndex);
        if (fromIndex > toIndex)
	    throw new IndexOutOfBoundsException("fromIndex: " + fromIndex +
                                                " > toIndex: " + toIndex);
        
        // Increase capacity if necessary
        int endUnitIndex = unitIndex(toIndex);
        int unitsRequired = endUnitIndex + 1;

        if (unitsInUse < unitsRequired) {
            ensureCapacity(unitsRequired);
            unitsInUse = unitsRequired;
        }

        int startUnitIndex = unitIndex(fromIndex);
        long bitMask = 0;
        if (startUnitIndex == endUnitIndex) {
            // Case 1: One word
            bitMask = (1L << (toIndex & BIT_INDEX_MASK)) -
                      (1L << (fromIndex & BIT_INDEX_MASK));
            bits[startUnitIndex] ^= bitMask;
            if (bits[unitsInUse-1] == 0)
                recalculateUnitsInUse();
            return;
        }
        
        // Case 2: Multiple words
        // Handle first word
        bitMask = bitsLeftOf(fromIndex & BIT_INDEX_MASK);
        bits[startUnitIndex] ^= bitMask;

        // Handle intermediate words, if any
        if (endUnitIndex - startUnitIndex > 1) {
            for(int i=startUnitIndex+1; i<endUnitIndex; i++)
                bits[i] ^= WORD_MASK;
        }

        // Handle last word
        bitMask = bitsRightOf(toIndex & BIT_INDEX_MASK);
        bits[endUnitIndex] ^= bitMask;

        // Check to see if we reduced size
        if (bits[unitsInUse-1] == 0)
            recalculateUnitsInUse();
    }

    
    private static long bitsRightOf(int x) {
        return (x==0 ? 0 : WORD_MASK >>> (64-x));
    }

    
    private static long bitsLeftOf(int x) {
        return WORD_MASK << x;
    }

    
    public void set(int bitIndex) {
	if (bitIndex < 0)
	    throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

        int unitIndex = unitIndex(bitIndex);
        int unitsRequired = unitIndex + 1;

        if (unitsInUse < unitsRequired) {
            ensureCapacity(unitsRequired);
            bits[unitIndex] |= bit(bitIndex);
            unitsInUse = unitsRequired;
        } else {
            bits[unitIndex] |= bit(bitIndex);
        }            
    }

    
    public void set(int bitIndex, boolean value) {
        if (value)
            set(bitIndex);
        else
            clear(bitIndex);
    }

    
    public void set(int fromIndex, int toIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
        if (toIndex < 0)
	    throw new IndexOutOfBoundsException("toIndex < 0: " + toIndex);
        if (fromIndex > toIndex)
	    throw new IndexOutOfBoundsException("fromIndex: " + fromIndex +
                                                " > toIndex: " + toIndex);

        // Increase capacity if necessary
        int endUnitIndex = unitIndex(toIndex);
        int unitsRequired = endUnitIndex + 1;

        if (unitsInUse < unitsRequired) {
            ensureCapacity(unitsRequired);
            unitsInUse = unitsRequired;
        }

        int startUnitIndex = unitIndex(fromIndex);
        long bitMask = 0;
        if (startUnitIndex == endUnitIndex) {
            // Case 1: One word
            bitMask = (1L << (toIndex & BIT_INDEX_MASK)) -
                      (1L << (fromIndex & BIT_INDEX_MASK));
            bits[startUnitIndex] |= bitMask;
            return;
        }
        
        // Case 2: Multiple words
        // Handle first word
        bitMask = bitsLeftOf(fromIndex & BIT_INDEX_MASK);
        bits[startUnitIndex] |= bitMask;

        // Handle intermediate words, if any
        if (endUnitIndex - startUnitIndex > 1) {
            for(int i=startUnitIndex+1; i<endUnitIndex; i++)
                bits[i] |= WORD_MASK;
        }

        // Handle last word
        bitMask = bitsRightOf(toIndex & BIT_INDEX_MASK);
        bits[endUnitIndex] |= bitMask;
    }

    
    public void set(int fromIndex, int toIndex, boolean value) {
	if (value)
            set(fromIndex, toIndex);
        else
            clear(fromIndex, toIndex);
    }

    
    public void clear(int bitIndex) {
	if (bitIndex < 0)
	    throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

	int unitIndex = unitIndex(bitIndex);
	if (unitIndex >= unitsInUse)
	    return;

	bits[unitIndex] &= ~bit(bitIndex);
        if (bits[unitsInUse-1] == 0)
            recalculateUnitsInUse();
    }

    
    public void clear(int fromIndex, int toIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
        if (toIndex < 0)
	    throw new IndexOutOfBoundsException("toIndex < 0: " + toIndex);
        if (fromIndex > toIndex)
	    throw new IndexOutOfBoundsException("fromIndex: " + fromIndex +
                                                " > toIndex: " + toIndex);

        int startUnitIndex = unitIndex(fromIndex);
	if (startUnitIndex >= unitsInUse)
	    return;
        int endUnitIndex = unitIndex(toIndex);

        long bitMask = 0;
        if (startUnitIndex == endUnitIndex) {
            // Case 1: One word
            bitMask = (1L << (toIndex & BIT_INDEX_MASK)) -
                      (1L << (fromIndex & BIT_INDEX_MASK));
            bits[startUnitIndex] &= ~bitMask;
            if (bits[unitsInUse-1] == 0)
                recalculateUnitsInUse();
            return;
        }

        // Case 2: Multiple words
        // Handle first word
        bitMask = bitsLeftOf(fromIndex & BIT_INDEX_MASK);
        bits[startUnitIndex] &= ~bitMask;

        // Handle intermediate words, if any
        if (endUnitIndex - startUnitIndex > 1) {
            for(int i=startUnitIndex+1; i<endUnitIndex; i++) {
                if (i < unitsInUse)
                    bits[i] = 0;
            }
        }

        // Handle last word
        if (endUnitIndex < unitsInUse) {
            bitMask = bitsRightOf(toIndex & BIT_INDEX_MASK);
            bits[endUnitIndex] &= ~bitMask;
        }

        if (bits[unitsInUse-1] == 0)
            recalculateUnitsInUse();
    }

    
    public void clear() {
        while (unitsInUse > 0)
            bits[--unitsInUse] = 0;
    }

    
    public boolean get(int bitIndex) {
	if (bitIndex < 0)
	    throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

	boolean result = false;
	int unitIndex = unitIndex(bitIndex);
	if (unitIndex < unitsInUse)
	    result = ((bits[unitIndex] & bit(bitIndex)) != 0);

	return result;
    }

    
    public JSFBitSet get(int fromIndex, int toIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
        if (toIndex < 0)
	    throw new IndexOutOfBoundsException("toIndex < 0: " + toIndex);
        if (fromIndex > toIndex)
	    throw new IndexOutOfBoundsException("fromIndex: " + fromIndex +
                                                " > toIndex: " + toIndex);

        // If no set bits in range return empty bitset
        if (length() <= fromIndex || fromIndex == toIndex)
            return new JSFBitSet(0);

        // An optimization
        if (length() < toIndex)
            toIndex = length();

        JSFBitSet result = new JSFBitSet(toIndex - fromIndex);
        int startBitIndex = fromIndex & BIT_INDEX_MASK;
        int endBitIndex = toIndex & BIT_INDEX_MASK;
        int targetWords = (toIndex - fromIndex + 63)/64;
        int sourceWords = unitIndex(toIndex) - unitIndex(fromIndex) + 1;
        int inverseIndex = 64 - startBitIndex;
        int targetIndex = 0;
        int sourceIndex = unitIndex(fromIndex);

        // Process all words but the last word
        while (targetIndex < targetWords - 1)
            result.bits[targetIndex++] =
               (bits[sourceIndex++] >>> startBitIndex) |
               ((inverseIndex==64) ? 0 : bits[sourceIndex] << inverseIndex);

        // Process the last word
        result.bits[targetIndex] = (sourceWords == targetWords ?
           (bits[sourceIndex] & bitsRightOf(endBitIndex)) >>> startBitIndex :
           (bits[sourceIndex++] >>> startBitIndex) | ((inverseIndex==64) ? 0 :
           (getBits(sourceIndex) & bitsRightOf(endBitIndex)) << inverseIndex));

        // Set unitsInUse correctly
        result.unitsInUse = targetWords;
        result.recalculateUnitsInUse();
	return result;
    }

    
    private long getBits(int j) {
        return (j < unitsInUse) ? bits[j] : 0;
    }

    
    public int nextSetBit(int fromIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
        int u = unitIndex(fromIndex);
        if (u >= unitsInUse)
            return -1;
        int testIndex = (fromIndex & BIT_INDEX_MASK);
        long unit = bits[u] >> testIndex;

        if (unit == 0)
            testIndex = 0;

        while((unit==0) && (u < unitsInUse-1))
            unit = bits[++u];

        if (unit == 0)
            return -1;

        testIndex  += trailingZeroCnt(unit);
        return ((u * BITS_PER_UNIT) + testIndex);
    }

    private static int trailingZeroCnt(long val) {
        // Loop unrolled for performance
        int byteVal = (int)val & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal];

        byteVal = (int)(val >>> 8) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 8;

        byteVal = (int)(val >>> 16) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 16;

        byteVal = (int)(val >>> 24) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 24;

        byteVal = (int)(val >>> 32) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 32;

        byteVal = (int)(val >>> 40) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 40;

        byteVal = (int)(val >>> 48) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 48;

        byteVal = (int)(val >>> 56) & 0xff;
        return trailingZeroTable[byteVal] + 56;
    }

    
    private final static byte trailingZeroTable[] = {
      -25, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	7, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0};

    
    public int nextClearBit(int fromIndex) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);

        int u = unitIndex(fromIndex);
        if (u >= unitsInUse)
            return fromIndex;
        int testIndex = (fromIndex & BIT_INDEX_MASK);
        long unit = bits[u] >> testIndex;

        if (unit == (WORD_MASK >> testIndex))
            testIndex = 0;

        while((unit==WORD_MASK) && (u < unitsInUse-1))
            unit = bits[++u];

        if (unit == WORD_MASK)
            return length();
        
        if (unit == 0)
            return u * BITS_PER_UNIT + testIndex;

        testIndex += trailingZeroCnt(~unit);
        return ((u * BITS_PER_UNIT) + testIndex);
    }

    
    public int length() {
        if (unitsInUse == 0)
            return 0;

	long highestUnit = bits[unitsInUse - 1];
	int highPart = (int)(highestUnit >>> 32);
        return 64 * (unitsInUse - 1) +
               (highPart == 0 ? bitLen((int)highestUnit)
                              : 32 + bitLen((int)highPart));
    }

    
    private static int bitLen(int w) {
        // Binary search - decision tree (5 tests, rarely 6)
        return
         (w < 1<<15 ?
          (w < 1<<7 ?
           (w < 1<<3 ?
            (w < 1<<1 ? (w < 1<<0 ? (w<0 ? 32 : 0) : 1) : (w < 1<<2 ? 2 : 3)) :
            (w < 1<<5 ? (w < 1<<4 ? 4 : 5) : (w < 1<<6 ? 6 : 7))) :
           (w < 1<<11 ?
            (w < 1<<9 ? (w < 1<<8 ? 8 : 9) : (w < 1<<10 ? 10 : 11)) :
            (w < 1<<13 ? (w < 1<<12 ? 12 : 13) : (w < 1<<14 ? 14 : 15)))) :
          (w < 1<<23 ?
           (w < 1<<19 ?
            (w < 1<<17 ? (w < 1<<16 ? 16 : 17) : (w < 1<<18 ? 18 : 19)) :
            (w < 1<<21 ? (w < 1<<20 ? 20 : 21) : (w < 1<<22 ? 22 : 23))) :
           (w < 1<<27 ?
            (w < 1<<25 ? (w < 1<<24 ? 24 : 25) : (w < 1<<26 ? 26 : 27)) :
            (w < 1<<29 ? (w < 1<<28 ? 28 : 29) : (w < 1<<30 ? 30 : 31)))));
    }

    
    public boolean isEmpty() {
        return (unitsInUse == 0);
    }

    
    public boolean intersects(JSFBitSet set) {
        for(int i = Math.min(unitsInUse, set.unitsInUse)-1; i>=0; i--)
            if ((bits[i] & set.bits[i]) != 0)
                return true;
        return false;
    }

    
    public int cardinality() {
        int sum = 0;
        for (int i=0; i<unitsInUse; i++)
            sum += bitCount(bits[i]);
        return sum;
    }

    
    private static int bitCount(long val) {
        val -= (val & 0xaaaaaaaaaaaaaaaaL) >>> 1;
        val =  (val & 0x3333333333333333L) + ((val >>> 2) & 0x3333333333333333L);
        val =  (val + (val >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
        val += val >>> 8;     
        val += val >>> 16;    
        return ((int)(val) + (int)(val >>> 32)) & 0xff;
    }

    
    public void and(JSFBitSet set) {
	if (this == set)
	    return;

	// Perform logical AND on bits in common
	int oldUnitsInUse = unitsInUse;
	unitsInUse = Math.min(unitsInUse, set.unitsInUse);
        int i;
	for(i=0; i<unitsInUse; i++)
	    bits[i] &= set.bits[i];

	// Clear out units no longer used
	for( ; i < oldUnitsInUse; i++)
	    bits[i] = 0;

        // Recalculate units in use if necessary
        if (unitsInUse > 0 && bits[unitsInUse - 1] == 0)
            recalculateUnitsInUse();
    }

    
    public void or(JSFBitSet set) {
	if (this == set)
	    return;

	ensureCapacity(set.unitsInUse);

	// Perform logical OR on bits in common
	int unitsInCommon = Math.min(unitsInUse, set.unitsInUse);
        int i;
	for(i=0; i<unitsInCommon; i++)
	    bits[i] |= set.bits[i];

	// Copy any remaining bits
	for(; i<set.unitsInUse; i++)
	    bits[i] = set.bits[i];

        if (unitsInUse < set.unitsInUse)
            unitsInUse = set.unitsInUse;
    }

    
    public void xor(JSFBitSet set) {
        int unitsInCommon;

        if (unitsInUse >= set.unitsInUse) {
            unitsInCommon = set.unitsInUse;
        } else {
            unitsInCommon = unitsInUse;
            int newUnitsInUse = set.unitsInUse;
            ensureCapacity(newUnitsInUse);
            unitsInUse = newUnitsInUse;
        }

	// Perform logical XOR on bits in common
        int i;
        for (i=0; i<unitsInCommon; i++)
	    bits[i] ^= set.bits[i];

	// Copy any remaining bits
        for ( ; i<set.unitsInUse; i++)
            bits[i] = set.bits[i];

        recalculateUnitsInUse();
    }

    
    public void andNot(JSFBitSet set) {
        int unitsInCommon = Math.min(unitsInUse, set.unitsInUse);

	// Perform logical (a & !b) on bits in common
        for (int i=0; i<unitsInCommon; i++) {
	    bits[i] &= ~set.bits[i];
        }

        recalculateUnitsInUse();
    }

    
    public int hashCode() {
	long h = 1234;
	for (int i = bits.length; --i >= 0; )
            h ^= bits[i] * (i + 1);

	return (int)((h >> 32) ^ h);
    }
    
    
    public int size() {
	return bits.length << ADDRESS_BITS_PER_UNIT;
    }

    
    public boolean equals(Object obj) {
	if (!(obj instanceof JSFBitSet))
	    return false;
	if (this == obj)
	    return true;

	JSFBitSet set = (JSFBitSet) obj;
	int minUnitsInUse = Math.min(unitsInUse, set.unitsInUse);

	// Check units in use by both JSFBitSets
	for (int i = 0; i < minUnitsInUse; i++)
	    if (bits[i] != set.bits[i])
		return false;

	// Check any units in use by only one JSFBitSet (must be 0 in other)
	if (unitsInUse > minUnitsInUse) {
	    for (int i = minUnitsInUse; i<unitsInUse; i++)
		if (bits[i] != 0)
		    return false;
	} else {
	    for (int i = minUnitsInUse; i<set.unitsInUse; i++)
		if (set.bits[i] != 0)
		    return false;
	}

	return true;
    }

    
    public Object clone() {
	JSFBitSet result = null;
	try {
	    result = (JSFBitSet) super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new InternalError();
	}
	result.bits = new long[bits.length];
	System.arraycopy(bits, 0, result.bits, 0, unitsInUse);
	return result;
    }

    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        // Assume maximum length then find real length
        // because recalculateUnitsInUse assumes maintenance
        // or reduction in logical size
        unitsInUse = bits.length;
        recalculateUnitsInUse();
    }

    
    public String toString() {
	int numBits = unitsInUse << ADDRESS_BITS_PER_UNIT;
	StringBuffer buffer = new StringBuffer(8*numBits + 2);
	String separator = "";
	buffer.append('{');

	for (int i = 0 ; i < numBits; i++) {
	    if (get(i)) {
		buffer.append(separator);
		separator = ", ";
	        buffer.append(i);
	    }
        }

	buffer.append('}');
	return buffer.toString();
    }
}
