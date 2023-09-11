package io.superloader.plugin.js.external;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class JSExternal {
    protected final Context sourceContext;
    protected final Value value;
    private boolean alive = true;

    public JSExternal(Context sourceContext,Value value){
        this.sourceContext = sourceContext;
        this.value = value;
    }

    public Context getSourceContext() {
        return sourceContext;
    }

    public Value getValue() {
        return value;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    protected final void checkAlive() {
        if(!alive) {
            throw new ReferenceNotAliveException("Reference targeting " + value.toString() + " has already be disposed.");
        }
    }

    public static final class ReferenceNotAliveException extends RuntimeException {
        public ReferenceNotAliveException(String message) {
            super(message);
        }
    }
}
