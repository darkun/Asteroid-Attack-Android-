package com.darkun.utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Keyboard utility class. If 2 keys pressed in onetime?
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
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