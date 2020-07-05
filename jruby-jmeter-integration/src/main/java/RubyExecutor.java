import org.jruby.embed.jsr223.JRubyEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;

public class RubyExecutor {
    public static void run(String rubyFilePath) throws IOException, ScriptException {
        final ScriptEngine scriptEngine = new JRubyEngineFactory().getScriptEngine();
        final FileReader rubyFileReader = new FileReader(rubyFilePath);
        scriptEngine.eval(rubyFileReader);
        rubyFileReader.close();
    }
}