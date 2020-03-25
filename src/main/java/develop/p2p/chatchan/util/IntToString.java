package develop.p2p.chatchan.util;

import java.util.Arrays;
import java.util.List;

public class IntToString
{
    public static String getStringFromInt(int i)
    {
        return Arrays.asList("q'a'z'w's'x'e'd'c'r'f'v't'g'b'y'h'n'u'j'm'i'k'o'l'p'1'0'2'9'3'8'4'7'5'6'Q'W'E'R'T'Y'U'I'O'P'L'M'K'N'J'B'H'V'G'C'F'X'D'Z'S'A".split("'")).get(i).toString();
    }
}
