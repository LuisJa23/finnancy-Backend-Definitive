
package finnancy.back;

import finnancy.back.controller.TestController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestControllerTest {
    TestController testController = new TestController();

    @Test
    void testTestConnection() {
        String result = testController.testConnection();
        assertEquals("Conexión a MongoDB establecida correctamente", result);
    }
}
