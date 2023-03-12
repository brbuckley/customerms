package customerms;

import customerms.api.model.customer.CustomerGender;
import customerms.api.model.customer.CustomerRequest;
import customerms.api.model.customer.CustomerResponse;
import customerms.api.model.order.OrderRequest;
import customerms.api.model.order.OrderResponse;
import customerms.api.model.order.OrderStatus;
import customerms.api.model.orderline.OrderLineRequest;
import customerms.api.model.orderline.OrderLineResponse;
import customerms.api.model.product.ProductCategory;
import customerms.api.model.product.ProductRequest;
import customerms.api.model.product.ProductResponse;
import customerms.persistence.domain.CustomerEntity;
import customerms.persistence.domain.OrderEntity;
import customerms.persistence.domain.OrderLineEntity;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerMsResponseUtil {

  public static CustomerResponse defaultCustomerResponse() throws ParseException {
    CustomerResponse customer = new CustomerResponse();

    customer.setId("CST0000001");
    customer.setFirstname("Bob");
    customer.setLastname("Smith");
    customer.setGender(CustomerGender.MALE.getValue());
    customer.setDob(new Date(new SimpleDateFormat("MM-dd-yyyy").parse("12-25-1980").getTime()));

    return customer;
  }

  public static OrderResponse defaultOrderResponse() throws ParseException {
    OrderResponse order = new OrderResponse();
    OrderLineResponse orderLine = new OrderLineResponse();
    ProductResponse product = new ProductResponse();

    product.setName("heineken");
    product.setId("PRD0000001");
    product.setPrice(new BigDecimal("4.99"));
    product.setCategory(ProductCategory.BEER.getValue());

    orderLine.setQuantity(2);
    orderLine.setProduct(product);

    order.setId("ORD0000001");
    order.setStatus("processing");
    order.setTotal(new BigDecimal("9.98"));
    order.setCustomer(defaultCustomerResponse());
    order.addOrderLine(orderLine);

    return order;
  }

  public static OrderRequest defaultOrderRequest() {
    // Arrange
    OrderRequest minOrder = new OrderRequest();
    OrderLineRequest orderLine = new OrderLineRequest();
    ProductRequest minProduct = new ProductRequest();

    // OrderMinimalObject
    minProduct.setId("PRD0000001");
    orderLine.setQuantity(2);
    orderLine.setProduct(minProduct);
    minOrder.addItemsItem(orderLine);

    return minOrder;
  }

  public static ProductResponse defaultProductResponse() {
    return new ProductResponse("PRD0000001", "Heineken", new BigDecimal("10"), "beer");
  }

  public static CustomerEntity defaultCustomerEntity() throws ParseException {
    CustomerEntity customer = new CustomerEntity();
    customer.setCustomerId(1);
    customer.setFirstname("Bob");
    customer.setLastname("Smith");
    customer.setGender(CustomerGender.MALE);
    customer.setDob(new Date(new SimpleDateFormat("MM-dd-yyyy").parse("12-25-1980").getTime()));
    return customer;
  }

  public static CustomerEntity minimalCustomerEntity() {
    CustomerEntity customer = new CustomerEntity();
    customer.setCustomerId(1);
    customer.setFirstname("Bob");
    customer.setLastname("Smith");
    return customer;
  }

  public static List<String> productIdList() {
    List<String> products = new ArrayList<>();
    products.add("PRD0000001");
    return products;
  }

  public static OrderEntity defaultOrderEntity() throws ParseException {
    OrderEntity order = new OrderEntity();
    OrderLineEntity orderLine = new OrderLineEntity();
    order.setOrderId(1);
    order.setCustomer(defaultCustomerEntity());
    order.setTotal(new BigDecimal("9.98"));
    order.setStatus(OrderStatus.PROCESSING);
    order.setDatetime(Timestamp.valueOf("2022-07-29 12:16:00"));

    orderLine.setOrder(order);
    orderLine.setQuantity(2);

    orderLine.setProductId(1);
    orderLine.setPrice(new BigDecimal("4.99"));
    orderLine.setCategory(ProductCategory.BEER.getValue());
    orderLine.setName("Heineken");

    order.addOrderLine(orderLine);

    return order;
  }

  public static String productMsResponse() {
    return "[{\"id\":\"PRD0000001\",\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\"}]";
  }

  public static String response404() {
    return "{\"error_code\": \"E_PRD_404\",\"description\": \"Resource Not Found\"}";
  }

  public static ProductResponse[] defaultProductList() {
    ProductResponse[] products = {defaultProductResponse()};
    return products;
  }

  public static CustomerRequest updateCustomerRequest() {
    CustomerRequest customerRequest = new CustomerRequest();
    customerRequest.setFirstname("Felipe");
    customerRequest.setLastname("Smith");
    customerRequest.setGender("male");
    return customerRequest;
  }

  public static CustomerResponse updateCustomerResponse() throws ParseException {
    CustomerResponse response = CustomerMsResponseUtil.defaultCustomerResponse();
    response.setFirstname("Felipe");
    response.setDob(null);
    return response;
  }

  public static CustomerRequest defaultCustomerRequest() {
    CustomerRequest customerRequest = new CustomerRequest();
    customerRequest.setDob("12-25-1980");
    customerRequest.setFirstname("Bob");
    customerRequest.setGender("male");
    customerRequest.setLastname("Smith");
    return customerRequest;
  }
}
