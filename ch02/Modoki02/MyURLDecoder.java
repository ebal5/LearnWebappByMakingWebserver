import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MyURLDecoder {
    private static int hex2int(byte b1, byte b2) {
        var digit = (b1 >= 'A') ? (b1 & 0xDF) - 'A' + 10 : b1 - '0';
        digit *= 0x10;
        digit += (b2 >= 'A') ? (b2 & 0xDF) - 'A' + 10 : b2 - '0';
        return digit;
    }

    public static String decode(String src, String enc) throws UnsupportedEncodingException {
        var srcBytes = src.getBytes("ISO_8859_1");
        var destBytes = new byte[srcBytes.length];
        var destIdx = 0;
        for (var srcIdx = 0; srcIdx < srcBytes.length; srcIdx++) {
            if (srcBytes[srcIdx] == (byte) '%') {
                destBytes[destIdx] = (byte) hex2int(srcBytes[srcIdx + 1], srcBytes[srcIdx + 2]);
                srcIdx += 2;
            } else {
                destBytes[destIdx] = srcBytes[srcIdx];
            }
            destIdx++;
        }
        var destBytes2 = Arrays.copyOf(destBytes, destIdx);

        return new String(destBytes2, enc);
    }
}
