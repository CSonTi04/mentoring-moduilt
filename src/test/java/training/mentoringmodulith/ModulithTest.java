package training.mentoringmodulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithTest {

    @Test
    void modules() {
        var modules = ApplicationModules.of(MentoringModulithApplication.class);
        modules.verify();
    }
}
