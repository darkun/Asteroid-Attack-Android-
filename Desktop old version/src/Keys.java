import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keys{

    // все нажатые кнопки (в данный момент)
    private static final ArrayList<Integer> keyChain = new ArrayList<>();;

    // true, если кнопка с кодом keyCode нажата
    public static boolean isPressed(int keyCode) {
        return keyChain.contains(keyCode);
    }

    // вызвать, как только была нажата кнопка с кодом keyCode
    public static void press(int keyCode) {
        if (!isPressed(keyCode)) {
            keyChain.add(keyCode);
        }
    }

    // вызвать, как только была отпущена кнопка с кодом keyCode
    public static void reset(int keyCode) {
        if (isPressed(keyCode)) {
            keyChain.remove(keyChain.indexOf(keyCode));
        }
    }

    public static void clear() {
        keyChain.clear();
    }
}