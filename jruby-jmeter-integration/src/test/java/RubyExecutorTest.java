import org.fest.assertions.Assertions;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;

public class RubyExecutorTest {

    @Test
    public void
    it_should_generate_a_file_from_ruby_code() throws IOException, ScriptException {
        assertJmxFileExists("simpleCode.rb");
    }

    @Test
    public void
    it_should_generate_a_jmx_file_from_ruby_code() throws IOException, ScriptException {
        assertJmxFileExists("simpleJmx.rb");
    }

    private void assertJmxFileExists(final String rubyScript) throws IOException, ScriptException {
        final String rubyFile = this.getClass().getResource("/RubyExecutor/" + rubyScript).getFile();
        RubyExecutor.run(rubyFile);
        final File expectedJmxFile = new File(rubyFile.replace("rb", "jmx"));
        Assertions.assertThat(expectedJmxFile).exists();
    }
}
