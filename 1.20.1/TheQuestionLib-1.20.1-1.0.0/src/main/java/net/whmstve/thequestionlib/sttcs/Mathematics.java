package net.whmstve.thequestionlib.sttcs;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class Mathematics {
    public static <N extends Number> N sum(Collection<N> numbers){
        N result = (N)(Integer)0;
        if(!numbers.isEmpty()){
            for(N number : numbers){
                result = (N)((Double)(result.doubleValue() + number.doubleValue()));
            }
        }
        return result;
    }

    public static float sqrt(float value) {
        if (value < 0) return -1; // Cannot sqrt negative numbers in real domain
        if (value == 0) return 0;

        float guess = value / 2f;
        float epsilon = 0.0001f;

        while (Math.abs(guess * guess - value) > epsilon) {
            guess = (guess + value / guess) / 2f;
        }

        return guess;
    }

    public static int sqrt(int value) {
        if (value < 0) return -1; // Cannot sqrt negative numbers
        if (value == 0 || value == 1) return value;

        int start = 1, end = value, result = 0;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (mid <= value / mid) {
                result = mid;       // mid is a possible answer
                start = mid + 1;    // Try to find a larger one
            } else {
                end = mid - 1;      // mid^2 is too big
            }
        }

        return result;
    }

}
