package at.fhtw.httpserver.utils;

import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.utils.RequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestBuilderTest {

    @Test
    void testBuildRequestFromGet() throws Exception {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("GET /echo/mehr?p=23 HTTP/1.1",
                "Content-Type: text/plain",
                "Content-Length: 8",
                "Accept: */*",
                "",
                "{'id':1}");

        Request request = new RequestBuilder().buildRequest(reader);
        assertEquals("/echo/mehr", request.getPathname());
        assertEquals("/echo", request.getServiceRoute());
        assertEquals("mehr", request.getPathParts().get(1));
        assertEquals(8, request.getHeaderMap().getContentLength());
    }
}