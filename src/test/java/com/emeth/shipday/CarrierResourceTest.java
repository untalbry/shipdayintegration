package com.emeth.shipday;

import com.emeth.shipday.infrastructure.adapter.out.shipday.ShipdayApiClient;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCarrierDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class CarrierResourceTest {

    @InjectMock
    @RestClient
    ShipdayApiClient shipdayApiClient;

    @Test
    void forwardsHeaderApiKeyToShipday() {
        Mockito.when(shipdayApiClient.getCarriers("Basic my-key")).thenReturn(List.of(
                new ShipdayCarrierDto(1L, 10L, "John Doe", "JD", "+15550001", 99L, 5L,
                        true, "john@example.com", null, true)));

        given().header("X-Shipday-Api-Key", "my-key")
                .when().get("/api/carriers")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].name", equalTo("John Doe"))
                .body("[0].onShift", equalTo(true));
    }

    @Test
    void rejectsRequestWithoutApiKey() {
        given().when().get("/api/carriers")
                .then()
                .statusCode(401)
                .body("error", equalTo("MISSING_API_KEY"));

        Mockito.verifyNoInteractions(shipdayApiClient);
    }

    @Test
    void mapsRejectedKeyToUnauthorized() {
        Mockito.when(shipdayApiClient.getCarriers("Basic bad-key")).thenThrow(new WebApplicationException(401));

        given().header("X-Shipday-Api-Key", "bad-key")
                .when().get("/api/carriers")
                .then()
                .statusCode(401)
                .body("error", equalTo("SHIPDAY_ERROR"));
    }

    @Test
    void mapsOtherShipdayErrorsToBadGateway() {
        Mockito.when(shipdayApiClient.getCarriers("Basic my-key")).thenThrow(new WebApplicationException(500));

        given().header("X-Shipday-Api-Key", "my-key")
                .when().get("/api/carriers")
                .then()
                .statusCode(502);
    }
}
