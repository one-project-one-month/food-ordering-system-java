# food-ordering-system-java

### 📘 Development Guide

### ✅ Overview

This project is organized using a **hybrid structure** combining **technical partitioning** and **domain-driven partitioning**, adhering to a **layered architectural style**. Each folder has a clear responsibility, either grouped by technical concern (e.g., configuration, security) or by business/domain logic (e.g., features, entities).

---

## 📂 Folder Structure Explanation

| Folder | Description | Partition Type | Layer |
| --- | --- | --- | --- |
| `common/` | Contains common utilities like enums, converters, and constants. | Technical Partition | Shared Utility Layer |
| `entity/` | Houses core domain master entity, typically reused across entities. | Domain Partition | Domain Layer |
| `jpa/` | Custom JPA configuration such as naming strategies. | Technical Partition | Infrastructure Layer |
| `storage/` | Implements storage-specific services (e.g., file storage). | Technical Partition | Infrastructure Layer |
| `config/` | Spring and general configuration beans (e.g., `ModelMapper`). | Technical Partition | Configuration Layer |
| `exceptions/` | Custom exception classes and exception handling utilities. | Technical Partition | Shared/Service Layer |
| `response/` | Standardized response DTOs and utilities (e.g., API response wrappers). | Technical Partition | Presentation Layer |
| `features/` | Contains business-specific logic grouped by feature/domain (e.g., User, Restaurant, etc.). | Domain Partition | Application/Domain Layer |
| `model/` | JPA entities used within the database mapping. | Domain Partition | Persistence Layer |
| `security/` | Security configuration (e.g., JWT, filters). | Technical Partition | Infrastructure Layer |
| `startup/` | Initialization logic (e.g., seed roles or default users using `CommandLineRunner`). | Technical Partition | Bootstrap/Init Layer |

---

## 📊 Partitioning Strategy

### 🔷 Technical Partition

Groups code by **technical responsibility**. 

### 🔶 Domain Partition

Groups code by **business domains or features**.

---

## 🏗️ Layered Architecture Mapping

The architecture generally follows this layer stack:

```
Presentation Layer
  └── response/
  └── controller classes inside features/

Application Layer
  └── services inside features/
  └── startup/ (business initialization logic)

Domain Layer
  └── entity/
  └── model/
  └── common/ (enums, value objects, etc.)

Persistence/Infrastructure Layer
  └── jpa/
  └── storage/
  └── config/
  └── security/

Shared/Utility Layer
  └── exceptions/
  └── common/
```

Each `feature/` sub-folder may itself follow mini-layered separation:

- `controller/` (Presentation)
- `service/` (Application)
- `repository/` (Persistence)
- `dto/` (Transport)

---

## 🔧 Development Best Practices

1. **Add new domain logic inside `features/`** grouped by feature.
2. **Reuse common enums or converters from `common/`.**
3. **Use DTOs in `features/` for exposure.**
4. **All configuration should live in `config/`, including beans.**
5. **Initialization logic (e.g., seeding roles) belongs in `startup/`.**
6. **Security-related logic (JWT filters, WebSecurityConfigurerAdapter, etc.) belongs in `security/`.**
7. **Use `exceptions/` for centralized error handling and throw meaningful custom exceptions.**

---

## 📦 Example of Creating a New Feature

If you’re adding a new feature called `Order`, you might create:

```
features/order/
├── controller/
│   └── OrderController.java          <-- Handles HTTP/API requests
│
├── service/
│   ├── OrderService.java             <-- Service interface (business logic)
│   └── impl/
│       └── OrderServiceImpl.java     <-- Concrete implementation of service
│
├── repository/
│   └── OrderRepository.java          <-- JPA Repository interface
│
├── dto/
│   ├── request/
│   │   └── OrderRequestDto.java      <-- Incoming request data structure
│   └── response/
│       └── OrderResponseDto.java     <-- Outgoing response data structure
```

Also consider:

- Adding startup logic in `startup/` if you need to seed orders.

## 1. `OrderController.java`

```java
@ResttController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(
            @RequestBody final OrderRequestDto orderRequest,
            final HttpServletRequest request) {

        ApiResponse response = orderService.createOrder(orderRequest);
        return ResponseUtils.buildResponse(request, response);
    }
}
```

## 2. `OrderService.java` (interface)

```java
public interface OrderService {
    ApiResponse createOrder(OrderRequestDto requestDto);
}

```

## 3. `OrderServiceImpl.java`

```java
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createOrder(OrderRequestDto requestDto) {

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(requestDto.getQuantity());
        order.setCustomerEmail(requestDto.getCustomerEmail());

        orderRepository.save(order);

        OrderResponseDto responseDto = modelMapper.map(order, OrderResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Order created successfully.")
                .data(Map.of("order", responseDto))
                .build();
    }
}

```

## 4. `OrderRequestDto.java`

```java
@Data
public class OrderRequestDto {
    private Long productId;
    private Integer quantity;
    private String customerEmail;
}
```

## 5. `OrderResponseDto.java`

```java
@Data
public class OrderResponseDto {
    private Long id;
    private String productName;
    private Integer quantity;
    private String customerEmail;
    private LocalDateTime createdAt;
}
```

---
