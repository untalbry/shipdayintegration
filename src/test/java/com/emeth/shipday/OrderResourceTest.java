package com.emeth.shipday;

import com.emeth.shipday.infrastructure.adapter.out.shipday.ShipdayApiClient;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderRequest;
import com.emeth.shipday.infrastructure.adapter.out.shipday.dto.ShipdayCreateOrderResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
class OrderResourceTest {

    private static final String VALID_ORDER = """
            {
              "orderNumber": "A-100",
              "customerName": "Jane Doe",
              "customerAddress": "123 Main St",
              "customerPhoneNumber": "+15550001",
              "restaurantName": "Pizza Place",
              "restaurantAddress": "9 Food Ave",
              "expectedDeliveryDate": "2026-11-04",
              "expectedPickupTime": "10:30",
              "deliveryFee": 3.50,
              "paymentMethod": "cash",
              "orderItem": [
                {"name": "Pepperoni", "unitPrice": 12.99, "quantity": 2}
              ]
            }
            """;

    @InjectMock
    @RestClient
    ShipdayApiClient shipdayApiClient;

    @Test
    void createsOrderInShipday() {
        Mockito.when(shipdayApiClient.createOrder(eq("Basic my-key"), any()))
                .thenReturn(new ShipdayCreateOrderResponse(true, "Updated Successfully", 7543L));

        given().header("X-Shipday-Api-Key", "my-key")
                .contentType(ContentType.JSON)
                .body(VALID_ORDER)
                .when().post("/api/orders")
                .then()
                .statusCode(201)
                .body("orderId", equalTo(7543))
                .body("orderNumber", equalTo("A-100"));

        ArgumentCaptor<ShipdayCreateOrderRequest> sent = ArgumentCaptor.forClass(ShipdayCreateOrderRequest.class);
        Mockito.verify(shipdayApiClient).createOrder(eq("Basic my-key"), sent.capture());
        ShipdayCreateOrderRequest body = sent.getValue();
        assertEquals("A-100", body.orderNumber());
        assertEquals("2026-11-04", body.expectedDeliveryDate());
        assertEquals("10:30:00", body.expectedPickupTime());
        assertEquals(1, body.orderItem().size());
        assertEquals(2, body.orderItem().get(0).quantity());
        assertEquals(new BigDecimal("12.99"), body.orderItem().get(0).unitPrice());
    }

    @Test
    void createsOrderWithOnlyRequiredFields() {
        Mockito.when(shipdayApiClient.createOrder(eq("Basic my-key"), any()))
                .thenReturn(new ShipdayCreateOrderResponse(true, "Updated Successfully", 7544L));

        given().header("X-Shipday-Api-Key", "my-key")
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "orderNumber": "A-200",
                          "customerName": "Jane Doe",
                          "customerAddress": "123 Main St",
                          "customerPhoneNumber": "+15550001",
                          "restaurantName": "Pizza Place",
                          "restaurantAddress": "9 Food Ave",
                          "orderItem": [
                            {"name": "Soda", "unitPrice": 1.50, "quantity": 1}
                          ]
                        }
                        """)
                .when().post("/api/orders")
                .then()
                .statusCode(201)
                .body("orderId", equalTo(7544));

        ArgumentCaptor<ShipdayCreateOrderRequest> sent = ArgumentCaptor.forClass(ShipdayCreateOrderRequest.class);
        Mockito.verify(shipdayApiClient).createOrder(eq("Basic my-key"), sent.capture());
        assertEquals(1, sent.getValue().orderItem().size());
        assertEquals("Soda", sent.getValue().orderItem().get(0).name());
        assertNull(sent.getValue().orderItem().get(0).addOns());
        assertNull(sent.getValue().expectedDeliveryDate());
    }

    @Test
    void rejectsOrderWithoutItems() {
        given().header("X-Shipday-Api-Key", "my-key")
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "orderNumber": "A-201",
                          "customerName": "Jane Doe",
                          "customerAddress": "123 Main St",
                          "customerPhoneNumber": "+15550001",
                          "restaurantName": "Pizza Place",
                          "restaurantAddress": "9 Food Ave"
                        }
                        """)
                .when().post("/api/orders")
                .then()
                .statusCode(400)
                .body("error", equalTo("INVALID_ORDER"))
                .body("message", containsString("orderItem must contain at least one item"));

        Mockito.verifyNoInteractions(shipdayApiClient);
    }

    @Test
    void rejectsRequestWithoutApiKey() {
        given().contentType(ContentType.JSON)
                .body(VALID_ORDER)
                .when().post("/api/orders")
                .then()
                .statusCode(401)
                .body("error", equalTo("MISSING_API_KEY"));

        Mockito.verifyNoInteractions(shipdayApiClient);
    }

    @Test
    void rejectsOrderMissingRequiredFields() {
        given().header("X-Shipday-Api-Key", "my-key")
                .contentType(ContentType.JSON)
                .body("""
                        {"orderNumber": "A-101", "orderItem": [{"name": "Soda", "quantity": 0}]}
                        """)
                .when().post("/api/orders")
                .then()
                .statusCode(400)
                .body("error", equalTo("INVALID_ORDER"))
                .body("message", containsString("customerName is required"))
                .body("message", containsString("orderItem[0].quantity must be greater than 0"));

        Mockito.verifyNoInteractions(shipdayApiClient);
    }

    @Test
    void mapsRejectedKeyToUnauthorized() {
        Mockito.when(shipdayApiClient.createOrder(eq("Basic bad-key"), any()))
                .thenThrow(new WebApplicationException(401));

        given().header("X-Shipday-Api-Key", "bad-key")
                .contentType(ContentType.JSON)
                .body(VALID_ORDER)
                .when().post("/api/orders")
                .then()
                .statusCode(401)
                .body("error", equalTo("SHIPDAY_ERROR"));
    }

    @Test
    void mapsUnsuccessfulShipdayResponseToBadGateway() {
        Mockito.when(shipdayApiClient.createOrder(eq("Basic my-key"), any()))
                .thenReturn(new ShipdayCreateOrderResponse(false, "Duplicate order", null));

        given().header("X-Shipday-Api-Key", "my-key")
                .contentType(ContentType.JSON)
                .body(VALID_ORDER)
                .when().post("/api/orders")
                .then()
                .statusCode(502)
                .body("message", containsString("Duplicate order"));
    }
}
