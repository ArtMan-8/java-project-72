package hexlet.code.controller;

import org.junit.jupiter.api.Test;

import io.javalin.http.Context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

public class RootControllerTest {
    @Test
    public void testIndex() {
        Context ctx = mock(Context.class);
        RootController.index(ctx);
        verify(ctx).render(eq("index.jte"), any());
    }
}
