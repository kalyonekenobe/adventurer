package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;

public class CustomInputProcessor implements InputProcessor {

    public HashMap<Integer, Boolean> keysStates = new HashMap<>();

    public CustomInputProcessor() {
        for (int i = 0; i < 1000; i++) {
            keysStates.put(i, false);
        }
    }

    public int lastKeycode = -1;

    @Override
    public boolean keyDown(int keycode) {
        keysStates.put(keycode, true);
        lastKeycode = keycode;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keysStates.put(keycode, false);
        lastKeycode = keycode;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
